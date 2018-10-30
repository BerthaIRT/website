package com.ua.cs495_f18.berthaIRT;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ua.cs495_f18.berthaIRT.Adapter.UserViewPagerAdapter;
import com.ua.cs495_f18.berthaIRT.Fragment.UserReportHistoryFragment;

public class UserReportHistoryActivity extends AppCompatActivity {
    private ViewPager viewPager;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reporthistory);

        Toolbar toolbar = findViewById(R.id.toolbar_user_reporthistory);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.container_user_report_history);
        UserViewPagerAdapter userViewPagerAdapter = new UserViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(userViewPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.input_filter);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                updateFilters(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                updateFilters(newText);
                return false;
            }
        });
        searchView.setOnCloseListener(() -> {
            updateFilters("");
            return false;
        });
        return true;
    }

    public void updateFilters(String filter) {
        UserReportHistoryFragment userReportHistoryFragment = (UserReportHistoryFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container_user_report_history + ":" + viewPager.getCurrentItem());
        userReportHistoryFragment.setFilter(filter);
    }
}