package com.universityofalabama.cs495f2018.berthaIRT.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.universityofalabama.cs495f2018.berthaIRT.Interface;
import com.universityofalabama.cs495f2018.berthaIRT.R;

public class YesNoDialog extends AlertDialog{
    String title;
    String text;
    Interface.YesNoHandler handler;
    public YesNoDialog(Context ctx, String title, String text, Interface.YesNoHandler handler) {
        super(ctx);
        this.title = title;
        this.text = text;
        this.handler = handler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_general);

        findViewById(R.id.generaldialog_button_ok).setVisibility(View.GONE);
        findViewById(R.id.generaldialog_button_yes).setVisibility(View.VISIBLE);
        findViewById(R.id.generaldialog_button_no).setVisibility(View.VISIBLE);

        ((TextView) findViewById(R.id.generaldialog_alt_text)).setText(text);
        if (title == null) findViewById(R.id.generaldialog_alt_title).setVisibility(View.GONE);
        else ((TextView) findViewById(R.id.generaldialog_alt_title)).setText(title);

        findViewById(R.id.generaldialog_button_no).setOnClickListener(x -> handler.onNoClicked());
        findViewById(R.id.generaldialog_button_yes).setOnClickListener(x -> handler.onYesClicked());
    }
}