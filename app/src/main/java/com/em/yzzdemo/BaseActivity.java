package com.em.yzzdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.em.yzzdemo.event.ConnectionEvent;
import com.em.yzzdemo.sign.SignInActivity;
import com.em.yzzdemo.utils.ConstantsUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Geri on 2016/11/28.
 */

public class BaseActivity extends AppCompatActivity {
    protected BaseActivity mActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = this;

    }

    @Subscribe(threadMode = ThreadMode.MAIN) public void onEventbus(ConnectionEvent event){
        switch (event.getType()){
            case ConstantsUtils.CONNECTION_TYPE_SUCCESS:
                break;

            case ConstantsUtils.CONNECTION_TYPE_USER_REMOVED:
                Toast.makeText(mActivity, "账号已被后台移除", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(mActivity, SignInActivity.class));
                break;

            case ConstantsUtils.CONNECTION_TYPE_USER_LOGIN_ANOTHER_DEVICE:
                Toast.makeText(mActivity, "账号已被踢", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(mActivity, SignInActivity.class));
                break;

            case ConstantsUtils.CONNECTION_TYPE_NOT_USE:
                Toast.makeText(mActivity, "目前网络不可用", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(mActivity);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(mActivity);
    }
}
