package com.ua.cs495_f18.berthaIRT.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ua.cs495_f18.berthaIRT.Fragment.AdminAllReportsFragment;
import com.ua.cs495_f18.berthaIRT.Fragment.AdminOpenReportsFragment;
import com.ua.cs495_f18.berthaIRT.Fragment.AdminRequiresActionFragment;


public class AdminViewPagerAdapter extends FragmentPagerAdapter {

    public AdminViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
            fragment = new AdminRequiresActionFragment();
        else if (position == 1)
            fragment = new AdminOpenReportsFragment();
        else if (position == 2)
            fragment = new AdminAllReportsFragment();
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
            title = "Requires Action";
        else if (position == 1)
            title = "Open";
        else if (position == 2)
            title = "All Reports";
        return title;
    }
}
