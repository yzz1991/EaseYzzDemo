package com.em.yzzdemo.chat;

import com.em.yzzdemo.utils.ConstantsUtils;
import com.em.yzzdemo.utils.DateUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;

/**
 * Created by Geri on 2016/12/26.
 */

public class MessageUtils {
    /**
     * 发送一条撤回消息的透传，加上扩展实现消息的撤回
     */
    public static void sendRecallMessage(EMMessage message, final EMCallBack callBack){
        boolean result = false;
        // 获取当前时间，用来判断后边撤回消息的时间点是否合法，这个判断不需要在接收方做，
        // 因为如果接收方之前不在线，很久之后才收到消息，将导致撤回失败
        long currentTime = DateUtil.getCurrentMillisecond();
        long msgTime = message.getMsgTime();
        if(currentTime < msgTime || (currentTime - msgTime > 300000)){
            callBack.onError(ConstantsUtils.ERROR_I_RECALL_TIME,"out of time");
            return;
        }
        //获取消息的id，作为撤回消息的参数
        String msgId = message.getMsgId();
        //创建一条CMD消息
        EMMessage cmdMessage = EMMessage.createSendMessage(EMMessage.Type.CMD);
        //判断消息类型，如果是群聊就设为群聊
        if(message.getChatType() == EMMessage.ChatType.GroupChat){
            cmdMessage.setChatType(EMMessage.ChatType.GroupChat);
        }
        //设置消息的接收者
        cmdMessage.setTo(message.getTo());
        //创建CMD消息体，并设置action为recall;
        String action = ConstantsUtils.ML_ATTR_RECALL;
        EMCmdMessageBody body = new EMCmdMessageBody(action);
        cmdMessage.addBody(body);
        // 设置消息的扩展为要撤回的 msgId
        cmdMessage.setAttribute(ConstantsUtils.ML_ATTR_MSG_ID,msgId);
        //callback回调
        cmdMessage.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                callBack.onSuccess();
            }

            @Override
            public void onError(int i, String s) {
                callBack.onError(i,s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        //都设置完成后发送消息
        EMClient.getInstance().chatManager().sendMessage(cmdMessage);
    }

    /**
     *接收撤回消息，并加上扩展实现
     */
    public static boolean receiveRecallMessage(EMMessage cmdMessage){
        boolean result = false;
        //从cmd扩展里取出消息id
        String msgId = cmdMessage.getStringAttribute(ConstantsUtils.ML_ATTR_MSG_ID, null);
        if(msgId == null){
            return result;
        }
        //根据获取的msgid去本地查找这条消息，如果本地没有了就不用撤回了
        EMMessage message = EMClient.getInstance().chatManager().getMessage(msgId);
        if(message == null){
            return result;
        }
        //设置扩展为撤回消息类型，为了区分消息的展示
        message.setAttribute(ConstantsUtils.ML_ATTR_RECALL,true);
        // 更新消息
        result = EMClient.getInstance().chatManager().updateMessage(message);
        return  result;
    }

}
