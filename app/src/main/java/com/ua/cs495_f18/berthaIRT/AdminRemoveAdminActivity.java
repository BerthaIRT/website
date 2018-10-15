package com.ua.cs495_f18.berthaIRT;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class AdminRemoveAdminActivity extends AppCompatActivity {


    Button mCategories;
    TextView mItemSelected;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_remove_admin);
        selectAdminsToRemove();
    }

    private void selectAdminsToRemove() {
        mCategories = (Button) findViewById(R.id.button_category_select_continue);
        mItemSelected = (TextView) findViewById(R.id.tvItemSelected);

        listItems = getResources().getStringArray(R.array.category_item);
        checkedItems = new boolean[listItems.length];
        for(int i = 0; i < listItems.length; i++) {
            mUserItems.add(-1);
        }
        mCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(UserCreateReportActivity.this);
                mBuilder.setTitle("Select The Name of an Administrator to Remove");
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if(isChecked) {
                            mUserItems.set(position,position);
                        }
                        else{
                            mUserItems.set(position,-1);
                        }
                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        StringBuilder item = new StringBuilder();
                        String prefix = "";
                        for (int i = 0; i < mUserItems.size(); i++) {
                            if (mUserItems.get(i) > -1) {
                                item.append(prefix);
                                prefix = ", ";
                                item.append(listItems[mUserItems.get(i)]);
                            }
                        }
                        mItemSelected.setText(item);
                    }
                });
                mBuilder.setNegativeButton("DISMISS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setNeutralButton("CLEAR ALL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                            mUserItems.clear();
                            mItemSelected.setText("");
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

    }
}
