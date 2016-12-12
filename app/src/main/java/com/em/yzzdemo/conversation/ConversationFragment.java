package com.em.yzzdemo.conversation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.em.yzzdemo.BaseFragment;
import com.em.yzzdemo.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

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

    private List<EMConversation> list;

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
        ConversationAdapter mConversationAdapter = new ConversationAdapter(mActivity, list);
        conversationView.setLayoutManager(new LinearLayoutManager(mActivity));
        conversationView.setAdapter(mConversationAdapter);


    }

    //所有会话
    private void loadConversationList() {
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        list = new ArrayList<>();
        synchronized (conversations){
            for(EMConversation temp : conversations.values()){
                list.add(temp);
            }

        }
    }
}
