package com.em.yzzdemo.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.em.yzzdemo.BaseActivity;
import com.em.yzzdemo.R;
import com.em.yzzdemo.contacts.ContactsFragmnet;
import com.em.yzzdemo.conversation.ConversationFragment;
import com.em.yzzdemo.setting.SettingFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_widget_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.main_vp)
    ViewPager viewPager;
    private String[] mToolbars;
    private ContactsFragmnet mContactsFragmnet;
    private ConversationFragment mConversationFragment;
    private SettingFragment mSettingFragment;
    private Fragment[] mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        ButterKnife.bind(mActivity);


        initView();
        
    }

    private void initView() {
        //设置toolbar值
        mToolbars = new String[]{"联系人","会话","设置"};
        setSupportActionBar(toolbar);
        //设置选中第一个
        getSupportActionBar().setTitle(mToolbars[0]);
        //toolbar字体颜色
        toolbar.setTitleTextColor(0xffffffff);
        //创建联系人、会话、设置fragment
        mContactsFragmnet = new ContactsFragmnet();
        mConversationFragment = new ConversationFragment();
        mSettingFragment = new SettingFragment();
        mFragment = new Fragment[]{mContactsFragmnet,mConversationFragment,mSettingFragment};
        tabLayout.setupWithViewPager(viewPager);
        //设置tabLayout选中前后的字体颜色
        tabLayout.setTabTextColors(0x89ffffff,0xffffffff);
        //viewpager适配器
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),mToolbars,mFragment);
        viewPager.setAdapter(adapter);
        //viewpager监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                toolbar.setTitle(mToolbars[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
