package com.em.yzzdemo.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
    private int mCurrentPageIndex = 0;

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
        //设置选中第一个
        toolbar.setTitle(mToolbars[0]);
        //toolbar字体颜色
        toolbar.setTitleTextColor(0xffffffff);
        //声明toolbar
        setSupportActionBar(toolbar);
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
                mCurrentPageIndex = position;
                toolbar.setTitle(mToolbars[position]);
                if(position == 0){
                    toolbar.getMenu().clear();
                    toolbar.inflateMenu(R.menu.menu_concacts);
                }else if(position == 1){
                    toolbar.getMenu().clear();
                    toolbar.inflateMenu(R.menu.menu_chat);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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
                    break;
            }
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (viewPager.getCurrentItem() == 0) {
            toolbar.inflateMenu(R.menu.menu_concacts);
            setSearchViewQueryListener();
        } else if (viewPager.getCurrentItem() == 1) {
            toolbar.inflateMenu(R.menu.menu_chat);
            setSearchViewQueryListener();
        }
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
        return super.onCreateOptionsMenu(menu);
    }

    private void setSearchViewQueryListener() {

        SearchView searchView;
        if (mCurrentPageIndex == 0) {
            searchView = (SearchView) toolbar.getMenu().findItem(R.id.action_contacts_search).getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override public boolean onQueryTextSubmit(String query) {
                    return true;
                }

                @Override public boolean onQueryTextChange(String newText) {
//                    mContactsFragmnet.filter(newText);
                    return true;
                }
            });
        } else if (mCurrentPageIndex == 1) {
            searchView = (SearchView) MenuItemCompat.getActionView(
                    toolbar.getMenu().findItem(R.id.action_conversation_search));
            // search conversations list
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override public boolean onQueryTextSubmit(String query) {
//                    mConversationFragment.filter(query);
                    return true;
                }

                @Override public boolean onQueryTextChange(String newText) {
//                    mConversationFragment.filter(newText);
                    return true;
                }
            });
        }
    }
}
