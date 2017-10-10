package com.em.yzzdemo.utils;

/**
 * Created by Geri on 2016/12/6.
 */

public class ConstantsUtils {

    //会话Id
    public static final String CHAT_ID = "chatId";
    //会话类型
    public static final String CHAT_TYPE = "chatType";
    public static final String EXTRA_MSG_ID = "chat_msg_id";


    // 消息id
    public static final String ML_ATTR_MSG_ID = "ml_attr_msg_id";



    /**
     * 聊天消息类型
     * 首先是SDK支持的正常的消息类型，紧接着是扩展类型
     */
    public static final int MSG_TYPE_TEXT_SEND = 0x00;
    public static final int MSG_TYPE_TEXT_RECEIVED = 0x01;
    public static final int MSG_TYPE_IMAGE_SEND = 0x02;
    public static final int MSG_TYPE_IMAGE_RECEIVED = 0x03;
    public static final int MSG_TYPE_LOCATION_SEND = 0x04;
    public static final int MSG_TYPE_LOCATION_RECEIVED = 0x05;
    public static final int MSG_TYPE_FILE_SEND = 0x04;
    public static final int MSG_TYPE_FILE_RECEIVED = 0x05;
    public static final int MSG_TYPE_VIDEO_SEND = 0x04;
    public static final int MSG_TYPE_VIDEO_RECEIVED = 0x05;
    public static final int MSG_TYPE_VOICE_SEND = 0x08;
    public static final int MSG_TYPE_VOICE_RECEIVED = 0x09;



    /**
     * 当界面跳转需要返回结果时，定义跳转请求码
     */
    public static final int REQUEST_CODE_CAMERA = 0x01;
    public static final int REQUEST_CODE_GALLERY = 0x02;

    /**
     * 自定义聊天界面消息列表项的点击与长按 Action
     */
    public static final int ACTION_MSG_CLICK = 0X00;
    public static final int ACTION_MSG_COPY = 0X10;
    public static final int ACTION_MSG_FORWARD = 0X11;
    public static final int ACTION_MSG_DELETE = 0X12;
    public static final int ACTION_MSG_RECALL = 0X13;

    /**
     * 动态获取权限请求码
     */
    public static final int REQUEST_CODE_ASK_CAMERA = 0x01;

    /**
     * evnetbus 事件状态码
     */
    public static final String CONNECTION_TYPE_SUCCESS = "connection_type_success";
    public static final String CONNECTION_TYPE_USER_REMOVED = "connection_type_user_removed";
    public static final String CONNECTION_TYPE_USER_LOGIN_ANOTHER_DEVICE = "connection_type_user_login_another_device";
    public static final String CONNECTION_TYPE_NOT_USE = "connection_type_not_use";



    /**
     * 自定义一些错误码，表示一些固定的错误
     */
    // 撤回消息错误码，超过时间限制
    public static final int ERROR_I_RECALL_TIME = 5001;


}
