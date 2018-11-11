package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class ReportDetailsAdminFragment extends Fragment {
    View v;
    CategoryTagAdapter catAdapter;
    CategoryTagAdapter tagAdapter;

    TextView tvReportId, tvStatus, tvCreateTimestamp, tvLastActionTimestamp, tvIncidentTimestamp, tvThreat, tvDescription, tvLocation;

    public ReportDetailsAdminFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater flater, ViewGroup tainer, Bundle savedInstanceState){
        v = flater.inflate(R.layout.fragment_admin_reportdetails, tainer, false);

        //Find all the views
        tvReportId = v.findViewById(R.id.admin_reportdetails_alt_id);
        tvStatus = v.findViewById(R.id.admin_reportdetails_alt_status);
        tvCreateTimestamp = v.findViewById(R.id.admin_reportdetails_alt_creationdate);
        tvLastActionTimestamp = v.findViewById(R.id.admin_reportdetails_alt_lastaction);
        tvIncidentTimestamp = v.findViewById(R.id.admin_reportdetails_alt_incidentdate);
        tvThreat = v.findViewById(R.id.admin_reportdetails_alt_threat);
        tvDescription = v.findViewById(R.id.admin_reportdetails_alt_description);
        tvLocation = v.findViewById(R.id.admin_reportdetails_alt_location);

        //Set the Category and Tag Recycler Views
        RecyclerView rvCat = v.findViewById(R.id.admin_reportdetails_rv_category);
        RecyclerView rvTag = v.findViewById(R.id.admin_reportdetails_rv_tags);
        catAdapter = new CategoryTagAdapter(false);
        tagAdapter = new CategoryTagAdapter(true);
        rvCat.setAdapter(catAdapter);
        rvTag.setAdapter(tagAdapter);

        //Set the required on click listeners
        v.findViewById(R.id.admin_reportdetails_button_viewlog).setOnClickListener(v1 ->
                startActivity(new Intent(getActivity(), LogActivity.class)) );

        v.findViewById(R.id.admin_reportdetails_button_attachments).setOnClickListener(v1 ->
                Toast.makeText(getActivity(), "View Attachments", Toast.LENGTH_SHORT).show() );


        //Listener for editing categories. It gets the selected ones first
        v.findViewById(R.id.admin_reportdetails_button_editcategory).setOnClickListener(v1 ->
                Util.showSelectCategoriesDialog(getActivity(), Util.getPreChecked(Arrays.asList(getResources().getStringArray(R.array.category_item)),Client.activeReport.categories),
                        Arrays.asList(getResources().getStringArray(R.array.category_item)), this::finishEditCategories) );

        v.findViewById(R.id.admin_reportdetails_button_edittags).setOnClickListener(v1 ->
                Toast.makeText(getActivity(), "Edit Tags", Toast.LENGTH_SHORT).show() );

        v.findViewById(R.id.admin_reportdetails_button_editassignees).setOnClickListener(v1 ->
                Toast.makeText(getActivity(), "Edit Admins", Toast.LENGTH_SHORT).show() );

        v.findViewById(R.id.admin_reportdetails_button_notes).setOnClickListener(v1 ->
                Toast.makeText(getActivity(), "View Notes", Toast.LENGTH_SHORT).show() );

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        populateReportDetails(Client.activeReport);
    }

    private void populateReportDetails(Report r) {
        tvReportId.setText(r.reportId);
        tvCreateTimestamp.setText(Util.formatTimestamp(r.creationTimestamp));
        tvLastActionTimestamp.setText(Util.formatTimestamp(r.lastActionTimestamp));
        tvStatus.setText(r.status);
        tvIncidentTimestamp.setText(Util.formatTimestamp(r.incidentTimeStamp));

        String threatString = r.threatLevel + "/5";
        tvThreat.setText(threatString);

        tvDescription.setText(r.description);
        tvLocation.setText(r.location);

        catAdapter.categoryList.clear();
        catAdapter.categoryList.addAll(r.categories);
        catAdapter.notifyDataSetChanged();

        tagAdapter.categoryList.clear();
        tagAdapter.categoryList.addAll(r.tags);
        tagAdapter.notifyDataSetChanged();
    }

    private void finishEditCategories(List<String> newList) {
        Log log = new Log(Log.reportDetailsUpdated());
        log.oldItem = Util.listToString(Client.activeReport.categories);
        log.newItem = Util.listToString(newList);
        //TODO get the current admin

        Client.activeReport.categories = newList;

        //Client.updateReportMap();
    }
}
