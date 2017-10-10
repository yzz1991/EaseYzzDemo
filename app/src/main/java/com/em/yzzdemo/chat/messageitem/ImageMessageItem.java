package com.em.yzzdemo.chat.messageitem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;
import android.util.Log;
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
import com.em.yzzdemo.utils.ImageCache;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.ImageUtils;
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
    // 缩略图本地路径
    String thumbnailsPath = "";
    // 原图在本地路径
    String originalPath = "";
    private String thumbnailUrl;
    private EMImageMessageBody imgBody;


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
        imgBody = (EMImageMessageBody) mMessage.getBody();
        // 取出图片原始宽高，这是在发送图片时发送方直接根据图片获得设置到body中的
        int width = imgBody.getWidth();
        int height = imgBody.getHeight();
        thumbnailUrl = imgBody.getThumbnailUrl();
        if(width <= 0 || height <= 0){
            BitmapFactory.Options var13 = ImageUtils.getBitmapOptions(imgBody.getLocalUrl());
            width = var13.outWidth;
            height = var13.outHeight;
        }
        Log.e("image Width and Height1", imgBody.getWidth()+":"+ imgBody.getHeight());
        float scale = BitmapUtil.getZoomScale(width, height, thumbnailsMax);
        // 根据图片原图大小，来计算缩略图要显示的大小，直接设置控件宽高
        ViewGroup.LayoutParams lp = imageContextView.getLayoutParams();
        if (width <= thumbnailsMax && height <= thumbnailsMax) {
            if (width < thumbnailsMin) {
                lp.width = thumbnailsMin;
                lp.height = height * thumbnailsMin / width;
                Log.e("image Width and Height2", lp.width+":"+lp.height);
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
        Log.e("image Width and Height3", mViewWidth+":"+mViewHeight);
        // 设置显示图片控件的显示大小
        imageContextView.setLayoutParams(lp);
        // 判断下是否是接收方的消息
//        if (mViewType == ConstantsUtils.MSG_TYPE_IMAGE_RECEIVED) {
//            // 判断消息是否处于下载状态，如果是下载状态设置一个默认的图片
//            if (imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING
//                    || imgBody.thumbnailDownloadStatus()
//                    == EMFileMessageBody.EMDownloadStatus.PENDING) {
//                imageContextView.setImageDrawable(getResources().getDrawable(R.mipmap.download_failed));
//            }
//        }
//        if (mViewType == ConstantsUtils.MSG_TYPE_IMAGE_RECEIVED) {
//            // 接收方获取缩略图的路径
//            originalPath = imgBody.getLocalUrl();
//            thumbnailsPath = imgBody.thumbnailLocalPath();
//            Log.d("thumbnailsPath", "Receive"+thumbnailsPath);
//        } else {
//            // 发送方获取图片路径
//            originalPath = imgBody.getLocalUrl();
//            thumbnailsPath = getThumbImagePath(originalPath);
//            Log.d("thumbnailsPath", "send"+thumbnailsPath);
//        }
//        // 为图片显示控件设置tag，在设置图片显示的时候，先判断下当前的tag是否是当前item的，是则显示图片
//        //        imageView.setTag(thumbnailsPath);
//        Log.e("statusCallback1", "statusCallback1");
//        message.setMessageStatusCallback(new EMCallBack() {
//            @Override
//            public void onSuccess() {
//                showThumbnailsImage(thumbnailsPath, originalPath);
//                Log.e("onSuccess", "onSuccess");
//            }
//
//            @Override
//            public void onError(int i, String s) {
//                imageContextView.setImageDrawable(getResources().getDrawable(R.mipmap.download_failed));
//
//            }
//
//            @Override
//            public void onProgress(int i, String s) {
//
//            }
//        });
//        Log.e("statusCallback2", "statusCallback2");
//
//        // 设置缩略图的显示
//        showThumbnailsImage(thumbnailsPath, originalPath);
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            if (imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                    imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING) {
                imageContextView.setImageResource(R.mipmap.download_failed);
                //set the receive message callback
                message.setMessageStatusCallback(new EMCallBack() {
                    @Override
                    public void onSuccess() {
//                        showThumbnailsImage(thumbnailsPath, originalPath);
                        Log.e("onSuccess", "onSuccess");
                    }

                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            } else {
                imageContextView.setImageResource(R.mipmap.download_failed);
                String thumbPath = imgBody.thumbnailLocalPath();
                showImageView(thumbPath, imgBody.getLocalUrl(), message);
            }
            return;
        }
        String filePath = imgBody.getLocalUrl();
//        String thumbPath = ImageUtils.getThumbnailImagePath(imgBody.getLocalUrl());
//        showImageView(thumbPath, filePath, message);

    }

    private void showImageView(final String thumbernailPath, final String localFullSizePath,final EMMessage message) {
        // first check if the thumbnail image already loaded into cache
        Bitmap bitmap = ImageCache.getInstance().get(thumbernailPath);
        if (bitmap != null) {
            // thumbnail image is already loaded, reuse the drawable
            imageContextView.setImageBitmap(bitmap);
        } else {
            AsyncTaskCompat.executeParallel(new AsyncTask<Object, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(Object... args) {
                    File file = new File(thumbernailPath);
                    if (file.exists()) {
                        return ImageUtils.decodeScaleImage(thumbernailPath, 160, 160);
                    } else if (new File(imgBody.thumbnailLocalPath()).exists()) {
                        return ImageUtils.decodeScaleImage(imgBody.thumbnailLocalPath(), 160, 160);
                    }
                    else {
                        if (message.direct() == EMMessage.Direct.SEND) {
                            if (localFullSizePath != null && new File(localFullSizePath).exists()) {
                                return ImageUtils.decodeScaleImage(localFullSizePath, 160, 160);
                            } else {
                                return null;
                            }
                        } else {
                            return null;
                        }
                    }
                }

                protected void onPostExecute(Bitmap image) {
                    if (image != null) {
                        imageContextView.setImageBitmap(image);
                        ImageCache.getInstance().put(thumbernailPath, image);
                    }
                }
            });
        }
    }

//    /**
//     * 设置缩略图的显示，并将缩略图添加到缓存
//     *
//     * @param thumbnailsPath 缩略图的路径
//     * @param originalPath 原始图片的路径
//     */
//    private void showThumbnailsImage(String thumbnailsPath, String originalPath) {
//        File thumbnailsFile = new File(thumbnailsPath);
//        File originalFile = new File(originalPath);
//        // 根据图片存在情况加载缩略图显示
//        if (originalFile.exists() && originalFile.isFile()) {
//            // 原图存在，直接通过原图加载缩略图
//            Glide.with(mContext)
//                    .load(originalFile)
//                    .crossFade()
//                    .override(mViewWidth, mViewHeight)
//                    .into(imageContextView);
//            Log.e("showThumbnailsImage1", "showThumbnailsImage1");
//        } else if (!originalFile.exists() && thumbnailsFile.exists()) {
//            // 原图不存在，只存在缩略图
//            Glide.with(mContext)
//                    .load(thumbnailsFile)
//                    .crossFade()
//                    .override(mViewWidth, mViewHeight)
//                    .into(imageContextView);
//            Log.e("showThumbnailsImage2", "showThumbnailsImage2");
//
//        } else if (!originalFile.exists() && !thumbnailsFile.exists()) {
//            // 原图和缩略图都不存在
//            Glide.with(mContext)
//                    .load(thumbnailUrl)
//                    .placeholder(R.mipmap.loading)
//                    .crossFade()
//                    .override(mViewWidth, mViewHeight)
//                    .into(imageContextView);
//            Log.e("showThumbnailsImage3", "showThumbnailsImage3");
//
//        }
//    }

    @Override
    protected void onItemLongClick() {

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
//
//    /**
//     * 获取图片消息的缩略图本地保存的路径
//     *
//     * @param fullSizePath 缩略图的原始路径
//     * @return 返回本地路径
//     */
//    public static String getThumbImagePath(String fullSizePath) {
//        String thumbImageName = CryptoUtil.cryptoStr2SHA1(fullSizePath);
//        String path = PathUtil.getInstance().getHistoryPath() + "/" + "thumb_" + thumbImageName;
//        return path;
//    }
}
