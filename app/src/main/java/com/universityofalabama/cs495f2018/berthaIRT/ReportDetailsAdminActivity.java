package com.universityofalabama.cs495f2018.berthaIRT;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class ReportDetailsAdminActivity extends AppCompatActivity {

    final Fragment fragDetails = new ReportDetailsAdminFragment();
    final Fragment fragMessaging = new MessagesFragment();
    final FragmentManager fragDaddy = getSupportFragmentManager();
    Fragment activeFrag = fragDetails;
    BottomNavigationView nav;
    private boolean keyboardListenersAttached = false;
    private ViewGroup rootLayout;
    private CardView c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_reportdetails);
        nav = findViewById(R.id.reportdetails_bottomnav);
        nav.setOnNavigationItemSelectedListener(bottomListener);
        c = findViewById(R.id.cardView9);

        //Client.updateReportMap();

        fragDaddy.beginTransaction().add(R.id.reportdetails_fragframe, fragDetails, "Details").commit();
        fragDaddy.beginTransaction().add(R.id.reportdetails_fragframe,fragMessaging, "Messages").hide(fragMessaging).commit();
        attachKeyboardListeners();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomListener = item -> {
        Fragment toFrag;
        if(item.getItemId() == R.id.menu_report_report) toFrag = fragDetails;
        else toFrag = fragMessaging;

        FragmentTransaction fTrans = fragDaddy.beginTransaction();
        if (activeFrag == fragMessaging) fTrans.setCustomAnimations(R.anim.slidein_left, R.anim.slideout_right);
        else fTrans.setCustomAnimations(R.anim.slidein_left, R.anim.slideout_right);

        fTrans.hide(activeFrag).show(toFrag).commit();
        activeFrag = toFrag;
        return true;
    };

    boolean mKeyboardVisible = false;
    private final ViewTreeObserver.OnGlobalLayoutListener mLayoutKeyboardVisibilityListener = () -> {
                final Rect rectangle = new Rect();
                final View contentView = rootLayout;
                contentView.getWindowVisibleDisplayFrame(rectangle);
                int screenHeight = contentView.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // If keypad is shown, the rectangle.bottom is smaller than that before.
                int keypadHeight = screenHeight - rectangle.bottom;
                // 0.15 ratio is perhaps enough to determine keypad height.
                boolean isKeyboardNowVisible = keypadHeight > screenHeight * 0.15;

                if (mKeyboardVisible != isKeyboardNowVisible) {
                    if (isKeyboardNowVisible)
                        onShowKeyboard();
                    else
                        onHideKeyboard();
                }

                mKeyboardVisible = isKeyboardNowVisible;
            };

    protected void onShowKeyboard() {
        // do things when keyboard is shown
        c.setVisibility(View.GONE);
        nav.setVisibility(View.GONE);
    }

    protected void onHideKeyboard() {
        // do things when keyboard is hidden
        c.setVisibility(View.VISIBLE);
        nav.setVisibility(View.VISIBLE);
    }




    protected void attachKeyboardListeners() {
        if (keyboardListenersAttached)
            return;
        rootLayout = findViewById(R.id.root_view_reportdetails);
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(mLayoutKeyboardVisibilityListener);

        keyboardListenersAttached = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (keyboardListenersAttached)
            rootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(mLayoutKeyboardVisibilityListener);
    }
}
