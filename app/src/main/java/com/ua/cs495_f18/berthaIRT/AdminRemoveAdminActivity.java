package com.ua.cs495_f18.berthaIRT;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.ua.cs495_f18.berthaIRT.R;

public class AdminRemoveAdminActivity extends ListActivity {

    String[] admin_name= {
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
            "TestName6"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_remove_admin);

        // -- Display mode of the ListView

        ListView listview= getListView();
        //	listview.setChoiceMode(listview.CHOICE_MODE_NONE);
        //	listview.setChoiceMode(listview.CHOICE_MODE_SINGLE);
        listview.setChoiceMode(listview.CHOICE_MODE_MULTIPLE);

        //--	text filtering
        listview.setTextFilterEnabled(true);

        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked,admin_name));
    }

    public void onListItemClick(ListView parent, View v,int position,long id){
        CheckedTextView item = (CheckedTextView) v;
        Toast.makeText(this, admin_name[position] + " checked : " +
                item.isChecked(), Toast.LENGTH_SHORT).show();
    }
}

