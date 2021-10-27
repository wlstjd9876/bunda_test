package kr.bunda.user.service.impl;

import kr.bunda.user.dto.PhoneModel;
import kr.bunda.user.service.PhoneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Service
@Slf4j
public class PhoneServiceImpl implements PhoneService {

    private boolean SEND_PHONE;

    PhoneServiceImpl(@Value("${bunda.phone.send}") boolean send_phone) {
        SEND_PHONE = send_phone;
    }

    @PostConstruct
    private void init() throws Exception {
    }

    private void send(PhoneModel.Send model) {
        Objects.requireNonNull(model);

        if (!SEND_PHONE) {
            log.info("mail 서비스 비활성화");
            return;
        }

    }

    //TODO :  핸드폰 미구현
    @Override
    public void sendAuthMail(String phone, String token) {
        log.info("핸드폰 전송 미구현 내용 : {}", token);
    }

    @Override
    public void sendForgotEmailToken(String phone, String token) {
        log.info("핸드폰 전송 미구현 내용 : {}", token);
    }
}