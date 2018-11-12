package com.universityofalabama.cs495f2018.berthaIRT.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.universityofalabama.cs495f2018.berthaIRT.Client;
import com.universityofalabama.cs495f2018.berthaIRT.Log;
import com.universityofalabama.cs495f2018.berthaIRT.LogActivity;
import com.universityofalabama.cs495f2018.berthaIRT.R;
import com.universityofalabama.cs495f2018.berthaIRT.Report;
import com.universityofalabama.cs495f2018.berthaIRT.Util;
import com.universityofalabama.cs495f2018.berthaIRT.dialog.AddRemoveDialog;
import com.universityofalabama.cs495f2018.berthaIRT.dialog.CheckboxDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminReportDetailsFragment extends Fragment {
    LinearLayout catTainer, tagTainer;

    TextView tvReportId, tvStatus, tvCreateTimestamp, tvLastActionTimestamp, tvIncidentTimestamp, tvThreat, tvDescription, tvLocation;

    public AdminReportDetailsFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater flater, ViewGroup tainer, Bundle savedInstanceState){
        View v = flater.inflate(R.layout.fragment_admin_reportdetails, tainer, false);

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
        catTainer = v.findViewById(R.id.admin_reportdetails_container_categories);
        tagTainer = v.findViewById(R.id.admin_reportdetails_container_tags);

        //Set the required on click listeners
        v.findViewById(R.id.admin_reportdetails_button_viewlog).setOnClickListener(v1 ->
                startActivity(new Intent(getActivity(), LogActivity.class)) );

        v.findViewById(R.id.admin_reportdetails_button_attachments).setOnClickListener(v1 ->
                Toast.makeText(getActivity(), "View Attachments", Toast.LENGTH_SHORT).show() );

        //Listener for editing categories. It gets the selected ones first
        v.findViewById(R.id.admin_reportdetails_button_editcategory).setOnClickListener(v1 ->
                new CheckboxDialog(getActivity(), Util.getPreChecked(Arrays.asList(getResources().getStringArray(R.array.category_item)),Client.activeReport.categories),
                        Arrays.asList(getResources().getStringArray(R.array.category_item)), this::finishEditCategories).show());

        v.findViewById(R.id.admin_reportdetails_button_edittags).setOnClickListener(v1 ->
                new AddRemoveDialog(getActivity(), Client.activeReport.tags, this::finishEditTags).show());

        //TEMP to make up admins
        List<String> admins = new ArrayList<>();
        admins.add("John Frank");
        admins.add("Fred Hurts");
        v.findViewById(R.id.admin_reportdetails_button_editassignees).setOnClickListener(v1 ->
                new CheckboxDialog(getActivity(), Util.getPreChecked(/*TODO get list of admins in group*/admins, Client.activeReport.assignedTo), /*TODO get list of admins in group*/admins, this::finishEditAdmins).show() );

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

        catTainer.removeAllViews();
        for(String cat : r.categories){
            View v = getLayoutInflater().inflate(R.layout.adapter_category, null, false);
            ((TextView) v.findViewById(R.id.adapter_alt_category)).setText(cat);
            catTainer.addView(v);
        }
        tagTainer.removeAllViews();
        for(String tag : r.tags){
            View v = getLayoutInflater().inflate(R.layout.adapter_tag, null, false);
            ((TextView) v.findViewById(R.id.adapter_alt_tag)).setText(tag);
            tagTainer.addView(v);
        }
    }

    private void finishEditCategories(List<String> newList) {
        Log log = new Log();
        log.text = "Report Categories Updated";
        log.oldItem = Util.listToString(Client.activeReport.categories);
        log.newItem = Util.listToString(newList);
        //TODO get the current admin

        Client.activeReport.categories = newList;
        Client.activeReport.logs.add(log);

        //Client.updateReportMap();
    }

    private void finishEditTags(List<String> newList) {
        Log log = new Log();
        log.text = "Report Tags Updated";
        log.oldItem = Util.listToString(Client.activeReport.tags);
        log.newItem = Util.listToString(newList);
        //TODO get the current admin

        Client.activeReport.tags = newList;
        Client.activeReport.logs.add(log);

        //Client.updateReportMap();
    }

    private void finishEditAdmins(List<String> newList) {
        Log log = new Log();
        log.text = "Report assigned to new admin.";
        log.oldItem = Util.listToString(Client.activeReport.assignedTo);
        log.newItem = Util.listToString(newList);
        //TODO get the current admin

        Client.activeReport.assignedTo = newList;
        Client.activeReport.logs.add(log);

        //Client.updateReportMap();
    }
}
