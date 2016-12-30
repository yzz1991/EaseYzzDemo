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

    /**
     * 设置自己扩展的 key，包括会话对象{@link com.hyphenate.chat.EMConversation}扩展，
     * 以及消息{@link com.hyphenate.chat.EMMessage}扩展
     */
    // at(@)
    public static final String ML_ATTR_AT = "ml_attr_at";
    // 阅后即焚
    public static final String ML_ATTR_BURN = "ml_attr_burn";
    // 视频通话扩展
    public static final String ML_ATTR_CALL_VIDEO = "ml_attr_call_video";
    // 语音通话扩展
    public static final String ML_ATTR_CALL_VOICE = "ml_attr_call_voice";
    // 草稿
    public static final String ML_ATTR_DRAFT = "ml_attr_draft";
    // 群组id
    public static final String ML_ATTR_GROUP_ID = "ml_attr_group_id";
    // 最后时间
    public static final String ML_ATTR_LAST_TIME = "ml_attr_list_time";
    // 消息id
    public static final String ML_ATTR_MSG_ID = "ml_attr_msg_id";
    // 置顶
    public static final String ML_ATTR_PUSHPIN = "ml_attr_pushpin";
    // 理由
    public static final String ML_ATTR_REASON = "ml_attr_reason";
    // 撤回
    public static final String ML_ATTR_RECALL = "ml_attr_recall";
    // 状态
    public static final String ML_ATTR_STATUS = "ml_attr_status";
    // 类型
    public static final String ML_ATTR_TYPE = "ml_attr_type";
    // 会话未读
    public static final String ML_ATTR_UNREAD = "ml_attr_unread";
    // 用户名
    public static final String ML_ATTR_USERNAME = "ml_attr_username";
    // 输入状态
    public static final String ML_ATTR_INPUT_STATUS = "ml_attr_input_status";

    // 申请与通知类型
    public static final int APPLY_TYPE_USER = 0x00;      // 联系人申请
    public static final int APPLY_TYPE_GROUP = 0x01;     // 群组申请

    /**
     * 自定义申请与通知列表项点击与长按的 Action
     */
    public static final int ACTION_APPLY_FOR_CLICK = 0X00;
    public static final int ACTION_APPLY_FOR_AGREE = 0X10;
    public static final int ACTION_APPLY_FOR_REFUSE = 0X11;
    public static final int ACTION_APPLY_FOR_DELETE = 0X12;

    // 定义好友申请与通知的 Conversation Id
    public static final String CONVERSATION_APPLY = "conversation_apply";

    /**
     * 聊天消息类型
     * 首先是SDK支持的正常的消息类型，紧接着是扩展类型
     */
    public static final int MSG_TYPE_TEXT_SEND = 0x00;
    public static final int MSG_TYPE_TEXT_RECEIVED = 0x01;
    public static final int MSG_TYPE_IMAGE_SEND = 0x02;
    public static final int MSG_TYPE_IMAGE_RECEIVED = 0x03;
    public static final int MSG_TYPE_FILE_SEND = 0x04;
    public static final int MSG_TYPE_FILE_RECEIVED = 0x05;
    public static final int MSG_TYPE_VIDEO_SEND = 0x06;
    public static final int MSG_TYPE_VIDEO_RECEIVED = 0x07;
    public static final int MSG_TYPE_VOICE_SEND = 0x08;
    public static final int MSG_TYPE_VOICE_RECEIVED = 0x09;
    public static final int MSG_TYPE_LOCATION_SEND = 0x0A;
    public static final int MSG_TYPE_LOCATION_RECEIVED = 0x0B;
    // 撤回类型消息
    public static final int MSG_TYPE_SYS_RECALL = 0x10;
    // 通话类型消息
    public static final int MSG_TYPE_CALL_SEND = 0x11;
    public static final int MSG_TYPE_CALL_RECEIVED = 0x12;


    /**
     * 当界面跳转需要返回结果时，定义跳转请求码
     */
    public static final int REQUEST_CODE_CAMERA = 0x01;
    public static final int REQUEST_CODE_GALLERY = 0x02;
    public static final int REQUEST_CODE_VIDEO = 0x03;
    public static final int REQUEST_CODE_FILE = 0x04;
    public static final int REQUEST_CODE_LOCATION = 0x05;
    public static final int REQUEST_CODE_GIFT = 0x06;
    public static final int REQUEST_CODE_CONTACTS = 0x07;

    /**
     * 自定义聊天界面消息列表项的点击与长按 Action
     */
    public static final int ACTION_MSG_CLICK = 0X00;
    public static final int ACTION_MSG_RESEND = 0X01;
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
     * 保存数据到 {@link android.content.SharedPreferences}的 key
     */
    public static final String ML_SHARED_USERNAME = "ml_username";

    /**
     * 自定义一些错误码，表示一些固定的错误
     */
    // 撤回消息错误码，超过时间限制
    public static final int ERROR_I_RECALL_TIME = 5001;
    // 撤回消息错误文字描述
    public static final String ERROR_S_RECALL_TIME = "ml_max_time";


}
