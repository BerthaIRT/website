package com.ua.cs495f2018.berthaIRT;

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
    public interface WithReportListListener{
        void onEvent(List<Report> list);
    }
    public interface WithGenericListener{
        void onEvent(Object o);
    }

    public interface YesNoHandler{
        void onYesClicked();
        void onNoClicked();
    }
}