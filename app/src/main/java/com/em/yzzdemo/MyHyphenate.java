package com.em.yzzdemo;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import com.em.yzzdemo.bean.UserEntity;
import com.em.yzzdemo.chat.ChatActivity;
import com.em.yzzdemo.chat.MessageUtils;
import com.em.yzzdemo.contacts.ContactsManager;
import com.em.yzzdemo.event.ApplyForEvent;
import com.em.yzzdemo.event.ConnectionEvent;
import com.em.yzzdemo.event.MessageEvent;
import com.em.yzzdemo.event.UserEntityEvent;
import com.em.yzzdemo.notification.Notifier;
import com.em.yzzdemo.sql.ContactsDao;
import com.em.yzzdemo.sql.ContactsHelper;
import com.em.yzzdemo.utils.ConstantsUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;

import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by Geri on 2016/11/25.
 */

public class MyHyphenate {

    // 上下文对象
    private Context mContext;

    // MLHyphenate 单例对象
    private static MyHyphenate instance;
    // 记录sdk是否初始化
    private boolean isInit;
    private EMConnectionListener mConnectionListener;
    private EMMessageListener mMessageListener;
    private EMContactListener mContactListener;
    private EMGroupChangeListener mGroupChangeListener;

    private MyHyphenate() {
    }

    //单例类，用来初始化环信的sdk,返回当前类的实例
    public static MyHyphenate getInstance() {
        if (instance == null) {
            instance = new MyHyphenate();
        }
        return instance;
    }

    /**
     * 初始化环信的SDK
     *
     * @param context 上下文菜单
     * @return 返回初始化状态是否成功
     */
    public synchronized boolean initHyphenate(Context context) {
        mContext = context;
        // 获取当前进程 id 并取得进程名
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        /**
         * 如果app启用了远程的service，此application:onCreate会被调用2次
         * 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
         * 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返回
         */
        if (processAppName == null || !processAppName.equalsIgnoreCase(context.getPackageName())) {
            // 则此application的onCreate 是被service 调用的，直接返回
            return true;
        }
        if (isInit) {
            return isInit;
        }
        mContext = context;

        // 调用初始化方法初始化sdk
        EMClient.getInstance().init(mContext, initOptions());

        // 设置开启debug模式
        EMClient.getInstance().setDebugMode(true);

        // 初始化全局监听
        initGlobalListener();

        // 初始化完成
        isInit = true;
        return isInit;
    }

    private EMOptions initOptions() {

        //SDK初始化的一些配置
        EMOptions options = new EMOptions();
        // 是否启动 DNS 信息配置
        options.enableDNSConfig(true);
        // 设置Appkey，如果配置文件已经配置，这里可以不用设置
        //options.setAppKey("meyki#elife");
        // 设置自动登录
        options.setAutoLogin(true);
        // 设置是否按照服务器时间排序，false按照本地时间排序
        options.setSortMessageByServerTime(false);
        // 设置是否需要发送已读回执
        options.setRequireAck(true);
        // 设置是否需要发送回执
        options.setRequireDeliveryAck(true);
        // 设置是否需要服务器收到消息确认
        options.setRequireServerAck(true);
        // 收到好友申请是否自动同意，如果是自动同意就不会收到好友请求的回调，因为sdk会自动处理，默认为true
        options.setAcceptInvitationAlways(false);
        // 设置是否自动接收加群邀请，如果设置了当收到群邀请会自动同意加入
        options.setAutoAcceptGroupInvitation(true);
        // 设置（主动或被动）退出群组时，是否删除群聊聊天记录
        options.setDeleteMessagesAsExitGroup(false);
        // 设置是否允许聊天室的Owner 离开并删除聊天室的会话
        options.allowChatroomOwnerLeave(true);

        return options;
    }

