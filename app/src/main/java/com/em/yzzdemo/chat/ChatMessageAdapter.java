package com.em.yzzdemo.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.em.yzzdemo.chat.messageitem.FileMessageItem;
import com.em.yzzdemo.chat.messageitem.ImageMessageItem;
import com.em.yzzdemo.chat.messageitem.MessageItem;
import com.em.yzzdemo.chat.messageitem.TextMessageItem;
import com.em.yzzdemo.chat.messageitem.VoiceMessageItem;
import com.em.yzzdemo.utils.ConstantsUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by Geri on 2016/12/6.
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ChatMessageViewHolder>{
    private Context mContext;
    // 当前会话对象
    private EMConversation mConversation;
    private List<EMMessage> mMessageList;
    private String chatId;
    public ChatMessageAdapter(Context context, String id) {
        mContext = context;
        chatId = id;
        mConversation = EMClient.getInstance().chatManager().getConversation(chatId, null, true);
        mMessageList = mConversation.getAllMessages();
    }

    /**
     * 更新数据源
     */
    public void refreshMessageData() {
        mMessageList.clear();
        mMessageList.addAll(mConversation.getAllMessages());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    /**
     * 重写 Adapter 的获取当前 Item 类型的方法（必须重写，同上）
     *
     * @param position 当前 Item 位置
     * @return 当前 Item 的类型
     */
    @Override
    public int getItemViewType(int position) {
        EMMessage message = mMessageList.get(position);
        int itemType = -1;
        // 判断消息类型
        if (message.getBooleanAttribute(ConstantsUtils.ML_ATTR_RECALL, false)) {
            // 撤回消息
            itemType = ConstantsUtils.MSG_TYPE_SYS_RECALL;
        } else if (message.getBooleanAttribute(ConstantsUtils.ML_ATTR_CALL_VIDEO, false)
                || message.getBooleanAttribute(ConstantsUtils.ML_ATTR_CALL_VOICE, false)) {
            // 音视频消息
            itemType = message.direct() == EMMessage.Direct.SEND ? ConstantsUtils.MSG_TYPE_CALL_SEND : ConstantsUtils.MSG_TYPE_CALL_RECEIVED;
        } else {
            switch (message.getType()) {
                case TXT:
                    // 文本消息
                    itemType = message.direct() == EMMessage.Direct.SEND ? ConstantsUtils.MSG_TYPE_TEXT_SEND : ConstantsUtils.MSG_TYPE_TEXT_RECEIVED;
                    break;
                case IMAGE:
                    // 语音消息
                    itemType = message.direct() == EMMessage.Direct.SEND ? ConstantsUtils.MSG_TYPE_IMAGE_SEND : ConstantsUtils.MSG_TYPE_IMAGE_RECEIVED;
                    break;
                case FILE:
                    // 文件消息
                    itemType = message.direct() == EMMessage.Direct.SEND ? ConstantsUtils.MSG_TYPE_FILE_SEND : ConstantsUtils.MSG_TYPE_FILE_RECEIVED;
                    break;
                case VOICE:
                    // 语音消息
                    itemType = message.direct() == EMMessage.Direct.SEND ? ConstantsUtils.MSG_TYPE_VOICE_SEND : ConstantsUtils.MSG_TYPE_VOICE_RECEIVED;
                    break;
                default:
                    // 默认返回txt类型
                    itemType = message.direct() == EMMessage.Direct.SEND ? ConstantsUtils.MSG_TYPE_TEXT_SEND : ConstantsUtils.MSG_TYPE_TEXT_RECEIVED;
                    break;
            }
        }
        return itemType;
    }

    @Override
    public ChatMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ChatMessageViewHolder holder = null;
        switch (viewType){
            /**
             * sdk默认的消息类型
             */
            //文本类型
            case ConstantsUtils.MSG_TYPE_TEXT_SEND:
            case ConstantsUtils.MSG_TYPE_TEXT_RECEIVED:
                holder = new ChatMessageViewHolder(new TextMessageItem(mContext,this,viewType));
                break;
            //图片类型
            case ConstantsUtils.MSG_TYPE_IMAGE_SEND:
            case ConstantsUtils.MSG_TYPE_IMAGE_RECEIVED:
                holder = new ChatMessageViewHolder(new ImageMessageItem(mContext,this,viewType));
                break;
            //文件类型
            case ConstantsUtils.MSG_TYPE_FILE_SEND:
            case ConstantsUtils.MSG_TYPE_FILE_RECEIVED:
                holder = new ChatMessageViewHolder(new FileMessageItem(mContext,this,viewType));
                break;
            //语音类型
            case ConstantsUtils.MSG_TYPE_VOICE_SEND:
            case ConstantsUtils.MSG_TYPE_VOICE_RECEIVED:
                holder = new ChatMessageViewHolder(new VoiceMessageItem(mContext,this,viewType));
                break;

            /**
             * 自定义的消息类型
             */
            //撤回消息类型
            case ConstantsUtils.MSG_TYPE_SYS_RECALL:

                break;
            //音视频消息类型
            case ConstantsUtils.MSG_TYPE_CALL_SEND:
            case ConstantsUtils.MSG_TYPE_CALL_RECEIVED:

                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ChatMessageViewHolder holder, int position) {
        EMMessage message = mMessageList.get(position);
        /**
         * 自定义item{@link MessageItem#onSetupView(EMMessage)}填充数据
         */
        ((MessageItem) holder.itemView).onSetupView(message);

    }

    /**
     * 自定义聊天ViewHolder 用来展示聊天数据
     */
    public static class ChatMessageViewHolder extends RecyclerView.ViewHolder{

        /**
         * 构造方法
         *
         * @param itemView 显示聊天数据的 ItemView
         */
        public ChatMessageViewHolder(View itemView) {
            super(itemView);
        }
    }

}
