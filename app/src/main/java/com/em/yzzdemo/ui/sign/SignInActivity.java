package com.em.yzzdemo.ui.sign;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.em.yzzdemo.R;
import com.em.yzzdemo.ui.main.MainActivity;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Geri on 2016/11/25.
 */

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.sign_input_user) EditText signInputUser;
    @BindView(R.id.sign_input_pwd) EditText signInputPwd;
    @BindView(R.id.bt_signIn) Button btSignIn;
    @BindView(R.id.tv_signUp) TextView tvSignUp;
    private ProgressDialog mDialog;
//    private EditText signInputUser;
//    private EditText signInputPwd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (EMClient.getInstance().isLoggedInBefore()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);
        initData();

    }

    private void initData() {
//        signInputUser = (EditText) findViewById(R.id.sign_input_user);
//        signInputPwd = (EditText) findViewById(R.id.sign_input_pwd);
//        Button btSignIn = (Button) findViewById(R.id.bt_signIn);
//        TextView tvSignUp = (TextView) findViewById(R.id.tv_signUp);
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
                startActivity(new Intent(this,SignUpActivity.class));
                finish();
                break;
        }
    }

    //登录
    public void signIn(){
        mDialog = new ProgressDialog(this);
        mDialog.setMessage(getResources().getString(R.string.sign_in_dialog));
        mDialog.show();
        String username = signInputUser.getText().toString().trim();
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
                //登录成功后，将所有会话和群组加载到内存
                EMClient.getInstance().chatManager().loadAllConversations();
                EMClient.getInstance().groupManager().loadAllGroups();
                startActivity(new Intent(SignInActivity.this,MainActivity.class));
                finish();
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(SignInActivity.this, "登录聊天服务器失败", Toast.LENGTH_SHORT).show();
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
