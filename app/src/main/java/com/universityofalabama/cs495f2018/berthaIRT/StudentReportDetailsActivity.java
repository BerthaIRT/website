package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.universityofalabama.cs495f2018.berthaIRT.fragment.AdminDashboardFragment;
import com.universityofalabama.cs495f2018.berthaIRT.fragment.AdminReportCardsFragment;
import com.universityofalabama.cs495f2018.berthaIRT.fragment.AlertCardsFragment;
import com.universityofalabama.cs495f2018.berthaIRT.fragment.MessagesFragment;
import com.universityofalabama.cs495f2018.berthaIRT.fragment.StudentReportDetailsFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StudentReportDetailsActivity extends AppCompatActivity {
    FragmentManager fragDaddy = getSupportFragmentManager();
    Fragment fragDetails, fragMessages, fromFrag;
    ImageView imgDetails, imgMessages;
    TextView tvDetails, tvMessages;
    View nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportdetails);

        nav = findViewById(R.id.reportdetails_bottomnav);
        final View activityRootView = findViewById(R.id.root_frame);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();

            activityRootView.getWindowVisibleDisplayFrame(r);

            int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
            if (heightDiff > (r.bottom - r.top)/8) {
                nav.setVisibility(View.GONE);
            }else{
                nav.setVisibility(View.VISIBLE);
            }
        });

        imgDetails = findViewById(R.id.reportdetails_img_details);
        imgMessages = findViewById(R.id.reportdetails_img_messages);
        tvDetails = findViewById(R.id.reportdetails_alt_details);
        tvMessages = findViewById(R.id.reportdetails_alt_messages);

        fragDetails = new StudentReportDetailsFragment();
        fragMessages = new MessagesFragment();

        fragDaddy.beginTransaction().add(R.id.reportdetails_fragframe, fragMessages, "Messages").hide(fragMessages).commit();
        fragDaddy.beginTransaction().add(R.id.reportdetails_fragframe, fragDetails, "Details").hide(fragDetails).commit();

        findViewById(R.id.reportdetails_button_details).setOnClickListener((v)->makeActive(fragDetails));
        findViewById(R.id.reportdetails_button_messages).setOnClickListener((v)->makeActive(fragMessages));

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        //if there was extras passed to the intent
        if(extras != null) {
            Client.activeReport = Client.reportMap.get(Integer.parseInt(extras.getString("id")));
            //if it's coming from a notification click
            if (intent.getStringExtra("frag").equals("messages"))
                makeActive(fragMessages);
            else
                makeActive(fragDetails);
        }
        else
            makeActive(fragDetails);
    }

    public void makeActive(Fragment toFrag){
        FragmentTransaction fTrans = fragDaddy.beginTransaction();

        if(fromFrag == null)
            fTrans.show(toFrag).commit();
        else {
            if (toFrag == fragDetails)
                fTrans.setCustomAnimations(R.anim.slidein_left, R.anim.slideout_right);
            else
                fTrans.setCustomAnimations(R.anim.slidein_right, R.anim.slideout_left);
            fTrans.hide(fromFrag).show(toFrag).commit();
        }

        List<ImageView> ivs = Arrays.asList(imgDetails, imgMessages);
        List<TextView> tvs = Arrays.asList(tvDetails, tvMessages);
        if(toFrag == fragMessages) {
            Collections.swap(ivs, 0, 1);
            Collections.swap(tvs, 0, 1);
        }
        ivs.get(0).setScaleX(1.0f);
        ivs.get(0).setScaleY(1.0f);
        tvs.get(0).setTypeface(null, Typeface.BOLD);
        tvs.get(0).setTextColor(Color.parseColor("#FFFFFFFF"));
        ivs.get(1).setScaleX(0.8f);
        ivs.get(1).setScaleY(0.8f);
        tvs.get(1).setTypeface(null, Typeface.NORMAL);
        tvs.get(1).setTextColor(Color.parseColor("#88FFFFFF"));

        fromFrag = toFrag;
    }
}