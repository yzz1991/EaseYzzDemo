package com.em.yzzdemo.chat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.em.yzzdemo.BaseActivity;
import com.em.yzzdemo.MyHyphenate;
import com.em.yzzdemo.R;
import com.em.yzzdemo.notification.Notifier;
import com.em.yzzdemo.sign.SignInActivity;
import com.em.yzzdemo.utils.BitmapUtil;
import com.em.yzzdemo.utils.ConstantsUtils;
import com.em.yzzdemo.utils.DateUtil;
import com.em.yzzdemo.utils.FileUtil;
import com.em.yzzdemo.widgit.RecordView;
import com.em.yzzdemo.widgit.Recorder;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import static android.view.View.GONE;

/**
 * Created by Geri on 2016/11/27.
 */

public class ChatActivity extends BaseActivity implements View.OnClickListener, ChatMessageAdapter.OnMessageItemClickListener {

    private final String TAG = this.getClass().getSimpleName();

    @BindView(R.id.chat_toolbar) Toolbar toolbar;
    @BindView(R.id.chatactivity_root) View chatActivityRoot;
    @BindView(R.id.chat_recyclerView) RecyclerView recyclerView;
    @BindView(R.id.iv_picture) ImageView pictureView;
    @BindView(R.id.ed_context) EditText mContextView;
    @BindView(R.id.iv_voice) ImageView mVoiceView;
    @BindView(R.id.iv_send) ImageView mSendView;
    @BindView(R.id.chat_refreshView) SwipeRefreshLayout mRefreshView;
    @BindView(R.id.recordView) RecordView mRecordView;

    // 聊天界面消息刷新类型
    private final int MSG_REFRESH_ALL = 0;
    private final int MSG_REFRESH_INSERTED = 1;
    private final int MSG_REFRESH_INSERTED_MORE = 2;
    private final int MSG_REFRESH_REMOVED = 3;
    private final int MSG_REFRESH_CHANGED = 4;

    private EMConversation.EMConversationType mConversationType;
    private EMConversation mConversation;
    //    private String chatId;
    // 是否发送原图
    private boolean isOrigin = true;
    private String id;
    private EMMessageListener mMessageListener;
    private ChatMessageAdapter mAdapter;
    //图片
    private Uri mCameraImageUri = null;
    private LinearLayoutManager manager;
    private InputMethodManager inputManager;
    private SharedPreferences sharedPreferences;
    private String kefuUserId;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog photoModeDialog;
    private SharedPreferences.Editor editor;
    private List<String> imagePathList = new ArrayList<>();
    private int imagePathListSize = 0;
    private Bitmap bitmap;
    private String imagePath;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        mActivity = this;

        sharedPreferences = getSharedPreferences("login_User", MODE_PRIVATE);
        editor = sharedPreferences.edit();
//        if (sharedPreferences.getString("chatId", "").equals("")) {
//            kefuUserId = getKefuUser();
//            editor.putString("chatId", kefuUserId);
//            editor.commit();
//        } else {
//            kefuUserId = sharedPreferences.getString("chatId", "");
//        }
        initView();
    }

