package com.em.yzzdemo.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.em.yzzdemo.BaseFragment;
import com.em.yzzdemo.R;
import com.em.yzzdemo.bean.UserEntity;
import com.em.yzzdemo.callback.OnItemClickListener;
import com.em.yzzdemo.chat.ChatActivity;
import com.em.yzzdemo.utils.ConstantsUtils;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Geri on 2016/11/27.
 */

public class ContactsFragment extends BaseFragment {

    @BindView(R.id.contacts_view)
    RecyclerView contactsView;
    @BindView(R.id.contacts_group_ll)
    LinearLayout contactsGroupLl;
    @BindView(R.id.contacts_refreshView)
    SwipeRefreshLayout mRefreshView;
    private ContactsClickPopWindow addPopWindow;
    private ContactsAdapter mAdapter;
    private List<UserEntity> mUserList;
    private int currentPosition = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, null);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();

    }

    //初使化联系人列表
    private void initView() {
        mActivity = getActivity();
        mUserList = new ArrayList<>();
        mUserList.addAll(ContactsManager.getInstance(mActivity).getContactList().values());
        mAdapter = new ContactsAdapter(mActivity, mUserList);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        contactsView.setLayoutManager(manager);
        contactsView.setAdapter(mAdapter);
        //下拉刷新
        refreshData();
        //点击进入群组
        contactsGroupLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity,GroupActivity.class));
            }
        });

        //recycleView点击事件
        mAdapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                currentPosition = position;
                addPopWindow = new ContactsClickPopWindow(mActivity,itemsOnClick,mUserList.get(position).getUserName());
                addPopWindow.showPopupWindow(view);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }

    private void refreshData() {
        // 设置下拉刷新控件颜色
        mRefreshView.setColorSchemeResources(R.color.refresh_one, R.color.refresh_two,
                R.color.refresh_three);
        mRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // 防止在下拉刷新的时候，当前界面关闭导致错误
                        if (mActivity.isFinishing()) {
                            return;
                        }
                        //加载数据

                        mRefreshView.setRefreshing(false);
                    }
                }, 500);
            }
        });
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener(){

        Intent intent = null;

        public void onClick(View v) {
            addPopWindow.dismiss();
            addPopWindow.backgroundAlpha(mActivity,1f);
            switch (v.getId()) {

                case R.id.pop_call:

                    break;
                case R.id.pop_chat:
                    intent = new Intent(mActivity, ChatActivity.class);
                    intent.putExtra(ConstantsUtils.CHAT_ID,mUserList.get(currentPosition).getUserName());
                    intent.putExtra(ConstantsUtils.CHAT_TYPE,EMConversation.EMConversationType.Chat);
                    startActivity(intent);
                    break;
                case R.id.pop_video:

                    break;
            }
        }

    };


}
