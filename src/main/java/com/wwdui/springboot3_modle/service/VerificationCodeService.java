package com.wwdui.springboot3_modle.service;

public interface VerificationCodeService {
    String generateAndStoreCode(String email);

    boolean verifyCode(String email, String code);

    void sendVerificationCode(String email, String code);
}
