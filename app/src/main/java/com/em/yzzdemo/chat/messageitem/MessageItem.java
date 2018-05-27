package com.em.yzzdemo.chat.messageitem;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.em.yzzdemo.callback.EaseChatRowVoicePlayClickListener;
import com.em.yzzdemo.chat.ChatMessageAdapter;
import com.em.yzzdemo.utils.ConstantsUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;

import java.io.File;

/**
 * Created by Geri on 2016/12/8.
 */

public abstract class MessageItem extends LinearLayout {
    //上下文对象
    protected Context mContext;
    protected Activity mActivity;
    //适配器对象
    protected ChatMessageAdapter mAdapter;
    // item 类型
    protected int mViewType;
    // 当前 Item 需要处理的 EMMessage 对象
    protected EMMessage mMessage;

    //布局内容填充者
    protected LayoutInflater mInflater;

    protected View bubbleLayout;
    // 显示用户名
    protected TextView usernameView;
    // 显示时间
    protected TextView msgTimeView;
    // 显示文本内容、文件消息就显示文件名
    protected TextView contentView;
    //显示图片内容
    protected ImageView imageContextView;
    //显示发送图片的进度
    protected ProgressBar progressBar;
    protected TextView percentageView;
    protected ImageView statusView;
    //显示语音图片
    protected ImageView voiceImage;
    //显示语音长度
    protected TextView voiceTime;
    // 弹出框
    protected AlertDialog.Builder alertDialogBuilder;
    protected AlertDialog alertDialog;
    protected EMCallBack messageSendCallback;

    public MessageItem(Context context, ChatMessageAdapter adapter, int viewType) {
        super(context);
        mContext = context;
        mActivity = (Activity) context;
        mAdapter = adapter;
        mViewType = viewType;
        mInflater = LayoutInflater.from(context);
        onInflateView();
        onBubbleListener();
    }

    protected void onBubbleListener(){
        if(bubbleLayout != null){
            //item点击
            bubbleLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mMessage.getType() == EMMessage.Type.VOICE){
                        new EaseChatRowVoicePlayClickListener(mMessage, voiceImage, mAdapter, mActivity).onClick(bubbleLayout);
                    }else if(mMessage.getType() == EMMessage.Type.IMAGE){
                        mAdapter.onItemAction(mMessage, ConstantsUtils.ACTION_MSG_CLICK);
                    }
                }
            });
            //item长按
            bubbleLayout.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClick();
                    return false;
                }
            });
        }
    }

    /**
     * set callback for sending message
     */
    protected void setMessageSendCallback(){
        if(messageSendCallback == null){
            messageSendCallback = new EMCallBack() {

                @Override
                public void onSuccess() {
                    if(mMessage.getType().equals(EMMessage.Type.IMAGE)){
                        EMImageMessageBody body = (EMImageMessageBody) mMessage.getBody();
                        String localUrl = body.getLocalUrl();
                        String fileName = body.getFileName();
                        File file = new File(localUrl);
                        String imageName = fileName.substring(fileName.indexOf("_") + 1);
                        String localPath = file.getParent() + "/" + imageName;
                        file.delete();
                        body.setLocalUrl(localPath);
                        EMClient.getInstance().chatManager().updateMessage(mMessage);
                    }
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }

                @Override
                public void onProgress(final int progress, String status) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(percentageView != null)
                                percentageView.setText(progress + "%");

                        }
                    });
                }

                @Override
                public void onError(int code, String error) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mActivity, "消息发送失败", Toast.LENGTH_SHORT).show();
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            };
        }
        mMessage.setMessageStatusCallback(messageSendCallback);
    }

    /**
     * 因为每个item点击效果不同，所有需要在子类中重写
     */
    protected abstract void onItemLongClick();

    /**
     * 填充当前 Item，子类必须实现
     * 解析对应的xml 布局，填充当前 ItemView，并初始化控件
     */
    protected abstract void onInflateView();

    /**
     * 处理数据显示
     *
     * @param message 需要展示的 EMMessage 对象
     */
    public abstract void onSetupView(EMMessage message);

    @Override
    protected void onAttachedToWindow() {
        //        MLLog.i("onAttachedToWindow %s", mMessage.getMsgId());
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        //        MLLog.i("onDetachedFromWindow %s", mMessage.getMsgId());
        // 检查是否有弹出框，如果有则销毁，防止界面销毁时出现异常
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        super.onDetachedFromWindow();
    }
}
