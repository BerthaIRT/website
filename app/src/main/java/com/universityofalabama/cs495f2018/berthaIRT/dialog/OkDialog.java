package com.universityofalabama.cs495f2018.berthaIRT.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.universityofalabama.cs495f2018.berthaIRT.Interface;
import com.universityofalabama.cs495f2018.berthaIRT.R;

public class OkDialog extends AlertDialog{
    String title;
    String text;
    Interface.WithVoidListener listener;
    public OkDialog(Context ctx, String title, String text, Interface.WithVoidListener listener) {
        super(ctx);
        this.title = title;
        this.text = text;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_general);

        findViewById(R.id.generaldialog_button_ok).setVisibility(View.VISIBLE);
        findViewById(R.id.generaldialog_button_yes).setVisibility(View.GONE);
        findViewById(R.id.generaldialog_button_no).setVisibility(View.GONE);

        ((TextView) findViewById(R.id.generaldialog_alt_text)).setText(text);
        if (title == null) findViewById(R.id.generaldialog_alt_title).setVisibility(View.GONE);
        else ((TextView) findViewById(R.id.generaldialog_alt_title)).setText(title);

        if (listener != null)
            findViewById(R.id.generaldialog_button_ok).setOnClickListener(x -> listener.onEvent());
    }
}