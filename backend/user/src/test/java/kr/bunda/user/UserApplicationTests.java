package kr.bunda.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.bunda.core.model.ResponseModel;
import kr.bunda.user.dto.TokenModel;
import kr.bunda.user.dto.UserModel;
import kr.bunda.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

/**
 * UserApplicationTests
 * - @SpringBootTest 옵션
 * -   WebEnviromemt : mock을 사용할건지 실제 포트를 열어서 테스트할지에 대한 옵션
 * -   Properties : server.port와 같은 스프링 프로퍼티를 설정
 * - @ActiveProfiles 로 Profile 선택 가능
 *
 * @author BeomHee Han
 * @since 2021-08-19
 **/
@SpringBootTest(properties = {"eureka.client.enabled=false", "bunda.email.expired=10"})
@AutoConfigureMockMvc
@Slf4j
class UserApplicationTests {


    @Autowired
    MockMvc mockMvc;

    ObjectMapper om = new ObjectMapper();

    //    @Test
    void emailAuth() throws Exception {
        UserModel.OnlyEmail email = new UserModel.OnlyEmail("kjhhjk1234@naver.com");
        mockMvc.perform(MockMvcRequestBuilders.put("/api/auth/email").content(om.writeValueAsString(email))).andExpect(MockMvcResultMatchers.status().isOk());
    }

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


    //    @Test
    void test() throws Exception {

        UserModel.SignUp sign = new UserModel.SignUp();
        sign.setAddress("서울");
        sign.setBirth("1990-11-21");
        sign.setMarketing(false);
        sign.setName("아무개");
        sign.setPassword("Q!2we3r4tTtc");
        sign.setPhoneNumber("010-0000-0000");
        sign.setEmail("testest@testest.com");

        String signContent = om.writeValueAsString(sign);
        mockMvc.perform(MockMvcRequestBuilders.put("/user")
                        .contentType(MediaType.APPLICATION_JSON).content(signContent))
                .andExpect(MockMvcResultMatchers.status().isOk());

        UserModel.Login login = new UserModel.Login();
        login.setPassword(sign.getPassword());
        login.setEmail(sign.getEmail());

        sign.setMarketing(true);

        String loginContent = om.writeValueAsString(login);
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON).content(loginContent))
                .andExpect(MockMvcResultMatchers.status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON).content(loginContent))
                .andExpect(MockMvcResultMatchers.status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON).content(loginContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> {
                    TokenModel.TokenDto token = om.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<ResponseModel<TokenModel.TokenDto>>() {
                            }).getData();
                    String modifyContent = om.writeValueAsString(sign);
                    mockMvc.perform(MockMvcRequestBuilders.post("/user").contentType(MediaType.APPLICATION_JSON).content(modifyContent).header("Authorization", "Bearer " + token.getAccessToken()))
                            .andExpect(MockMvcResultMatchers.status().isOk());
                    mockMvc.perform(MockMvcRequestBuilders.delete("/user").contentType(MediaType.APPLICATION_JSON).content(modifyContent).header("Authorization", "Bearer " + token.getAccessToken()))
                            .andExpect(MockMvcResultMatchers.status().isOk());
                });

    }

}
