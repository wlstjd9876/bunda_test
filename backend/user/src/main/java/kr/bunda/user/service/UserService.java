package kr.bunda.user.service;

import kr.bunda.user.dto.TokenModel;
import kr.bunda.user.dto.UserModel;
import kr.bunda.user.exception.AlreadyExistException;
import kr.bunda.user.exception.BadRequestException;
import kr.bunda.user.exception.InvalidArgumentException;
import kr.bunda.user.exception.WrongCredentialsException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserService {

    @Transactional
    void create(UserModel.SignUp userModel) throws BadRequestException, AlreadyExistException, InvalidArgumentException;

    @Transactional
    void update(UserModel.SignUp userModel) throws BadRequestException;

    @Transactional
    TokenModel.TokenDto getTokenByUserNamePassword(UserModel.Login userModel, String ipAddress) throws WrongCredentialsException;

    @Transactional
    TokenModel.TokenDto getTokenByRefreshToken(String refreshToken, String ipAddress) throws WrongCredentialsException;

    @Transactional
    void quit(UserModel.OnlyEmail userModel) throws BadRequestException;

    @Transactional
    void delete(UserModel.OnlyEmail userModel);

    @Transactional
    Optional<UserModel.UserDto> getUser(String email);

    @Transactional
    String sendSignUpMailToken(UserModel.OnlyEmail userModel) throws AlreadyExistException;

    @Transactional
    void checkSignUpMailToken(UserModel.EmailToken userModel) throws BadRequestException, InvalidArgumentException;

    @Transactional
    String sendSignUpPhoneToken(UserModel.PhoneEmailToken userModel);

    @Transactional
    void checkSignUpPhoneToken(UserModel.PhoneEmailToken userModel) throws BadRequestException, InvalidArgumentException;

    @Transactional
    int deleteEmailToken();

    @Transactional
    String sendForgotMailToken(UserModel.OnlyPhone model);

    @Transactional
    UserModel.OnlyEmail checkForgotMailToken(UserModel.PhoneToken model) throws BadRequestException, InvalidArgumentException;
}
