package com.em.yzzdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.em.yzzdemo.chat.ChatActivity;
import com.em.yzzdemo.sign.SignInActivity;
import com.hyphenate.chat.EMClient;

/**
 * Created by geri on 2017/9/29.
 */

public class StartActivity extends BaseActivity{

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        sharedPreferences = getSharedPreferences("login_User", MODE_PRIVATE);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if(!sharedPreferences.getString("user", "").equals("")){
                    if (EMClient.getInstance().isLoggedInBefore()) {
                        startActivity(new Intent(mActivity,ChatActivity.class));
                    }
                }else{
                    startActivity(new Intent(mActivity,SignInActivity.class));
                }
                finish();
            }
        }, 3000);

    }
}
