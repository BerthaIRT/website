package com.universityofalabama.cs495f2018.berthaIRT.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.universityofalabama.cs495f2018.berthaIRT.Interface;
import com.universityofalabama.cs495f2018.berthaIRT.R;

import java.util.Objects;

public class InputDialog extends AlertDialog{

    private Context ctx;
    private String label;
    private String text;
    private String buttonText;
    private Interface.WithStringListener listener;

    public InputDialog(Context ctx, String label, String text, String buttonText, Interface.WithStringListener listener) {
        super(ctx);
        this.ctx = ctx;
        this.label = label;
        this.text = text;
        this.buttonText = buttonText;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_notes);

        ((TextView) findViewById(R.id.inputdialog_button_confirm)).setText(label);
        ((TextView) findViewById(R.id.inputdialog_alt_text)).setText(text);
        ((TextView) findViewById(R.id.inputdialog_alt_button)).setText(buttonText);

        findViewById(R.id.inputdialog_button_confirm).setOnClickListener(x -> {
            dismiss();
            if(listener != null)
                listener.onEvent(((EditText) findViewById(R.id.inputdialog_input)).getText().toString());
        });
        findViewById(R.id.inputdialog_button_close).setOnClickListener(x -> dismiss());


        getWindow().clearFlags( WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    public void setHint(String hint){
        ((EditText) (View) findViewById(R.id.inputdialog_input)).setHint(hint);
    }
}