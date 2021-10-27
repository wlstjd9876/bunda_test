package kr.bunda.user.service;

public interface MailService {
    void sendAuthMail(String email, String token);
}
