package com.em.yzzdemo.sign;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.em.yzzdemo.BaseActivity;
import com.em.yzzdemo.R;
import com.em.yzzdemo.bean.UserEntity;
import com.em.yzzdemo.main.MainActivity;
import com.em.yzzdemo.sql.ContactsDao;
import com.em.yzzdemo.utils.ConstantsUtils;
import com.em.yzzdemo.utils.SPUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Geri on 2016/11/25.
 */

public class SignInActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.sign_input_user) EditText signInputUser;
    @BindView(R.id.sign_input_pwd) EditText signInputPwd;
    @BindView(R.id.bt_signIn) Button btSignIn;
    @BindView(R.id.tv_signUp) TextView tvSignUp;
    private ProgressDialog mDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (EMClient.getInstance().isLoggedInBefore()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.activity_signin);
        mActivity = this;
        ButterKnife.bind(mActivity);
        initData();

    }

    private void initData() {
        //下划线
        tvSignUp.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        btSignIn.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //登录
            case R.id.bt_signIn:
                signIn();
                break;
            //注册
            case R.id.tv_signUp:
                startActivity(new Intent(mActivity,SignUpActivity.class));
                finish();
                break;
        }
    }

    //登录
    public void signIn(){
        mDialog = new ProgressDialog(mActivity);
        mDialog.setMessage(getResources().getString(R.string.sign_in_dialog));
        mDialog.show();
        final String username = signInputUser.getText().toString().trim();
        String pwd = signInputPwd.getText().toString().trim();
        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(pwd)){
            Toast.makeText(this, getResources().getString(R.string.sign_in_input_null), Toast.LENGTH_SHORT).show();
            mDialog.dismiss();
            return;
        }
        //登录回调
        EMClient.getInstance().login(username, pwd, new EMCallBack() {
            @Override
            public void onSuccess() {
                // 登录成功，把用户名保存在本地（可以不保存，根据自己的需求）
                SPUtil.put(mActivity, ConstantsUtils.ML_SHARED_USERNAME, username);
                //同步群组到本地
                try {
                    EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                //登录成功后，将所有会话和群组加载到内存
                EMClient.getInstance().chatManager().loadAllConversations();
                EMClient.getInstance().groupManager().loadAllGroups();
                //获取服务器联系人
                try {
                    List<String> userList = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    UserEntity userEntity= null;
                    for(int i=0; i<userList.size();i++){
                        userEntity = new UserEntity(userList.get(i));
                        ContactsDao.getInstance(mActivity).saveUser(userEntity);
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                mDialog.dismiss();
                startActivity(new Intent(mActivity,MainActivity.class));
                finish();
            }

            @Override
            public void onError(int i, String s) {
                mDialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mActivity, "登录聊天服务器失败", Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDialog != null){
            mDialog.dismiss();
        }
    }
}
