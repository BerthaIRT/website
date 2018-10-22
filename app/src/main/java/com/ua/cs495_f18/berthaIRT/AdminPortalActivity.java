package com.ua.cs495_f18.berthaIRT;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.ua.cs495_f18.berthaIRT.Adapter.AdminViewPagerAdapter;
import com.ua.cs495_f18.berthaIRT.Fragment.AdminAllReportsFragment;
import com.ua.cs495_f18.berthaIRT.Fragment.AdminOpenReportsFragment;
import com.ua.cs495_f18.berthaIRT.Fragment.AdminRequiresActionFragment;

public class AdminPortalActivity extends AppCompatActivity {
    private Menu menu;
    ActionBarDrawerToggle t;
    private ViewPager viewPager;
    private DrawerLayout dl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_portal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_admin_portal);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.container_admin_portal);
        AdminViewPagerAdapter adminViewPagerAdapter = new AdminViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adminViewPagerAdapter);
        TabLayout tabLayout =  (TabLayout) findViewById(R.id.tablayout_admin_portal);
        tabLayout.setupWithViewPager(viewPager);

        initMenuDrawer();

        getData();
    }

    private void getData() {
        Client.net.secureSend("admin/admingroupinfo", null, (r)->{
            JsonObject jay = Client.net.jp.parse(r).getAsJsonObject();
            String adminName = jay.get("adminName").getAsString();
            String groupName = jay.get("name").getAsString();
            String groupID = jay.get("id").getAsString();
            ((TextView)findViewById(R.id.message_admin_drawer_name)).setText(adminName);
            ((TextView)findViewById(R.id.message_admin_drawer_institution)).setText(groupName);
            ((TextView)findViewById(R.id.message_admin_drawer_accesscode)).setText(groupID);
        });
    }

    // Will place onCreate stuff here so that the values update when restarted, maybe.
    @Override
    protected void onStart() {
        super.onStart();
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
                //so that after it sends the filter the correct tab is selected
                int startPosition = viewPager.getCurrentItem();
                sendFilter(query);
                viewPager.setCurrentItem(startPosition,false);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //so that after it sends the filter the correct tab is selected
                int startPosition = viewPager.getCurrentItem();
                sendFilter("");
                viewPager.setCurrentItem(startPosition,false);
                return false;
            }
        });
        return true;
    }

    public void sendFilter(String filter) {
        //makes sure that the fragment will exist
        viewPager.setCurrentItem(0,false);
        AdminRequiresActionFragment adminRequiresActionFragment = (AdminRequiresActionFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container_admin_portal + ":" + 0);
        adminRequiresActionFragment.setFilter(filter);

        //makes sure that the fragment will exist
        viewPager.setCurrentItem(1,false);
        AdminOpenReportsFragment adminOpenReportsFragment = (AdminOpenReportsFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container_admin_portal + ":" + 1);
        adminOpenReportsFragment.setFilter(filter);

        //makes sure that the fragment will exist
        viewPager.setCurrentItem(2,false);
        AdminAllReportsFragment adminAllReportsFragment = (AdminAllReportsFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container_admin_portal + ":" + 2);
        adminAllReportsFragment.setFilter(filter);
    }

    //Handles if the nav drawer button is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (t.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    private void initMenuDrawer() {
        dl = findViewById(R.id.drawer_admin_portal);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);
        dl.addDrawerListener(t);
        t.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView nv = findViewById(R.id.nav_admin_portal);
        menu = nv.getMenu();
        //TODO if user is SuperAdmin, make Admin Tools visible. else they are hidden.
        //function changes between Open Registration and Close Registration depending on current value.
        checkRegistration(); //TODO examine

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
                else if (id == R.id.viewMetrics) {
                    //TODO Add Activity
                    Toast.makeText(AdminPortalActivity.this, "viewMetrics", Toast.LENGTH_LONG).show();
                }
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
        if (dl.isDrawerOpen(GravityCompat.START))
            dl.closeDrawer(Gravity.LEFT);
        else {
            Toast.makeText(AdminPortalActivity.this, "APPLYING BACK", Toast.LENGTH_LONG).show();
            super.onBackPressed();
        }
    }

    private void actionLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminPortalActivity.this);
        builder.setTitle("Are you sure you want to Logout?");
        builder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Unm",null);
                        editor.putString("Psw",null);
                        editor.apply();
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

    private void actionResetPassword(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminPortalActivity.this);
        builder.setTitle("Are you sure?");
        builder.setMessage("A temporary code for you to reset your password will be sent to your email and you will be logged out.");
        builder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Unm",null);
                        editor.putString("Psw",null);
                        editor.apply();
                        Client.net.secureSend("admin/resetpassword", null, (r)->{
                            startActivity(new Intent(AdminPortalActivity.this, AdminLoginActivity.class));
                            finish();
                        });
                    }
                });
        builder.setNegativeButton(android.R.string.no, null);
        AlertDialog dialog = builder.show();
    }

    //Currently working on...
    //TODO get registration value from SQL and adjust accordingly...
    int tempValueToDelete = 0; // will delete when to do is finished... For checkRegistration & actionChangeRegistration function...
    private void checkRegistration(){
        if(tempValueToDelete == 1) {
            menu.findItem(R.id.openCloseRegistration).setTitle("Open Registration");
            menu.findItem(R.id.openCloseRegistration).setIcon(getResources().getDrawable(R.drawable.ic_lock_open_black_24dp));
        }
        else {
            menu.findItem(R.id.openCloseRegistration).setTitle("Close Registration");
            menu.findItem(R.id.openCloseRegistration).setIcon(getResources().getDrawable(R.drawable.ic_lock_outline_black_24dp));
        }
    }

    //Currently working on
    private void actionChangeRegistration() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminPortalActivity.this);

        // If Case is checking to see if current value is Open. Else If Case checks to see if current value is closed. Else there is an error...
        if(menu.findItem(R.id.openCloseRegistration).getTitle() == "Open Registration") {
            builder.setMessage("You are attempting to CLOSE registration. This means no one will be allowed to register to your group. Is this correct?");
            builder.setPositiveButton(android.R.string.yes,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tempValueToDelete = 0; // set it to closed and restart. will change
                            checkRegistration(); // update title / icon
                        }
                    });
            builder.setNegativeButton(android.R.string.no, null);
            AlertDialog dialog = builder.show();
            TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
            messageView.setGravity(Gravity.LEFT);
        }
        else if(menu.findItem(R.id.openCloseRegistration).getTitle() == "Close Registration") {
            builder.setMessage("You are attempting to OPEN registration. This means users will be able to join your group. Is this correct?");
            builder.setPositiveButton(android.R.string.yes,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tempValueToDelete = 1; // set it to open and restart... will change
                            checkRegistration(); // update title / icon
                        }
                    });
            builder.setNegativeButton(android.R.string.no, null);
            AlertDialog dialog = builder.show();
            TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
            messageView.setGravity(Gravity.LEFT);
        }
        else {
            //print error
        }
    }

    private void serverInformation(){
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.activity_group_details_dialog, null);
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
        builder.setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        //TODO will need to set a max length for numbers before becoming scrollable.
        TextView t1 = (TextView) dialoglayout.findViewById(R.id.numOfAdminsRetVal);
        TextView t2 = (TextView) dialoglayout.findViewById(R.id.numOfReportsRequiringActionRetVal);
        TextView t3 = (TextView) dialoglayout.findViewById(R.id.numOfReportsTotalRetVal);
        t1.setText("2"); // Will Check SQL
        t2.setText("5"); // Will Check SQL
        t3.setText("5"); // Will Check SQL
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

        b.setMultiChoiceItems(adminItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                if(isChecked)
                    checkedItems[position] = true;
                else
                    checkedItems[position] = false;
            }
        });

        b.setPositiveButton("REMOVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int x) {
                verifyRemoveAdmins(checkedItems,adminItems);
                dialogInterface.dismiss();
            }
        });

        b.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int x) {
                dialogInterface.dismiss();
            }
        });

        b.create().show();
    }

    private void verifyRemoveAdmins(boolean[] checkedItems, String[] items) {
        StringBuilder sb = new StringBuilder();
        String prefix = "";
        for (int i=0; i<checkedItems.length; i++) {
            if (checkedItems[i]) {
                sb.append(prefix);
                prefix = ", ";
                sb.append(items[i]);
            }
        }
        AlertDialog.Builder b = new AlertDialog.Builder(AdminPortalActivity.this);
        b.setCancelable(false);
        if(sb.toString().equals("")) {
            b.setTitle("No Admins Selected");
            b.setPositiveButton("DISMISS", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //TODO Remove Admin
                }
            });
        }
        else {
            b.setTitle("Are you sure?");
            b.setMessage("Removing " + sb + " as Admins");
            b.setPositiveButton("REMOVE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //TODO Remove Admin
                }
            });
            b.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    dialogInterface.dismiss();
                }
            });
        }
        b.create().show();
    }

    //private boolean isSuperAdmin(){
        //IF ADMIN IS OWNER OF GROUP, RETURN TRUE; ELSE FALSE;
    //this will be a cognito-related function anyway and won't be in this class
    //}
}