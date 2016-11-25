package com.em.yzzdemo.ui.sign;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.em.yzzdemo.R;
import com.em.yzzdemo.ui.main.MainActivity;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Geri on 2016/11/25.
 */
public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.sign_input_user) EditText signInputUser;
    @BindView(R.id.sign_input_pwd) EditText signInputPwd;
    @BindView(R.id.bt_signUp) Button btSignUp;
    @BindView(R.id.tv_signIn) TextView tvSignIn;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setContentView(R.layout.activity_signup);

        initData();

    }

    private void initData() {
        btSignUp.setOnClickListener(this);
        tvSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //注册
            case R.id.bt_signUp:
                signUp();
                break;
            //跳转登录页面
            case R.id.tv_signIn:
                startActivity(new Intent(this,SignInActivity.class));
                finish();
                break;
        }
    }

    public void signUp(){
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("注册中，请稍等...");
        mDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String username = signInputUser.getText().toString().trim();
                    final String pwd = signInputPwd.getText().toString().trim();
                    EMClient.getInstance().createAccount(username, pwd);//同步方法
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!SignUpActivity.this.isFinishing()){
                                mDialog.dismiss();
                            }
                            Toast.makeText(SignUpActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            signIn(username,pwd);
                        }
                    });

                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!SignUpActivity.this.isFinishing()){
                                mDialog.dismiss();
                            }
                            Toast.makeText(SignUpActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                            int errorCode = e.getErrorCode();
                            String message = e.getMessage();
                            Log.d("lzan13", String.format("sign up - errorCode:%d, errorMsg:%s", errorCode, e.getMessage()));
                            switch (errorCode) {
                                // 网络错误
                                case EMError.NETWORK_ERROR:
                                    Toast.makeText(SignUpActivity.this, "网络错误 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                    break;
                                // 用户已存在
                                case EMError.USER_ALREADY_EXIST:
                                    Toast.makeText(SignUpActivity.this, "用户已存在 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                    break;
                                // 参数不合法，一般情况是username 使用了uuid导致，不能使用uuid注册
                                case EMError.USER_ILLEGAL_ARGUMENT:
                                    Toast.makeText(SignUpActivity.this, "参数不合法，一般情况是username 使用了uuid导致，不能使用uuid注册 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                    break;
                                // 服务器未知错误
                                case EMError.SERVER_UNKNOWN_ERROR:
                                    Toast.makeText(SignUpActivity.this, "服务器未知错误 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                    break;
                                //用户注册失败
                                case EMError.USER_REG_FAILED:
                                    Toast.makeText(SignUpActivity.this, "账户注册失败 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Toast.makeText(SignUpActivity.this, "ml_sign_up_failed code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    });
                }
            }
        }).start();
    }

    //登录
    public void signIn(String username,String pwd){
        //登录回调
        EMClient.getInstance().login(username, pwd, new EMCallBack() {
            @Override
            public void onSuccess() {
                //登录成功后，将所有会话和群组加载到内存
                EMClient.getInstance().chatManager().loadAllConversations();
                EMClient.getInstance().groupManager().loadAllGroups();
                startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                finish();
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(SignUpActivity.this, "登录聊天服务器失败", Toast.LENGTH_SHORT).show();
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
