package com.tgsbesar.myapplication.UnitTesting;

public interface RegisterView {
    String getEmail();
    void showEmailError(String message);
    String getPassword();
    void showPasswordError(String message);
    void startMainActivity(String email);
    void showRegisterError(String message);
    void showErrorResponse(String message);
}
