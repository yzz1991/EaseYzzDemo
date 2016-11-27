package com.em.yzzdemo.main;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.em.yzzdemo.R;
import com.em.yzzdemo.contacts.ContactsFragmnet;
import com.em.yzzdemo.conversation.ConversationFragment;
import com.em.yzzdemo.setting.SettingFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

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
        ButterKnife.bind(this);


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
        //
        mContactsFragmnet = new ContactsFragmnet();
        mConversationFragment = new ConversationFragment();
        mSettingFragment = new SettingFragment();
        mFragment = new Fragment[]{mContactsFragmnet,mConversationFragment,mSettingFragment};
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(0x89ffffff,0xffffffff);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),mToolbars,mFragment);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
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
