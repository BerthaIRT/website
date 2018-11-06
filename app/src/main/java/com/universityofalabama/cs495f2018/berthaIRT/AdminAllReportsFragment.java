package com.universityofalabama.cs495f2018.berthaIRT;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminAllReportsFragment extends Fragment {

    View v;
    private RecyclerView recyclerView;
    private List<ReportObject> reportList = new ArrayList<>();
    private AdminReportCardAdapter recyclerViewAdapter;
    private LinearLayoutManager mLayoutManager;

    private String filter = "";
    private SwipeRefreshLayout swipeContainer;
    private boolean loading = true;

    public AdminViewPagerAdapter.FragmentRefreshInterface fraggo;

    public AdminAllReportsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fraggo = (AdminViewPagerAdapter.FragmentRefreshInterface) getArguments().getSerializable("interface");

        v = inflater.inflate(R.layout.fragment_admin_all_reports, container, false);
        recyclerView = v.findViewById(R.id.view_fragment_admin_all_reports);
        recyclerViewAdapter = new AdminReportCardAdapter(getContext(),reportList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);


        pullToRefresh();
        //infiniteScroll();

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        populateFragment();
    }


    public void populateFragment() {
        int pastItemCount = mLayoutManager.getItemCount();
        reportList.clear();
        Toast.makeText(getActivity(),"Pop All" + filter + "~",Toast.LENGTH_SHORT).show();

        Log.e("Tag", filter);

        if(!filter.equals("")) {
            Log.e("Tag", "Toast2");

            for(Map.Entry<String,ReportObject> entry : Client.reportMap.entrySet()) {
                if(entry.getKey().contains(filter))
                    reportList.add(entry.getValue());
            }
        }
        else {
            for(Map.Entry<String,ReportObject> entry : Client.reportMap.entrySet()) {
                reportList.add(entry.getValue());
            }
        }
        recyclerViewAdapter.notifyItemRangeRemoved(0, pastItemCount);
        recyclerViewAdapter.notifyItemRangeInserted(0, mLayoutManager.getItemCount());
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void addMore() {
        for(Map.Entry<String,ReportObject> entry : Client.reportMap.entrySet()) {
            reportList.add(entry.getValue());
        }
    }

    public void setFilter(String string) {
        //Toast.makeText(getActivity(),"FILTER 2",Toast.LENGTH_SHORT).show();
        filter = string;
        populateFragment();
        recyclerViewAdapter.notifyItemRangeRemoved(0,mLayoutManager.findFirstVisibleItemPosition());
        recyclerViewAdapter.notifyItemRangeInserted(0, mLayoutManager.getItemCount());
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void pullToRefresh() {
        
        swipeContainer = v.findViewById(R.id.fragment_all_reports);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(() -> {
            swipeContainer.setRefreshing(true);
            {
                Client.updateReportMap();
                fraggo.refresh();
                /*int pastItemCount = mLayoutManager.getItemCount();
                //update the hashMap
                Client.updateReportMap();
                //This forces all 3 fragments to reload
                ((AdminPortalActivity) Objects.requireNonNull(getActivity())).updateFilters(filter);
                recyclerViewAdapter.notifyItemRangeRemoved(0, pastItemCount);
                recyclerViewAdapter.notifyItemRangeInserted(0, mLayoutManager.getItemCount());
                recyclerViewAdapter.notifyDataSetChanged();*/
            }
            if(swipeContainer.isRefreshing())
                swipeContainer.setRefreshing(false);
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

//    private void infiniteScroll() {
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if(dy > 0) {
//                    if (loading) {
//                        int visibleItemCount = mLayoutManager.getChildCount();
//                        int totalItemCount = mLayoutManager.getItemCount();
//                        int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
//                        if (pastVisibleItems + visibleItemCount >= totalItemCount) {
//                            loading = false;
//                            addMore();
//                            recyclerViewAdapter.notifyItemRangeRemoved(0,totalItemCount);
//                            //Toast.makeText(getActivity(),visibleItemCount + " " + totalItemCount + " " + pastVisiblesItems,Toast.LENGTH_SHORT).show();
//                            recyclerViewAdapter.notifyItemRangeInserted(0, mLayoutManager.getItemCount());
//                            recyclerViewAdapter.notifyDataSetChanged();
//                            loading = true;
//                        }
//
//                    }
//                }
//            }
//        });
//    }

}
