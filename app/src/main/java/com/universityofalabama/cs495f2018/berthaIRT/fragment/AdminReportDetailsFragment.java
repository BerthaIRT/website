package com.universityofalabama.cs495f2018.berthaIRT.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.universityofalabama.cs495f2018.berthaIRT.Client;
import com.universityofalabama.cs495f2018.berthaIRT.LogActivity;
import com.universityofalabama.cs495f2018.berthaIRT.R;
import com.universityofalabama.cs495f2018.berthaIRT.Report;
import com.universityofalabama.cs495f2018.berthaIRT.Report.Log;
import com.universityofalabama.cs495f2018.berthaIRT.Util;
import com.universityofalabama.cs495f2018.berthaIRT.adapter.NotesAdapter;
import com.universityofalabama.cs495f2018.berthaIRT.dialog.AddRemoveDialog;
import com.universityofalabama.cs495f2018.berthaIRT.dialog.CheckboxDialog;
import com.universityofalabama.cs495f2018.berthaIRT.dialog.NotesDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AdminReportDetailsFragment extends Fragment {
    LinearLayout catTainer, tagTainer;

    TextView tvReportId, tvStatus, tvCreateTimestamp, tvLastActionTimestamp, tvIncidentTimestamp, tvThreat, tvDescription, tvLocation, tvNoNotes, tvOpen, tvClosed, tvResolved;

    CardView cvOpen, cvClosed, cvResolved;

    private NotesAdapter adapter;
    List<Report.Log> notesList = new ArrayList<>();

    final int[] pushedState = new int[]{android.R.attr.state_enabled,android.R.attr.state_pressed};
    final int[] unpushedState = new int[]{android.R.attr.state_enabled,-android.R.attr.state_pressed};

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
        cvOpen = v.findViewById(R.id.cardviewOpen);
        cvClosed = v.findViewById(R.id.cardviewClosed);
        cvResolved = v.findViewById(R.id.cardviewResolved);
        tvOpen = v.findViewById(R.id.textViewOpen);
        tvClosed = v.findViewById(R.id.textViewClosed);
        tvResolved = v.findViewById(R.id.textViewResolved);

        //Set the Category and Tag Recycler Views
        catTainer = v.findViewById(R.id.admin_reportdetails_container_categories);
        tagTainer = v.findViewById(R.id.admin_reportdetails_container_tags);
        tvNoNotes = v.findViewById(R.id.admin_reportdetails_notes_no);

        RecyclerView rv = v.findViewById(R.id.admin_reportdetails_notes_rv);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new NotesAdapter(notesList);
        rv.setAdapter(adapter);

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
                new AddRemoveDialog(getActivity(), Client.activeReport.tags, null, null, this::finishEditTags).show());


/*        //TEMP to make up admins
        List<String> admins = new ArrayList<>();
        admins.add("TEMP 1");
        admins.add("TEMP 2");*/
        v.findViewById(R.id.admin_reportdetails_button_editassignees).setOnClickListener(v1 ->
                new CheckboxDialog(getActivity(), Util.getPreChecked(Util.getAdmins(getActivity()), Client.activeReport.assignedTo), Util.getAdmins(getActivity()), this::finishEditAdmins).show() );

        v.findViewById(R.id.admin_reportdetails_button_addnotes).setOnClickListener(v1 ->
                Toast.makeText(getActivity(), "View Notes", Toast.LENGTH_SHORT).show() );

        v.findViewById(R.id.admin_reportdetails_button_addnotes).setOnClickListener(v1 ->
                new NotesDialog(getActivity(), "Notes", this::actionUpdateNotes).show());

        View.OnClickListener statusOnClick = v12 -> {
            cvOpen.getBackground().setState(unpushedState);
            cvClosed.getBackground().setState(unpushedState);
            cvResolved.getBackground().setState(unpushedState);
            tvOpen.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            tvClosed.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            tvResolved.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            tvClosed.setTypeface(null, Typeface.NORMAL);
            tvOpen.setTypeface(null, Typeface.NORMAL);
            tvResolved.setTypeface(null, Typeface.NORMAL);

            TextView clickedButtonText;
            if(v12 == cvOpen) clickedButtonText = tvOpen;
            else if(v12 == cvClosed) clickedButtonText = tvClosed;
            else clickedButtonText = tvResolved;

            v12.getBackground().setState(pushedState);
            clickedButtonText.setTypeface(null, Typeface.BOLD);
            clickedButtonText.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        };

        //Listeners for Report Status Change
        v.findViewById(R.id.cardviewOpen).setOnClickListener(statusOnClick);
        v.findViewById(R.id.cardviewClosed).setOnClickListener(statusOnClick);
        v.findViewById(R.id.cardviewResolved).setOnClickListener(statusOnClick);

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

        notesList.clear();
        notesList = Client.activeReport.notes;
        adapter.notifyDataSetChanged();

        //if there is no log then show message
        if (notesList.size() == 0)
            tvNoNotes.setVisibility(View.VISIBLE);
    }

    private void finishEditCategories(List<String> newList) {
        //TODO get the current admin

        Client.activeReport.categories = newList;

    }

    private void finishEditTags(List<String> newList) {
        //TODO get the current admin

        Client.activeReport.tags = newList;
    }

    private void finishEditAdmins(List<String> newList) {
        //TODO get the current admin

        Client.activeReport.assignedTo = newList;

        //Client.updateReportMap();
    }

    private void actionUpdateNotes(String note) {
        Log newNote = new Log();
        newNote.text = note;
        //TODO update the report
        Client.activeReport.notes.add(newNote);
    }
}
