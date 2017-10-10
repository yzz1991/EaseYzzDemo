package com.em.yzzdemo.sign;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.em.yzzdemo.BaseActivity;
import com.em.yzzdemo.R;
import com.em.yzzdemo.chat.ChatActivity;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Geri on 2016/11/25.
 */

public class SignInActivity extends BaseActivity {

    @BindView(R.id.sign_input_user)
    EditText signInputUser;
    @BindView(R.id.bt_signIn)
    Button btSignIn;
    private ProgressDialog mDialog;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        mActivity = this;
        ButterKnife.bind(mActivity);

        sharedPreferences = getSharedPreferences("login_User", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }

    //登录
    public void signIn() {
        mDialog = new ProgressDialog(mActivity);
        mDialog.setMessage(getResources().getString(R.string.sign_in_dialog));
        mDialog.show();
        final String username = signInputUser.getText().toString().trim();
        final String pwd = username.substring(username.length() - 6);
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, getResources().getString(R.string.sign_in_input_null), Toast.LENGTH_SHORT).show();
            mDialog.dismiss();
            return;
        } else if (username.length() != 11) {
            Toast.makeText(this, "用户名必须是11位的手机号", Toast.LENGTH_SHORT).show();
            mDialog.dismiss();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(username, pwd);//同步方法
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 注册成功,登录
                            login(username, pwd);
                        }
                    });

                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int errorCode = e.getErrorCode();
                            String message = e.getMessage();
                            if(errorCode != EMError.USER_ALREADY_EXIST){
                                mDialog.dismiss();
                            }
                            Log.d("lzan13", String.format("sign up - errorCode:%d, errorMsg:%s", errorCode, e.getMessage()));
                            switch (errorCode) {
                                // 网络错误
                                case EMError.NETWORK_ERROR:
                                    Toast.makeText(mActivity, "网络错误 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                    break;
                                // 用户已存在
                                case EMError.USER_ALREADY_EXIST:
                                    login(username, pwd);
                                    break;
                                // 参数不合法，一般情况是username 使用了uuid导致，不能使用uuid注册
                                case EMError.USER_ILLEGAL_ARGUMENT:
                                    Toast.makeText(mActivity, "参数不合法，一般情况是username 使用了uuid导致，不能使用uuid注册 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                    break;
                                // 服务器未知错误
                                case EMError.SERVER_UNKNOWN_ERROR:
                                    Toast.makeText(mActivity, "服务器未知错误 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                    break;
                                //用户注册失败
                                case EMError.USER_REG_FAILED:
                                    Toast.makeText(mActivity, "账户注册失败 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Toast.makeText(mActivity, "ml_sign_up_failed code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    });
                }
            }
        }).start();
    }

    //登录方法
    public void login(final String username, String pwd) {
        //登录回调
        EMClient.getInstance().login(username, pwd, new EMCallBack() {
            @Override
            public void onSuccess() {

                editor.putString("user", username);
                editor.commit();
                //登录成功后，将所有会话和群组加载到内存
                EMClient.getInstance().chatManager().loadAllConversations();
                mDialog.dismiss();
                startActivity(new Intent(mActivity, ChatActivity.class));
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
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }
}
