package com.universityofalabama.cs495f2018.berthaIRT.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.TextView;

import com.universityofalabama.cs495f2018.berthaIRT.Interface;
import com.universityofalabama.cs495f2018.berthaIRT.R;

public class InputDialog extends AlertDialog{
    String label;
    String text;
    String buttonText;
    Interface.WithStringListener listener;
    public InputDialog(Context ctx, String label, String text, String buttonText, Interface.WithStringListener listener) {
        super(ctx);
        this.label = label;
        this.text = text;
        this.buttonText = buttonText;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input);

        ((TextView) findViewById(R.id.inputdialog_button_confirm)).setText(label);
        ((TextView) findViewById(R.id.inputdialog_alt_text)).setText(text);
        ((TextView) findViewById(R.id.inputdialog_alt_button)).setText(buttonText);

        if (listener != null)
            findViewById(R.id.inputdialog_button_confirm).setOnClickListener(x -> listener.onEvent(
                    ((EditText) findViewById(R.id.inputdialog_input)).getText().toString()
            ));
        findViewById(R.id.inputdialog_button_close).setOnClickListener(x -> dismiss());
    }

    public void setHint(String hint){
        ((EditText) findViewById(R.id.inputdialog_input)).setHint(hint);
    }
}