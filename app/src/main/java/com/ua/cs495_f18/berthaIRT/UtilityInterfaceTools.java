package com.ua.cs495_f18.berthaIRT;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

public class UtilityInterfaceTools {
    public static void hideSoftKeyboard(Activity activity) {
    InputMethodManager inputMethodManager =
            (InputMethodManager) activity.getSystemService(
                    Activity.INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromWindow(
            activity.getCurrentFocus().getWindowToken(), 0);
    }
}
