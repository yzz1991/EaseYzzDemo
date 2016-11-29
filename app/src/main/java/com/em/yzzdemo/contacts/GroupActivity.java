package com.em.yzzdemo.contacts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.em.yzzdemo.BaseActivity;
import com.em.yzzdemo.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Geri on 2016/11/29.
 */
public class GroupActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.group_back)
    TextView groupBack;
    @BindView(R.id.group_search)
    ImageView groupSearch;
    @BindView(R.id.group_recyclerView)
    RecyclerView groupRecyclerView;
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
        groupBack.setOnClickListener(this);
        groupSearch.setOnClickListener(this);
        //从本地加载群组列表
        mGroupList = EMClient.getInstance().groupManager().getAllGroups();
        Log.i("groupList", mGroupList.size()+"");
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        GroupAdapter adapter = new GroupAdapter(mActivity, mGroupList);
        groupRecyclerView.setLayoutManager(manager);
        groupRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //返回
            case R.id.group_back:
                finish();
                break;
            //搜索
            case R.id.group_search:

                break;
        }
    }
}
