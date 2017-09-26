package com.em.yzzdemo.chat;

import android.content.res.AssetManager;
import android.util.Log;
import android.widget.Toast;

import com.em.yzzdemo.MyApplication;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.PathUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by geri on 2017/7/5.
 */

public class ImportMessageUtils {

    public static String getJson(String fileName) {
        String str = "";
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = MyApplication.getContext().getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
            str = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static void importMessages() {
        List<EMMessage> messageList = new ArrayList<EMMessage>();
        try {
            JSONArray array = new JSONArray(getJson("ceshi.json"));
            Log.d("json", array.toString());
//            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(0);
                long timestamp = jsonObject.optLong("timestamp");
                String from = jsonObject.optString("from");
                String to = jsonObject.optString("to");
                String msgId = jsonObject.optString("msg_id");
                String chatType = jsonObject.optString("chat_type");

                JSONObject bodyObject = jsonObject.optJSONObject("payload").optJSONArray("bodies").getJSONObject(0);
                String type = bodyObject.optString("type");
                EMMessage message = null;
                if (type.equals("txt")) {
                    message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
                    EMTextMessageBody body = new EMTextMessageBody(bodyObject.optString("msg"));
                    message.addBody(body);
                } else if (type.equals("img")) {
                    message = EMMessage.createReceiveMessage(EMMessage.Type.IMAGE);
                    File file = new File("");
                    // 这里使用反射获取 ImageBody，为了设置 size
                    Class<?> bodyClass = Class.forName("com.hyphenate.chat.EMImageMessageBody");
                    Class<?>[] parTypes = new Class<?>[1];
                    parTypes[0] = File.class;
                    Constructor<?> constructor = bodyClass.getDeclaredConstructor(parTypes);
                    Object[] pars = new Object[1];
                    pars[0] = file;
                    EMImageMessageBody body = (EMImageMessageBody) constructor.newInstance(pars);
                    Method setSize = Class.forName("com.hyphenate.chat.EMImageMessageBody")
                            .getDeclaredMethod("setSize", int.class, int.class);
                    setSize.setAccessible(true);
                    int width = bodyObject.optJSONObject("size").optInt("width");
                    int height = bodyObject.optJSONObject("size").optInt("height");
                    setSize.invoke(body, width, height);

                    body.setFileName(bodyObject.optString("filename"));
                    body.setSecret(bodyObject.optString("secret"));
                    body.setRemoteUrl(bodyObject.optString("url"));
                    body.setThumbnailUrl(bodyObject.optString("url"));
                    // to make it compatible with thumbnail received in previous version
                    String thumbPath = getThumbnailImagePath(bodyObject.optString("url"));
                    body.setThumbnailLocalPath(thumbPath);
                    String localPath = getLocalPath(bodyObject.optString("url"));
                    body.setLocalUrl(localPath);
                    Log.d("thumbPath", "send"+thumbPath);
                    message.addBody(body);
                }
                else if (type.equals("video")) {
                    message = EMMessage.createReceiveMessage(EMMessage.Type.VIDEO);
                    EMVideoMessageBody body = new EMVideoMessageBody();
                    body.setThumbnailUrl(bodyObject.optString("thumb"));
                    body.setThumbnailSecret(bodyObject.optString("thumb_secret"));
                    body.setRemoteUrl(bodyObject.optString("url"));
                    body.setVideoFileLength(bodyObject.optLong("file_length"));
                    body.setSecret(bodyObject.optString("secret"));
                    message.addBody(body);
                } else if (type.equals("audio")) {
                    message = EMMessage.createReceiveMessage(EMMessage.Type.VOICE);
                    File file = new File("");
                    EMVoiceMessageBody body = new EMVoiceMessageBody(file, bodyObject.optInt("length"));
                    body.setRemoteUrl(bodyObject.optString("url"));
                    body.setSecret(bodyObject.optString("secret"));
                    body.setFileName(bodyObject.optString("filename"));
                    message.addBody(body);
                }

                message.setFrom(from);
                message.setTo(to);
                message.setMsgTime(timestamp);
                message.setLocalTime(timestamp);
                message.setMsgId(msgId);
                if(chatType.equals("chat")){
                    message.setChatType(EMMessage.ChatType.Chat);
                } else if(chatType.equals("GroupChat")){
                    message.setChatType(EMMessage.ChatType.GroupChat);
                } else if(chatType.equals("ChatRoom")){
                    message.setChatType(EMMessage.ChatType.ChatRoom);
                }
                message.setStatus(EMMessage.Status.SUCCESS);
//                messageList.add(message);
//            }
//            Log.d("conversation1", EMClient.getInstance().chatManager().getAllConversations().size()+"");
            EMClient.getInstance().chatManager().saveMessage(message);
//            Log.d("conversation2", EMClient.getInstance().chatManager().getAllConversations().size()+"");
            message.setMessageStatusCallback(new EMCallBack() {
                @Override
                public void onSuccess() {
                    Log.d("downloadThumbnail", "onSuccess");
                }

                @Override
                public void onError(int i, String s) {
                    Log.e("downloadThumbnail", i+":"+s);
                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
            EMClient.getInstance().chatManager().downloadThumbnail(message);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    //缩略图地址
    public static String getThumbnailImagePath(String thumbRemoteUrl) {
        String thumbImageName= thumbRemoteUrl.substring(thumbRemoteUrl.lastIndexOf("/") + 1, thumbRemoteUrl.length());
        String path = PathUtil.getInstance().getImagePath()+"/"+ "th"+thumbImageName;
        Log.d("msg", "thum image path:" + path);
        return path;
    }

    //原图地址
    public static String getLocalPath(String thumbRemoteUrl) {
        String thumbImageName= thumbRemoteUrl.substring(thumbRemoteUrl.lastIndexOf("/") + 1, thumbRemoteUrl.length());
        String path = PathUtil.getInstance().getImagePath()+"/" + thumbImageName;
        Log.d("msg", "thum image path:" + path);
        return path;
    }

    //语音地址
    public static String getVoiceLocalPath(String voiceRemoteUrl) {
        String voiceName= voiceRemoteUrl.substring(voiceRemoteUrl.lastIndexOf("/") + 1, voiceRemoteUrl.length());
        String path = PathUtil.getInstance().getVoicePath()+"/" + voiceName;
        Log.d("msg", "thum image path:" + path);
        return path;
    }
}
