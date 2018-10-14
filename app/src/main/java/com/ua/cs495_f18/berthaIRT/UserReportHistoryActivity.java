package com.ua.cs495_f18.berthaIRT;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ua.cs495_f18.berthaIRT.Adapter.ViewPagerAdapter;
import com.ua.cs495_f18.berthaIRT.Fragment.UserReportHistoryFragment;

public class UserReportHistoryActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reporthistory);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container_user_report_history);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new UserReportHistoryFragment(), "User Report History");
        mViewPager.setAdapter(adapter);
    }
}
