package com.em.yzzdemo.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.em.yzzdemo.BaseActivity;
import com.em.yzzdemo.R;
import com.em.yzzdemo.utils.ConstantsUtils;
import com.em.yzzdemo.utils.FileUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Geri on 2016/12/27.
 */
public class BigImageActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.imageView)
    ImageView mImageView;
    private EMMessage mMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bigimage);
        ButterKnife.bind(this);

        initView();

    }

    private void initView() {
        mActivity = this;
        String msgId = getIntent().getStringExtra(ConstantsUtils.EXTRA_MSG_ID);
        mMessage = EMClient.getInstance().chatManager().getMessage(msgId);

        // 图片本地路径
        String localPath = ((EMImageMessageBody) mMessage.getBody()).getLocalUrl();
        String remotePath = ((EMImageMessageBody) mMessage.getBody()).getRemoteUrl();
        // 根据图片存在情况加载缩略图显示
        if (FileUtil.isFileExists(localPath)) {
            // 原图存在，直接通过原图路径加载显示
            Glide.with(mActivity)
                    .load(localPath)
                    .crossFade()
                    .dontAnimate()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(mImageView);
        } else {
            // 原图不存在
            Glide.with(mActivity)
                    .load(remotePath)
                    .crossFade()
                    .dontAnimate()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(mImageView);
        }

        mToolbar.setTitle(((EMImageMessageBody) mMessage.getBody()).getFileName());
        setSupportActionBar(mToolbar);
        // 设置toolbar图标
        mToolbar.setNavigationIcon(R.mipmap.arrow_back);
        // 设置Toolbar图标点击事件，Toolbar上图标的id是 -1
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
