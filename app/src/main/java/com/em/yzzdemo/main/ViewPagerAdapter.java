package com.em.yzzdemo.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Geri on 2016/11/27.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter{
    private String[] mToolbars;
    private Fragment[] mFragment;

    public ViewPagerAdapter(FragmentManager fm, String[] toolbars, Fragment[] fragment) {
        super(fm);
        this.mToolbars = toolbars;
        this.mFragment = fragment;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragment[position];
    }

    @Override
    public int getCount() {
        return mFragment.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mToolbars[position];
    }


}
