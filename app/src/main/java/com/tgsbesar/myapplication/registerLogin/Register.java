package com.tgsbesar.myapplication.registerLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.tgsbesar.myapplication.R;
import com.tgsbesar.myapplication.UnitTesting.ActivityUtil;
import com.tgsbesar.myapplication.UnitTesting.RegisterPresenter;
import com.tgsbesar.myapplication.UnitTesting.RegisterService;
import com.tgsbesar.myapplication.UnitTesting.RegisterView;

import static android.widget.Toast.LENGTH_SHORT;

public class Register extends AppCompatActivity implements RegisterView{

    private Button signUp, signIn;
    private TextInputLayout errorRM, errorPass;
    private FirebaseAuth firebaseAuth;
    private TextInputEditText txtRM, txtPass;
    private TextView loginNext;
    private RegisterPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signUp = findViewById(R.id.signUp);

        txtRM = findViewById(R.id.tv_rmRegister);
        txtPass = findViewById(R.id.tv_pass);
        errorRM = findViewById(R.id.layout_rmRegister);
        errorPass = findViewById(R.id.layout_pass);

        loginNext = findViewById(R.id.toLogin);

        presenter = new RegisterPresenter(this, new RegisterService());

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onRegisterClicked();
            }
        });

        loginNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this,Login.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateForm() {
        boolean result = true;

        if(TextUtils.isEmpty(txtRM.getText().toString())){
            result = false;
            txtRM.setError("Email harus diisi");

        }else{
            txtRM.setError(null);
        }

        if(TextUtils.isEmpty(txtPass.getText().toString())){
            result = false;
            txtPass.setError("Password harus diisi");

        }else{
            txtPass.setError(null);
        }

        return result;
    }

    @Override
    public String getEmail() {
        return txtRM.getText().toString();
    }

    @Override
    public void showEmailError(String message) {
        txtRM.setError(message);
    }

    @Override
    public String getPassword() {
        return txtPass.getText().toString();
    }

    @Override
    public void showPasswordError(String message) {
        txtPass.setError(message);
    }

    @Override
    public void startMainActivity(String email, String password) {
        new ActivityUtil(this).startMainActivity(email,password);
    }

    @Override
    public void showRegisterError(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorResponse(String message) {
        Toast.makeText(this, message, LENGTH_SHORT).show();
    }
}