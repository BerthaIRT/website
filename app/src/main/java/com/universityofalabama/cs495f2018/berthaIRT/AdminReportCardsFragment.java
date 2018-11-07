package com.universityofalabama.cs495f2018.berthaIRT;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class AdminReportCardsFragment extends Fragment {
    List<Report> fragList = new ArrayList<>();
    ReportCardAdapter adapter;
    SwipeRefreshLayout swipeContainer;
    //Filter Option Data
    private EditText etStartDate;
    private EditText etStartTime;
    private EditText etEndDate;
    private EditText etEndTime;
    private TextView tvCategories;
    private TextView tvCategoriesSelected;
    private List<String> newCategories;
    private ImageView vertDots;

    public AdminReportCardsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater flater, ViewGroup tainer, Bundle savedInstanceState){
        System.out.println("onCreateView (Report)");
        View v = flater.inflate(R.layout.fragment_admin_reportcards, tainer, false);
        RecyclerView rv = v.findViewById(R.id.admin_reports_rv);

        adapter = new ReportCardAdapter(getContext(), fragList);
        rv.setAdapter(adapter);
        swipeContainer = v.findViewById(R.id.admin_reports_sr);
        swipeContainer.setOnRefreshListener(this::refresh);
        vertDots = v.findViewById(R.id.filter_options);
        vertDots.setOnClickListener(v1 -> showFilterOptions());

        populateFraglist();
        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        populateFraglist();

    }

    private void populateFraglist() {
        fragList.clear();
        for(Map.Entry e : Client.reportMap.entrySet())
            fragList.add((Report) e.getValue());

        String filter = " ";//TODO
       // applyFilter(filter);

        adapter.notifyDataSetChanged();
    }

    private void refresh() {
        swipeContainer.setRefreshing(true);
        {
            Client.updateReportMap();
            populateFraglist();
            adapter.notifyDataSetChanged();
        }
        if(swipeContainer.isRefreshing())
            swipeContainer.setRefreshing(false);
    }

    /*private void applyFilter(String filter){
        //TODO
    }*/

    class ReportCardAdapter extends RecyclerView.Adapter<ReportViewHolder>{
        Context ctx;
        List<Report> data;
        CategoryTagAdapter catAdapter;

        public ReportCardAdapter(Context c, List<Report> d){
            ctx = c;
            data = d;
        }

        @NonNull
        @Override
        public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(ctx).inflate(R.layout.adapter_reportcard, parent, false);
            RecyclerView rv = v.findViewById(R.id.admin_reportcard_rv_categories);

            catAdapter = new CategoryTagAdapter(false);
            rv.setAdapter(catAdapter);

            return new ReportViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
            Report r = data.get(position);
            holder.tvReportID.setText(r.reportId);
            holder.tvStatus.setText(r.status);
            holder.tvSubmitted.setText(r.submittedDate);
            System.out.println(r.categories);
            catAdapter.categoryList.clear();
            for(String s : r.categories) catAdapter.categoryList.add(s);
            catAdapter.notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    class ReportViewHolder extends RecyclerView.ViewHolder {
        CardView cardContainer;
        TextView tvReportID, tvSubmitted, tvStatus;

        public ReportViewHolder(View itemView) {
            super(itemView);
            cardContainer = itemView.findViewById(R.id.reportcard_cv);
            tvReportID = itemView.findViewById(R.id.reportcard_alt_id);
            tvStatus = itemView.findViewById(R.id.reportcard_alt_status);
            tvSubmitted = itemView.findViewById(R.id.reportcard_alt_action);
        }
    }

    private void showFilterOptions(){
        //TODO add an if any element has changed check?
        final String[] categoryItems = getResources().getStringArray(R.array.category_item);
        List<String> temp = new ArrayList();
        for(int i = 0; i < categoryItems.length; i++)
            temp.add(categoryItems[i]);

        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.dialog_filter_options, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AppCompatAlertDialogStyle);
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Cancel", null);
        builder.setNeutralButton("Default", null);
        builder.setView(dialoglayout);
        builder.setTitle("Filter Options");

        //Create this way so neutral button doesn't close filter options.
        AlertDialog mBuilder = builder.create();
        mBuilder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final String MY_PREFS_NAME = "MyFilterPreferences";
                SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

                //Sorting Options:
                //Radio Button Group 1: only 1 can be selected at a time. DEFAULT = byThreatLevelOption1
                RadioButton byThreatLevelOption1, byThreatLevelOption2;
                RadioGroup byThreatLevelGroup;
                byThreatLevelOption1 = (RadioButton) dialoglayout.findViewById(R.id.radio_byThreatLevelOption1);
                byThreatLevelOption2 = (RadioButton) dialoglayout.findViewById(R.id.radio_byThreatLevelOption2);
                byThreatLevelGroup = (RadioGroup) dialoglayout.findViewById(R.id.radio_byThreatLevelGroup);
                //Get older preferences
                String threatLevelData = prefs.getString("threatLevelOption", null);

                //Radio Button Group 2: only 1 can be selected at a time. DEFAULT = byDateOption1.
                RadioButton byDateOption1, byDateOption2;
                RadioGroup byDateGroup;
                byDateOption1 = (RadioButton) dialoglayout.findViewById(R.id.radio_byDateOption1);
                byDateOption2 = (RadioButton) dialoglayout.findViewById(R.id.radio_byDateOption2);
                byDateGroup = (RadioGroup) dialoglayout.findViewById(R.id.radio_byDateGroup);
                //Get older preferences
                String byDateData = prefs.getString("dateOption", null);

                //Report Status:
                //CheckBoxes Group 1: Multiple may be selected. DEFAULT = all options selected.
                CheckBox reportStatusOption1, reportStatusOption2, reportStatusOption3, reportStatusOption4;
                reportStatusOption1 = (CheckBox) dialoglayout.findViewById(R.id.checkBox_reportStatusOption1);
                reportStatusOption2 = (CheckBox) dialoglayout.findViewById(R.id.checkBox_reportStatusOption2);
                reportStatusOption3 = (CheckBox) dialoglayout.findViewById(R.id.checkBox_reportStatusOption3);
                reportStatusOption4 = (CheckBox) dialoglayout.findViewById(R.id.checkBox_reportStatusOption4);
                //Get older preferences
                String reportStatusData1 = prefs.getString("reportStatusOptionNo1", null);
                String reportStatusData2 = prefs.getString("reportStatusOptionNo2", null);
                String reportStatusData3 = prefs.getString("reportStatusOptionNo3", null);
                String reportStatusData4 = prefs.getString("reportStatusOptionNo4", null);

                //Location: DEFAULT = empty.
                EditText locationOption;
                locationOption = (EditText) dialoglayout.findViewById(R.id.input_filter_location);
                //Get older preferences
                String locationData = prefs.getString("locationOption", null);

                //START DATE/TIME: DEFAULT = empty.
                etStartDate = dialoglayout.findViewById(R.id.input_filter_start_date);
                etStartDate.setOnClickListener(v -> {
                    actionSelectDateStart();
                });
                etStartTime = dialoglayout.findViewById(R.id.input_filter_start_time);
                etStartTime.setOnClickListener(v -> actionSelectTimeStart());
                //Get old preferences
                String startDateData = prefs.getString("startDateOption", null);
                String startTimeData = prefs.getString("startTimeOption", null);

                //END DATE/Time: DEFAULT = empty.
                etEndDate = dialoglayout.findViewById(R.id.input_filter_end_date);
                etEndDate.setOnClickListener(v -> actionSelectDateEnd());
                etEndTime = dialoglayout.findViewById(R.id.input_filter_end_time);
                etEndTime.setOnClickListener(v -> actionSelectTimeEnd());
                //Get old preferences
                String endDateData = prefs.getString("endDateOption", null);
                String endTimeData = prefs.getString("endTimeOption", null);

                //CheckBoxes Group 2: Only one option is selectable. DEFAULT = checked.
                CheckBox isMediaAllowedOption;
                isMediaAllowedOption = (CheckBox) dialoglayout.findViewById(R.id.checkBox_isMediaAllowed);
                //Get older preferences
                String mediaAllowedData = prefs.getString("mediaAllowedOption", null);

                //Categories list: DEFAULT = all categories checked.
                tvCategories = dialoglayout.findViewById(R.id.alt_filter_categories);
                tvCategoriesSelected = dialoglayout.findViewById(R.id.alt_filter_categories_selected);
                Button bCategories = dialoglayout.findViewById(R.id.button_filter_showcategories);
                bCategories.setOnClickListener(v -> {
                    actionSelectCategories();
                });
                //Get old preferences
                String categoryData = prefs.getString("categoryOption", null);

                //TODO Select Assigned Admins list: DEFAULT = NO assigned admins checked.

                /*Set Fields Using Data from Shared Preferences.
                 *   For CheckBoxes SharedPreference Values: Values stored as Strings
                 *       null == no current value stored in SharedPreferences
                 *       1 == isChecked(true)
                 *       2 == isChecked(false)
                 *
                 *   For RadioButtonGroups SharedPreference Values: Values stored as Strings
                 *       null == no current value stored in SharedPreferences
                 *       1 == isChecked(true), where only ONE option is allowed to be selected out of the group.
                 *       2 == isChecked(false), when ONE option == true, the other options == false.
                 *
                 *   For RadioButtons SharedPreference Values: Values stored as Strings
                 *       null == no current value stored in SharedPreferences
                 *       1 == isChecked(true)
                 *       2 == isChecked(true)
                 *
                 *   For Location SharedPreferences Values: Values stored as Strings
                 *       null == no Value.
                 *       "" == DEFAULT Location String
                 *       "Any Value" == Location String currently saved in SharedPreferences
                 *
                 *   For Start and End Date/Time SharedPreferences Values: Values stored as Strings
                 *       null == no Value.
                 *       "" == DEFAULT Location String
                 *       "Any Value" == Location String currently saved in SharedPreferences
                 *
                 *   For Categories SharedPreferences Values: Values stored as List<String>
                 *       null == no Value.
                 *       "" == No Values selected.
                 *       "List of Categories to be Parsed" == Category String currently saved in SharedPreferences.
                 *
                 *   For AssignedAdmins Shared Preferences Values: Values stored as List<String>
                 *       null == no Value.
                 */
                if(threatLevelData != null){ //Set to old preference value
                    if(threatLevelData.equals("1"))
                        byThreatLevelOption1.setChecked(true);
                    else // else it is 2, option == unchecked
                        byThreatLevelOption2.setChecked(true);
                }
                else{ //Set default option
                    byThreatLevelOption1.setChecked(true);
                }

                if(byDateData != null){ //Set to old preference value
                    if(threatLevelData.equals("1"))
                        byDateOption1.setChecked(true);
                    else // else it is 2, option == unchecked
                        byDateOption2.setChecked(true);
                }
                else{ //Set default option
                    byDateOption1.setChecked(true);
                }

                if(reportStatusData1 != null){ //Set to old preference value
                    if(reportStatusData1.equals("1"))
                        reportStatusOption1.setChecked(true);
                    else // else it is 2, option == unchecked
                        reportStatusOption1.setChecked(false);
                }
                else{ //Set default option
                    reportStatusOption1.setChecked(true);
                }

                if(reportStatusData2 != null){ //Set to old preference value
                    if(reportStatusData2.equals("1"))
                        reportStatusOption2.setChecked(true);
                    else // else it is 2, option == unchecked
                        reportStatusOption2.setChecked(false);
                }
                else{ //Set default option
                    reportStatusOption2.setChecked(true);
                }

                if(reportStatusData3 != null){ //Set to old preference value
                    if(reportStatusData3.equals("1"))
                        reportStatusOption3.setChecked(true);
                    else // else it is 2, option == unchecked
                        reportStatusOption3.setChecked(false);
                }
                else{ //Set default option
                    reportStatusOption3.setChecked(true);
                }

                if(reportStatusData4 != null){ //Set to old preference value
                    if(reportStatusData4.equals("1"))
                        reportStatusOption4.setChecked(true);
                    else // else it is 2, option == unchecked
                        reportStatusOption4.setChecked(false);
                }
                else{ //Set default option
                    reportStatusOption4.setChecked(true);
                }

                if(locationData != null){ //Set to old preference value
                    locationOption.setText(locationData);
                }
                else{ //Set default option
                    locationOption.setText("");
                }

                if(startDateData != null){ //Set to old preference value
                    etStartDate.setText(startDateData);
                }
                else{ //Set default option
                    etStartDate.setText("");
                }

                if(startTimeData != null){ //Set to old preference value
                    etStartTime.setText(startTimeData);
                }
                else{ //Set default option
                    etStartTime.setText("");
                }

                if(endDateData != null){ //Set to old preference value
                    etEndDate.setText(endDateData);
                }
                else{ //Set default option
                    etEndDate.setText("");
                }

                if(endTimeData != null){ //Set to old preference value
                    etEndTime.setText(endTimeData);
                }
                else{ //Set default option
                    etEndTime.setText("");
                }

                if(mediaAllowedData != null){ //Set to old preference value
                    if(mediaAllowedData.equals("1"))
                        isMediaAllowedOption.setChecked(true);
                    else // else it is 2, option == unchecked.
                        isMediaAllowedOption.setChecked(false);
                }
                else{ //Set default option
                    isMediaAllowedOption.setChecked(true);
                }

                if(categoryData != null){ //Set to old preference value
                    if(categoryData.isEmpty()){
                        tvCategoriesSelected.setText("");
                        tvCategories.setText("No Categories Selected");
                    }
                    else {
                        tvCategoriesSelected.setText(categoryData);
                        // Parse the String into boolean array.
                        boolean[] checkedCat = getPreChecked(categoryItems, tvCategoriesSelected.getText().toString());
                        // find the number of category items in the list
                        int num = 0;
                        for (int i = 0; i < checkedCat.length; i++) {
                            if (checkedCat[i] == true)
                                num = num + 1;
                        }
                        if(num == temp.size())
                            tvCategories.setText("All Categories Selected");
                        else
                            tvCategories.setText(num + " " + "Categories Selected");
                    }
                }
                else{ //Set default option
                    tvCategoriesSelected.setText("");
                    tvCategories.setText("No Categories Selected");
                }

                Button a = mBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
                Button b = mBuilder.getButton(AlertDialog.BUTTON_NEGATIVE);
                Button c = mBuilder.getButton(AlertDialog.BUTTON_NEUTRAL);
                //POSITIVE BUTTON -- Get Rid Of Old SharedPreferences, Set New SharedPreferences
                a.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences.Editor editor = prefs.edit();
                        //Remove Previous Shared Preferences.
                        editor.remove("threatLevelOption");
                        editor.remove("dateOption");
                        editor.remove("reportStatusOptionNo1");
                        editor.remove("reportStatusOptionNo2");
                        editor.remove("reportStatusOptionNo3");
                        editor.remove("reportStatusOptionNo4");
                        editor.remove("locationOption");
                        editor.remove("mediaAllowedOption");
                        editor.remove("categoryOption");
                        //Set New Shared Preferences by checking current data fields.
                        if(byThreatLevelOption1.isChecked())
                            editor.putString("threatLevelOption", "1");
                        else
                            editor.putString("threatLevelOption", "2");

                        if(byDateOption1.isChecked())
                            editor.putString("dateOption", "1");
                        else
                            editor.putString("dateOption", "1");

                        if(reportStatusOption1.isChecked())
                            editor.putString("reportStatusOptionNo1", "1");
                        else
                            editor.putString("reportStatusOptionNo1", "2");

                        if(reportStatusOption2.isChecked())
                            editor.putString("reportStatusOptionNo2", "1");
                        else
                            editor.putString("reportStatusOptionNo2", "2");

                        if(reportStatusOption3.isChecked())
                            editor.putString("reportStatusOptionNo3", "1");
                        else
                            editor.putString("reportStatusOptionNo3", "2");

                        if(reportStatusOption4.isChecked())
                            editor.putString("reportStatusOptionNo4", "1");
                        else
                            editor.putString("reportStatusOptionNo4", "2");

                        if(TextUtils.isEmpty(locationOption.getText()))
                            editor.putString("locationOption", "");
                        else
                            editor.putString("locationOption", locationOption.getText().toString());

                        if(TextUtils.isEmpty(etStartDate.getText()))
                            editor.putString("startDateOption", "");
                        else
                            editor.putString("startDateOption", etStartDate.getText().toString());

                        if(TextUtils.isEmpty(etStartTime.getText()))
                            editor.putString("startTimeOption", "");
                        else
                            editor.putString("startTimeOption", etStartTime.getText().toString());

                        if(TextUtils.isEmpty(etEndDate.getText()))
                            editor.putString("endDateOption", "");
                        else
                            editor.putString("endDateOption", etEndDate.getText().toString());

                        if(TextUtils.isEmpty(etEndTime.getText()))
                            editor.putString("endTimeOption", "");
                        else
                            editor.putString("endTimeOption", etEndTime.getText().toString());

                        if(isMediaAllowedOption.isChecked())
                            editor.putString("mediaAllowedOption", "1");
                        else
                            editor.putString("mediaAllowedOption", "2");

                        if(TextUtils.isEmpty(tvCategoriesSelected.getText()))
                            editor.putString("categoryOption", "");
                        else
                            editor.putString("categoryOption", tvCategoriesSelected.getText().toString());

                        editor.apply();

                        mBuilder.dismiss();
                    }
                });
                //NEGATIVE BUTTON -- Dismiss the Dialog Box, Keeping Old SharedPreferences
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mBuilder.dismiss();
                    }
                });
                //NEUTRAL BUTTON -- Set All Options To Default Option
                c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Set values to default.
                        byThreatLevelOption1.setChecked(true);
                        byDateOption1.setChecked(true);
                        reportStatusOption1.setChecked(true);
                        reportStatusOption2.setChecked(true);
                        reportStatusOption3.setChecked(true);
                        reportStatusOption4.setChecked(true);
                        locationOption.setText("");
                        etStartDate.setText("");
                        etStartTime.setText("");
                        etEndDate.setText("");
                        etEndTime.setText("");
                        isMediaAllowedOption.setChecked(true);
                        tvCategoriesSelected.setText((getStringBuilder(temp)).toString());
                        tvCategories.setText("All Categories Selected");
                        //TODO assignedadmins
                    }
                });
            }
        });

        mBuilder.show();
    }

    private void actionSelectDateStart() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view, year, monthOfYear, dayOfMonth) ->
        {
            String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
            etStartDate.setText(date);
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void actionSelectDateEnd() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view, year, monthOfYear, dayOfMonth) ->
        {
            String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
            etEndDate.setText(date);
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void actionSelectTimeStart() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(hourOfDay == 12){
                    String time = String.format(Locale.ENGLISH, "%02d:%02d PM", hourOfDay, minute);
                    etStartTime.setText(time);
                }
                else if(hourOfDay == 0){
                    hourOfDay = hourOfDay + 12;
                    String time = String.format(Locale.ENGLISH, "%02d:%02d AM", hourOfDay, minute);
                    etStartTime.setText(time);
                }
                else if(hourOfDay > 12) {
                    hourOfDay = hourOfDay - 12;
                    String time = String.format(Locale.ENGLISH, "%02d:%02d PM", hourOfDay, minute);
                    etStartTime.setText(time);
                }
                else {
                    String time = String.format(Locale.ENGLISH, "%02d:%02d AM", hourOfDay, minute);
                    etStartTime.setText(time);
                }
            }
        }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void actionSelectTimeEnd() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(hourOfDay == 12){
                    String time = String.format(Locale.ENGLISH, "%02d:%02d PM", hourOfDay, minute);
                    etStartTime.setText(time);
                }
                else if(hourOfDay == 0){
                    hourOfDay = hourOfDay + 12;
                    String time = String.format(Locale.ENGLISH, "%02d:%02d AM", hourOfDay, minute);
                    etStartTime.setText(time);
                }
                else if(hourOfDay > 12) {
                    hourOfDay = hourOfDay - 12;
                    String time = String.format(Locale.ENGLISH, "%02d:%02d PM", hourOfDay, minute);
                    etStartTime.setText(time);
                }
                else {
                    String time = String.format(Locale.ENGLISH, "%02d:%02d AM", hourOfDay, minute);
                    etStartTime.setText(time);
                }
            }
        }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void actionSelectCategories(){
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setPositiveButton("OK", null);
        b.setNegativeButton("Cancel", null);
        b.setNeutralButton("CLEAR ALL", null);
        b.setTitle("Select Categories");
        b.setCancelable(false);
        final String[] categoryItems = getResources().getStringArray(R.array.category_item);
        boolean[] checkedCategories = getPreChecked(categoryItems, tvCategoriesSelected.getText().toString());
        final String[] newCategoryItems = new String[categoryItems.length + 1];
        boolean[] newCheckedCategories = new boolean[checkedCategories.length + 1];

        for(int i = 0; i < categoryItems.length; i++){
            newCategoryItems[i] = categoryItems[i];
            newCheckedCategories[i] = checkedCategories[i];
        }
        newCategoryItems[newCategoryItems.length-1] = "Select All";
        newCheckedCategories[newCheckedCategories.length-1] = false;

        b.setMultiChoiceItems(newCategoryItems, newCheckedCategories, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                if(position == newCheckedCategories.length-1){
                    Arrays.fill(newCheckedCategories, Boolean.TRUE);
                    Arrays.fill(checkedCategories, Boolean.TRUE);
                    for(int i = 0; i < newCategoryItems.length; i++){
                        if(i == newCheckedCategories.length - 1) {
                            newCheckedCategories[i] = false;
                            ((AlertDialog) dialog).getListView().setItemChecked(i, false);
                        }
                        else
                            ((AlertDialog) dialog).getListView().setItemChecked(i, true);
                    }
                }
                else {
                    newCheckedCategories[position] = isChecked;
                    checkedCategories[position] = isChecked;
                }
            }
        });
        //Create this way so neutral button doesn't close filter options.
        AlertDialog bBuilder = b.create();
        bBuilder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                //Set Buttons so that dialog box doesn't dismiss when neutral button is hit.
                Button posButton = bBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negButton = bBuilder.getButton(AlertDialog.BUTTON_NEGATIVE);
                Button neutralButton = bBuilder.getButton(AlertDialog.BUTTON_NEUTRAL);
                posButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String label;
                        newCategories = new ArrayList<>();
                        for (int i = 0; i < checkedCategories.length; i++)
                            if (checkedCategories[i]) {
                                newCategories.add(categoryItems[i]);
                            }
                        tvCategoriesSelected.setText((getStringBuilder(newCategories)).toString());
                        if (newCategories.size() > 0) {
                            if(newCategories.size() == checkedCategories.length)
                                label = "All Categories Selected";
                            else
                                label = newCategories.size() + " Categories Selected";
                        } else {
                            label = "No Categories Selected";
                        }
                        tvCategories.setText(label);

                        bBuilder.dismiss();
                    }
                });
                negButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bBuilder.dismiss();
                    }
                });
                neutralButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Arrays.fill(checkedCategories, Boolean.FALSE);
                        Arrays.fill(newCheckedCategories, Boolean.FALSE);
                        for(int i = 0; i < newCategoryItems.length; i++){
                            ((AlertDialog) dialog).getListView().setItemChecked(i, false);
                        }
                    }
                });

            }
        });
        bBuilder.show();
    }

    private boolean[] getPreChecked(String[] items, String selected) {
        boolean[] checkedItems = new boolean[items.length];
        if (selected.equals("")) {
            Arrays.fill(checkedItems, Boolean.FALSE);
            return checkedItems;
        }
        //Creates an array of each item that is selected
        String[] array = selected.split("\\s*,\\s*|\\s*,\\s*and\\s*|\\s*and\\s*");

        //read through the array and see if it matches with the items
        for(int i = 0; i < checkedItems.length; i++) {
            for (String anArray : array) {
                //System.out.println(anArray);
                if (items[i].equals(anArray))
                    checkedItems[i] = true;
            }
        }
        return checkedItems;
    }

    private static StringBuilder getStringBuilder(List<String> string) {
        StringBuilder sb = new StringBuilder();
        if(string.size() == 0)
            return sb;
        String prefix = "";
        for (int i=0; i<string.size(); i++) {
            sb.append(prefix);
            prefix = ", ";
            //makes the last thing have an and
            if (i == string.size() - 2)
                prefix = ", and ";
            if (string.size() == 2)
                prefix = " and ";
            sb.append(string.get(i));
        }
        return sb;
    }


    private List<Report> applyFilter(HashMap<String, Report> nonFilteredReportMap){
        //Get Filter Options from Stored Preferences
        final String MY_PREFS_NAME = "MyFilterPreferences";
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        boolean [] whatDataWillWeSearch = new boolean[14];
        //HashMap<String, ReportObject> nonFilteredReportMap = Client.reportMap;                UNCOMMENT THISSSS????
        HashMap<String, Report> filteredReportMap;
        List<Report> reportObjectList = new ArrayList<>();
        List<Report> sortedReportObjectList = new ArrayList<>();

        /*
         * Boolean Layout:
         *      Sorting Options:
         *  0 == By Threat Level
         *  1 == By Date
         *  2 == Report Status Option 1 ()
         *  3 == Report Status Option 2 ()
         *  4 == Report Status Option 3 ()
         *  5 == Report Status Option 4 ()
         *  6 == Location Data
         *  7 == Start Date
         *  8 == Start Time
         *  9 == End Date
         *  10 == End Time
         *  11 == Media Allowed
         *  12 == Category Data
         *  13 == Assigned Admin Data
         *
         */
        String threatLevelData = prefs.getString("threatLevelOption", null);
        String byDateData = prefs.getString("dateOption", null);
        String reportStatusData1 = prefs.getString("reportStatusOptionNo1", null);
        String reportStatusData2 = prefs.getString("reportStatusOptionNo2", null);
        String reportStatusData3 = prefs.getString("reportStatusOptionNo3", null);
        String reportStatusData4 = prefs.getString("reportStatusOptionNo4", null);
        String locationData = prefs.getString("locationOption", null);
        String startDateData = prefs.getString("startDateOption", null);
        String startTimeData = prefs.getString("startTimeOption", null);
        String endDateData = prefs.getString("endDateOption", null);
        String endTimeData = prefs.getString("endTimeOption", null);
        String mediaAllowedData = prefs.getString("mediaAllowedOption", null);
        String categoryData = prefs.getString("categoryOption", null);
        //TODO Assigned Admin SharedPref
        String assignedAdminData = "";

        //Determine each Filter Option
        if(threatLevelData != null){
            whatDataWillWeSearch[0] = true;
        }
        else{
            threatLevelData = "1"; // High -> Low
            whatDataWillWeSearch[0] = false;
        }

        if(byDateData != null){
            whatDataWillWeSearch[1] = true;
        }
        else{
            byDateData = "1"; // Newest -> Oldest
            whatDataWillWeSearch[1] = false;
        }

        if(reportStatusData1 != null){
            if(reportStatusData1.equals("1"))
                whatDataWillWeSearch[2] = false;
            else
                whatDataWillWeSearch[2] = true;
        }
        else{
            reportStatusData1 = "1"; // Checked
            whatDataWillWeSearch[2] = false;
        }

        if(reportStatusData2 != null){
            if(reportStatusData2.equals("1"))
                whatDataWillWeSearch[3] = false;
            else
                whatDataWillWeSearch[3] = true;
        }
        else{
            reportStatusData2 = "1"; // Checked
            whatDataWillWeSearch[3] = false;
        }

        if(reportStatusData3 != null){
            if(reportStatusData3.equals("1"))
                whatDataWillWeSearch[4] = false;
            else
                whatDataWillWeSearch[4] = true;
        }
        else{
            reportStatusData3 = "1"; // Checked
            whatDataWillWeSearch[4] = false;
        }

        if(reportStatusData4 != null){
            if(reportStatusData4.equals("1"))
                whatDataWillWeSearch[5] = false;
            else
                whatDataWillWeSearch[5] = true;
        }
        else{
            reportStatusData4 = "1"; // Checked
            whatDataWillWeSearch[5] = false;
        }

        if(locationData != null){
            if(locationData.isEmpty())
                whatDataWillWeSearch[6] = false;
            else
                whatDataWillWeSearch[6] = true;
        }
        else{
            locationData = "";
            whatDataWillWeSearch[6] = false;
        }

        if(startDateData != null){
            if(startDateData.isEmpty())
                whatDataWillWeSearch[7] = false;
            else
                whatDataWillWeSearch[7] = true;
        }
        else{
            startDateData = "";
            whatDataWillWeSearch[7] = false;
        }

        if(startTimeData != null){
            if(startTimeData.isEmpty())
                whatDataWillWeSearch[8] = false;
            else
                whatDataWillWeSearch[8] = true;
        }
        else{
            startTimeData = "";
            whatDataWillWeSearch[8] = false;
        }

        if(endDateData != null){
            if(endDateData.isEmpty())
                whatDataWillWeSearch[9] = false;
            else
                whatDataWillWeSearch[9] = true;
        }
        else{
            endDateData = "";
            whatDataWillWeSearch[9] = false;
        }

        if(endTimeData != null){
            if(endTimeData.isEmpty())
                whatDataWillWeSearch[10] = false;
            else
                whatDataWillWeSearch[10] = true;
        }
        else{
            endTimeData = "";
            whatDataWillWeSearch[10] = false;
        }

        if(mediaAllowedData != null){
            if(mediaAllowedData.equals("1")) // checked
                whatDataWillWeSearch[11] = false;
            else
                whatDataWillWeSearch[11] = true;
        }
        else{
            mediaAllowedData = "1";
            whatDataWillWeSearch[11] = false;
        }

        if(categoryData != null){
            //Create full category String to test for equality
            final String[] categoryItems = getResources().getStringArray(R.array.category_item);
            List<String> temp = new ArrayList();
            for(int i = 0; i < categoryItems.length; i++)
                temp.add(categoryItems[i]);

            String tempData = getStringBuilder(temp).toString();

            if(categoryData.equals(tempData))
                whatDataWillWeSearch[12] = false; // String has All Categories
            else
                whatDataWillWeSearch[12] = true;
        }
        else{
            final String[] categoryItems = getResources().getStringArray(R.array.category_item);
            List<String> temp = new ArrayList();
            for(int i = 0; i < categoryItems.length; i++)
                temp.add(categoryItems[i]);

            categoryData = getStringBuilder(temp).toString();
            whatDataWillWeSearch[12] = false; // String has All Categories
        }

        whatDataWillWeSearch[13] = false;

        //TODO add Assigned Admin If Else
        //Check to see if items exist in the HashMap

        //Add all items in HashMap to List of ReportObjects
        for (String key: nonFilteredReportMap.keySet()) {
            System.out.println("key : " + key);
            reportObjectList.add(nonFilteredReportMap.get(key));
        }

        //Do Filter
        for(int i = 0; i < reportObjectList.size(); i++){
            boolean passReportCheck = false;
            if(checkReportStatusData(whatDataWillWeSearch[2], reportStatusData1, reportObjectList.get(i).status, 0)){
                passReportCheck = true;
            }
            if(checkReportStatusData(whatDataWillWeSearch[3], reportStatusData2, reportObjectList.get(i).status, 1)){
                passReportCheck = true;
            }
            if(checkReportStatusData(whatDataWillWeSearch[4], reportStatusData3, reportObjectList.get(i).status, 2)){
                passReportCheck = true;
            }
            if(checkReportStatusData(whatDataWillWeSearch[5], reportStatusData4, reportObjectList.get(i).status, 3)){
                passReportCheck = true;
            }
            if(passReportCheck == false){
                continue; // Go to next item. ReportObject failed to pass filter.
            }
            if(!checkLocationData(whatDataWillWeSearch[6], locationData, reportObjectList.get(i).location)){
                continue; // Go to next item. ReportObject failed to pass filter.
            }
            if(!checkSubmitDate(whatDataWillWeSearch[7], startDateData, reportObjectList.get(i).submittedDate, true)){
                continue;
            }
            if(!checkSubmitTime(whatDataWillWeSearch[8], startTimeData, reportObjectList.get(i).submittedTime, true)){
                continue;
            }
            if(!checkSubmitDate(whatDataWillWeSearch[9], endDateData, reportObjectList.get(i).submittedDate, false)){
                continue;
            }
            if(!checkSubmitTime(whatDataWillWeSearch[10], endTimeData, reportObjectList.get(i).submittedTime, false)){
                continue;
            }
            if(!checkMediaAllowed(whatDataWillWeSearch[11], mediaAllowedData, reportObjectList.get(i).media)){
                continue;
            }
            if(!checkCategoryData(whatDataWillWeSearch[12], categoryData, reportObjectList.get(i).categories)){
                continue;
            }
            if(!checkAssignedAdminData(whatDataWillWeSearch[13], assignedAdminData, reportObjectList.get(i).assignedTo)){
                continue;
            }

            //Passed Filter. Add to new ReportObject List.
            sortedReportObjectList.add(reportObjectList.get(i));
        }
        //TODO Finished Filtering Into sortedReportObjectList. Now Sort List. First Sort by Threat Level. Then Sort by Date.
        //sortedReportObjectList = sortReportListByThreatLevel(whatDataWillWeSearch[0], sortedReportObjectList);
        sortedReportObjectList = sortReportListByDate(whatDataWillWeSearch[1], sortedReportObjectList);

        return sortedReportObjectList;
    }

    private boolean checkReportStatusData(boolean arr, String data, String reportData, int state){
        if(arr){ //unchecked value
            return false;
        }
        else{
            if(data.equals(reportData) || reportData.equals("Open")) {
                if(state == 0)
                    return true;
            }
            else if(data.equals(reportData) || reportData.equals("In Progress")) {
                if(state == 1)
                    return true;
            }
            else if(data.equals(reportData) || reportData.equals("Closed")) {
                if(state == 2)
                    return true;
            }
            else if(data.equals(reportData) || reportData.equals("Resolved")){
                if(state == 3)
                    return true;
            }

            return false;
        }
    }

    private boolean checkLocationData(boolean arr, String data, String reportData){
        if(arr == false)
            return true; // don't need to check. as it is empty.
        else{
            if(reportData.toLowerCase().contains(data.toLowerCase()))
                return true;
            else
                return false;
        }
    }

    // startOrEnd == true == Start; startOrEnd == false == End;
    private boolean checkSubmitDate(boolean arr, String data, String reportData, boolean startOrEnd){
        if(arr == false) {
            return true; // don't need to check. as it is empty.
        }
        else{
            String firstDay = "";
            String secondDay = "";
            String firstMonth = "";
            String secondMonth = "";
            String firstYear = "";
            String secondYear = "";

            //Parse first Date String. of Form "Day/Month/Year"
            int count = 0;
            for(int i = 0; i < data.length(); i++){
                if(data.charAt(i) == '/'){
                    count = count + 1;
                    continue;
                }
                else if(count == 0){ // Day
                    firstDay = firstDay + data.charAt(i);
                }
                else if(count == 1){ // Month
                    firstMonth = firstMonth + data.charAt(i);
                }
                else if(count == 2){ // Year
                    firstYear = firstYear + data.charAt(i);
                }
                else{
                    //Error.
                }
            }
            //Parse second Date String. of Form "Day/Month/Year"
            count = 0;
            for(int i = 0; i < data.length(); i++){
                if(reportData.charAt(i) == '/'){
                    count = count + 1;
                    continue;
                }
                else if(count == 0){ // Day
                    secondDay = secondDay + reportData.charAt(i);
                }
                else if(count == 1){ // Month
                    secondMonth = secondMonth + reportData.charAt(i);
                }
                else if(count == 2){ // Year
                    secondYear = secondYear + reportData.charAt(i);
                }
                else{
                    //Error.
                }
            }
            //Compare Values Based on Start/End Date data vs. Report Submission Date Data
            if(startOrEnd){ //Is Start Date
                if(Integer.parseInt(firstYear) == Integer.parseInt(secondYear)){
                    //check month
                    if(Integer.parseInt(firstMonth) == Integer.parseInt(secondMonth)){
                        //check day
                        if(Integer.parseInt(firstDay) == Integer.parseInt(secondDay)){
                            //Valid Report. return true;
                            return true;
                        }
                        else if(Integer.parseInt(firstDay) < Integer.parseInt(secondDay)){
                            //Valid Report. return true;
                            return true;
                        }
                        else{
                            //Invalid Report. return false;
                            return false;
                        }
                    }
                    else if(Integer.parseInt(firstMonth) < Integer.parseInt(secondMonth)){
                        //Valid Report. return true;
                        return true;
                    }
                    else{
                        //Invalid Report. return false;
                        return false;
                    }
                }
                else if(Integer.parseInt(firstYear) < Integer.parseInt(secondYear)){
                    //Valid Report. return true;
                    return true;
                }
                else{
                    //Invalid Report. return false;
                    return false;
                }
            }
            else if(!startOrEnd){ //Is End Date
                if(Integer.parseInt(firstYear) == Integer.parseInt(secondYear)){
                    //check month
                    if(Integer.parseInt(firstMonth) == Integer.parseInt(secondMonth)){
                        //check day
                        if(Integer.parseInt(firstDay) == Integer.parseInt(secondDay)){
                            //Valid Report. return true;
                            return true;
                        }
                        else if(Integer.parseInt(firstDay) > Integer.parseInt(secondDay)){
                            //Valid Report. return true;
                            return true;
                        }
                        else{
                            //Invalid Report. return false;
                            return false;
                        }
                    }
                    else if(Integer.parseInt(firstMonth) > Integer.parseInt(secondMonth)){
                        //Valid Report. return true;
                        return true;
                    }
                    else{
                        //Invalid Report. return false;
                        return false;
                    }
                }
                else if(Integer.parseInt(firstYear) > Integer.parseInt(secondYear)){
                    //Valid Report. return true;
                    return true;
                }
                else{
                    //Invalid Report. return false;
                    return false;
                }
            }
            else{
                //Error. Shouldn't reach this point.
                return false;

            }
        }
    }

    private boolean checkSubmitTime(boolean arr, String data, String reportData, boolean startOrEnd){
        if(arr == false)
            return true; // don't need to check. as it is empty.
        else{
            String firstMinute = "";
            String secondMinute = "";
            String firstHour = "";
            String secondHour = "";
            String firstAmOrPm = "";
            String secondAmOrPm = "";

            //Parse first Time String. of Form "Hour:Minute AM" || "Hour:Minute PM"
            int count = 0;
            for(int i = 0; i < data.length(); i++){
                if(data.charAt(i) == ':' || data.charAt(i) == ' '){
                    count = count + 1;
                    continue;
                }
                else if(count == 0){ // Day
                    firstHour = firstHour + data.charAt(i);
                }
                else if(count == 1){ // Month
                    firstMinute = firstMinute + data.charAt(i);
                }
                else if(count == 2){ // Year
                    firstAmOrPm = firstAmOrPm + data.charAt(i);
                }
                else{
                    //Error.
                }
            }
            //Parse second Time String. of Form "Hour:Minute AM" || "Hour:Minute PM"
            count = 0;
            for(int i = 0; i < data.length(); i++){
                if(reportData.charAt(i) == ':' || reportData.charAt(i) == ' '){
                    count = count + 1;
                    continue;
                }
                else if(count == 0){ // Day
                    secondHour = secondHour + reportData.charAt(i);
                }
                else if(count == 1){ // Month
                    secondMinute = secondMinute + reportData.charAt(i);
                }
                else if(count == 2){ // Year
                    secondAmOrPm = secondAmOrPm + reportData.charAt(i);
                }
                else{
                    //Error.
                }
            }
            //Compare Values Based on Start/End Time data vs. Report Submission Time Data
            if(startOrEnd) { //Is Start Time
                if(firstAmOrPm.equals(secondAmOrPm)){
                    //Check Hour
                    if(Integer.parseInt(firstHour) == Integer.parseInt(secondHour)){
                        //Check Minute
                        if(Integer.parseInt(firstMinute) == Integer.parseInt(secondMinute)){
                            //Valid Report. return true;
                            return true;
                        }
                        else if(Integer.parseInt(firstMinute) < Integer.parseInt(secondMinute)){ // preferences are less than report Time.
                            //Valid Report. return true;
                            return true;
                        }
                        else{ // preferences are more than report Time.
                            //Invalid Report. return false;
                            return false;
                        }
                    }
                    else if(Integer.parseInt(firstHour) < Integer.parseInt(secondHour)){ // preferences are less than report Time.
                        //Valid Report. return true;
                        return true;
                    }
                    else{ // preferences are more than report Time.
                        //Invalid Report. return false;
                        return false;
                    }
                }
                else if(firstAmOrPm.equals("PM")){ // if Preferences are PM, and it doesn't equal report Time, then must be AM
                    //Invalid Report. return false;
                    return false;
                }
                else{ // preferences must be AM and report Time must be PM
                    //Valid Report. return true;
                    return true;
                }
            }
            else if(!startOrEnd) { //Is End Time
                if(firstAmOrPm.equals(secondAmOrPm)){
                    //Check Hour
                    if(Integer.parseInt(firstHour) == Integer.parseInt(secondHour)){
                        //Check Minute
                        if(Integer.parseInt(firstMinute) == Integer.parseInt(secondMinute)){
                            //Valid Report. return true;
                            return true;
                        }
                        else if(Integer.parseInt(firstMinute) > Integer.parseInt(secondMinute)){ // preferences are greater than report Time.
                            //Valid Report. return true;
                            return true;
                        }
                        else{ // preferences are less than report Time.
                            //Invalid Report. return false;
                            return false;
                        }
                    }
                    else if(Integer.parseInt(firstHour) > Integer.parseInt(secondHour)){ // preferences are greater than report Time.
                        //Valid Report. return true;
                        return true;
                    }
                    else{ // preferences are less than report Time.
                        //Invalid Report. return false;
                        return false;
                    }
                }
                else if(firstAmOrPm.equals("AM")){ // if Preferences are AM, and it doesn't equal report Time, then must be PM
                    //Invalid Report. return false;
                    return false;
                }
                else{ // preferences must be PM and report Time must be AM
                    //Valid Report. return true;
                    return true;
                }
            }
            else{
                //Error. as startOrEnd should be true or false;
                return false;
            }
        }
    }

    private boolean checkMediaAllowed(boolean arr, String data, String reportData){
        if(arr == false) {
            return true;
        }
        else{
            if(reportData.isEmpty())
                return true;
            else
                return false;
        }
    }

    private boolean checkCategoryData(boolean arr, String data, List<String> reportData){
        final String[] categoryItems = getResources().getStringArray(R.array.category_item);
        boolean[] checkedCategories = getPreChecked(categoryItems, data);

        if(arr == false) {
            //Valid Report. All Categories Selected.
            return true;
        }
        //Check the Items to find matching Categories.
        for (int i = 0; i < categoryItems.length; i++) {
            //Check if Preference items == Report Data Category
            if(reportData.contains(categoryItems[i])){
                //Check if a Value that matches is checked.
                if(checkedCategories[i]){
                    //Valid Report. Value that matches IS checked. return true;
                    return true;
                }
            }
        }
        //Invalid Report. Nothing was checked in preferences. return false;
        return false;
    }

    private boolean checkAssignedAdminData(boolean arr, String data, List<String> reportData){
        //TODO whole thing. ALSO ASK IF ASSIGNED ADMINS SHOULD BE A LIST<STRING> or JUST STRING???????????
        return true;
    }

    //TODO add the low->high version...
    private List<Report> sortReportListByThreatLevel(boolean arr, List<Report> reportList){
        List<Report> sortedReportList = new ArrayList<>();

        //Add one item.
        sortedReportList.add(reportList.get(0));


        int size = 1;
        int set = 0;
        for(int i = 1; i < reportList.size(); i++){
            for(int j = 0; j < size; j++){
                if(Integer.valueOf(reportList.get(i).threatLevel) > Integer.valueOf(sortedReportList.get(j).threatLevel)){
                    sortedReportList.add(j, reportList.get(i));
                    set = 1;
                    break;
                }
            }
            if(set == 0){
                sortedReportList.add(size, reportList.get(i));
            }
            size = size + 1;
            set = 0;
        }

        return sortedReportList;
    }

    //TODO newest->oldest. oldest->newest
    private List<Report> sortReportListByDate(boolean arr, List<Report> reportList){
        List<Report> sortedReportList = new ArrayList<>();

        //Check to see if reportList is empty
        if(reportList.size() == 0)
            return reportList;

        //Add one item.
        sortedReportList.add(reportList.get(0));

        //Newest -> Oldest
        int size = 1;
        int set = 0;
        for(int i = 1; i < reportList.size(); i++){
            for(int j = 0; j < size;j++){
                if(checkSubmitDate(true, sortedReportList.get(j).submittedDate, reportList.get(i).submittedDate,true)){
                    if(checkSubmitTime(true, sortedReportList.get(j).submittedTime,reportList.get(i).submittedTime, true)){
                        sortedReportList.add(j, reportList.get(i));
                        set = 1;
                        break;
                    }
                    else{
                        sortedReportList.add(j+1, reportList.get(i));
                        set = 1;
                        break;
                    }
                }
            }
            if(set == 0){
                sortedReportList.add(size, reportList.get(i));
            }
            size = size + 1;
            set = 0;
        }

        return sortedReportList;
    }

}

