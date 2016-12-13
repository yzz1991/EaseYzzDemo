package com.em.yzzdemo.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.em.yzzdemo.MyApplication;
import com.em.yzzdemo.R;
import com.em.yzzdemo.chat.ChatActivity;
import com.em.yzzdemo.main.MainActivity;
import com.em.yzzdemo.utils.ConstantsUtils;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.List;

/**
 * Created by Geri on 2016/12/13.
 */

public class Notifier {
    private Context mContext;
    // 通知栏提醒ID
    private int mMsgNotifyId = 5121;
    private static Notifier instance;
    private NotificationManager mNotificationManager = null;
    private NotificationCompat.Builder mBuilder = null;

    public Notifier() {
        mContext = MyApplication.getContext();
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(mContext);
        init();
    }

    public static Notifier getInstance(){
        if(instance == null){
            instance = new Notifier();
        }
        return instance;
    }

    private void init() {
        //设置通知小图标
        mBuilder.setSmallIcon(R.mipmap.app_icon);
        //设置通知优先级
        mBuilder.setPriority(Notification.PRIORITY_HIGH);
        //设置这个标志当用户单击面板就可以让通知将自动取消
        mBuilder.setAutoCancel(true);
        //向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
        mBuilder.setAutoCancel(true);
        /**
         * 设置默认提醒，默认的有声音，振动，三色灯提醒
         * Notification.DEFAULT_VIBRATE //添加默认震动提醒  需要 VIBRATE permission
         * Notification.DEFAULT_SOUND   // 添加默认声音提醒
         * Notification.DEFAULT_LIGHTS  // 添加默认三色灯提醒
         * Notification.DEFAULT_ALL     // 添加默认以上3种全部提醒
         */
        mBuilder.setDefaults(Notification.DEFAULT_ALL);
    }

    /**
     * 发送消息通知
     *
     * @param message 收到的消息，根据这个消息去发送通知
     */
    public void sendNotificationMessage(EMMessage message){
        String content = "";
        EMMessage.Type type = message.getType();
        switch (type){
            case TXT:
                EMTextMessageBody textBody = (EMTextMessageBody) message.getBody();
                content = textBody.getMessage().toString();
                break;
            case IMAGE:
                content = "[图片消息]";
                break;
            case FILE:
                content = "[文件消息]";
                break;
            case LOCATION:
                content = "[位置消息]";
                break;
            case VIDEO:
                content = "[视频消息]";
                break;
            case VOICE:
                content = "[语音消息]";
                break;
            case CMD:
                break;
        }
        mBuilder.setContentText(content);
        mBuilder.setContentTitle("EaseYzzDemo");
        // 设置通知栏点击意图（点击通知栏跳转到相应的页面）
        Intent intent = new Intent(mContext, ChatActivity.class);
        intent.putExtra(ConstantsUtils.CHAT_ID,message.getFrom());
        intent.putExtra(ConstantsUtils.CHAT_TYPE, message.getChatType());
        PendingIntent pIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(pIntent);
        // 通知首次出现在通知栏，带上升动画效果的（这里是一闪而过的，带有上升动画）
        mBuilder.setTicker("有一条新消息，别忘记看~");
        // 通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
        mBuilder.setWhen(System.currentTimeMillis());
        // 发送通知栏通知
        mNotificationManager.notify(mMsgNotifyId, mBuilder.build());
    }

    /**
     * 发送消息通知
     *
     * @param messages 收到的消息集合，根据这个集合去发送通知栏提醒
     */
    public void sendNotificationMessageList(List<EMMessage> messages) {
        mBuilder.setContentText(String.format("你有 %d 条新消息", messages.size()));

        mBuilder.setContentTitle("EaseYzzDemo");
        // 设置通知栏点击意图（点击通知栏跳转到相应的页面）
        Intent intent = new Intent(mContext, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(pIntent);
        // 通知首次出现在通知栏，带上升动画效果的（这里是一闪而过的，带有上升动画）
        mBuilder.setTicker("你有新消息");
        // 通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
        mBuilder.setWhen(System.currentTimeMillis());
        // 发送通知栏通知
        mNotificationManager.notify(mMsgNotifyId, mBuilder.build());
    }


}
