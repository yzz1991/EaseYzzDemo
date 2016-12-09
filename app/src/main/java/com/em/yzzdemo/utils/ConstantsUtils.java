package com.em.yzzdemo.utils;

/**
 * Created by Geri on 2016/12/6.
 */

public class ConstantsUtils {

    //会话Id
    public static final String CHAT_ID = "chatId";
    //会话类型
    public static final String CHAT_TYPE = "chatType";

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
    // 名片消息
    public static final int MSG_TYPE_CARD_SEND = 0x13;
    public static final int MSG_TYPE_CARD_RECEIVED = 0x14;
    // 礼物消息
    public static final int MSG_TYPE_GIFT_SEND = 0x15;
    public static final int MSG_TYPE_GIFT_RECEIVED = 0x16;
}
