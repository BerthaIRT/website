package com.ua.cs495f2018.berthaIRT.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ua.cs495f2018.berthaIRT.Client;
import com.ua.cs495f2018.berthaIRT.R;
import com.ua.cs495f2018.berthaIRT.Report;
import com.ua.cs495f2018.berthaIRT.adapter.AdminReportCardAdapter;
import com.ua.cs495f2018.berthaIRT.dialog.FilterDialog;

import java.util.ArrayList;
import java.util.List;

public class AdminReportCardsFragment extends Fragment {
    RecyclerView rv;
    AdminReportCardAdapter adapter;
    TextView tvNoReports;
    ImageView ivSearch;
    EditText etSearch;
    FilterDialog filterDialog;
    List<Report> filterData;

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
        ivSearch = v.findViewById(R.id.imageView2);
        etSearch = v.findViewById(R.id.admin_reports_input_searchbox);

        adapter.updateReports(Client.reportMap.values());
        filterData = adapter.getData();

        filterDialog = new FilterDialog(getContext(), filteredReports-> {
            adapter.updateReports(filteredReports);
            filterData = filteredReports;
        });

        v.findViewById(R.id.admin_reports_button_filter).setOnClickListener(x->actionShowFilters());

        //Client.makeRefreshTask(getContext(), ()-> adapter.updateReports(Client.reportMap.values()));

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etSearch.getText().toString().isEmpty()) {
                    adapter.updateReports(filterData);
                }
            }
        });

        //Begin Search when Icon is Clicked
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = etSearch.getText().toString();
                List<Report> reportList = new ArrayList<>();
               // reportList.addAll(Client.reportMap.values());
                reportList = adapter.getData();
                List<Report> searchedList = new ArrayList<>();
                boolean check = false;
                for(int i = 0; i < reportList.size(); i++){
                    //reset check
                    check = false;
                    //Search ReportIds
                    if(reportList.get(i).getReportID().toString().contains(searchText)) {
                        searchedList.add(reportList.get(i));
                        continue;
                    }
                    //Search Categories
                    for(int j = 0; j < reportList.get(i).getCategories().size(); j++){
                        if((reportList.get(i).getCategories().get(j).toLowerCase()).contains(searchText.toLowerCase())) {
                            searchedList.add(reportList.get(i));
                            check = true;
                            break;
                        }
                    }
                    //Check if item was added to list from Categories
                    if(check == true)
                        continue;
                    //Search Tags
                    for(int k = 0; k < reportList.get(i).getTags().size(); k++){
                        if((reportList.get(i).getTags().get(k).toLowerCase()).contains(searchText.toLowerCase())) {
                            searchedList.add(reportList.get(i));
                            break;
                        }
                    }
                }
                //Update The Report Display with User Searched Reports.
                adapter.updateReports(searchedList);
            }
        });
        //Search button push view toggle
        ivSearch.setOnTouchListener(new View.OnTouchListener() {
            private boolean touchStayedWithinViewBounds;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        ViewCompat.setElevation(ivSearch, 0);
                        touchStayedWithinViewBounds = true;
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        if(touchStayedWithinViewBounds) {
                            ivSearch.callOnClick();
                        }
                        ViewCompat.setElevation(ivSearch, 20);
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_MOVE:
                        // Used held down Search Icon and MOVED out of its BOUNDS.
                        if(touchStayedWithinViewBounds && !isMotionEventInsideView(ivSearch, event)){
                            touchStayedWithinViewBounds = false;
                        }
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        // If moved out of bounds, cancel and do not perform search, as user may have mis-clicked.
                        ViewCompat.setElevation(ivSearch, 20);
                        return true;
                    default:
                        return false;
                }
            }
            //Determines if the motion inside the Search Icon is moved outside.
            private boolean isMotionEventInsideView(View view, MotionEvent event) {
                Rect viewRect = new Rect(
                        view.getLeft(),
                        view.getTop(),
                        view.getRight(),
                        view.getBottom()
                );
                return viewRect.contains(
                        view.getLeft() + (int) event.getX(),
                        view.getTop() + (int) event.getY()
                );
            }
        });

        Client.makeRefreshTask(getContext(), ()-> adapter.updateReports(Client.reportMap.values()));

        return v;
    }

    private void actionShowFilters() {
        filterDialog.resetUnfilteredList(Client.reportMap.values());
        filterDialog.show();
    }
}