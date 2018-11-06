package com.universityofalabama.cs495f2018.berthairt;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AdminDashboardFragment extends Fragment {
    public AdminDashboardFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater flater, ViewGroup tainer, Bundle savedInstanceState){
        return flater.inflate(R.layout.fragment_admin_dashboard, tainer, false);
    }
}
