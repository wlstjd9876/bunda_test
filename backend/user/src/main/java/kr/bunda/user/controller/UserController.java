package kr.bunda.user.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.bunda.core.model.ResponseModel;
import kr.bunda.user.dto.UserModel;
import kr.bunda.user.exception.AlreadyExistException;
import kr.bunda.user.exception.BadRequestException;
import kr.bunda.user.exception.InvalidArgumentException;
import kr.bunda.user.service.UserService;
import kr.bunda.user.type.UserServiceResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "Sign Up", notes = "회원가입")
    @PreAuthorize("permitAll")
    @PutMapping
    public ResponseEntity createUser(@Valid @RequestBody UserModel.SignUp userModel) throws InvalidArgumentException, BadRequestException, AlreadyExistException {
        String type = "User-Create";
        userService.create(userModel);
        return ResponseEntity.ok(ResponseModel.builder().code(UserServiceResult.COMPLETED.name()).data(userModel).type(type).build());
    }

    @ApiOperation(value = "update user", notes = "회원 수정")
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and #userModel.email==authentication.principal)")
    @PostMapping
    public ResponseEntity updateUser(Authentication authentication, @Valid @RequestBody UserModel.SignUp userModel) throws BadRequestException {
        String type = "User-Update";
        userService.update(userModel);
        return ResponseEntity.ok(ResponseModel.builder().type(type).build());
    }

    @ApiOperation(value = "delete user", notes = "회원 탈퇴")
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and #userModel.email==authentication.principal)")
    @DeleteMapping
    public ResponseEntity deleteUser(@Valid @RequestBody UserModel.OnlyEmail userModel) {
        String type = "User-Delete";
        userService.delete(userModel);
        return ResponseEntity.ok(ResponseModel.builder().data(userModel).type(type).build());
    }

    @ApiOperation(value = "get user", notes = "회원 정보")
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and #email==authentication.principal)")
    @GetMapping("/{email}")
    public ResponseEntity getUser(@ApiParam(value = "email", required = true) @PathVariable("email") String email, Authentication authentication) {
        String type = "User-Get";
        return ResponseEntity.ok(ResponseModel.builder().data(userService.getUser(email)).type(type).build());
    }

}
