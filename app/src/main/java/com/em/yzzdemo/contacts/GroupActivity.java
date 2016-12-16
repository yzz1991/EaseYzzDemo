package com.em.yzzdemo.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.em.yzzdemo.BaseActivity;
import com.em.yzzdemo.R;
import com.em.yzzdemo.callback.OnItemClickListener;
import com.em.yzzdemo.chat.ChatActivity;
import com.em.yzzdemo.utils.ConstantsUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Geri on 2016/11/29.
 */
public class GroupActivity extends BaseActivity {


    @BindView(R.id.group_recyclerView)
    RecyclerView groupRecyclerView;
    @BindView(R.id.group_toolbarView)
    Toolbar mToolbarView;
    private List<EMGroup> mGroupList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        ButterKnife.bind(this);
        mActivity = this;

        initView();
    }

    private void initView() {
        mToolbarView.setTitle("group");
        //声明toolbar
        setSupportActionBar(mToolbarView);
        //设置back图标
        mToolbarView.setNavigationIcon(R.mipmap.arrow_back);
        mToolbarView.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //toolbar字体颜色
        mToolbarView.setTitleTextColor(0xffffffff);
        mToolbarView.setOnMenuItemClickListener(onMenuItemClick);
        //从本地加载群组列表
        mGroupList = EMClient.getInstance().groupManager().getAllGroups();
        final LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        GroupAdapter adapter = new GroupAdapter(mActivity, mGroupList);
        groupRecyclerView.setLayoutManager(manager);
        groupRecyclerView.setAdapter(adapter);

        //RecyclerView的点击事件
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mActivity, ChatActivity.class);
                intent.putExtra(ConstantsUtils.CHAT_ID,mGroupList.get(position).getGroupId());
                intent.putExtra(ConstantsUtils.CHAT_TYPE, EMConversation.EMConversationType.GroupChat);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //设定菜单各按钮的点击动作
    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            Toast.makeText(mActivity, "search", Toast.LENGTH_SHORT).show();
            return true;
        }
    };
}
