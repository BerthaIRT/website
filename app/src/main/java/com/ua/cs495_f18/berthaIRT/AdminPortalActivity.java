package com.ua.cs495_f18.berthaIRT;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ua.cs495_f18.berthaIRT.Adapter.ViewPagerAdapter;
import com.ua.cs495_f18.berthaIRT.Fragment.AdminAllReportsFragment;
import com.ua.cs495_f18.berthaIRT.Fragment.AdminOpenReportsFragment;
import com.ua.cs495_f18.berthaIRT.Fragment.AdminRequiresActionFragment;

public class AdminPortalActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_portal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_admin_portal);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tablayout_admin_portal);
        mViewPager = (ViewPager) findViewById(R.id.container_admin_portal);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.AddFragment(new AdminRequiresActionFragment(), "Requires Action");
        adapter.AddFragment(new AdminOpenReportsFragment(), "Open");
        adapter.AddFragment(new AdminAllReportsFragment(), "All Reports");

        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);

        dl = findViewById(R.id.drawer_admin_portal);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);
        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = findViewById(R.id.nav_admin_portal);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.editProfile)
                    actionEditProfile();
                else if (id == R.id.myCode)
                    actionDisplayCode();
                else if (id == R.id.menuLogout)
                    actionLogout();
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (t.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    private void actionDisplayCode() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminPortalActivity.this);
        //TODO Look up string for school using code
        String schoolCode = "XXX";
        builder.setTitle("Your School Code Is: ");
        builder.setMessage(schoolCode);
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.show();
        TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }

    private void actionLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminPortalActivity.this);
        builder.setTitle("Are you sure you want to Logout?");
        builder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(AdminPortalActivity.this, AdminLoginActivity.class));
                        //don't allow the app to go back
                        finish();
                    }
                });
        builder.setNegativeButton(android.R.string.no, null);
        AlertDialog dialog = builder.show();
        TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }

    private void actionEditProfile(){
        startActivity(new Intent(AdminPortalActivity.this, AdminEditProfileActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.input_admin_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

}
