package com.em.yzzdemo.chat.messageitem;

import android.content.Context;
import android.widget.TextView;

import com.em.yzzdemo.R;
import com.em.yzzdemo.chat.ChatMessageAdapter;
import com.em.yzzdemo.utils.DateUtil;
import com.hyphenate.chat.EMMessage;

/**
 * Created by Geri on 2016/12/26.
 */
public class RecallMessageItem extends MessageItem {
    public RecallMessageItem(Context context, ChatMessageAdapter adapter, int viewType) {
        super(context, adapter, viewType);
    }

    @Override
    protected void onItemLongClick() {

    }

    @Override
    public void onSetupView(EMMessage message) {
        mMessage = message;
        // 设置消息时间
        msgTimeView.setText(DateUtil.getRelativeTime(message.getMsgTime()));
        // 设置显示内容
        String messageStr = null;
        if (mMessage.direct() == EMMessage.Direct.SEND) {
            messageStr = String.format("此条消息已撤回");
        }else{
            messageStr = String.format("此条消息已被 %1$s 撤回", message.getUserName());
        }
        contentView.setText(messageStr);
    }

    @Override
    protected void onInflateView() {
        mInflater.inflate(R.layout.item_msg_sys_recall, this);
        bubbleLayout = findViewById(R.id.layout_bubble);
        msgTimeView = (TextView) findViewById(R.id.text_time);
        contentView = (TextView) findViewById(R.id.text_content);
    }
}
