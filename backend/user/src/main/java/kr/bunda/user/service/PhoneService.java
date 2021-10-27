package kr.bunda.user.service;

public interface PhoneService {
    void sendAuthMail(String phone, String token);

    void sendForgotEmailToken(String phone, String token);
}
