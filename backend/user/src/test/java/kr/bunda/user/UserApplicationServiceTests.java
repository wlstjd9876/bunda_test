package kr.bunda.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.bunda.user.dto.UserModel;
import kr.bunda.user.exception.AlreadyExistException;
import kr.bunda.user.exception.BadRequestException;
import kr.bunda.user.exception.InvalidArgumentException;
import kr.bunda.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@SpringBootTest(properties = {"eureka.client.enabled=false", "bunda.email.expired=10", "bunda.email.send=false"})
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserApplicationServiceTests {

    ObjectMapper om = new ObjectMapper();

    @Autowired
    UserService userService;

    //    @Test
    void testTokenDelete() throws Exception {
        UserModel.OnlyEmail userModel = new UserModel.OnlyEmail("kjhhjk1234@naver.com");
        String authNumber = userService.sendSignUpMailToken(userModel);
        log.info("인증번호 : {}", authNumber);

        Thread.sleep(25000);
        userService.deleteEmailToken();

        UserModel.EmailToken checkModel = new UserModel.EmailToken();
        checkModel.setToken(authNumber);
        checkModel.setEmail("kjhhjk1234@naver.com");
        Throwable thrown = catchThrowable(() -> {
            userService.checkSignUpMailToken(checkModel);
        });
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("인증 코드가 존재하지 않음");

    }

    @Order(0)
    @Test
    void signUp() throws AlreadyExistException, InvalidArgumentException, BadRequestException {

        String token = userService.sendSignUpMailToken(new UserModel.OnlyEmail("kjhhjk1234@naver.com"));
        UserModel.EmailToken emailToken = new UserModel.EmailToken();
        emailToken.setEmail("kjhhjk1234@naver.com");
        emailToken.setToken(token);
        userService.checkSignUpMailToken(emailToken);

        UserModel.SignUp sign = new UserModel.SignUp();
        sign.setAddress("서울");
        sign.setBirth("1990-11-21");
        sign.setMarketing(false);
        sign.setName("아무개");
        sign.setPassword("Q!2we3r4tTtc");
        sign.setPhoneNumber("010-8033-0000");
        sign.setEmail("kjhhjk1234@naver.com");

        userService.create(sign);


    }

    @Order(1)
    @Test
    void findEmail() throws InvalidArgumentException, BadRequestException {
        String phone = "010-8033-0000";
        UserModel.OnlyPhone onlyPhone = new UserModel.OnlyPhone();
        onlyPhone.setPhoneNumber(phone);
        String token = userService.sendForgotMailToken(onlyPhone);

        UserModel.PhoneToken phoneToken = new UserModel.PhoneToken();
        phoneToken.setToken(token);
        phoneToken.setPhoneNumber(phone);
        UserModel.OnlyEmail onlyEmail = userService.checkForgotMailToken(phoneToken);
        assertThat(onlyEmail.getEmail().equals("kjhhjk1234@naver.com")).isTrue();
    }

}
