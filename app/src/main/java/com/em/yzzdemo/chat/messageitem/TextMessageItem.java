package com.em.yzzdemo.chat.messageitem;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.em.yzzdemo.R;
import com.em.yzzdemo.chat.ChatMessageAdapter;
import com.em.yzzdemo.utils.ConstantsUtils;
import com.em.yzzdemo.utils.DateUtil;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

/**
 * Created by Geri on 201612/8.
 */
public class TextMessageItem extends MessageItem  {
    /**
     * 构造方法，创建item的view，需要传递对应的参数
     *
     * @param context  上下文对象
     * @param adapter  适配器
     * @param viewType item类型
     */
    public TextMessageItem(Context context, ChatMessageAdapter adapter, int viewType) {
        super(context, adapter, viewType);
    }



    /**
     * 实现数据的填充
     *
     * @param message  需要展示的 EMMessage 对象
     */
    @Override
    public void onSetupView(EMMessage message) {
        mMessage = message;

        // 判断如果是单聊或者消息是发送方，不显示username
        if (mMessage.getChatType() == EMMessage.ChatType.Chat || mMessage.direct() == EMMessage.Direct.SEND) {
            usernameView.setVisibility(View.GONE);
            //设置链接字体的颜色
            contentView.setLinkTextColor(0xffffffff);
        } else {
            // 设置消息消息发送者的名称
            usernameView.setText(message.getFrom());
            usernameView.setVisibility(View.VISIBLE);
            //设置链接字体的颜色
//            contentView.setLinkTextColor(0xffffffff);
        }
        // 设置消息时间
        msgTimeView.setText(DateUtil.getRelativeTime(message.getMsgTime()));

        EMTextMessageBody body = (EMTextMessageBody) mMessage.getBody();
        String messageStr = body.getMessage().toString();
        contentView.setText(messageStr);
    }

    @Override
    protected void onItemLongClick() {
        String[] menus = null;
        // 这里要根据消息的类型去判断要弹出的菜单，是否是发送方，并且是发送成功才能撤回
        if (mViewType == ConstantsUtils.MSG_TYPE_TEXT_RECEIVED) {
            menus = new String[]{"复制","转发消息", "删除消息"};
        } else {
            menus = new String[]{"复制","转发消息", "删除消息", "撤回消息"};
        }
        //设置弹出item长按菜单，并设置菜单的每个点击监听
        alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder.setItems(menus, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        mAdapter.onItemAction(mMessage, ConstantsUtils.ACTION_MSG_COPY);
                        break;

                    case 1:
                        mAdapter.onItemAction(mMessage, ConstantsUtils.ACTION_MSG_FORWARD);
                        break;

                    case 2:
                        mAdapter.onItemAction(mMessage, ConstantsUtils.ACTION_MSG_DELETE);
                        break;

                    case 3:
                        mAdapter.onItemAction(mMessage, ConstantsUtils.ACTION_MSG_RECALL);
                        break;
                }
            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * 解析对应的xml 布局，填充当前 ItemView，并初始化控件
     */
    @Override
    protected void onInflateView() {
        if(mViewType == ConstantsUtils.MSG_TYPE_TEXT_SEND){
            mInflater.inflate(R.layout.item_msg_text_send,this);
        }else{
            mInflater.inflate(R.layout.item_msg_text_received,this);
        }
        bubbleLayout = findViewById(R.id.layout_bubble);
        usernameView = (TextView) findViewById(R.id.text_username);
        contentView = (TextView) findViewById(R.id.text_content);
        msgTimeView = (TextView) findViewById(R.id.message_time);
    }
}
