package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

public class AdminPortalActivity extends AppCompatActivity {

    private Menu menu;
    ActionBarDrawerToggle drawerToggle;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;

    public static String filter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Client.updateReportMap();
        setContentView(R.layout.activity_adminportal);
        Toolbar toolbar = findViewById(R.id.toolbar_adminportal);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.container_adminportal);
        viewPager.setOffscreenPageLimit(2);
        AdminViewPagerAdapter adminViewPagerAdapter = new AdminViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adminViewPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tablayout_adminportal);
        tabLayout.setupWithViewPager(viewPager);

        initMenuDrawer();
        getDrawerData();
    }

    private void getDrawerData() {
        Client.net.secureSend("admin/admingroupinfo", null, (r)->{
            JsonObject jay = Client.net.jp.parse(r).getAsJsonObject();
            String adminName = jay.get("adminName").getAsString();
            String groupName = jay.get("name").getAsString();
            String groupID = jay.get("id").getAsString();
            String groupStatus = jay.get("status").getAsString();
            ((TextView)findViewById(R.id.alt_admindrawer_name)).setText(adminName);
            ((TextView)findViewById(R.id.alt_admindrawer_institution)).setText(groupName);
            ((TextView)findViewById(R.id.alt_admindrawer_accesscode)).setText(groupID);
            updateRegistrationToggle(groupStatus);
        });
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
        searchView.setOnCloseListener( () -> {
                updateFilters("");
                return false;
        });
        return true;
    }

    public void updateFilters(String string) {
        filter = string;
        AdminRequiresActionFragment adminRequiresActionFragment = (AdminRequiresActionFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container_adminportal + ":" + 0);
        adminRequiresActionFragment.setFilter(filter);

        AdminOpenReportsFragment adminOpenReportsFragment = (AdminOpenReportsFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container_adminportal + ":" + 1);
        adminOpenReportsFragment.setFilter(filter);

        AdminAllReportsFragment adminAllReportsFragment = (AdminAllReportsFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container_adminportal + ":" + 2);
        adminAllReportsFragment.setFilter(filter);
    }



    public void refreshFragments() {

    }

    //Handles if the nav drawerLayout button is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    private void initMenuDrawer() {
        drawerLayout = findViewById(R.id.drawer_admin_portal);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView nv = findViewById(R.id.nav_admin_portal);
        menu = nv.getMenu();
        //TODO if user is SuperAdmin, make Admin Tools visible. else they are hidden.

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.resetPassword)
                    actionResetPassword();
                else if (id == R.id.groupDetails)
                    serverInformation();
                else if (id == R.id.inviteOtherAdmin) {
                    actionInviteAdmin();
                } else if (id == R.id.removeOtherAdmin)
                    actionRemoveAdmins();
                else if (id == R.id.openCloseRegistration)
                    actionChangeRegistration();
                else if (id == R.id.viewMetrics)
                    //TODO Add Activity
                    Toast.makeText(AdminPortalActivity.this, "viewMetrics", Toast.LENGTH_LONG).show();
                else if (id == R.id.menuLogout)
                    actionLogout();
                return true;
            }
        });
    }

    private void actionInviteAdmin() {
        startActivity(new Intent(AdminPortalActivity.this, AdminInviteActivity.class));
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(Gravity.LEFT);
        else {
            //Toast.makeText(AdminPortalActivity.this, "APPLYING BACK", Toast.LENGTH_LONG).show();
            super.onBackPressed();
        }
    }

    private void actionLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminPortalActivity.this);
        builder.setTitle("Are you sure you want to Logout?");
        builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    startActivity(new Intent(AdminPortalActivity.this, AdminLoginActivity.class));
                    finish();
                });
        builder.setNegativeButton(android.R.string.no, null);
        AlertDialog dialog = builder.show();
        TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }

    private void actionResetPassword(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminPortalActivity.this);
        builder.setTitle("Are you sure?");
        builder.setMessage("A temporary code for you to reset your password will be sent to your email and you will be logged out.");
        builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    Client.net.secureSend("admin/resetpassword", null, (r)->{
                        startActivity(new Intent(AdminPortalActivity.this, AdminLoginActivity.class));
                        finish();
                    });
                });
        builder.setNegativeButton(android.R.string.no, null);
        builder.show();
    }

    //Currently working on
    private void actionChangeRegistration() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminPortalActivity.this);

        String message = "You are about to CLOSE your group to new members.  No one may use your institution's access code until you reopen.";
        if(menu.findItem(R.id.openCloseRegistration).getTitle() == "Open Registration") message = "You are about to OPEN your group to new members and your access code will become active.";
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                Client.net.secureSend("admin/toggleregistration", null, (r)->{
                    updateRegistrationToggle(r);
                });
            });
        builder.setNegativeButton(android.R.string.no, null);
            //AlertDialog dialog = builder.show();
            //TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
            //messageView.setGravity(Gravity.LEFT);
        builder.show();
    }

    private void updateRegistrationToggle(String groupStatus){
        if(groupStatus.equals("Closed")) {
            menu.findItem(R.id.openCloseRegistration).setTitle("Open Registration");
            menu.findItem(R.id.openCloseRegistration).setIcon(getResources().getDrawable(R.drawable.ic_lock_open_black_24dp));
        }
        else {
            menu.findItem(R.id.openCloseRegistration).setTitle("Close Registration");
            menu.findItem(R.id.openCloseRegistration).setIcon(getResources().getDrawable(R.drawable.ic_lock_outline_black_24dp));
        }
    }

    //TODO very important stuff here -scott

    private void serverInformation(){
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.fragment_group_details_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialoglayout);

        TextView title = new TextView(this);
        // You Can Customise your Title here
        title.setText("Group Details");
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(20);
        title.setTextColor(Color.BLACK);
        builder.setCustomTitle(title);
        builder.setNegativeButton("OK", null);
        //TODO will need to set a max length for numbers before becoming scrollable.
        TextView t1 = (TextView) dialoglayout.findViewById(R.id.alt_groupdetails_noadmins);
        TextView t2 = (TextView) dialoglayout.findViewById(R.id.alt_groupdetails_totalreports);
        t1.setText("9"); // Will Check SQL
        t2.setText("20"); // Will Check SQL
        // add registration status
        // number of registered users?
        builder.show();
    }

    private void actionRemoveAdmins(){
        AlertDialog.Builder b = new AlertDialog.Builder(AdminPortalActivity.this);

        final String[] adminItems = {
                "Johnathan",
                "Jake",
                "Scott",
                "Fahad",
                "Lucy",
                "Jason",
                "TestName1",
                "Test Name 2"
        };
        boolean[] checkedItems = new boolean[adminItems.length];

        b.setTitle("Select Admins to Remove");
        b.setCancelable(false);

        b.setMultiChoiceItems(adminItems, checkedItems, (dialog, position, isChecked) -> {
            if(isChecked)
                checkedItems[position] = true;
            else
                checkedItems[position] = false;
        });

        b.setPositiveButton("REMOVE", (dialogInterface, x) -> {
            //verifyRemoveAdmins(checkedItems,adminItems);
            dialogInterface.dismiss();
        });

        b.setNegativeButton("CANCEL", (dialogInterface, x) -> dialogInterface.dismiss());

        b.create().show();
    }

//    private void verifyRemoveAdmins(boolean[] checkedItems, String[] items) {
//        List<String> sCheckedItems = StaticUtilities.getListOfStrings(checkedItems,items);
//        StringBuilder sb = StaticUtilities.getStringBuilder(sCheckedItems);
//
//        AlertDialog.Builder b = new AlertDialog.Builder(AdminPortalActivity.this);
//        b.setCancelable(false);
//        if(sb.toString().equals("")) {
//            b.setTitle("No Admins Selected");
//            b.setPositiveButton("DISMISS", (dialog, which) -> {
//                //TODO Remove Admin
//            });
//        }
//        else {
//            b.setTitle("Are you sure?");
//            b.setMessage("Removing " + sb + " as Admins");
//            b.setPositiveButton("REMOVE", (dialog, which) -> {
//                //TODO Remove Admin
//            });
//            b.setNegativeButton("CANCEL", (dialogInterface, which) -> dialogInterface.dismiss());
//        }
//        b.create().show();
//    }
}