package com.universityofalabama.cs495f2018.berthaIRT;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Log {

    public String text = null;
    public String oldItem = null;
    public String newItem = null;
    public String timestamp = new SimpleDateFormat("MM/dd/yy hh:mm a", Locale.getDefault()).format(new Date());
    public String admin = null;
}