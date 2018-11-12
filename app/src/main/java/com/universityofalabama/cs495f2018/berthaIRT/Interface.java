package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.List;

public class Interface {
    public interface WithVoidListener{
        void onEvent();
    }
    public interface WithStringListener{
        void onEvent(String string);
    }
    public interface WithStringListListener{
        void onEvent(List<String> list);
    }
    public interface YesNoHandler{
        void onYesClicked();
        void onNoClicked();
    }
}