package com.em.yzzdemo.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.em.yzzdemo.BaseActivity;
import com.em.yzzdemo.R;
import com.em.yzzdemo.contacts.AddContactsActivity;
import com.em.yzzdemo.contacts.ContactsFragment;
import com.em.yzzdemo.conversation.ConversationFragment;
import com.em.yzzdemo.setting.SettingFragment;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.exceptions.HyphenateException;

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
    private ContactsFragment mContactsFragment;
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
        mToolbars = new String[]{"联系人", "会话", "设置"};
        //设置选中第一个
        toolbar.setTitle(mToolbars[0]);
        //toolbar字体颜色
        toolbar.setTitleTextColor(0xffffffff);
        //声明toolbar
        setSupportActionBar(toolbar);
        //创建联系人、会话、设置fragment
        mContactsFragment = new ContactsFragment();
        mConversationFragment = new ConversationFragment();
        mSettingFragment = new SettingFragment();
        mFragment = new Fragment[]{mContactsFragment, mConversationFragment, mSettingFragment};
        //viewpager适配器
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), mToolbars, mFragment);
        viewPager.setAdapter(adapter);
        //设置tabLayout
        setupTabLayout();

        //viewpager监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                toolbar.setTitle(mToolbars[position]);
                if (position == 0) {
                    toolbar.getMenu().clear();
                    tabLayout.getTabAt(0).getCustomView().findViewById(R.id.img_tab_item);
                    toolbar.inflateMenu(R.menu.menu_concacts);
                } else if (position == 1) {
                    toolbar.getMenu().clear();
                    toolbar.inflateMenu(R.menu.menu_conversation);
                } else {
                    toolbar.getMenu().clear();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                EMGroupOptions option = new EMGroupOptions();
                option.maxUsers = 200;
                option.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
                option.extField = "测试aaaaaaaaaa";
                try {
                    EMGroup group = EMClient.getInstance().groupManager().createGroup("测试", "dddd", new String[]{"yzz4"},
                            "jiajiajia", option);
                    String ext = group.getExtension();
                    group.getGroupId();
                    Log.d("groupExt", ext);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        EMClient.getInstance().groupManager().asyncJoinGroup("17101883506689", new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.d("JoinGroup", "onSuccess");
            }

            @Override
            public void onError(int i, String s) {
                Log.e("JoinGroup", i+s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });



    }

    //设置tabLayout
    private void setupTabLayout() {
        //设置下划线为白色
        tabLayout.setSelectedTabIndicatorColor(0xffffffff);
        //设置tablayout和viewpager绑定
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < 3; i++) {
            //加载布局
            View view = LayoutInflater.from(this).inflate(R.layout.main_tab_layout_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.img_tab_item);
            if (i == 0) {
                imageView.setImageDrawable(
                        getResources().getDrawable(R.drawable.em_tab_contacts_selector));
            } else if (i == 1) {
                imageView.setImageDrawable(
                        getResources().getDrawable(R.drawable.em_tab_chats_selector));
            } else {
                imageView.setImageDrawable(
                        getResources().getDrawable(R.drawable.em_tab_settings_selector));
            }
            //设置view给tablayout
            tabLayout.getTabAt(i).setCustomView(view);
        }
    }

    //设定菜单各按钮的点击动作
    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                case R.id.menu_create_group:
//                    startActivity(new Intent(MainActivity.this, InviteMembersActivity.class));
                    break;

                case R.id.menu_public_groups:
//                    startActivity(new Intent(MainActivity.this, PublicGroupsListActivity.class));
                    break;
                case R.id.menu_add_contacts:
                    startActivity(new Intent(mActivity, AddContactsActivity.class));
                    break;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (viewPager.getCurrentItem() == 0) {
            toolbar.inflateMenu(R.menu.menu_concacts);
        } else if (viewPager.getCurrentItem() == 1) {
            toolbar.inflateMenu(R.menu.menu_conversation);
        }
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
        return true;
    }


}
