package com.ua.cs495_f18.berthaIRT.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ua.cs495_f18.berthaIRT.AdminPortalActivity;
import com.ua.cs495_f18.berthaIRT.R;
import com.ua.cs495_f18.berthaIRT.Adapter.AdminReportCardAdapter;
import com.ua.cs495_f18.berthaIRT.ReportObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.ua.cs495_f18.berthaIRT.AdminPortalActivity.adminReportMap;


public class AdminOpenReportsFragment extends Fragment {

    View v;
    private RecyclerView recyclerView;
    private List<ReportObject> reportList = new ArrayList<>();
    private AdminReportCardAdapter recyclerViewAdapter;
    private LinearLayoutManager mLayoutManager;

    private String filter = "";
    private SwipeRefreshLayout swipeContainer;
    private boolean loading = true;

    public AdminOpenReportsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_admin_open_reports, container, false);
        recyclerView = v.findViewById(R.id.view_fragment_admin_open_reports);
        recyclerViewAdapter = new AdminReportCardAdapter(getContext(),reportList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);

        pullToRefresh();
        infiniteScroll();

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateFragment();
    }

    private void populateFragment() {
        reportList.clear();
        if(!filter.equals("")) {
            for(Map.Entry<String,ReportObject> entry : adminReportMap.getHashMap().entrySet()) {
                if(entry.getKey().equals("1111111"))
                    reportList.add(entry.getValue());
            }
        }
        else {
            for(Map.Entry<String,ReportObject> entry : adminReportMap.getHashMap().entrySet()) {
                reportList.add(entry.getValue());
            }
        }

    }

    private void addMore() {
        for(Map.Entry<String,ReportObject> entry : adminReportMap.getHashMap().entrySet()) {
            reportList.add(entry.getValue());
        }
    }

    public void setFilter(String string) {
        //Toast.makeText(getActivity(),"FILTER 1",Toast.LENGTH_SHORT).show();
        filter = string;
        populateFragment();
        recyclerViewAdapter.notifyItemRangeRemoved(0,mLayoutManager.findFirstVisibleItemPosition());
        recyclerViewAdapter.notifyItemRangeInserted(0, mLayoutManager.getItemCount());
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void pullToRefresh() {
        swipeContainer = v.findViewById(R.id.fragment_open_reports);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(() -> {
            swipeContainer.setRefreshing(true);
            {
                int pastItemCount = mLayoutManager.getItemCount();
                //update the hashMap
                adminReportMap.populateHashMap();
                //This forces all 3 fragments to reload
                ((AdminPortalActivity)Objects.requireNonNull(getActivity())).sendFilter(filter);
                recyclerViewAdapter.notifyItemRangeRemoved(0, pastItemCount);
                recyclerViewAdapter.notifyItemRangeInserted(0, mLayoutManager.getItemCount());
                recyclerViewAdapter.notifyDataSetChanged();
            }
            if(swipeContainer.isRefreshing())
                swipeContainer.setRefreshing(false);
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    private void infiniteScroll() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0) {
                    if (loading) {
                        int visibleItemCount = mLayoutManager.getChildCount();
                        int totalItemCount = mLayoutManager.getItemCount();
                        int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                        if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                            loading = false;
                            addMore();
                            recyclerViewAdapter.notifyItemRangeRemoved(0,totalItemCount);
                            //Toast.makeText(getActivity(),visibleItemCount + " " + totalItemCount + " " + pastVisiblesItems,Toast.LENGTH_SHORT).show();
                            recyclerViewAdapter.notifyItemRangeInserted(0, mLayoutManager.getItemCount());
                            recyclerViewAdapter.notifyDataSetChanged();
                            loading = true;
                        }

                    }
                }
            }
        });
    }
}