//    /**
//     * 随机选择一个客服账号
//     */
//    private String getKefuUser() {
//        String[] allKefuUser = { "pinganagent101", "pinganagent102", "pinganagent103" };
//        int index = (int) (Math.random() * allKefuUser.length);
//        Log.e("index :", index + "");
//        String kefuUser = allKefuUser[index];
//        Log.e("kefuUser :", kefuUser);
//        return kefuUser;
//    }

    private void initView() {
        kefuUserId = "pinganagent101";
        mConversationType = EMConversation.EMConversationType.Chat;
        mConversation = EMClient.getInstance().chatManager().getConversation(kefuUserId, mConversationType, true);
        id = mConversation.conversationId();

        toolbar.setTitle(id);
        //声明toolbar
        setSupportActionBar(toolbar);
        //设置back图标
        toolbar.setNavigationIcon(R.mipmap.chat_header);
        //toolbar字体颜色
        toolbar.setTitleTextColor(0xffffffff);
        //设置toolbar点击监听
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
        //输入框的监听
        mContextView.addTextChangedListener(textWatcher);
        mSendView.setOnClickListener(this);
        pictureView.setOnClickListener(this);
        mVoiceView.setOnClickListener(this);

        if (mRecordView.getVisibility() == View.VISIBLE) {
            mRecordView.setVisibility(GONE);
        }
        //设置消息监听
        setMessageListener();

        //设置下拉加载更多消息
        refreshData();

        mAdapter = new ChatMessageAdapter(mActivity, kefuUserId);
        manager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.scrollToPosition(mConversation.getAllMessages().size() - 1);
        mAdapter.setOnItemClickListener(this);
        setVoiceListener();

        //        inputManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        //        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //监听recyclerView的Touch事件，隐藏软键盘
        recyclerView.setOnTouchListener(new View.OnTouchListener() {

            @Override public boolean onTouch(View v, MotionEvent event) {
                //隐藏软键盘
                InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                // 隐藏录音view
                mRecordView.setVisibility(GONE);
                return false;
            }
        });

        if (mRecordView.getVisibility() == View.VISIBLE) {
            mContextView.clearFocus();
        }
        mContextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {//获得焦点
                    //隐藏录音view及弹出软键盘
                    mRecordView.setVisibility(GONE);
                    InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
                } else {//失去焦点
                    //显示录音view及隐藏软键盘
                    mRecordView.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
    }

    private void setVoiceListener() {
        mRecordView.setRecordCallback(new RecordView.MLRecordCallback() {
            @Override public void onCancel() {
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        Toast.makeText(mActivity, "录音取消", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override public void onFailed(int error) {
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        Toast.makeText(mActivity, "录音失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override public void onStart() {

            }

            @Override public void onSuccess(String path, int time) {
                int voiceLength = time / 1000;
                if (voiceLength < 1) {
                    runOnUiThread(new Runnable() {
                        @Override public void run() {
                            Toast.makeText(mActivity, "录音时间过短，请重新录制", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                sendVoiceMessage(path, voiceLength);
            }
        });
    }

    //下拉加载更多消息
    private void refreshData() {
        //设置下拉控件的颜色
        mRefreshView.setColorSchemeResources(R.color.refresh_one, R.color.refresh_two, R.color.refresh_three);
        mRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                // 防止在下拉刷新的时候，当前界面关闭导致错误
                if (mActivity.isFinishing()) {
                    return;
                }
                // 只有当前会话不为空时才可以下拉加载更多，否则会出现错误
                if (mConversation.getAllMessages().size() > 0) {
                    // 加载更多消息到当前会话的内存中
                    List<EMMessage> messages =
                            mConversation.loadMoreMsgFromDB(mConversation.getAllMessages().get(0).getMsgId(), 20);
                    if (messages.size() > 0) {
                        refreshInsertedMore(0, messages.size());
                    } else {
                        Toast.makeText(mActivity, "没有更多消息了", Toast.LENGTH_SHORT).show();
                    }
                }
                // 取消刷新布局
                mRefreshView.setRefreshing(false);
            }
        });
    }

    /**
     * 消息点击事件，不同的消息有不同的触发
     *
     * @param message 点击的消息
     */
    @Override public void onItemAction(EMMessage message, int action) {
        switch (action) {
            case ConstantsUtils.ACTION_MSG_CLICK:
                //图片消息点击查看大图
                if (message.getType() == EMMessage.Type.IMAGE) {
                    // 图片
                    Intent intent = new Intent();
                    intent.setClass(mActivity, BigImageActivity.class);
                    // 将被点击的消息ID传递过去
                    intent.putExtra(ConstantsUtils.EXTRA_MSG_ID, message.getMsgId());
                    mActivity.startActivity(intent);
                }
                break;
        }
    }

    //输入框的监听
    private TextWatcher textWatcher = new TextWatcher() {

        @Override public void afterTextChanged(Editable s) {

        }

        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
            String text = mContextView.getText().toString().trim();
            if (TextUtils.isEmpty(text)) {
                mVoiceView.setVisibility(View.VISIBLE);
                mSendView.setVisibility(GONE);
            } else {
                mVoiceView.setVisibility(GONE);
                mSendView.setVisibility(View.VISIBLE);
            }
        }
    };

    //各种点击事件
    @Override public void onClick(View view) {
        switch (view.getId()) {
            //发送文本消息
            case R.id.iv_send:
                String text = mContextView.getText().toString().trim();
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(this, "输入内容为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendTextMessage(text);
                mContextView.setText("");
                break;
            //发送语音消息
            case R.id.iv_voice:
                InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                if (mRecordView.getVisibility() == GONE) {
                    mRecordView.setVisibility(View.VISIBLE);
                    mContextView.clearFocus();
                } else if (mRecordView.getVisibility() == View.VISIBLE) {
                    mRecordView.setVisibility(GONE);
                }
                break;
            //发送图片
            case R.id.iv_picture:
                selectPhotoMode();
                InputMethodManager im = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(view.getWindowToken(), 0);
                break;
        }
    }

    /**
     * 弹出选择图片发方式，是使用相机还是图库
     */
    private void selectPhotoMode() {
        String[] menus = {
                mActivity.getString(R.string.menu_chat_camera), mActivity.getString(R.string.menu_chat_gallery)
        };
        if (alertDialogBuilder == null) {
            alertDialogBuilder = new AlertDialog.Builder(mActivity);
        }
        // 设置弹出框 title
        //        alertDialogBuilder.setTitle(activity.getString(R.string.dialog_title_select_photo_mode));
        // 设置弹出框的菜单项及点击事件
        alertDialogBuilder.setItems(menus, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // 打开相机直接拍照
                        if (Build.VERSION.SDK_INT >= 23) {
                            int cameraPermission = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA);
                            if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(mActivity, new String[] { Manifest.permission.CAMERA },
                                        ConstantsUtils.REQUEST_CODE_ASK_CAMERA);
                                return;
                            } else {
                                openCamera();
                            }
                        } else {
                            openCamera();
                        }
                        break;
                    case 1:
                        // 打开图库选择图片
                        openGallery();
                        break;
                    default:
                        openGallery();
                        break;
                }
            }
        });
        photoModeDialog = alertDialogBuilder.create();
        photoModeDialog.show();
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //设定菜单按钮退出登录
    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {

        @Override public boolean onMenuItemClick(MenuItem menuItem) {
            if (menuItem.getItemId() == R.id.action_logout) {
                logoutUser();
            }
            return true;
        }
    };

    /**
     * 弹出选择图片发方式，是使用相机还是图库
     */
    private void logoutUser() {
        String[] menus = { "退出当前账号" };
        if (alertDialogBuilder == null) {
            alertDialogBuilder = new AlertDialog.Builder(mActivity);
        }
        // 设置弹出框的菜单项及点击事件
        alertDialogBuilder.setItems(menus, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    MyHyphenate.getInstance().signOut(new EMCallBack() {
                        @Override public void onSuccess() {
                            runOnUiThread(new Runnable() {
                                @Override public void run() {
                                    Toast.makeText(mActivity, "退出成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                            editor.clear();
                            editor.commit();
                            startActivity(new Intent(mActivity, SignInActivity.class));
                        }

                        @Override public void onError(int i, String s) {
                            runOnUiThread(new Runnable() {
                                @Override public void run() {
                                    Toast.makeText(mActivity, "退出失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override public void onProgress(int i, String s) {

                        }
                    });
                }
            }
        });
        photoModeDialog = alertDialogBuilder.create();
        photoModeDialog.show();
    }

    /**
     * 发送文本消息
     */
    private void sendTextMessage(String content) {
        // 创建一条文本消息
        EMMessage textMessage = EMMessage.createTxtSendMessage(content, id);
        sendMessage(textMessage);
    }

    //发送单张图片消息
    private void sendImageMessage(String path) {
        /**
         * 根据图片路径创建一条图片消息，需要三个参数，
         * path     图片路径
         * isOrigin 是否发送原图
         * mChatId  接收者
         */
        EMMessage imgMessage = EMMessage.createImageSendMessage(path, isOrigin, id);
        sendMessage(imgMessage);
    }

    /**
     * 发送语音消息
     *
     * @param path 语音文件的路径
     */
    private void sendVoiceMessage(String path, int time) {
        EMMessage voiceMessage = EMMessage.createVoiceSendMessage(path, time, id);
        if (voiceMessage == null) {
            Toast.makeText(mActivity, "录音失败", Toast.LENGTH_SHORT).show();
            mRecordView.cancelRecordVoice();
        } else {
            sendMessage(voiceMessage);
        }
    }

    /**
     * 最终调用发送信息方法
     *
     * @param message 需要发送的消息
     */
    private void sendMessage(final EMMessage message) {

        /**
         *  调用sdk的消息发送方法发送消息，发送消息时要尽早的设置消息监听，防止消息状态已经回调，
         *  但是自己没有注册监听，导致检测不到消息状态的变化
         *  所以这里在发送之前先设置消息的状态回调
         */
        message.setMessageStatusCallback(new EMCallBack() {
            @Override public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        Toast.makeText(mActivity, "发送成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override public void onError(final int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        Toast.makeText(mActivity, "发送失败 " + i + s, Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("error", "发送失败 " + i + s);
            }

            @Override public void onProgress(int i, String s) {
                Log.e("error", "发送中 " + i + s);
            }
        });
        // 发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
        // 刷新 UI 界面
        refreshInserted(mConversation.getAllMessages().indexOf(message));
    }

    /**
     * ----------------------------------- 聊天界面刷新 ------------------------------------------
     * 刷新聊天界面 Handler
     */
    Handler handler = new Handler() {
        @Override public void handleMessage(Message msg) {
            mAdapter.refreshAll();
            recyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
//            int what = msg.what;
//            int position = msg.arg1;
//            int count = msg.arg2;
//            switch (what) {
//                case MSG_REFRESH_ALL:
//                    mAdapter.refreshAll();
//                    break;
//                case MSG_REFRESH_INSERTED:
//                    mAdapter.refreshInserted(position);
//                    recyclerView.smoothScrollToPosition(position);
//                    break;
//                case MSG_REFRESH_INSERTED_MORE:
//                    mAdapter.refreshInsertedMore(position, count);
//                    recyclerView.smoothScrollToPosition(position);
//                    break;
//                case MSG_REFRESH_REMOVED:
//                    mAdapter.refreshRemoved(position);
//                    break;
//                case MSG_REFRESH_CHANGED:
//                    mAdapter.refreshChanged(position);
//                    break;
//            }
        }
    };

    /**
     * 有新消息来时的刷新方法
     *
     * @param position 数据添加位置
     */
    private void refreshInserted(int position) {
        Message msg = handler.obtainMessage(MSG_REFRESH_INSERTED);
        msg.arg1 = position;
        handler.sendMessage(msg);
    }

    /**
     * 加载更多消息时的刷新方法
     *
     * @param position 数据添加位置
     * @param count 数据添加数量
     */
    private void refreshInsertedMore(int position, int count) {
        Message msg = handler.obtainMessage(MSG_REFRESH_INSERTED_MORE);
        msg.arg1 = position;
        msg.arg2 = count;
        handler.sendMessage(msg);
    }

    /**
     * 打开系统图库，去进行选择图片
     */
    private void openGallery() {

        MultiImageSelector.create(mActivity)
                .showCamera(false) // 是否显示相机. 默认为显示
                .count(6) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                .single() // 单选模式
                .multi() // 多选模式, 默认模式;
                .start(mActivity, ConstantsUtils.REQUEST_CODE_GALLERY);
    }

    /**
     * 打开相机去拍摄图片发送
     */
    private void openCamera() {
        // 定义拍照后图片保存的路径以及文件名
        String imagePath = FileUtil.getDCIM() + "IMG" + DateUtil.getDateTimeNoSpacing() + ".jpg";
        Log.e("imagePath", imagePath);
        // 激活相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以用，可用进行存储
        if (FileUtil.hasSdcard()) {
            // 根据文件路径解析成Uri
            mCameraImageUri = Uri.fromFile(new File(imagePath));
            // 将Uri设置为媒体输出的目标，目的就是为了等下拍照保存在自己设定的路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraImageUri);
        }
        // 根据 Intent 启动一个带有返回值的 Activity，这里启动的就是相机，返回选择图片的地址
        mActivity.startActivityForResult(intent, ConstantsUtils.REQUEST_CODE_CAMERA);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ConstantsUtils.REQUEST_CODE_CAMERA:
                bitmap = BitmapUtil.getimage(mCameraImageUri.getPath(), 40);
                String path = mCameraImageUri.getPath();
                String image = path.substring(path.lastIndexOf("/")+1);
                imagePath = FileUtil.saveFile(bitmap, FileUtil.getDCIM()+"TEMP_"+image);
                // 相机拍摄的图片
                sendImageMessage(imagePath);

                break;
            case ConstantsUtils.REQUEST_CODE_GALLERY:
                if(resultCode == RESULT_OK && data != null){
                    // 获取返回的图片列表
                    imagePathList = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    Log.e("path", imagePathList.toString());
                    for(int i = 0; i <= imagePathList.size()-1; i++){
                        bitmap = BitmapUtil.getimage(imagePathList.get(i), 40);
                        String titlePath = imagePathList.get(i).substring(0,imagePathList.get(i).lastIndexOf("/")+1);
                        String imageName = imagePathList.get(i).substring(imagePathList.get(i).lastIndexOf("/")+1);
                        imagePath = FileUtil.saveFile(bitmap, titlePath + "TEMP_"+ imageName);
                        if(imagePath != null){
                            sendImageMessage(imagePath);
                        }else if (imagePath == null || imagePath.equals("null")) {
                            Toast toast = Toast.makeText(mActivity, "找不到图片", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return;
                        }else {
                            File file = new File(imagePath);
                            if (!file.exists()) {
                                Toast toast = Toast.makeText(mActivity, "找不到图片", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                return;
                            }
                            sendImageMessage(imagePath);
                        }
                    }

                }
                break;
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ConstantsUtils.REQUEST_CODE_ASK_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    openCamera();
                } else {
                    // Permission Denied
                    Toast.makeText(mActivity, "CALL_PHONE Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 重写父类的onNewIntent方法，防止打开两个聊天界面
     *
     * @param intent 带有参数的intent
     */
    @Override protected void onNewIntent(Intent intent) {
        String id = intent.getStringExtra(ConstantsUtils.CHAT_ID);
        // 判断 intent 携带的数据是否是当前聊天对象
        if (kefuUserId.equals(id)) {
            super.onNewIntent(intent);
        } else {
            finish();
            startActivity(intent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 注册消息监听
     */
    @Override protected void onStart() {
        super.onStart();
        EMClient.getInstance().chatManager().addMessageListener(mMessageListener);
    }

    //    @Override
    //    protected void onPause() {
    //        super.onPause();
    //        mRecordView.cancelRecordVoice();
    //    }

    @Override protected void onStop() {
        super.onStop();
    }

    /**
     * ---------------------------------------------------------------接收消息的监听----------------------------------------------------------------------
     */
    private void setMessageListener() {
        mMessageListener = new EMMessageListener() {
            @Override public void onMessageReceived(List<EMMessage> list) {
                boolean isNotify = false;
                // 循环遍历当前收到的消息
                for (EMMessage message : list) {
                    String username = "";
                    if (mConversationType == EMConversation.EMConversationType.Chat) {
                        username = message.getFrom();
                    } else {
                        username = message.getTo();
                    }
                    // 判断消息是否是当前会话的消息
                    if (kefuUserId.equals(username)) {
                        // 设置消息为已读
                        mConversation.markMessageAsRead(message.getMsgId());
                        // 刷新界面
                        refreshInserted(mConversation.getAllMessages().indexOf(message));
                    } else {
                        // 不发送通知
                        isNotify = true;
                    }
                }
                if (isNotify) {
                    // 如果消息不是当前会话的消息发送通知栏通知
                    Notifier.getInstance().sendNotificationMessageList(list);
                }
            }

            @Override public void onCmdMessageReceived(List<EMMessage> list) {

            }

            @Override public void onMessageRead(List<EMMessage> list) {

            }

            @Override public void onMessageDelivered(List<EMMessage> list) {

            }

            @Override public void onMessageRecalled(List<EMMessage> list) {
                Log.i(TAG, "onMessageRecalled");
            }

            @Override public void onMessageChanged(EMMessage message, Object o) {
                Log.i(TAG, "onMessageChanged" + message);
            }
        };
    }

    /**
     * 移除消监听
     */
    @Override protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(mMessageListener);
    }
}
