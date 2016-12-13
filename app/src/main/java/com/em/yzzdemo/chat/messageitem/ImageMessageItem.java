package com.em.yzzdemo.chat.messageitem;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.em.yzzdemo.MyApplication;
import com.em.yzzdemo.R;
import com.em.yzzdemo.chat.ChatMessageAdapter;
import com.em.yzzdemo.utils.BitmapUtil;
import com.em.yzzdemo.utils.ConstantsUtils;
import com.em.yzzdemo.utils.CryptoUtil;
import com.em.yzzdemo.utils.DateUtil;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.PathUtil;

import java.io.File;

/**
 * Created by Geri on 2016/12/8.
 */
public class ImageMessageItem extends MessageItem {
    // 定义图片缩略图限制
    private int thumbnailsMax = MyApplication.getContext().getResources().getDimensionPixelSize(R.dimen.ml_dimen_150);
    private int thumbnailsMin = 150;
    private int mViewWidth;
    private int mViewHeight;

    public ImageMessageItem(Context context, ChatMessageAdapter adapter, int viewType) {
        super(context,adapter,viewType);
    }

    @Override
    public void onSetupView(EMMessage message) {
        mMessage = message;
        // 判断如果是单聊或者消息是发送方，不显示username
        if (mMessage.getChatType() == EMMessage.ChatType.Chat || mMessage.direct() == EMMessage.Direct.SEND) {
            usernameView.setVisibility(View.GONE);
        } else {
            // 设置消息消息发送者的名称
            usernameView.setText(message.getFrom());
            usernameView.setVisibility(View.VISIBLE);
        }
        // 设置消息时间
        msgTimeView.setText(DateUtil.getRelativeTime(message.getMsgTime()));
        EMImageMessageBody imgBody = (EMImageMessageBody) mMessage.getBody();
        // 取出图片原始宽高，这是在发送图片时发送方直接根据图片获得设置到body中的
        int width = imgBody.getWidth();
        int height = imgBody.getHeight();
        float scale = BitmapUtil.getZoomScale(width, height, thumbnailsMax);
        // 根据图片原图大小，来计算缩略图要显示的大小，直接设置控件宽高
        ViewGroup.LayoutParams lp = imageContextView.getLayoutParams();
        if (width <= thumbnailsMax && height <= thumbnailsMax) {
            if (width < thumbnailsMin) {
                lp.width = thumbnailsMin;
                lp.height = height * thumbnailsMin / width;
            } else {
                lp.width = width;
                lp.height = height;
            }
        } else {
            lp.width = (int) (width / scale);
            lp.height = (int) (height / scale);
        }
        mViewWidth = lp.width;
        mViewHeight = lp.height;
        // 设置显示图片控件的显示大小
        imageContextView.setLayoutParams(lp);
        // 判断下是否是接收方的消息
        if (mViewType == ConstantsUtils.MSG_TYPE_IMAGE_RECEIVED) {
            // 判断消息是否处于下载状态，如果是下载状态设置一个默认的图片
            if (imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING
                    || imgBody.thumbnailDownloadStatus()
                    == EMFileMessageBody.EMDownloadStatus.PENDING) {
            }
        }

        // 缩略图本地路径
        String thumbnailsPath = "";
        // 原图在本地路径
        String originalPath = "";
        if (mViewType == ConstantsUtils.MSG_TYPE_IMAGE_RECEIVED) {
            // 接收方获取缩略图的路径
            originalPath = imgBody.getLocalUrl();
            thumbnailsPath = imgBody.thumbnailLocalPath();
        } else {
            // 发送方获取图片路径
            originalPath = imgBody.getLocalUrl();
            thumbnailsPath = getThumbImagePath(originalPath);
        }
        // 为图片显示控件设置tag，在设置图片显示的时候，先判断下当前的tag是否是当前item的，是则显示图片
        //        imageView.setTag(thumbnailsPath);
        // 设置缩略图的显示
        showThumbnailsImage(thumbnailsPath, originalPath);
    }

    /**
     * 设置缩略图的显示，并将缩略图添加到缓存
     *
     * @param thumbnailsPath 缩略图的路径
     * @param originalPath 原始图片的路径
     */
    private void showThumbnailsImage(String thumbnailsPath, String originalPath) {
        File thumbnailsFile = new File(thumbnailsPath);
        File originalFile = new File(originalPath);
        // 根据图片存在情况加载缩略图显示
        if (originalFile.exists()) {
            // 原图存在，直接通过原图加载缩略图
            Glide.with(mContext)
                    .load(originalFile)
                    .crossFade()
                    .override(mViewWidth, mViewHeight)
                    .into(imageContextView);
        } else if (!originalFile.exists() && thumbnailsFile.exists()) {
            // 原图不存在，只存在缩略图
            Glide.with(mContext)
                    .load(thumbnailsFile)
                    .crossFade()
                    .override(mViewWidth, mViewHeight)
                    .into(imageContextView);
        } else if (!originalFile.exists() && !thumbnailsFile.exists()) {
            // 原图和缩略图都不存在
            Glide.with(mContext)
                    .load(thumbnailsFile)
                    .crossFade()
                    .override(mViewWidth, mViewHeight)
                    .into(imageContextView);
        }
    }

    @Override
    protected void onInflateView() {
        if(mViewType == ConstantsUtils.MSG_TYPE_IMAGE_SEND){
            mInflater.inflate(R.layout.item_msg_image_send,this);
        }else{
            mInflater.inflate(R.layout.item_msg_image_received,this);
        }
        bubbleLayout = findViewById(R.id.layout_bubble);
        usernameView = (TextView) findViewById(R.id.image_username);
        imageContextView = (ImageView) findViewById(R.id.image_content);
        msgTimeView = (TextView) findViewById(R.id.message_time);
    }

    /**
     * 获取图片消息的缩略图本地保存的路径
     *
     * @param fullSizePath 缩略图的原始路径
     * @return 返回本地路径
     */
    public static String getThumbImagePath(String fullSizePath) {
        String thumbImageName = CryptoUtil.cryptoStr2SHA1(fullSizePath);
        String path = PathUtil.getInstance().getHistoryPath() + "/" + "thumb_" + thumbImageName;
        return path;
    }
}
