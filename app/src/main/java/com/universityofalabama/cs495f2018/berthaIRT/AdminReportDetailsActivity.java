package com.universityofalabama.cs495f2018.berthaIRT;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.universityofalabama.cs495f2018.berthaIRT.fragment.AdminReportDetailsFragment;
//import com.universityofalabama.cs495f2018.berthaIRT.fragment.MessagesFragment;

public class AdminReportDetailsActivity extends AppCompatActivity {

    final Fragment fragDetails = new AdminReportDetailsFragment();
    //final Fragment fragMessaging = new MessagesFragment();
    final FragmentManager fragDaddy = getSupportFragmentManager();
    Fragment activeFrag = fragDetails;
    BottomNavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_reportdetails);
        nav = findViewById(R.id.reportdetails_bottomnav);
        //nav.setOnNavigationItemSelectedListener(bottomListener);
        fragDaddy.beginTransaction().add(R.id.reportdetails_fragframe, fragDetails, "Details").commit();
        //fragDaddy.beginTransaction().add(R.id.reportdetails_fragframe,fragMessaging, "Messages").hide(fragMessaging).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomListener = item -> {
        Fragment toFrag;
        if(item.getItemId() == R.id.menu_report_report) toFrag = fragDetails;
        //else toFrag = fragMessaging;

        FragmentTransaction fTrans = fragDaddy.beginTransaction();
        //if (activeFrag == fragMessaging) fTrans.setCustomAnimations(R.anim.slidein_left, R.anim.slideout_right);
        //else fTrans.setCustomAnimations(R.anim.slidein_left, R.anim.slideout_right);

        //fTrans.hide(activeFrag).show(toFrag).commit();
        //activeFrag = toFrag;
        return true;
    };
}
