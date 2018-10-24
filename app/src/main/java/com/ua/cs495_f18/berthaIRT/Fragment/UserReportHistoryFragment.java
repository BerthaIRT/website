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
import android.widget.Toast;

import com.ua.cs495_f18.berthaIRT.Adapter.UserReportCardAdapter;
import com.ua.cs495_f18.berthaIRT.R;
import com.ua.cs495_f18.berthaIRT.ReportObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.ua.cs495_f18.berthaIRT.UserReportHistoryActivity.userReportMap;


public class UserReportHistoryFragment extends Fragment {

    View v;
    private RecyclerView recyclerView;
    private List<ReportObject> reportList = new ArrayList<>();
    private UserReportCardAdapter recyclerViewAdapter;
    private LinearLayoutManager mLayoutManager;

    private String filter = "";
    private SwipeRefreshLayout swipeContainer;
    private boolean loading = true;

    public UserReportHistoryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_user_report_history, container, false);
        recyclerView = v.findViewById(R.id.view_fragment_user_report_history);
        recyclerViewAdapter = new UserReportCardAdapter(getContext(),reportList);
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
            for(Map.Entry<String,ReportObject> entry : userReportMap.getHashMap().entrySet()) {
                if(entry.getKey().equals("1111111"))
                    reportList.add(entry.getValue());
            }
        }
        else {
            for(Map.Entry<String,ReportObject> entry : userReportMap.getHashMap().entrySet()) {
                reportList.add(entry.getValue());
            }
        }

    }

    private void addMore() {
        for(Map.Entry<String,ReportObject> entry : userReportMap.getHashMap().entrySet()) {
            reportList.add(entry.getValue());
        }

    }

    public void setFilter(String string) {
        filter = string;
        populateFragment();
        recyclerViewAdapter.notifyItemRangeRemoved(0,mLayoutManager.findFirstVisibleItemPosition());
        recyclerViewAdapter.notifyItemRangeInserted(0, mLayoutManager.getItemCount());
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void pullToRefresh() {
        swipeContainer = v.findViewById(R.id.fragment_user_report_history);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(() -> {
            swipeContainer.setRefreshing(true);
            userReportMap.populateHashMap();
            Toast.makeText(getActivity(),"REFRESHED",Toast.LENGTH_SHORT).show();
            //populateFragment();
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
