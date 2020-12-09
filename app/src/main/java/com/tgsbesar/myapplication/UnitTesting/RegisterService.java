package com.tgsbesar.myapplication.UnitTesting;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tgsbesar.myapplication.database.Preferences;
import com.tgsbesar.myapplication.registerLogin.Register;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class RegisterService {
    private FirebaseAuth firebaseAuth;

    public void register(final RegisterView view, String email, String password, final RegisterCallback callback){
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            firebaseAuth.getCurrentUser().sendEmailVerification()
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            callback.onError();
                                        }
                                    })
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                callback.onSuccess(true);
                                                view.showErrorResponse("Register berhasil, silahkan verifikasi email");
                                                view.startMainActivity(email);
                                            }else{
                                                callback.onError();
                                                view.showRegisterError("Register gagal");
                                            }
                                        }
                                    });
                        } else{
                            callback.onError();
                            view.showRegisterError("Email sudah digunakan akun lain");
                        }
                    }
                });
    }

    public Boolean getValid(final RegisterView view, String email, String password){
        final Boolean[] bool = new Boolean[1];
        register(view, email, password, new RegisterCallback() {
            @Override
            public void onSuccess(boolean value) {
                bool[0] = true;
            }
            @Override
            public void onError() {
                bool[0] = false;
            }
        });
        return bool[0];
    }
}
