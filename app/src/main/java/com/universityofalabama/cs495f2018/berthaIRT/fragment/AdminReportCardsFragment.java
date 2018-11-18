package com.universityofalabama.cs495f2018.berthaIRT.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.amazonaws.auth.policy.conditions.StringCondition;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.universityofalabama.cs495f2018.berthaIRT.Client;
import com.universityofalabama.cs495f2018.berthaIRT.R;
import com.universityofalabama.cs495f2018.berthaIRT.Report;
import com.universityofalabama.cs495f2018.berthaIRT.Util;
import com.universityofalabama.cs495f2018.berthaIRT.adapter.AdminReportCardAdapter;
import com.universityofalabama.cs495f2018.berthaIRT.dialog.FilterDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AdminReportCardsFragment extends Fragment {
    RecyclerView rv;
    AdminReportCardAdapter adapter;
    SwipeRefreshLayout swipeContainer;
    TextView tvNoReports;
    ImageView ivSearch;
    EditText etSearch;
    FilterDialog filterDialog;

    public AdminReportCardsFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater flater, ViewGroup tainer, Bundle savedInstanceState) {
        View v = flater.inflate(R.layout.fragment_admin_reportcards, tainer, false);

        adapter = new AdminReportCardAdapter(getContext());

        rv = v.findViewById(R.id.admin_reports_rv);
        rv.setAdapter(adapter);

        swipeContainer = v.findViewById(R.id.admin_reports_sr);
        swipeContainer.setOnRefreshListener(this::actionSwipeRefresh);

        tvNoReports = v.findViewById(R.id.admin_reports_alt_noreports);

        //Set the Image search button and edit logText for it.
        ivSearch = v.findViewById(R.id.imageView2); //guys what is this id come on
        etSearch = v.findViewById(R.id.admin_reports_input_searchbox);

        filterDialog = new FilterDialog(getContext(), filteredReports->{
            adapter.updateReports(filteredReports);
        });

        v.findViewById(R.id.admin_reports_button_filter).setOnClickListener(x->actionShowFilters());

        actionSwipeRefresh();
        return v;
    }

    private void actionShowFilters() {
        filterDialog.resetUnfilteredList(Client.reportMap.values());
        filterDialog.show();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    private void actionSwipeRefresh() {
        swipeContainer.setRefreshing(true); {
            Client.net.getGroupReports(getContext(), x->{
                adapter.updateReports(Client.reportMap.values());
                swipeContainer.setRefreshing(false);
            });
        }
    }
}