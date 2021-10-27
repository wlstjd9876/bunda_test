package kr.bunda.user.service.impl;

import kr.bunda.user.dto.MailModel;
import kr.bunda.user.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

@Service
@Slf4j
public class MailServiceImpl implements MailService {

    private JavaMailSender mailSender;
    private MailModel.Send authMail;
    private boolean SEND_MAIL;

    MailServiceImpl(JavaMailSender mailSender, @Value("${bunda.email.send}") boolean send_mail) {
        this.mailSender = mailSender;
        SEND_MAIL = send_mail;
    }

    @PostConstruct
    private void init() throws Exception {
        ClassPathResource cpr1 = new ClassPathResource("templates/AuthMail.html");
        @SuppressWarnings("resource")
        BufferedReader br1 = new BufferedReader(new InputStreamReader(cpr1.getInputStream()));
        String line;
        String createContent = "";
        String createSubject = br1.readLine();
        while ((line = br1.readLine()) != null) {
            createContent += line;
        }
        authMail = MailModel.Send.builder().contents(createContent).subject(createSubject).build();
    }

    private void send(MailModel.Send mailDto) {
        Objects.requireNonNull(mailDto);

        if (!SEND_MAIL) {
            log.info("mail 서비스 비활성화");
            return;
        }
        try {
            MimeMessage createMimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper handler = new MimeMessageHelper(createMimeMessage, true, "UTF-8");
            handler.setTo(mailDto.getTo());
            handler.setFrom(mailDto.getFrom(), mailDto.getFromName());
            handler.setSubject(mailDto.getSubject());
            handler.setText(mailDto.getContents(), true);
            mailSender.send(createMimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException(e.getMessage() + " dto : " + mailDto);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage() + " dto : " + mailDto);
        }

    }

    @Override
    public void sendAuthMail(String email, String token) {
        send(
                MailModel.Send.builder()
                        .to(email)
                        .from("p-bunda@naver.com")
                        .contents(authMail.getContents().replace("authNumber", token))
                        .subject(authMail.getSubject()).build()
        );
    }


}