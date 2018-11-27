package com.ua.cs495f2018.berthaIRT.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ua.cs495f2018.berthaIRT.Client;
import com.ua.cs495f2018.berthaIRT.R;
import com.ua.cs495f2018.berthaIRT.adapter.AdminReportCardAdapter;
import com.ua.cs495f2018.berthaIRT.dialog.FilterDialog;

public class AdminReportCardsFragment extends Fragment {
    RecyclerView rv;
    AdminReportCardAdapter adapter;
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

        tvNoReports = v.findViewById(R.id.admin_reports_alt_noreports);

        //Set the Image search button and edit logText for it.
        ivSearch = v.findViewById(R.id.imageView2); //guys what is this id come on
        etSearch = v.findViewById(R.id.admin_reports_input_searchbox);

        adapter.updateReports(Client.reportMap.values());

        filterDialog = new FilterDialog(getContext(), filteredReports-> adapter.updateReports(filteredReports));

        v.findViewById(R.id.admin_reports_button_filter).setOnClickListener(x->actionShowFilters());

        Client.makeRefreshTask(getContext(), ()-> adapter.updateReports(Client.reportMap.values()));

        return v;
    }

    private void actionShowFilters() {
        filterDialog.resetUnfilteredList(Client.reportMap.values());
        filterDialog.show();
    }
}