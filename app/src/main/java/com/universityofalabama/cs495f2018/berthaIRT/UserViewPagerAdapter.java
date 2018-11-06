package com.universityofalabama.cs495f2018.berthaIRT;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.universityofalabama.cs495f2018.berthaIRT.UserReportHistoryFragment;


public class UserViewPagerAdapter extends FragmentPagerAdapter {

    public UserViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
            fragment = new UserReportHistoryFragment();
        return fragment;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
            title = "User Report History";
        return title;
    }
}