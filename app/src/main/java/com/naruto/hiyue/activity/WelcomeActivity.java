package com.naruto.hiyue.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.naruto.hiyue.MyApplication;
import com.naruto.hiyue.R;
import com.naruto.hiyue.base.BaseActivity;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String userId = MyApplication.getSharedPreferences().getString("userId", "");
                String password = MyApplication.getSharedPreferences().getString("password", "");
                if (userId != null && !userId.isEmpty() && password != null && !password.isEmpty()) {
                    LoginActivity.doLogin(WelcomeActivity.this, userId, password);
                } else {
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    WelcomeActivity.this.finish();
                }
            }
        }, 3000);
    }
}
