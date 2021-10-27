package kr.bunda.user.service.impl;

import kr.bunda.user.dto.TokenModel;
import kr.bunda.user.dto.UserModel;
import kr.bunda.user.entity.*;
import kr.bunda.user.exception.AlreadyExistException;
import kr.bunda.user.exception.BadRequestException;
import kr.bunda.user.exception.InvalidArgumentException;
import kr.bunda.user.exception.WrongCredentialsException;
import kr.bunda.user.message.MessageCode;
import kr.bunda.user.repository.*;
import kr.bunda.user.security.TokenProvider;
import kr.bunda.user.service.MailService;
import kr.bunda.user.service.PhoneService;
import kr.bunda.user.service.UserService;
import kr.bunda.user.type.Auth;
import kr.bunda.user.type.SignUpProcess;
import kr.bunda.user.type.TokenUse;
import kr.bunda.user.type.UserStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final DateTimeFormatter birthFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final TokenProvider tokenProvider;
    private final MailTokenRepository mailAuthRepository;
    private final PhoneTokenRepository phoneTokenRepository;
    private final SignUpRepository signUpRepository;
    private final MailService mailService;
    private final PhoneService phoneService;

    private final int SIGN_UP_EXPIRED;
    private final int EMAIL_TOKEN_EXPIRED;
    private final int PHONE_TOKEN_EXPIRED;

    public UserServiceImpl(UserRepository userRepository, AuthRepository authRepository, TokenRepository tokenRepository, PasswordEncoder passwordEncoder,
                           TokenProvider tokenProvider, MailTokenRepository mailAuthRepository, PhoneTokenRepository phoneTokenRepository, SignUpRepository signUpRepository, MailService mailService,
                           PhoneService phoneService, @Value("${bunda.signup.expired:600}") int sign_up_expired,
                           @Value("${bunda.email.expired:600}") int email_token_expired,
                           @Value("${bunda.phone.expired:180}") int phone_token_expired) {

        this.userRepository = userRepository;
        this.authRepository = authRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.mailAuthRepository = mailAuthRepository;
        this.phoneTokenRepository = phoneTokenRepository;
        this.signUpRepository = signUpRepository;
        this.mailService = mailService;
        this.phoneService = phoneService;
        SIGN_UP_EXPIRED = sign_up_expired;
        EMAIL_TOKEN_EXPIRED = email_token_expired;
        PHONE_TOKEN_EXPIRED = phone_token_expired;
    }

    @Override
    @Transactional
    public void create(UserModel.SignUp userModel) throws BadRequestException, AlreadyExistException, InvalidArgumentException {
        SignUpEntity signUpEntity = signUpRepository.findByEmail(userModel.getEmail())
                .orElseThrow(() -> new BadRequestException(MessageCode.ABNORMAL_REQUEST.name(), userModel.getEmail()));

        //TODO:이메일만 인증할건지 , 핸드폰도 인증할건지?
        if ((!signUpEntity.getProcess().equals(SignUpProcess.EMAIL))) {
            throw new BadRequestException(MessageCode.ABNORMAL_REQUEST.name(), userModel.getEmail());
        }
        if (signUpEntity.getModifyTime().plusSeconds(SIGN_UP_EXPIRED).isBefore(Instant.now())) {
            throw new InvalidArgumentException(MessageCode.SIGNUP_TIMEOUT.name(), userModel.getEmail());
        }
        signUpRepository.delete(signUpEntity);

        //이메일 확인
        checkExistUser(userModel.getEmail());

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userModel.getEmail());
        userEntity.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userEntity.setName(userModel.getName());
        userEntity.setPhoneNumber(userModel.getPhoneNumber());
        userEntity.setAddress(userModel.getAddress());
        userEntity.setPhoneNumber(userModel.getPhoneNumber());
        userEntity.setMarketing(userModel.isMarketing());
        userEntity.setBirth(LocalDate.from(birthFormat.parse(userModel.getBirth())));

        AuthEntity authEntity = new AuthEntity();
        authEntity.setAuth(Auth.ROLE_USER);
        authEntity.setUser(userEntity);

        userRepository.save(userEntity);
        authRepository.save(authEntity);
    }

    @Override
    @Transactional
    public void update(UserModel.SignUp userModel) throws BadRequestException {

        UserEntity userEntity = userRepository.findByEmail(userModel.getEmail()).orElseThrow(() -> new BadRequestException(MessageCode.NOT_EXIST_USER.name(), userModel.getEmail()));

        userEntity.setEmail(userModel.getEmail());
        userEntity.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userEntity.setName(userModel.getName());
        userEntity.setPhoneNumber(userModel.getPhoneNumber());
        userEntity.setAddress(userModel.getAddress());
        userEntity.setPhoneNumber(userModel.getPhoneNumber());
        userEntity.setMarketing(userModel.isMarketing());
        userEntity.setBirth(LocalDate.from(birthFormat.parse(userModel.getBirth())));

        userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public void quit(UserModel.OnlyEmail userModel) throws BadRequestException {
        UserEntity userEntity = userRepository.findByEmail(userModel.getEmail())
                .orElseThrow(() -> new BadRequestException(MessageCode.NOT_EXIST_USER.name(), userModel.getEmail()));
        userEntity.setStatus(UserStatus.QUIT);
        userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public void delete(UserModel.OnlyEmail userModel) {
        userRepository.deleteByEmail(userModel.getEmail());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserModel.UserDto> getUser(String email) {
        return userRepository.findByEmail(email)
                .map(UserModel.UserDto::of);

    }

    @Override
    @Transactional
    public TokenModel.TokenDto getTokenByUserNamePassword(UserModel.Login userModel, String ipAddress) throws WrongCredentialsException {
        Objects.requireNonNull(userModel);
        Objects.requireNonNull(ipAddress);

        UserEntity userEntity = userRepository.findByEmail(userModel.getEmail())
                .orElseThrow(() -> new WrongCredentialsException(MessageCode.NOT_EXIST_USER.name(), userModel.getEmail()));

        UserModel.UserDto user = UserModel.UserDto.of(userEntity);

        //비밀번호 틀림
        if (!passwordEncoder.matches(userModel.getPassword(), user.getPassword())) {
            throw new WrongCredentialsException(MessageCode.ABNORMAL_REQUEST.name());
        }
        TokenModel.TokenDto tokenDto = tokenProvider.generate(user, ipAddress);
        String signature = tokenDto.getRefreshToken().split("\\.")[2];
        RefreshTokenEntity token = RefreshTokenEntity.builder().token(tokenDto.getRefreshToken()).ipAddress(ipAddress).user(userEntity).signature(signature).build();
        userEntity.setToken(token);
        userRepository.save(userEntity);
        tokenDto.setRefreshToken(signature);
        return tokenDto;
    }

    @Override
    public TokenModel.TokenDto getTokenByRefreshToken(String refreshToken, String ipAddress) throws WrongCredentialsException {
        Objects.requireNonNull(refreshToken);
        Objects.requireNonNull(ipAddress);

        RefreshTokenEntity tokenEntity = tokenRepository.findBySignature(refreshToken)
                .orElseThrow(() -> new WrongCredentialsException(MessageCode.NOT_EXIST_TOKEN.name()));

        if (!tokenEntity.getIpAddress().equals(ipAddress)) {
            tokenRepository.delete(tokenEntity);
            throw new WrongCredentialsException(MessageCode.RE_LOGIN.name());
        }
        UserModel.UserDto user = UserModel.UserDto.of(tokenEntity.getUser());
        return tokenProvider.generate(user);
    }


    @Override
    @Transactional
    public String sendSignUpMailToken(UserModel.OnlyEmail userModel) throws AlreadyExistException {
        checkExistUser(userModel.getEmail());
        mailAuthRepository.deleteByEmail(userModel.getEmail());
        String token = generateEmailToken();
        MailTokenEntity mailTokenEntity = MailTokenEntity.builder().token(token).tokenUse(TokenUse.SIGNUP).email(userModel.getEmail()).build();
        mailAuthRepository.save(mailTokenEntity);
        mailService.sendAuthMail(userModel.getEmail(), token);
        return token;
    }

    @Override
    @Transactional
    public void checkSignUpMailToken(UserModel.EmailToken userModel) throws BadRequestException, InvalidArgumentException {
        MailTokenEntity mailTokenEntity = mailAuthRepository.findByEmailAndTokenUse(userModel.getEmail(), TokenUse.SIGNUP)
                .orElseThrow(() -> new BadRequestException(MessageCode.NOT_EXIST_TOKEN.name()));
        if (mailTokenEntity.getCreatedTime().plusSeconds(EMAIL_TOKEN_EXPIRED).isBefore(Instant.now())) {
            throw new BadRequestException(MessageCode.TOKEN_EXPIRED.name());
        }
        if (mailTokenEntity.getToken().equals(userModel.getToken())) {
            mailAuthRepository.delete(mailTokenEntity);
            signUpRepository.deleteByEmail(userModel.getEmail());
            SignUpEntity signUpEntity = SignUpEntity.builder().email(userModel.getEmail()).process(SignUpProcess.EMAIL).build();
            signUpRepository.save(signUpEntity);
        } else {
            throw new InvalidArgumentException(MessageCode.TOKEN_INVALID.name());
        }
    }

    @Override
    @Transactional
    public String sendSignUpPhoneToken(UserModel.PhoneEmailToken userModel) {
        phoneTokenRepository.deleteByPhone(userModel.getPhoneNumber());
        String token = generatePhoneToken();
        PhoneTokenEntity phoneTokenEntity = PhoneTokenEntity.builder().token(token).tokenUse(TokenUse.SIGNUP).phone(userModel.getPhoneNumber()).build();
        phoneTokenRepository.save(phoneTokenEntity);
        phoneService.sendAuthMail(userModel.getPhoneNumber(), token);
        return token;
    }

    @Override
    @Transactional
    public void checkSignUpPhoneToken(UserModel.PhoneEmailToken userModel) throws BadRequestException, InvalidArgumentException {
        PhoneTokenEntity phoneTokenEntity = phoneTokenRepository.findByPhone(userModel.getPhoneNumber()).orElseThrow(() -> new BadRequestException(MessageCode.NOT_EXIST_TOKEN.name()));

        if (phoneTokenEntity.getCreatedTime().plusSeconds(PHONE_TOKEN_EXPIRED).isBefore(Instant.now())) {
            throw new BadRequestException(MessageCode.TOKEN_EXPIRED.name());
        }
        if (phoneTokenEntity.getToken().equals(userModel.getToken())) {
            phoneTokenRepository.delete(phoneTokenEntity);
            SignUpEntity signUpEntity = signUpRepository.findByEmail(userModel.getEmail())
                    .orElseThrow(() -> new BadRequestException(MessageCode.ABNORMAL_REQUEST.name()));
            signUpEntity.setProcess(SignUpProcess.PHONE);
            signUpRepository.save(signUpEntity);
        } else {
            throw new InvalidArgumentException(MessageCode.TOKEN_INVALID.name());
        }
    }

    @Transactional
    public void sendForgotPasswordToken() {

    }

    @Override
    @Transactional
    public String sendForgotMailToken(UserModel.OnlyPhone model) {
        phoneTokenRepository.deleteByPhoneAndTokenUse(model.getPhoneNumber(), TokenUse.EMAIL);

        String token = generatePhoneToken();
        PhoneTokenEntity phoneTokenEntity = PhoneTokenEntity.builder().phone(model.getPhoneNumber()).token(token).tokenUse(TokenUse.EMAIL).build();
        phoneTokenRepository.save(phoneTokenEntity);
        log.debug("sendForgotMailToken phone : {},token : {}", model.getPhoneNumber(), token);

        phoneService.sendForgotEmailToken(model.getPhoneNumber(), token);
        return token;
    }

    @Override
    @Transactional
    public UserModel.OnlyEmail checkForgotMailToken(UserModel.PhoneToken model) throws BadRequestException, InvalidArgumentException {

        PhoneTokenEntity phoneTokenEntity = phoneTokenRepository.findByPhoneAndTokenUse(model.getPhoneNumber(), TokenUse.EMAIL)
                .orElseThrow(() -> new BadRequestException(MessageCode.NOT_EXIST_TOKEN.name()));
        if (phoneTokenEntity.getCreatedTime().plusSeconds(PHONE_TOKEN_EXPIRED).isBefore(Instant.now())) {
            throw new BadRequestException(MessageCode.TOKEN_EXPIRED.name());
        }
        if (phoneTokenEntity.getToken().equals(model.getToken())) {
            phoneTokenRepository.delete(phoneTokenEntity);
            UserEntity userEntity = userRepository.findByPhoneNumber(model.getPhoneNumber())
                    .orElseThrow(() -> new BadRequestException(MessageCode.NOT_EXIST_USER.name()));
            return new UserModel.OnlyEmail(userEntity.getEmail());
        } else {
            throw new InvalidArgumentException(MessageCode.TOKEN_INVALID.name());
        }
    }

    @Override
    @Transactional
    public int deleteEmailToken() {
        return mailAuthRepository.deleteByCreatedTimeBefore(Instant.now().minusSeconds(EMAIL_TOKEN_EXPIRED));
    }

    public void checkExistUser(String email) throws AlreadyExistException {
        Optional<UserEntity> oldEntity = userRepository.findByEmail(email);
        if (oldEntity.isPresent()) {
            throw new AlreadyExistException(MessageCode.ALREADY_EXISTS.name());
        }
    }

    private String generateEmailToken() {
        Random rnd = new Random();
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            if (rnd.nextBoolean()) {
                buf.append((char) ((rnd.nextInt(26)) + 97));
            } else {
                buf.append((rnd.nextInt(10)));
            }
        }
        return buf.toString();
    }

    private String generatePhoneToken() {
        Random rnd = new Random();
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            buf.append((rnd.nextInt(10)));
        }
        return buf.toString();
    }
}
