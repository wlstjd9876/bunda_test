package kr.bunda.user.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.bunda.core.model.ErrorResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 스프링 시큐리티 403 핸들러
 */
@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper om;

    public CustomAccessDeniedHandler(ObjectMapper om) {
        this.om = om;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        log.debug("CustomAccessDeniedHandler - start");

        //401 응답
        ErrorResponseModel er = ErrorResponseModel.builder()
                .type("/error/auth")
                .status(HttpStatus.FORBIDDEN.value())
                .title(HttpStatus.FORBIDDEN.getReasonPhrase())
                .detail("access denied")
                .instance(request.getContextPath())
                .build();

        // 실패코드 403
        response.setStatus(HttpStatus.FORBIDDEN.value());

        // json 리턴 및 한글깨짐 수정.
        response.setContentType("application/json;charset=utf-8");

        response.getOutputStream().println(om.writeValueAsString(er));

        log.debug("CustomAccessDeniedHandler - end");
    }

}