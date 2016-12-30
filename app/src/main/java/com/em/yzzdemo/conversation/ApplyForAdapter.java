package com.em.yzzdemo.conversation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.em.yzzdemo.R;
import com.em.yzzdemo.utils.ConstantsUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.Collections;
import java.util.List;

/**
 * Created by Geri on 2016/12/28.
 */
public class ApplyForAdapter extends RecyclerView.Adapter<ApplyForAdapter.ApplyForViewHolder>{
    private Context mContext;
    private LayoutInflater mInflater;
    // 当前会话对象
    private EMConversation mConversation;
    private List<EMMessage> mMessages;
    private ApplyForItemCallBack mCallBack;

    public ApplyForAdapter(Context context) {
        this.mContext =  context;
        mInflater = LayoutInflater.from(context);
        mConversation = EMClient.getInstance()
                .chatManager()
                .getConversation(ConstantsUtils.CONVERSATION_APPLY, null, true);
        mMessages = mConversation.getAllMessages();
        // 将list集合倒序排列
        Collections.reverse(mMessages);
    }

    @Override
    public ApplyForViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_apply, parent, false);
        return new ApplyForViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ApplyForViewHolder holder, int position) {
        final EMMessage message = mMessages.get(position);

        holder.imageViewAvatar.setImageResource(R.mipmap.header1);

        String username = message.getStringAttribute(ConstantsUtils.ML_ATTR_USERNAME, "");
        // 设置申请的人
        holder.textViewUsername.setText(username);
        // 设置申请理由
        String reason = message.getStringAttribute(ConstantsUtils.ML_ATTR_REASON, "");
        holder.textViewReason.setText(reason);

        String status = message.getStringAttribute(ConstantsUtils.ML_ATTR_STATUS, "");
        if (!TextUtils.isEmpty(status)) {
            holder.btnAgree.setVisibility(View.VISIBLE);
            holder.textViewStatus.setVisibility(View.GONE);
        } else {
            holder.btnAgree.setVisibility(View.GONE);
            holder.textViewStatus.setVisibility(View.VISIBLE);
            holder.textViewStatus.setText(status);
        }
        holder.btnAgree.setTag(message.getMsgId());
        holder.btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onAction(ConstantsUtils.ACTION_APPLY_FOR_AGREE,message.getMsgId());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onAction(ConstantsUtils.ACTION_APPLY_FOR_CLICK,message.getMsgId());
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View v) {
                // 这里直接给长按设置删除操作
                mCallBack.onAction(ConstantsUtils.ACTION_APPLY_FOR_DELETE, message.getMsgId());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public static class ApplyForViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewAvatar;
        TextView textViewUsername;
        TextView textViewReason;
        TextView textViewStatus;
        Button btnAgree;

        public ApplyForViewHolder(View itemView) {
            super(itemView);
            imageViewAvatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            textViewUsername = (TextView) itemView.findViewById(R.id.text_username);
            textViewReason = (TextView) itemView.findViewById(R.id.text_reason);
            textViewStatus = (TextView) itemView.findViewById(R.id.text_status);
            btnAgree = (Button) itemView.findViewById(R.id.btn_agree);
        }
    }

    /**
     * 自定义点击接口回调
     */
    public interface ApplyForItemCallBack{
        /**
         * Item 点击及长按事件的处理
         *
         * @param action 长按菜单需要处理的动作，
         * @param tag 需要操作的 Item 的 tag，这里定义为一个 Object，可以根据需要进行类型转换
         */
        void onAction(int action, Object tag);
    }
    /**
     * 设置 Item 回调
     *
     * @param callback 自定义实现的回调接口
     */
    public void setItemCallBack(ApplyForItemCallBack callback) {
        this.mCallBack = callback;
    }
}
