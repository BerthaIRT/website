package com.ua.cs495f2018.berthaIRT.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.ua.cs495f2018.berthaIRT.Interface;
import com.ua.cs495f2018.berthaIRT.R;

import java.util.Objects;

public class NotesDialog extends AlertDialog{

    private String title;
    private Interface.WithStringListener listener;

    public NotesDialog(Context ctx, String title, Interface.WithStringListener listener) {
        super(ctx);
        this.title = title;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_notes);

//        if(Objects.requireNonNull(getOwnerActivity()).findViewById(R.id.reportdetails_bottomnav) != null)
            Objects.requireNonNull(getWindow()).clearFlags( WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        ((TextView) Objects.requireNonNull((View) findViewById(R.id.notesdialog_alt_title))).setText(title);

        Objects.requireNonNull((View) findViewById(R.id.notesdialog_button_confirm)).setOnClickListener(x -> {
            dismiss();
            if(listener != null)
                listener.onEvent(((EditText) Objects.requireNonNull((View) findViewById(R.id.notesdialog_alt_text))).getText().toString());
        });
        Objects.requireNonNull((View) findViewById(R.id.notesdialog_button_close)).setOnClickListener(x -> dismiss());
    }
}