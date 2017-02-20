package com.em.yzzdemo.conversation;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.em.yzzdemo.R;
import com.em.yzzdemo.callback.OnItemClickListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.List;

import static com.hyphenate.chat.EMMessage.Type.FILE;
import static com.hyphenate.chat.EMMessage.Type.IMAGE;
import static com.hyphenate.chat.EMMessage.Type.LOCATION;
import static com.hyphenate.chat.EMMessage.Type.TXT;
import static com.hyphenate.chat.EMMessage.Type.VIDEO;

/**
 * Created by Geri on 2016/12/12.
 */
public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationHolder>{
    private Context mContext;
    private List<EMConversation> mList;
    private LayoutInflater mInflater;
    private String content;
    // 刷新会话列表
    private final int HANDLER_CONVERSATION_REFRESH = 0;
    private MyHandler mHandler;
    //自定义item点击接口
    private OnItemClickListener mOnItemClickListener;

    public ConversationAdapter(Context context, List<EMConversation> list) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(context);
        mHandler = new MyHandler();
    }

    @Override
    public ConversationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ConversationHolder(mInflater.inflate(R.layout.item_conversation,null));
    }

    @Override
    public void onBindViewHolder(final ConversationHolder holder, int position) {
        EMGroup group = EMClient.getInstance().groupManager().getGroup(mList.get(position).conversationId());
        if(mList.get(position).isGroup() && group != null){
            holder.usernameView.setText(group.getGroupName());
        }else{
            holder.usernameView.setText(mList.get(position).conversationId());
        }

        if(mList.get(position).getAllMessages().size() > 0){
            EMMessage lastMessage = mList.get(position).getLastMessage();
            if(lastMessage.getType() == TXT){
                content = ((EMTextMessageBody) lastMessage.getBody()).getMessage();
            } else if(lastMessage.getType() == FILE){
                content = "[" + "文件" + "]";
            }else if(lastMessage.getType() == IMAGE){
                content = "[" + "图片" + "]";
            }else if(lastMessage.getType() == LOCATION ){
                content = "[" + "位置" + "]";
            }else if(lastMessage.getType()== VIDEO ){
                content = "[" + "视频" + "]";
            }else if(lastMessage.getType() == VIDEO ){
                content = "[" + "语音" + "]";
            }
            // 判断这条消息状态，如果失败加上失败前缀提示
            if(lastMessage.status() == EMMessage.Status.FAIL){
                content = "[" + "失败" + "]" + content;
            }
        }else{
            content = "空";
        }
        holder.messageView.setText(content);
//        holder.timeView.setText(DateUtil.getRelativeTime(mList.get(position).getLastMessage().localTime()));

        //item的点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition();
                mOnItemClickListener.onItemClick(holder.itemView, pos);
            }
        });
        //item的长按事件
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int pos = holder.getLayoutPosition();
                mOnItemClickListener.onItemLongClick(holder.itemView,pos);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ConversationHolder extends RecyclerView.ViewHolder{
        LinearLayout item;
        ImageView avatarView;
        TextView usernameView;
        TextView messageView;
        TextView timeView;

        public ConversationHolder(View itemView) {
            super(itemView);
            item = (LinearLayout) itemView.findViewById(R.id.item_ll);
            avatarView = (ImageView) itemView.findViewById(R.id.iv_avatar);
            usernameView = (TextView) itemView.findViewById(R.id.tv_username);
            messageView = (TextView) itemView.findViewById(R.id.last_message);
            timeView = (TextView) itemView.findViewById(R.id.conversation_time);
        }
    }

    public void setmOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    /**
     * 自定义Handler，用来处理消息的刷新等
     */
    protected class MyHandler extends Handler {
        private void refresh() {
            notifyDataSetChanged();
        }

        @Override
        public void handleMessage(Message msg) {
            //            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_CONVERSATION_REFRESH:
                    refresh();
                    break;
            }
        }
    }

    /**
     * 供界面调用的刷新 Adapter 的方法
     */
    public void refreshList() {
        Message msg = mHandler.obtainMessage();
        msg.what = HANDLER_CONVERSATION_REFRESH;
        mHandler.sendMessage(msg);
    }
}
