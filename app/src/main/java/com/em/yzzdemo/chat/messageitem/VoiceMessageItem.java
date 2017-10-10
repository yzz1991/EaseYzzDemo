package com.em.yzzdemo.chat.messageitem;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.em.yzzdemo.R;
import com.em.yzzdemo.callback.EaseChatRowVoicePlayClickListener;
import com.em.yzzdemo.chat.ChatMessageAdapter;
import com.em.yzzdemo.utils.ConstantsUtils;
import com.em.yzzdemo.utils.DateUtil;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVoiceMessageBody;

/**
 * Created by Geri on 2016/12/8.
 */
public class VoiceMessageItem extends MessageItem {
    public VoiceMessageItem(Context context, ChatMessageAdapter adapter, int viewType) {
        super(context, adapter, viewType);
    }

    @Override
    protected void onItemLongClick() {

    }

    @Override
    public void onSetupView(EMMessage message) {

        mMessage = message;
        EMVoiceMessageBody body = (EMVoiceMessageBody) mMessage.getBody();
        // 设置消息时间
        msgTimeView.setText(DateUtil.getRelativeTime(message.getMsgTime()));
        int voiceLength = body.getLength();
        voiceTime.setText(voiceLength + "\"");
        // 判断如果是单聊或者消息是发送方，不显示username
        if (mMessage.getChatType() == EMMessage.ChatType.Chat || mMessage.direct() == EMMessage.Direct.SEND) {
            usernameView.setVisibility(View.GONE);
        } else {
            // 设置消息消息发送者的名称
            usernameView.setText(message.getFrom());
            usernameView.setVisibility(View.VISIBLE);
        }
        if (EaseChatRowVoicePlayClickListener.playMsgId != null
                && EaseChatRowVoicePlayClickListener.playMsgId.equals(message.getMsgId()) && EaseChatRowVoicePlayClickListener.isPlaying) {
            AnimationDrawable voiceAnimation;
            if (message.direct() == EMMessage.Direct.RECEIVE) {
                voiceImage.setImageResource(R.anim.voice_from_icon);
            } else {
                voiceImage.setImageResource(R.anim.voice_to_icon);
            }
            voiceAnimation = (AnimationDrawable) voiceImage.getDrawable();
            voiceAnimation.start();
        } else {
            if (message.direct() == EMMessage.Direct.RECEIVE) {
                voiceImage.setImageResource(R.mipmap.ease_chatfrom_voice_playing);
            } else {
                voiceImage.setImageResource(R.mipmap.ease_chatto_voice_playing);
            }
        }
    }

    @Override
    protected void onInflateView() {
        if(mViewType == ConstantsUtils.MSG_TYPE_VOICE_SEND){
            mInflater.inflate(R.layout.item_msg_voice_send,this);
        }else{
            mInflater.inflate(R.layout.item_msg_voice_received,this);
        }
        bubbleLayout = findViewById(R.id.layout_bubble);
        usernameView = (TextView) findViewById(R.id.text_username);
        contentView = (TextView) findViewById(R.id.text_content);
        voiceImage = (ImageView) findViewById(R.id.voice_img);
        msgTimeView = (TextView) findViewById(R.id.message_time);
        voiceTime = (TextView) findViewById(R.id.voice_time);


    }
}
