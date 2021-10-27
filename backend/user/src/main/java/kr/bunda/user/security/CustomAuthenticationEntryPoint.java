package kr.bunda.user.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.bunda.core.model.ErrorResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * 인증안된 상태로  들어갈때 인증 필요한 url 들어갈때, RestAPI에서는 필수
 * */
@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper om;

    public CustomAuthenticationEntryPoint(ObjectMapper om) {
        this.om = om;
    }


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.debug("CustomAuthenticationEntryPoint - start");

        //401 응답
        ErrorResponseModel er = ErrorResponseModel.builder()
                .type("/error/auth")
                .status(HttpStatus.UNAUTHORIZED.value())
                .title(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .detail("unauthorized")
                .instance(request.getPathInfo())
                .build();

        // 실패코드 403
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        // json 리턴 및 한글깨짐 수정.
        response.setContentType("application/json;charset=utf-8");

        response.getOutputStream().println(om.writeValueAsString(er));

        log.debug("CustomAuthenticationEntryPoint - end");

    }

}