    /**
     * 根据Pid获取当前进程的名字，一般就是当前app的包名
     *
     * @param pid 进程的id
     * @return 返回进程的名字
     */
    private String getAppName(int pid) {
        String processName = null;
        ActivityManager activityManager =
                (ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
        List list = activityManager.getRunningAppProcesses();
        Iterator i = list.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info =
                    (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pid) {
                    // 根据进程的信息获取当前进程的名字
                    processName = info.processName;
                    // 返回当前进程名
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 没有匹配的项，返回为null
        return null;
    }

    /**
     * 初始化全局监听
     */
    private void initGlobalListener() {
        //网络监听
        setConnectionListener();
        //全局消息监听
        setMessageListener();
        //全局联系人监听
        setContactListener();
        // 设置全局的群组变化监听
        setGroupChangeListener();
    }

    /**
     * ---------------------------------------------------------------网络监听---------------------------------------------------------------------
     */
    private void setConnectionListener() {
        mConnectionListener = new EMConnectionListener() {
            @Override
            public void onConnected() {
                ConnectionEvent connectionEvent = new ConnectionEvent();
                connectionEvent.setType(ConstantsUtils.CONNECTION_TYPE_SUCCESS);
                EventBus.getDefault().post(connectionEvent);
            }

            @Override
            public void onDisconnected(int errorCode) {
                ConnectionEvent connectionEvent = new ConnectionEvent();
                if(errorCode == EMError.USER_REMOVED){
                    Log.i("MyHyphenate", "显示帐号已经被移除");
                    connectionEvent.setType(ConstantsUtils.CONNECTION_TYPE_USER_REMOVED);
                    signOut(null);
                }else if (errorCode == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    Log.i("MyHyphenate", "显示帐号在其他设备登录");
                    connectionEvent.setType(ConstantsUtils.CONNECTION_TYPE_USER_LOGIN_ANOTHER_DEVICE);
                    signOut(null);
                } else {
                    connectionEvent.setType(ConstantsUtils.CONNECTION_TYPE_NOT_USE);
                    Log.i("MyHyphenate", "当前网络不可用，请检查网络设置");
                }
                EventBus.getDefault().post(connectionEvent);
            }
        };
        EMClient.getInstance().addConnectionListener(mConnectionListener);
    }

    /**
     * --------------------------------------------------------------------全局消息监听-----------------------------------------------------------------
     */
    private void setMessageListener() {
        mMessageListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                ActivityManager am = (ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
                ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
                if(cn.getClassName().equals(ChatActivity.class.getName())){
                    return;
                }
                for(EMMessage message : list){
                    MessageEvent messageEvent = new MessageEvent();
                    messageEvent.setMessage(message);
                    messageEvent.setStatus(message.status());
                    EventBus.getDefault().post(messageEvent);
                }

                if (list.size() > 1) {
                    // 收到多条新消息，发送一条消息集合的通知
                    Notifier.getInstance().sendNotificationMessageList(list);
                } else {
                    // 只有一条消息，发送单条消息的通知
                    Notifier.getInstance().sendNotificationMessage(list.get(0));
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {
                ActivityManager am = (ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
                ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
                if(cn.getClassName().equals(ChatActivity.class.getName())){
                    return;
                }
                for (EMMessage cmdMessage : list) {
                    EMCmdMessageBody body = (EMCmdMessageBody) cmdMessage.getBody();

                    // 使用 EventBus 发布消息，可以被订阅此类型消息的订阅者监听到
                    MessageEvent event = new MessageEvent();
                    event.setMessage(cmdMessage);
                    event.setStatus(cmdMessage.status());
                    EventBus.getDefault().post(event);

                    // 判断是不是撤回消息的透传
                    if (body.action().equals(ConstantsUtils.ML_ATTR_RECALL)) {
                        MessageUtils.receiveRecallMessage(cmdMessage);
                    }
                }
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
        EMClient.getInstance().chatManager().addMessageListener(mMessageListener);
    }

    /**
     * ----------------------------------------------------------全局联系人监听----------------------------------------------------------------------
     */
    private void setContactListener() {
        mContactListener = new EMContactListener() {
            /**
             * 监听到好友被添加
             * @param s  被添加的用户
             */
            @Override
            public void onContactAdded(String s) {
                UserEntity userEntity = new UserEntity(s);
                ContactsDao.getInstance(mContext).saveUser(userEntity);
                EventBus.getDefault().post(new UserEntityEvent());
            }

            /**
             * 监听到删除好友
             * @param s  被删除的好友
             */
            @Override
            public void onContactDeleted(String s) {
                UserEntity userEntity = new UserEntity(s);
                ContactsDao.getInstance(mContext).saveUser(userEntity);
                EventBus.getDefault().post(new UserEntityEvent());
            }
            /**
             * 收到对方添加好友申请
             *
             * @param s 发送好友申请者 username
             * @param s1 申请理由
             */
            @Override
            public void onContactInvited(String s, String s1) {
                //根据申请人的username和当前时间组成msgId
                String msgId = s + System.currentTimeMillis();
                //创建一条消息用来保存申请信息
                EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
                //将理由保存为内容直接显示
                EMTextMessageBody body = new EMTextMessageBody("您有新的申请与通知信息");
                message.addBody(body);
                //被申请者
                message.setAttribute(ConstantsUtils.ML_ATTR_USERNAME,s);
                //申请原因
                message.setAttribute(ConstantsUtils.ML_ATTR_REASON,s1);
                //申请与通知类型
                message.setAttribute(ConstantsUtils.ML_ATTR_TYPE, ConstantsUtils.APPLY_TYPE_USER);
                //申请状态
                message.setAttribute(ConstantsUtils.ML_ATTR_STATUS,"");
                //设置发送者
                message.setFrom(ConstantsUtils.CONVERSATION_APPLY);
                //设置消息id
                message.setMsgId(msgId);
                //将消息保存到本地和数据库
                EMClient.getInstance().chatManager().saveMessage(message);

                // 调用发送通知栏提醒方法，提醒用户查看申请通知
//                Notifier.getInstance().sendNotificationMessage(message);

                // 使用 EventBus 发布消息，通知订阅者申请与通知信息有变化
                MessageEvent messageEvent = new MessageEvent();
                messageEvent.setMessage(message);
                EventBus.getDefault().post(messageEvent);

            }
            /**
             * 对方同意了自己的好友申请
             *
             * @param s 对方的 username
             */
            @Override
            public void onContactAgreed(String s) {
                //根据申请人的username和当前时间组成msgId
                String msgId = s + System.currentTimeMillis();
                //创建一条消息用来保存申请信息
                EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
                //将理由保存为内容直接显示
                EMTextMessageBody body = new EMTextMessageBody("您有新的申请与通知信息");
                message.addBody(body);
                //被申请者
                message.setAttribute(ConstantsUtils.ML_ATTR_USERNAME,s);
                //设置理由
                message.setAttribute(ConstantsUtils.ML_ATTR_REASON,"对方已同意您的好友申请");
                //申请与通知类型
                message.setAttribute(ConstantsUtils.ML_ATTR_TYPE,ConstantsUtils.APPLY_TYPE_USER);
                //设置申请状态
                message.setAttribute(ConstantsUtils.ML_ATTR_STATUS,"已同意");
                //设置发送者
                message.setFrom(ConstantsUtils.CONVERSATION_APPLY);
                //消息ID
                message.setMsgId(msgId);
                //将消息保存到本地和数据库
                EMClient.getInstance().chatManager().sendMessage(message);
                // 调用发送通知栏提醒方法，提醒用户查看申请通知
                Notifier.getInstance().sendNotificationMessage(message);

                // 使用 EventBus 发布消息，通知订阅者申请与通知信息有变化
                ApplyForEvent event = new ApplyForEvent();
                event.setMessage(message);
                EventBus.getDefault().post(event);
            }
            /**
             * 对方拒绝了自己的好友申请
             *
             * @param s 对方的 username
             */
            @Override
            public void onContactRefused(String s) {
                //根据申请人的username和当前时间组成msgId
                String msgId = s + System.currentTimeMillis();
                //创建一条消息用来保存申请信息
                EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
                //将理由保存为内容直接显示
                EMTextMessageBody body = new EMTextMessageBody("您有新的申请与通知信息");
                message.addBody(body);
                //被申请者
                message.setAttribute(ConstantsUtils.ML_ATTR_USERNAME,s);
                //设置理由
                message.setAttribute(ConstantsUtils.ML_ATTR_REASON,"对方已拒绝您的好友申请");
                //申请与通知类型
                message.setAttribute(ConstantsUtils.ML_ATTR_TYPE,ConstantsUtils.APPLY_TYPE_USER);
                //设置申请状态
                message.setAttribute(ConstantsUtils.ML_ATTR_STATUS,"已拒绝");
                //设置发送者
                message.setFrom(ConstantsUtils.CONVERSATION_APPLY);
                //消息ID
                message.setMsgId(msgId);
                //将消息保存到本地和数据库
                EMClient.getInstance().chatManager().sendMessage(message);
                // 调用发送通知栏提醒方法，提醒用户查看申请通知
                Notifier.getInstance().sendNotificationMessage(message);

                // 使用 EventBus 发布消息，通知订阅者申请与通知信息有变化
                ApplyForEvent event = new ApplyForEvent();
                event.setMessage(message);
                EventBus.getDefault().post(event);

            }
        };
        EMClient.getInstance().contactManager().setContactListener(mContactListener);
    }

    /**
     * ------------------------------------------------------------全局的群组变化监听--------------------------------------------------------------------------------
     */
    private void setGroupChangeListener() {
        mGroupChangeListener = new EMGroupChangeListener() {
            //收到加入群组的邀请
            @Override
            public void onInvitationReceived(String s, String s1, String s2, String s3) {

            }
            //收到加群申请
            @Override
            public void onApplicationReceived(String s, String s1, String s2, String s3) {

            }
            //加群申请被同意
            @Override
            public void onApplicationAccept(String s, String s1, String s2) {


            }
            // 加群申请被拒绝
            @Override
            public void onApplicationDeclined(String s, String s1, String s2, String s3) {

            }
            //群组邀请被接受
            @Override
            public void onInvitationAccepted(String s, String s1, String s2) {

            }
            //群组邀请被拒绝
            @Override
            public void onInvitationDeclined(String s, String s1, String s2) {

            }

            //当前用户被管理员移除出群组
            @Override
            public void onUserRemoved(String s, String s1) {

            }

            @Override
            public void onGroupDestroyed(String s, String s1) {
                //群组被解散
            }

            /**
             * 自动同意加入群组 sdk会先加入这个群组，并通过此回调通知应用
             *
             * @param s 收到邀请加入的群组id
             * @param s1 邀请者
             * @param s2 邀请信息
             */
            @Override
            public void onAutoAcceptInvitationFromGroup(String s, String s1, String s2) {
                //
            }
        };
        EMClient.getInstance().groupManager().addGroupChangeListener(mGroupChangeListener);
    }

    /**
     * 重置app操作，主要是在退出登录时清除内存
     */
    private void resetApp() {
        //数据库清空
        ContactsHelper.getInstance(mContext).resetDBHelper();
        //Dao清空
        ContactsDao.getInstance(mContext).resetDatabase();
        //map清空
        ContactsManager.getInstance(mContext).resetUserMap();
    }

    //退出登录
    public void signOut(final EMCallBack callBack){
        resetApp();
        EMClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                if(callBack != null){
                    callBack.onSuccess();
                }
            }

            @Override
            public void onError(int i, String s) {
                if(callBack != null){
                    callBack.onError(i,s);
                }
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

}
