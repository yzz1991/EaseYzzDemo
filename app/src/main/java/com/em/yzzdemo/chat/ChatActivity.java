package com.em.yzzdemo.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.em.yzzdemo.BaseActivity;
import com.em.yzzdemo.R;
import com.em.yzzdemo.utils.ConstantsUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Geri on 2016/11/27.
 */

public class ChatActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.chat_toolbar)
    Toolbar toolbar;
    @BindView(R.id.chat_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.iv_emoji)
    ImageView emojiView;
    @BindView(R.id.ed_context)
    EditText mContextView;
    @BindView(R.id.iv_voice)
    ImageView mVoiceView;
    @BindView(R.id.iv_send)
    ImageView mSendView;
    private ChatMorePopWindow morePopWindow;
    private EMConversation.EMConversationType mConversationType;
    private EMConversation mConversation;
    private String chatId;
    // 是否发送原图
    private boolean isOrigin = true;
    private String id;
    private String userName;
    private EMGroup group;
    private EMMessageListener mMessageListener;
    private ChatMessageAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        mActivity = this;

        initView();

    }

    private void initView() {
        chatId = getIntent().getStringExtra(ConstantsUtils.CHAT_ID);
        mConversationType = (EMConversation.EMConversationType) getIntent().getExtras().get(ConstantsUtils.CHAT_TYPE);
        mConversation = EMClient.getInstance().chatManager().getConversation(chatId, mConversationType, true);
        id = mConversation.conversationId();
        userName = mConversation.getUserName();
        //如果是群组设置群组名称，否则设置单聊名称
        if (mConversation.getType().equals(EMConversation.EMConversationType.GroupChat)) {
            group = EMClient.getInstance().groupManager().getGroup(id);
            if(mConversation.isGroup() && group != null){
                //设置title
                toolbar.setTitle(group.getGroupName());
            }
        } else {
            toolbar.setTitle(userName);
        }
        //声明toolbar
        setSupportActionBar(toolbar);
        //设置back图标
        toolbar.setNavigationIcon(R.mipmap.arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //toolbar字体颜色
        toolbar.setTitleTextColor(0xffffffff);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
        //输入框的监听
        mContextView.addTextChangedListener(textWatcher);
        mSendView.setOnClickListener(this);
        //设置消息监听
        setMessageListener();

        mAdapter = new ChatMessageAdapter(mActivity,id);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);


    }



    //输入框的监听
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String text = mContextView.getText().toString().trim();
            if(TextUtils.isEmpty(text)){
                mVoiceView.setVisibility(View.VISIBLE);
                mSendView.setVisibility(View.GONE);
            }else{
                mVoiceView.setVisibility(View.GONE);
                mSendView.setVisibility(View.VISIBLE);
            }

        }
    };
    //各种点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //发送文本消息
            case R.id.iv_send:
                String text = mContextView.getText().toString().trim();
                if(TextUtils.isEmpty(text)){
                    Toast.makeText(this, "输入内容为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendTextMessage(text);
                mContextView.setText("");
                mAdapter.refreshMessageData();
                mAdapter.notifyDataSetChanged();
                break;
            //发送语音消息
            case R.id.iv_voice:

                break;
            //发送emoji表情
            case R.id.iv_emoji:

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //设定菜单各按钮的点击动作
    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_call:
                    Toast.makeText(mActivity, "call", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_video:
                    Toast.makeText(mActivity, "video", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_more:
                    Toast.makeText(mActivity, "more", Toast.LENGTH_SHORT).show();
                    morePopWindow = new ChatMorePopWindow(mActivity, itemsOnClick);
                    morePopWindow.showPopupWindow(toolbar);
                    break;
            }
            return true;
        }
    };

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            morePopWindow.dismiss();
//            morePopWindow.backgroundAlpha(mActivity,1f);
            switch (v.getId()) {
                case R.id.item_camera:

                    break;
                case R.id.item_picture:

                    break;
                case R.id.item_location:

                    break;
                case R.id.item_file:

                    break;
            }
        }

    };

    /**
     * 发送文本消息
     */
    private void sendTextMessage(String content) {
        // 创建一条文本消息
        EMMessage textMessage = EMMessage.createTxtSendMessage(content, id);
        sendMessage(textMessage);
    }


    //发送图片消息
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
        sendMessage(voiceMessage);

    }

    /**
     * 发送位置信息
     */
    private void sendLocationMessage(double latitude, double longitude, String locationAddress) {
        //latitude为纬度，longitude为经度，locationAddress为具体位置内容
        EMMessage locationmessage = EMMessage.createLocationSendMessage(latitude, longitude, locationAddress, id);
        sendMessage(locationmessage);

    }


    /**
     * 最终调用发送信息方法
     *
     * @param message 需要发送的消息
     */
    private void sendMessage(final EMMessage message) {

        // 设置不同的会话类型
        if (mConversationType == EMConversation.EMConversationType.Chat) {
            message.setChatType(EMMessage.ChatType.Chat);
        } else if (mConversationType == EMConversation.EMConversationType.GroupChat) {
            message.setChatType(EMMessage.ChatType.GroupChat);
        } else if (mConversationType == EMConversation.EMConversationType.ChatRoom) {
            message.setChatType(EMMessage.ChatType.ChatRoom);
        }
        /**
         *  调用sdk的消息发送方法发送消息，发送消息时要尽早的设置消息监听，防止消息状态已经回调，
         *  但是自己没有注册监听，导致检测不到消息状态的变化
         *  所以这里在发送之前先设置消息的状态回调
         */
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i("message", "发送成功");
            }

            @Override
            public void onError(final int i, final String s) {
                Log.i("message", "发送失败");
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        // 发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
    }
    /**
     * 注册消息监听
     */
    @Override
    protected void onStart() {
        super.onStart();
        EMClient.getInstance().chatManager().addMessageListener(mMessageListener);
    }
    /**
     * 接收消息的监听
     */
    private void setMessageListener() {
        mMessageListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
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
                    if (chatId.equals(username)) {
                        mAdapter.refreshMessageData();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                            }
                        });

                    }
                }
            }
            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {

            }
        };
    }

    /**
     * 移除消监听
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(mMessageListener);
    }
}
