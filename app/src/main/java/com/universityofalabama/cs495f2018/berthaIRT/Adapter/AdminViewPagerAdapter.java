package com.universityofalabama.cs495f2018.berthaIRT.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.universityofalabama.cs495f2018.berthaIRT.Fragment.AdminAllReportsFragment;
import com.universityofalabama.cs495f2018.berthaIRT.Fragment.AdminOpenReportsFragment;
import com.universityofalabama.cs495f2018.berthaIRT.Fragment.AdminRequiresActionFragment;
import com.universityofalabama.cs495f2018.berthaIRT.R;

import java.io.Serializable;


public class AdminViewPagerAdapter extends FragmentPagerAdapter {
    public static FragmentManager frag;
    public Bundle bundo;

    public AdminViewPagerAdapter(FragmentManager fm) {
        super(fm);
        frag = fm;
        bundo = new Bundle();
        bundo.putSerializable("interface", fragRefresh);
    }

    public interface FragmentRefreshInterface extends Serializable {
        void refresh();
    }

    public FragmentRefreshInterface fragRefresh = new FragmentRefreshInterface() {
        @Override
        public void refresh() {
            AdminRequiresActionFragment adminRequiresActionFragment = (AdminRequiresActionFragment) frag.findFragmentByTag("android:switcher:" + R.id.container_adminportal + ":" + 0);
            adminRequiresActionFragment.populateFragment();

            AdminOpenReportsFragment adminOpenReportsFragment = (AdminOpenReportsFragment) frag.findFragmentByTag("android:switcher:" + R.id.container_adminportal + ":" + 1);
            adminOpenReportsFragment.populateFragment();

            AdminAllReportsFragment adminAllReportsFragment = (AdminAllReportsFragment) frag.findFragmentByTag("android:switcher:" + R.id.container_adminportal + ":" + 2);
            adminAllReportsFragment.populateFragment();
        }
    };

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
            fragment = new AdminRequiresActionFragment();
        else if (position == 1)
            fragment = new AdminOpenReportsFragment();
        else if (position == 2)
            fragment = new AdminAllReportsFragment();
        fragment.setArguments(bundo);
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