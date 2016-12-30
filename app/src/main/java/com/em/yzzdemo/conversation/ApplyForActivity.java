package com.em.yzzdemo.conversation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.em.yzzdemo.BaseActivity;
import com.em.yzzdemo.R;
import com.em.yzzdemo.event.ApplyForEvent;
import com.em.yzzdemo.utils.ConstantsUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Geri on 2016/12/27.
 */
public class ApplyForActivity extends BaseActivity {

    @BindView(R.id.apply_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.apply_recyclerView)
    RecyclerView mRecyclerView;
    // 当前会话对象，这里主要是记录申请与记录信息
    private EMConversation mConversation;
    private ApplyForAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mActivity = this;
        mToolbar.setTitle("申请与通知");
        setSupportActionBar(mToolbar);
        //toolbar字体颜色
        mToolbar.setTitleTextColor(0xffffffff);
        mToolbar.setNavigationIcon(R.mipmap.arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mConversation = EMClient.getInstance()
                .chatManager()
                .getConversation(ConstantsUtils.CONVERSATION_APPLY, null, true);
        // 设置当前会话未读数为 0
        mConversation.markAllMessagesAsRead();
        mAdapter = new ApplyForAdapter(mActivity);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        // 设置适配器
        mRecyclerView.setAdapter(mAdapter);
        // 通过自定义接口来实现RecyclerView item的点击和长按事件
        setItemClickListener();

    }

    private void setItemClickListener() {
        mAdapter.setItemCallBack(new ApplyForAdapter.ApplyForItemCallBack() {
            @Override public void onAction(int action, Object tag) {
                String msgId = (String) tag;
                switch (action) {
                    case ConstantsUtils.ACTION_APPLY_FOR_CLICK:
//                        jumpUserInfo(msgId);
                        break;
                    case ConstantsUtils.ACTION_APPLY_FOR_AGREE:
//                        agreeApply(msgId);
                        break;
                    case ConstantsUtils.ACTION_APPLY_FOR_DELETE:
//                        deleteApply(msgId);
                        break;
                }
            }
        });
    }

    /**
     * 刷新邀请信息列表
     */
    private void refresh() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 使用 EventBus 的订阅方式监听事件的变化，这里 EventBus 3.x 使用注解的方式确定方法调用的线程
     */
    @Subscribe(threadMode = ThreadMode.MAIN) public void onEventBus(ApplyForEvent event) {
        refresh();
    }

    /**
     * 重写父类的onResume方法， 在这里注册广播
     */
    @Override public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(mActivity);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(mActivity);
    }
}
