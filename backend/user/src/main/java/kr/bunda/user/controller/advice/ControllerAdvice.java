package kr.bunda.user.controller.advice;

import kr.bunda.core.model.ErrorResponseModel;
import kr.bunda.core.model.ResponseModel;
import kr.bunda.user.exception.AlreadyExistException;
import kr.bunda.user.exception.BadRequestException;
import kr.bunda.user.exception.InvalidArgumentException;
import kr.bunda.user.exception.WrongCredentialsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Properties;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {
    Properties properties;

    @PostConstruct
    public void init() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("Error_Message.properties");
        properties = new Properties();
        properties.load(classPathResource.getInputStream());
    }

    @ExceptionHandler({AlreadyExistException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseModel custom(HttpServletRequest req, AlreadyExistException e) {
        log.warn("code {}, parameter : {}", e.getMessage(), e.getLog());
        return ResponseModel.builder().code(e.getMessage()).message(properties.getProperty(e.getMessage())).build();
    }

    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseModel custom(HttpServletRequest req, BadRequestException e) {
        log.warn("code {}, parameter : {}", e.getMessage(), e.getLog());
        return ResponseModel.builder().code(e.getMessage()).message(properties.getProperty(e.getMessage())).build();
    }

    @ExceptionHandler({InvalidArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseModel custom(HttpServletRequest req, InvalidArgumentException e) {
        log.warn("code {}, parameter : {}", e.getMessage(), e.getLog());
        return ResponseModel.builder().code(e.getMessage()).message(properties.getProperty(e.getMessage())).build();
    }

    @ExceptionHandler({WrongCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseModel custom(HttpServletRequest req, WrongCredentialsException e) {
        log.warn("code {}, parameter : {}", e.getMessage(), e.getLog());
        return ResponseModel.builder().code(e.getMessage()).message(properties.getProperty(e.getMessage())).build();
    }


    @ExceptionHandler({IllegalStateException.class})
    public ResponseEntity<ErrorResponseModel> custom(HttpServletRequest req, Exception e) {
        log.error("Error!", e);
        String type = "Global Error";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(ErrorResponseModel.builder().instance(req.getRequestURI()).status(status.value()).title(status.name()).detail(status.getReasonPhrase()).type(type).build());
    }

    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseModel> customVaild(HttpServletRequest req, Exception e) {

        //TODO: SCG오면 아이피 헤더에 추가
        if (e instanceof MethodArgumentNotValidException) {
            log.error("Host " + req.getRemoteHost() + " " + validation((MethodArgumentNotValidException) e));
        } else {
            log.error("Error!", e);
        }
        String type = "Parameter Error";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(ErrorResponseModel.builder().instance(req.getRequestURI()).status(status.value()).title(status.name()).detail(status.getReasonPhrase()).type(type).build());
    }

    //권한 에러
    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponseModel> handleAuth(HttpServletRequest req, AccessDeniedException e) {
        log.warn("Authorization Error! " + e.getMessage());
        String type = "Authorization Error";
        HttpStatus status = HttpStatus.FORBIDDEN;
        return ResponseEntity.status(status).body(ErrorResponseModel.builder().instance(req.getRequestURI()).status(status.value()).title(status.name()).detail(status.getReasonPhrase()).type(type).build());
    }

    //404
    @ExceptionHandler({NoHandlerFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponseModel> noHandler(HttpServletRequest req, Exception e) {
        String type = "404 Error";
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(ErrorResponseModel.builder().instance(req.getRequestURI()).status(status.value()).title(status.name()).detail(status.getReasonPhrase()).type(type).build());
    }

    //예상 못한 에러처리
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponseModel> all(HttpServletRequest req, Exception e) {
        log.error("Error!", e);
        String type = "Global Error";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(ErrorResponseModel.builder().instance(req.getRequestURI()).status(status.value()).title(status.name()).detail(status.getReasonPhrase()).type(type).build());
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
