package kr.bunda.web;

import kr.bunda.core.model.ErrorResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

    @ExceptionHandler({IllegalStateException.class})
    public ErrorResponseModel custom(HttpServletRequest req, Exception e) {
        log.error("Error!", e);
        String type = "Global Error";
        return ErrorResponseModel.builder().instance(req.getRequestURI()).status(HttpStatus.INTERNAL_SERVER_ERROR.value()).title(HttpStatus.INTERNAL_SERVER_ERROR.name()).detail(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).type(type).build();
    }

    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class, MethodArgumentNotValidException.class})
    public ErrorResponseModel customVaild(HttpServletRequest req, Exception e) {

        //TODO: SCG오면 아이피 헤더에 추가
        if (e instanceof MethodArgumentNotValidException) {
            log.error("Host " + req.getRemoteHost() + " " + validation((MethodArgumentNotValidException) e));
        } else {
            log.error("Error!", e);
        }
        String type = "Parameter Error";
        return ErrorResponseModel.builder().instance(req.getRequestURI()).status(HttpStatus.BAD_REQUEST.value()).title(HttpStatus.BAD_REQUEST.name()).detail(HttpStatus.BAD_REQUEST.getReasonPhrase()).type(type).build();
    }

    //예상 못한 에러처리
    @ExceptionHandler({AccessDeniedException.class})
    public ErrorResponseModel handleAuth(HttpServletRequest req, Exception e) {
        log.error("Authorization Error!", e);
        String type = "Authorization Error";
        return ErrorResponseModel.builder().instance(req.getRequestURI()).status(HttpStatus.INTERNAL_SERVER_ERROR.value()).title(HttpStatus.INTERNAL_SERVER_ERROR.name()).detail(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).type(type).build();
    }

    //예상 못한 에러처리
    @ExceptionHandler({Exception.class})
    public ErrorResponseModel all(HttpServletRequest req, Exception e) {
        log.error("Error!", e);
        String type = "Global Error";
        return ErrorResponseModel.builder().instance(req.getRequestURI()).status(HttpStatus.INTERNAL_SERVER_ERROR.value()).title(HttpStatus.INTERNAL_SERVER_ERROR.name()).detail(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).type(type).build();
    }

    /**
     * validation error 체크 method
     *
     * @param errors
     */
    private String validation(MethodArgumentNotValidException errors) {

        StringBuilder sb = new StringBuilder();
        if (!ObjectUtils.isEmpty(errors) && errors.hasErrors()) {
            for (FieldError fieldError : errors.getFieldErrors()) {
                sb.append(" ");
                sb.append(fieldError.getField());
                sb.append("(은)는 ");
                sb.append(fieldError.getDefaultMessage());
                sb.append(" 입력된 값: ");
                sb.append(fieldError.getRejectedValue());
                sb.append("\n");
            }
        }
        return sb.toString();
    }


}
