package com.universityofalabama.cs495f2018.berthairt;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class AdminMainActivity extends AppCompatActivity {

    final Fragment fragAlerts = new AlertCardsFragment();
    final Fragment fragReports = new AdminReportCardsFragment();
    final Fragment fragDashboard = new AdminDashboardFragment();
    final FragmentManager fragDaddy = getSupportFragmentManager();
    Fragment activeFrag = fragAlerts;
    BottomNavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        nav = findViewById(R.id.admin_main_bottomnav);
        nav.setOnNavigationItemSelectedListener(bottomListener);

        fragDaddy.beginTransaction().add(R.id.adminmain_fragframe, fragDashboard, "Dashboard").hide(fragDashboard).commit();
        fragDaddy.beginTransaction().add(R.id.adminmain_fragframe, fragReports, "Reports").hide(fragReports).commit();
        fragDaddy.beginTransaction().add(R.id.adminmain_fragframe, fragAlerts, "Alerts").hide(fragAlerts).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomListener = item -> {
            Fragment toFrag;
            if(item.getItemId() == R.id.menu_admin_alerts) toFrag = fragAlerts;
            else if(item.getItemId() == R.id.menu_admin_reports) toFrag = fragReports;
            else toFrag = fragDashboard;

            FragmentTransaction fTrans = fragDaddy.beginTransaction();
            if (activeFrag == fragDashboard) fTrans.setCustomAnimations(R.anim.slidein_left, R.anim.slideout_right);
            else if (activeFrag == fragAlerts || toFrag == fragDashboard) fTrans.setCustomAnimations(R.anim.slidein_right, R.anim.slideout_left);
            else fTrans.setCustomAnimations(R.anim.slidein_left, R.anim.slideout_right);

            fTrans.hide(activeFrag).show(toFrag).commit();
            activeFrag = toFrag;
            return true;
        };
}
