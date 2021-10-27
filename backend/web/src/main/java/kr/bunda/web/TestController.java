package kr.bunda.web;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
public class TestController {

    //권한 테스트
    @Secured({"ROLE_USER"})
    @GetMapping("/user")
    public String user(){
        return "user";
    }
    @Secured({"ROLE_ANONYMOUS"})
    @GetMapping("/guest")
    public String guest(){
        return "guest";
    }
    @Secured({"ROLE_ADMIN"})
    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }

    //에러테스트
    @GetMapping("/err")
    public String error(){
        throw new IllegalStateException("test");
    }


}
