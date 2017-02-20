package com.em.yzzdemo.conversation;

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

import com.em.yzzdemo.BaseFragment;
import com.em.yzzdemo.R;
import com.em.yzzdemo.callback.OnItemClickListener;
import com.em.yzzdemo.chat.ChatActivity;
import com.em.yzzdemo.event.MessageEvent;
import com.em.yzzdemo.utils.ConstantsUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Geri on 2016/11/27.
 */

public class ConversationFragment extends BaseFragment {
    @BindView(R.id.conversationView)
    RecyclerView conversationView;
    @BindView(R.id.conversation_refreshView)
    SwipeRefreshLayout mRefreshView;

    private List<EMConversation> list = new ArrayList<>();
    ;
    private ConversationAdapter mConversationAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }

    private void initView() {
        mActivity = getActivity();
        loadConversationList();

        // 实例化会话列表的 Adapter 对象
        mConversationAdapter = new ConversationAdapter(mActivity, list);
        conversationView.setLayoutManager(new LinearLayoutManager(mActivity));
        conversationView.setAdapter(mConversationAdapter);
        refreshData();
        //recyclerView的点击事件
        mConversationAdapter.setmOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                EMConversation conversation = list.get(position);
                Intent intent = new Intent();
                if(conversation.conversationId().equals(ConstantsUtils.CONVERSATION_APPLY)){
                    intent.setClass(mActivity, ApplyForActivity.class);
                }else{
                    intent.setClass(mActivity, ChatActivity.class);
                    intent.putExtra(ConstantsUtils.CHAT_ID, list.get(position).conversationId());
                    intent.putExtra(ConstantsUtils.CHAT_TYPE, list.get(position).getType());
                }
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });


    }

    private void refreshData() {
        //设置下拉的颜色
        mRefreshView.setColorSchemeResources(R.color.refresh_one, R.color.refresh_two,
                R.color.refresh_three);
        mRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 防止在下拉刷新的时候，当前界面关闭导致错误
                        if (mActivity.isFinishing()) {
                            return;
                        }
                        //加载数据
                        refreshConversation();
                        mRefreshView.setRefreshing(false);
                    }
                },500);
            }
        });
    }

    //所有会话
    private void loadConversationList() {
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        synchronized (conversations) {
            for (EMConversation temp : conversations.values()) {
                list.add(temp);
            }
        }
    }

    /**
     * 刷新会话列表，重新加载会话列表到list集合，然后刷新adapter
     */
    public void refreshConversation() {
        if (mConversationAdapter != null) {
            list.clear();
            loadConversationList();
            mConversationAdapter.refreshList();
        }
    }

    /**
     * @param event 订阅事件类型
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(MessageEvent event) {
        refreshConversation();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // 刷新会话界面
        refreshConversation();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
