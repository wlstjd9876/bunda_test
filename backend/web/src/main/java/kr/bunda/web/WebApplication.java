package kr.bunda.web;

import org.springframework.boot.SpringApplication;

//@ComponentScan(basePackages = "kr.bunda")
//@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
//@EnableGlobalMethodSecurity(
//        prePostEnabled = true,
//        securedEnabled = true,
//        jsr250Enabled = true)
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

}
