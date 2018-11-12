package com.universityofalabama.cs495f2018.berthaIRT.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.universityofalabama.cs495f2018.berthaIRT.Interface;
import com.universityofalabama.cs495f2018.berthaIRT.R;
import com.universityofalabama.cs495f2018.berthaIRT.adapter.AddRemoveAdapter;

import java.util.List;
import java.util.Objects;

public class AddRemoveDialog extends AlertDialog{

    private List<String> labelList;
    private Interface.WithStringListListener listener;

    public AddRemoveDialog(Context ctx, List<String> labelList, Interface.WithStringListListener listener) {
        super(ctx);
        this.labelList = labelList;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_addremove);

        Objects.requireNonNull(getWindow()).clearFlags( WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        RecyclerView rv = findViewById(R.id.addremove_rv);
        AddRemoveAdapter adapter = new AddRemoveAdapter(getContext(), labelList);
        Objects.requireNonNull(rv).setAdapter(adapter);

        Objects.requireNonNull((View) findViewById(R.id.addremove_button_add)).setOnClickListener(x->{
            EditText et = findViewById(R.id.addremove_input);
            adapter.addToList(Objects.requireNonNull(et).getText().toString());
            et.setText("");
        });

        Objects.requireNonNull((View) findViewById(R.id.addremove_button_confirm)).setOnClickListener(x->{
            dismiss();
            listener.onEvent(adapter.getList());
        });
        Objects.requireNonNull((View) findViewById(R.id.addremove_button_close)).setOnClickListener(x -> dismiss());
    }
}