package com.em.yzzdemo.contacts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.em.yzzdemo.BaseActivity;
import com.em.yzzdemo.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Geri on 2016/12/15.
 */
public class AddContactsActivity extends BaseActivity {
    @BindView(R.id.addContacts_toolbarView)
    Toolbar mToolbarView;
    @BindView(R.id.addContacts_input)
    EditText mSearchView;
    @BindView(R.id.addContacts_bt)
    Button mButtonView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {

        mActivity = this;
        mToolbarView.setTitle("添加好友");
        setSupportActionBar(mToolbarView);
        mToolbarView.setNavigationIcon(R.mipmap.arrow_back);
        mToolbarView.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //toolbar字体颜色
        mToolbarView.setTitleTextColor(0xffffffff);
        mButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String addUsername = mSearchView.getText().toString().trim();
                if (TextUtils.isEmpty(addUsername)) {
                    Toast.makeText(mActivity, "输入不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override public void run() {
                        try {
                            EMClient.getInstance().contactManager().addContact(addUsername, "Add friends");
                            runOnUiThread(new Runnable() {
                                @Override public void run() {
                                    Toast.makeText(mActivity, "添加成功",
                                            Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            int errorCode = e.getErrorCode();
                            String errorMsg = e.getMessage();
                            Log.e("AddContactsActivity", "add contacts: error - " + errorCode + ", msg - " + errorMsg);
                            runOnUiThread(new Runnable() {
                                @Override public void run() {
                                    Toast.makeText(mActivity, "添加好友失败",
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }


}
