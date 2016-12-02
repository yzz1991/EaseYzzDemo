package com.em.yzzdemo.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.em.yzzdemo.BaseActivity;
import com.em.yzzdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Geri on 2016/11/27.
 */

public class ChatActivity extends BaseActivity {
    @BindView(R.id.chat_toolbar) Toolbar toolbar;
    @BindView(R.id.chat_recyclerView) RecyclerView recyclerView;
    private ChatMorePopWindow morePopWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        mActivity = this;

        initView();

    }

    private void initView() {
        //声明toolbar
        setSupportActionBar(toolbar);
        //设置back图标
        toolbar.setNavigationIcon(R.mipmap.arrow_back);
        //设置title
        toolbar.setTitle("Username");
        //toolbar字体颜色
        toolbar.setTitleTextColor(0xffffffff);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //设定菜单各按钮的点击动作
    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_call:
                    Toast.makeText(mActivity, "call", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_video:
                    Toast.makeText(mActivity, "video", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_more:
                    Toast.makeText(mActivity, "more", Toast.LENGTH_SHORT).show();
                    morePopWindow = new ChatMorePopWindow(mActivity,itemsOnClick);
                    morePopWindow.showPopupWindow(toolbar);
                    break;
            }
            return true;
        }
    };

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener(){

        public void onClick(View v) {
            morePopWindow.dismiss();
//            morePopWindow.backgroundAlpha(mActivity,1f);
            switch (v.getId()) {
                case R.id.item_camera:

                    break;
                case R.id.item_picture:

                    break;
                case R.id.item_location:

                    break;
                case R.id.item_file:

                    break;
            }
        }

    };



}
