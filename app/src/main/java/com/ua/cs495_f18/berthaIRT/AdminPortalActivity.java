package com.ua.cs495_f18.berthaIRT;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
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

import com.ua.cs495_f18.berthaIRT.Adapter.ViewPagerAdapter;
import com.ua.cs495_f18.berthaIRT.Fragment.AdminAllReportsFragment;
import com.ua.cs495_f18.berthaIRT.Fragment.AdminOpenReportsFragment;
import com.ua.cs495_f18.berthaIRT.Fragment.AdminRequiresActionFragment;

public class AdminPortalActivity extends AppCompatActivity {
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Will place onCreate stuff here so that the values update when restarted, maybe.
    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_admin_portal);
        Toolbar toolbar = findViewById(R.id.toolbar_admin_portal);
        setSupportActionBar(toolbar);

        TabLayout tabLayout =  findViewById(R.id.tablayout_admin_portal);
        ViewPager mViewPager = findViewById(R.id.container_admin_portal);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.AddFragment(new AdminRequiresActionFragment(), "Requires Action");
        adapter.AddFragment(new AdminOpenReportsFragment(), "Open");
        adapter.AddFragment(new AdminAllReportsFragment(), "All Reports");

        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);

        initMenuDrawer();
    }

    private void initMenuDrawer() {
        DrawerLayout dl = findViewById(R.id.drawer_admin_portal);
        ActionBarDrawerToggle t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);
        dl.addDrawerListener(t);
        t.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView nv = findViewById(R.id.nav_admin_portal);
        menu = nv.getMenu();

        //function changes between Open Registration and Close Registration depending on current value.
        checkRegistration(); //TODO examine

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.editProfile)
                    actionEditProfile();
                else if (id == R.id.myCode)
                    actionDisplayCode();
                else if (id == R.id.groupDetails) {
                    serverInformation();
                }
                else if (id == R.id.inviteOtherAdmin) {
                    startActivity(new Intent(AdminPortalActivity.this, AdminInviteActivity.class));
                }
                else if (id == R.id.removeOtherAdmin) {
                    actionRemoveAdmins();

                }
                else if (id == R.id.openCloseRegistration) {
                    //Toast.makeText(AdminPortalActivity.this, "open/closeregistration", Toast.LENGTH_LONG).show();
                    actionChangeRegistration();
                }
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

    //TODO CHECK
    //@Override
    //public boolean onOptionsItemSelected(MenuItem item) {
    //    if (t.onOptionsItemSelected(item))
    //        return true;
    //    return super.onOptionsItemSelected(item);
    //}

    private void actionDisplayCode() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminPortalActivity.this);
        //TODO Look up string for school using code
        String schoolCode = "XXX";
        builder.setTitle("Give this code to students");
        builder.setMessage(schoolCode);
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.show();
        TextView messageView = dialog.findViewById(android.R.id.message);
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

    private void actionRemoveAdmins(){
        AdminSelectDialog d = new AdminSelectDialog(AdminPortalActivity.this);
        d.show();
    }

    private void serverInformation(){
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.activity_group_details_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialoglayout);
        builder.show();
        /*AlertDialog.Builder builder = new AlertDialog.Builder(AdminPortalActivity.this);
        builder.setTitle("Group Details:");
        builder.setView(layoutInflator.inflate(R.))
        builder.setNegativeButton(android.R.string.no, null);
        AlertDialog dialog = builder.show();
        TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.LEFT);*/
    }
}

class AdminSelectDialog extends AlertDialog.Builder{
    boolean confirmed;
    boolean[] checkedItems;
    public AdminSelectDialog(Context context){
        super(context);

        String[] adminItems= {
                "Johnathan",
                "Jake",
                "Scott",
                "Fahad",
                "Lucy",
                "Jason",
                "TestName1",
                "Test Name 2",
                "TestName3",
                "TestName4",
                "TestName5",
                "TestName6",
                "TestName7",
                "TestName8",
                "TestName9"
        };
        confirmed = false;
        checkedItems = new boolean[adminItems.length];

        setTitle("Select Admins to Remove");
        setCancelable(false);

        setMultiChoiceItems(adminItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                if(isChecked)
                    checkedItems[position] = true;
                else
                    checkedItems[position] = false;
            }
        });

        setPositiveButton("REMOVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int x) {
                confirmed = true;
                dialogInterface.dismiss();
            }
        });

        setNeutralButton("CLEAR ALL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int x) {
                for (int i=0; i<checkedItems.length; i++)
                    checkedItems[i] = false;
            }
        });

        setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int x) {
                dialogInterface.dismiss();
            }
        });

        create();
    }

}


