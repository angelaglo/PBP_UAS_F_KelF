package com.tgsbesar.myapplication.UnitTesting;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.tgsbesar.myapplication.MainActivity;
import com.tgsbesar.myapplication.database.Preferences;
import com.tgsbesar.myapplication.registerLogin.popUpProfile;

public class ActivityUtil {
    private Context context;

    public ActivityUtil(Context context){
        this.context=context;
    }

    public void startMainActivity(String email){
        Preferences preferences = new Preferences(context);
        preferences.setEmailNorm(email);
        context.startActivity(new Intent(context, popUpProfile.class));
    }
}
