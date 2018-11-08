package com.universityofalabama.cs495f2018.berthaIRT;

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


public class ReportDetailsStudentFragment extends Fragment {
    View v;
    CategoryTagAdapter catAdapter;

    TextView tvReportId, tvStatus, tvCreateTimestamp, tvLastActionTimestamp, tvIncidentTimestamp, tvThreat, tvDescription, tvLocation;

    public ReportDetailsStudentFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater flater, ViewGroup tainer, Bundle savedInstanceState){
        v = flater.inflate(R.layout.fragment_student_reportdetails, tainer, false);

        //Get the required views
        tvReportId = v.findViewById(R.id.student_reportdetails_alt_id);
        tvStatus = v.findViewById(R.id.student_reportdetails_alt_status);
        tvCreateTimestamp = v.findViewById(R.id.student_reportdetails_alt_creationdate);
        tvLastActionTimestamp = v.findViewById(R.id.student_reportdetails_alt_lastaction);
        tvIncidentTimestamp = v.findViewById(R.id.student_reportdetails_alt_incidentdate);
        tvThreat = v.findViewById(R.id.student_reportdetails_alt_threat);
        tvDescription = v.findViewById(R.id.student_reportdetails_alt_description);
        tvLocation = v.findViewById(R.id.student_reportdetails_alt_location);

        //Set the recyclerview for categories
        RecyclerView rv = v.findViewById(R.id.admin_reportdetails_rv_category);
        catAdapter = new CategoryTagAdapter(false);
        rv.setAdapter(catAdapter);

        //set the media listener
        v.findViewById(R.id.student_reportdetails_button_attachments).setOnClickListener(v1 ->
            Toast.makeText(getActivity(),"View Media", Toast.LENGTH_SHORT).show() );

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        populateReportDetails(Client.activeReport);
    }

    private void populateReportDetails(Report r) {
        tvReportId.setText(r.reportId);
        tvCreateTimestamp.setText(r.creationTimestamp);
        tvLastActionTimestamp.setText(r.lastActionTimestamp);
        tvStatus.setText(r.status);
        tvIncidentTimestamp.setText(r.incidentTimeStamp);

        //format the string properly
        String threatString = r.threatLevel + "/5";
        tvThreat.setText(threatString);

        tvDescription.setText(r.description);
        tvLocation.setText(r.location);

        catAdapter.categoryList.clear();
        catAdapter.categoryList.addAll(r.categories);
        catAdapter.notifyDataSetChanged();
    }
}
