package com.tgsbesar.myapplication.UnitTesting;

import com.tgsbesar.myapplication.registerLogin.Register;

public class RegisterPresenter {
    private RegisterView view;
    private RegisterService service;
    private RegisterCallback callback;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public RegisterPresenter (RegisterView view, RegisterService service){
        this.view = view;
        this.service = service;
    }

    public void onRegisterClicked(){
        if (view.getEmail().isEmpty() && view.getPassword().isEmpty()) {
            view.showEmailError("Email tidak boleh kosong");
            view.showPasswordError("Password tidak boleh kosong");
            return;
        }else if(view.getEmail().isEmpty()){
            view.showEmailError("Email tidak boleh kosong");
            return;
        }else if(view.getPassword().isEmpty()){
            view.showPasswordError("Password tidak boleh kosong");
            return;
        } else if (!view.getEmail().toString().trim().matches(emailPattern)) {
            view.showEmailError("Email Tidak Valid");
            return;
        } else if (view.getPassword().length()<6) {
            view.showPasswordError("Password Tidak Boleh Kurang dari 6");
            return;
        }else{
            service.register(view, view.getEmail(), view.getPassword(), new RegisterCallback() {
                @Override
                public void onSuccess(boolean value) {

                }

                @Override
                public void onError() {

                }
            });
            return;
        }
    }
}
