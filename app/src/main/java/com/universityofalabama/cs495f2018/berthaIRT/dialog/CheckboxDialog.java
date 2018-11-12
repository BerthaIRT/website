package com.universityofalabama.cs495f2018.berthaIRT.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;

import com.universityofalabama.cs495f2018.berthaIRT.Interface;
import com.universityofalabama.cs495f2018.berthaIRT.R;
import com.universityofalabama.cs495f2018.berthaIRT.adapter.CheckboxAdapter;

import java.util.Arrays;
import java.util.List;

public class CheckboxDialog extends AlertDialog{
    List<Boolean> boolList;
    List<String> labelList;
    Interface.WithStringListListener listener;
    public CheckboxDialog(Context ctx, List<Boolean> boolList, List<String> labelList, Interface.WithStringListListener listener) {
        super(ctx);
        this.labelList = labelList;
        this.listener = listener;

        if(boolList == null) {
            Boolean[] falseBools = new Boolean[labelList.size()];
            Arrays.fill(falseBools, Boolean.FALSE);
            this.boolList = Arrays.asList(falseBools);
        }
        else this.boolList = boolList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_checkboxes);

        RecyclerView rv = findViewById(R.id.checkboxes_rv);
        CheckboxAdapter adapter = new CheckboxAdapter(getContext(), labelList, boolList);
        rv.setAdapter(adapter);

        findViewById(R.id.checkboxes_button_confirm).setOnClickListener(x->{
            listener.onEvent(adapter.getCheckedItems());
        });
        findViewById(R.id.checkboxes_button_close).setOnClickListener(x -> dismiss());
    }
}