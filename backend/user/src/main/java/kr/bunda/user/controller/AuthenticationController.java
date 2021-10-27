package kr.bunda.user.controller;

import io.swagger.annotations.ApiOperation;
import kr.bunda.core.model.ResponseModel;
import kr.bunda.user.dto.TokenModel;
import kr.bunda.user.dto.UserModel;
import kr.bunda.user.exception.AlreadyExistException;
import kr.bunda.user.exception.BadRequestException;
import kr.bunda.user.exception.InvalidArgumentException;
import kr.bunda.user.exception.WrongCredentialsException;
import kr.bunda.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    @ApiOperation(value = "username password login", notes = "로그인 기능 , token 발급")
    @PreAuthorize("permitAll")
    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody UserModel.Login userModel, HttpServletRequest req) throws WrongCredentialsException {
        String ipAddress = req.getHeader("x-forward-for");
        if (ipAddress == null) {
            ipAddress = req.getRemoteAddr();
        }
        String type = "Auth-Login";
        return ResponseEntity.ok(ResponseModel.builder().data(userService.getTokenByUserNamePassword(userModel, ipAddress)).type(type).build());
    }

    @ApiOperation(value = "Refresh token login", notes = "Refresh Token으로 Access Token 발급")
    @PreAuthorize("permitAll")
    @PostMapping("/refresh")
    public ResponseEntity refresh(@RequestBody TokenModel.LOGIN tokenModel, HttpServletRequest req) throws WrongCredentialsException {
        String ipAddress = req.getHeader("x-forward-for");
        if (ipAddress == null) {
            ipAddress = req.getRemoteAddr();
        }
        String type = "Auth-Login";
        return ResponseEntity.ok(ResponseModel.builder().data(userService.getTokenByRefreshToken(tokenModel.getRefreshToken(), ipAddress)).type(type).build());
    }


    @ApiOperation(value = "check email(send) ", notes = "이메일 인증, 인증번호 전송")
    @PreAuthorize("permitAll")
    @PutMapping("/email")
    public ResponseEntity sendEmail(@Valid @RequestBody UserModel.OnlyEmail userModel) throws AlreadyExistException {

        String type = "Auth-Send-Email";
        userService.sendSignUpMailToken(userModel);
        return ResponseEntity.ok(ResponseModel.builder().data(true).type(type).build());
    }

    @ApiOperation(value = "check email(auth)", notes = "이메일 인증, 인증번호 확인")
    @PreAuthorize("permitAll")
    @PostMapping("/email")
    public ResponseEntity checkEmail(@Valid @RequestBody UserModel.EmailToken userModel) throws InvalidArgumentException, BadRequestException {
        String type = "Auth-Check-Email";
        //code는 valid 체크를 여기서함
        if (userModel.getToken() == null) {
            throw new IllegalArgumentException("code is null");
        }
        userService.checkSignUpMailToken(userModel);
        return ResponseEntity.ok(ResponseModel.builder().type(type).build());
    }

    @ApiOperation(value = "check phone(send)", notes = "핸드폰 인증, 미구현")
    @PreAuthorize("permitAll")
    @PutMapping("/phone")
    public ResponseEntity sendEmail(@Valid @RequestBody UserModel.PhoneEmailToken userModel, HttpServletRequest req) {

        String type = "Auth-Send-Phone";
        userService.sendSignUpPhoneToken(userModel);
        return ResponseEntity.ok(ResponseModel.builder().data("ok").type(type).build());
    }

    @ApiOperation(value = "check phone(auth)", notes = "핸드폰 인증, 미구현")
    @PreAuthorize("permitAll")
    @PostMapping("/phone")
    public ResponseEntity checkEmail(@Valid @RequestBody UserModel.PhoneEmailToken userModel, HttpServletRequest req) throws InvalidArgumentException, BadRequestException {
        String type = "Auth-Check-Phone";
        //code는 valid 체크를 여기서함
        if (userModel.getToken() == null) {
            throw new IllegalArgumentException("code is null");
        }
        userService.checkSignUpPhoneToken(userModel);
        return ResponseEntity.ok(ResponseModel.builder().type(type).build());
    }
}
