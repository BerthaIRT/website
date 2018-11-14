package com.universityofalabama.cs495f2018.berthaIRT.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.amazonaws.auth.policy.conditions.StringCondition;
import com.google.gson.JsonObject;
import com.universityofalabama.cs495f2018.berthaIRT.Client;
import com.universityofalabama.cs495f2018.berthaIRT.R;
import com.universityofalabama.cs495f2018.berthaIRT.Report;
import com.universityofalabama.cs495f2018.berthaIRT.Util;
import com.universityofalabama.cs495f2018.berthaIRT.adapter.AdminReportCardAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AdminReportCardsFragment extends Fragment {
    RecyclerView rv;
    AdminReportCardAdapter adapter;
    SwipeRefreshLayout swipeContainer;
    TextView tvNoReports;

    //Filter Option Data
    private EditText etStartDate;
    private EditText etEndDate;
    private TextView tvCategories;
    private TextView tvCategoriesSelected;
    private List<String> newCategories;
    private long startDateStamp = GregorianCalendar.getInstance().getTimeInMillis();
    private long endDateStamp = GregorianCalendar.getInstance().getTimeInMillis();
    ImageView ivSearch;
    EditText etSearch;

    public AdminReportCardsFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater flater, ViewGroup tainer, Bundle savedInstanceState) {
        View v = flater.inflate(R.layout.fragment_admin_reportcards, tainer, false);

        adapter = new AdminReportCardAdapter(getContext());

        rv = v.findViewById(R.id.admin_reports_rv);
        rv.setAdapter(adapter);

        swipeContainer = v.findViewById(R.id.admin_reports_sr);
        swipeContainer.setOnRefreshListener(this::actionSwipeRefresh);

        v.findViewById(R.id.filter_options).setOnClickListener(v1 -> showFilterOptions());

        tvNoReports = v.findViewById(R.id.admin_reports_alt_noreports);

        //Set the Image search button and edit text for it.
        ivSearch = v.findViewById(R.id.imageView2);
        etSearch = v.findViewById(R.id.admin_reports_input_searchbox);


        //TODO search all of list for more detailed results
        v.findViewById(R.id.imageView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = etSearch.getText().toString();
                List<Report> reportList = applyFilter(Client.reportMap.values());
                List<Report> searchedList = new ArrayList<>();
                boolean check = false;
                for(int i = 0; i < reportList.size(); i++){
                    //reset check
                    check = false;

                    //Search ReportIds
                    if(reportList.get(i).reportId.contains(searchText)) {
                        searchedList.add(reportList.get(i));
                        continue;
                    }

                    //Search Categories
                    for(int j = 0; j < reportList.get(i).categories.size(); j++){
                        if((reportList.get(i).categories.get(j).toLowerCase()).contains(searchText.toLowerCase())) {
                            searchedList.add(reportList.get(i));
                            check = true;
                            break;
                        }
                    }

                    //Check if item was added to list from Categories
                    if(check == true)
                        continue;

                    //Search Tags
                    for(int k = 0; k < reportList.get(i).tags.size(); k++){
                        if((reportList.get(i).tags.get(k).toLowerCase()).contains(searchText.toLowerCase())) {
                            searchedList.add(reportList.get(i));
                            break;
                        }
                    }
                }
                adapter.updateReports(searchedList);
            }
        });
        //Search button push view toggle
        v.findViewById(R.id.imageView2).setOnTouchListener(new View.OnTouchListener() {
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
                        if(touchStayedWithinViewBounds && !isMotionEventInsideView(ivSearch, event)){
                            touchStayedWithinViewBounds = false;
                        }
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        ViewCompat.setElevation(ivSearch, 20);
                        return true;
                    default:
                        return false;
                }
            }

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

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        refreshReports();
    }

    public void refreshReports() {
        Client.net.secureSend(getContext(), "/report/retrieve/all", "", r -> {
            Client.reportMap.clear();
            JsonObject jay = Client.net.jp.parse(r).getAsJsonObject();
            for (String id : jay.keySet()) {
                String jayReport = jay.get(id).getAsString();
                Report report = Client.net.gson.fromJson(jayReport, Report.class);
                Client.reportMap.put(id, report);
            }
            adapter.updateReports(applyFilter(Client.reportMap.values()));
        });
    }

    private void actionSwipeRefresh() {
        swipeContainer.setRefreshing(true); {
            refreshReports();
        }
        if (swipeContainer.isRefreshing())
            swipeContainer.setRefreshing(false);
    }



    private void showFilterOptions(){
        //TODO add an if any element has changed check?
        final String[] categoryItems = getResources().getStringArray(R.array.category_item);
        List<String> temp = new ArrayList<>(Arrays.asList(categoryItems));

        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View dialoglayout = inflater.inflate(R.layout.dialog_filter_options, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AppCompatAlertDialogStyle);
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Cancel", null);
        builder.setNeutralButton("Default", null);
        builder.setView(dialoglayout);
        builder.setTitle("Filter Options");

        //Create this way so neutral button doesn't close filter options.
        AlertDialog mBuilder = builder.create();
        mBuilder.setOnShowListener(dialog -> {
            SharedPreferences prefs = getActivity().getSharedPreferences("MyFilterPreferences", MODE_PRIVATE);

            Boolean byDateData = prefs.getBoolean("dateOption", true);
            Boolean reportStatusData1 = prefs.getBoolean("reportStatusOptionNo1", true);
            Boolean reportStatusData2 = prefs.getBoolean("reportStatusOptionNo2", true);
            Boolean reportStatusData3 = prefs.getBoolean("reportStatusOptionNo3", true);
            Boolean reportStatusData4 = prefs.getBoolean("reportStatusOptionNo4", true);
            Boolean reportStatusData5 = prefs.getBoolean("reportStatusOptionNo5", true);
            String locationData = prefs.getString("locationOption", null);
            Long startDateData = prefs.getLong("startDateOption", 0);
            Long endDateData = prefs.getLong("endDateOption", 0);
            Boolean mediaAllowedData = prefs.getBoolean("mediaAllowedOption", true);
            String categoryData = prefs.getString("categoryOption", null);



            //Sorting Options:
            //Radio Button Group 2: only 1 can be selected at a time. DEFAULT = byDateOption1.
            RadioButton byDateOption1 = dialoglayout.findViewById(R.id.radio_byDateOption1);
            RadioButton byDateOption2 = dialoglayout.findViewById(R.id.radio_byDateOption2);

            //Report Status:
            //CheckBoxes Group 1: Multiple may be selected. DEFAULT = all options selected.
            CheckBox reportStatusOption1, reportStatusOption2, reportStatusOption3, reportStatusOption4, reportStatusOption5;
            reportStatusOption1 = dialoglayout.findViewById(R.id.checkBox_reportStatusOption1);
            reportStatusOption2 = dialoglayout.findViewById(R.id.checkBox_reportStatusOption2);
            reportStatusOption3 = dialoglayout.findViewById(R.id.checkBox_reportStatusOption3);
            reportStatusOption4 = dialoglayout.findViewById(R.id.checkBox_reportStatusOption4);
            reportStatusOption5 = dialoglayout.findViewById(R.id.checkBox_reportStatusOption5);

            //Location: DEFAULT = empty.
            EditText locationOption = dialoglayout.findViewById(R.id.input_filter_location);

            //START DATE/TIME: DEFAULT = empty.
            etStartDate = dialoglayout.findViewById(R.id.input_filter_start_date);
            etStartDate.setOnClickListener(v -> actionSelectDateStart());

            //END DATE/Time: DEFAULT = empty.
            etEndDate = dialoglayout.findViewById(R.id.input_filter_end_date);
            etEndDate.setOnClickListener(v -> actionSelectDateEnd());

            //CheckBoxes Group 2: Only one option is selectable. DEFAULT = checked.
            CheckBox isMediaAllowedOption = dialoglayout.findViewById(R.id.checkBox_isMediaAllowed);

            //Categories list: DEFAULT = all categories checked.
            tvCategories = dialoglayout.findViewById(R.id.alt_filter_categories);
            tvCategoriesSelected = dialoglayout.findViewById(R.id.alt_filter_categories_selected);
            Button bCategories = dialoglayout.findViewById(R.id.button_filter_showcategories);
            bCategories.setOnClickListener(v -> actionSelectCategories());

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
            if(byDateData) { //Set to old preference value
                byDateOption1.setChecked(true);
            }
            else
                byDateOption2.setChecked(true);

            if(reportStatusData1)
                reportStatusOption1.setChecked(true);
            else
                reportStatusOption1.setChecked(false);

            if(reportStatusData2)
                reportStatusOption2.setChecked(true);
            else
                reportStatusOption2.setChecked(false);

            if(reportStatusData3)
                reportStatusOption3.setChecked(true);
            else
                reportStatusOption3.setChecked(false);

            if(reportStatusData4)
                reportStatusOption4.setChecked(true);
            else
                reportStatusOption4.setChecked(false);

            if(reportStatusData5)
                reportStatusOption5.setChecked(true);
            else
                reportStatusOption5.setChecked(false);

            if(locationData != null) //Set to old preference value
                locationOption.setText(locationData);
            else //Set default option
                locationOption.setText("");

            if(startDateData != 0) //Set to old preference value
                etStartDate.setText(Util.formatTimestamp(startDateData));
            else //Set default option
                etStartDate.setText("");

            if(endDateData != 0) //Set to old preference value
                etEndDate.setText(Util.formatTimestamp(endDateData));
            else //Set default option
                etEndDate.setText("");

            if(mediaAllowedData)
                isMediaAllowedOption.setChecked(true);
            else
                isMediaAllowedOption.setChecked(false);

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
                    for (boolean aCheckedCat : checkedCat) {
                        if (aCheckedCat)
                            num++;
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
            a.setOnClickListener(view -> {
                SharedPreferences.Editor editor = prefs.edit();
                //Remove Previous Shared Preferences.
                editor.remove("dateOption");
                editor.remove("reportStatusOptionNo1");
                editor.remove("reportStatusOptionNo2");
                editor.remove("reportStatusOptionNo3");
                editor.remove("reportStatusOptionNo4");
                editor.remove("reportStatusOptionNo5");
                editor.remove("locationOption");
                editor.remove("mediaAllowedOption");
                editor.remove("categoryOption");
                //Set New Shared Preferences by checking current data fields.
                editor.putBoolean("dateOption", byDateOption1.isChecked());
                editor.putBoolean("reportStatusOptionNo1", reportStatusOption1.isChecked());
                editor.putBoolean("reportStatusOptionNo2", reportStatusOption2.isChecked());
                editor.putBoolean("reportStatusOptionNo3", reportStatusOption3.isChecked());
                editor.putBoolean("reportStatusOptionNo4", reportStatusOption4.isChecked());
                editor.putBoolean("reportStatusOptionNo5", reportStatusOption5.isChecked());

                if(TextUtils.isEmpty(locationOption.getText()))
                    editor.putString("locationOption", "");
                else
                    editor.putString("locationOption", locationOption.getText().toString());

                if(TextUtils.isEmpty(etStartDate.getText()))
                    editor.putLong("startDateOption", 0);
                else
                    editor.putLong("startDateOption", startDateStamp);

                if(TextUtils.isEmpty(etEndDate.getText()))
                    editor.putLong("endDateOption", 0);
                else
                    editor.putLong("endDateOption", endDateStamp);

                editor.putBoolean("mediaAllowedOption", isMediaAllowedOption.isChecked());

                if(TextUtils.isEmpty(tvCategoriesSelected.getText()))
                    editor.putString("categoryOption", "");
                else
                    editor.putString("categoryOption", tvCategoriesSelected.getText().toString());

                editor.apply();
                refreshReports();
                mBuilder.dismiss();
            });
            //NEGATIVE BUTTON -- Dismiss the Dialog Box, Keeping Old SharedPreferences
            b.setOnClickListener(view -> mBuilder.dismiss());
            //NEUTRAL BUTTON -- Set All Options To Default Option
            c.setOnClickListener(view -> {
                //Set values to default.
                byDateOption1.setChecked(true);
                reportStatusOption1.setChecked(true);
                reportStatusOption2.setChecked(true);
                reportStatusOption3.setChecked(true);
                reportStatusOption4.setChecked(true);
                reportStatusOption5.setChecked(true);
                locationOption.setText("");
                etStartDate.setText("");
                startDateStamp = 0;
                etEndDate.setText("");
                endDateStamp = 0;
                isMediaAllowedOption.setChecked(true);
                tvCategoriesSelected.setText((getStringBuilder(temp)).toString());
                tvCategories.setText("All Categories Selected");
                //TODO assignedadmins
            });
        });

        mBuilder.show();
    }

    private void actionSelectDateStart() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(getActivity(), (view, year, monthOfYear, dayOfMonth) ->
        {
            String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
            etStartDate.setText(date);
            startDateStamp = new GregorianCalendar(year,monthOfYear, dayOfMonth).getTimeInMillis();
        }, mYear, mMonth, mDay).show();
    }

    private void actionSelectDateEnd() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(getActivity(), (view, year, monthOfYear, dayOfMonth) ->
        {
            String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
            etEndDate.setText(date);
            endDateStamp = new GregorianCalendar(year,monthOfYear, dayOfMonth).getTimeInMillis();
        }, mYear, mMonth, mDay).show();
    }

    private void actionSelectCategories(){
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity(),R.style.AppCompatAlertDialogStyle);
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

        b.setMultiChoiceItems(newCategoryItems, newCheckedCategories, (dialog, position, isChecked) -> {
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
        });
        //Create this way so neutral button doesn't close filter options.
        AlertDialog bBuilder = b.create();
        bBuilder.setOnShowListener(dialog -> {
            //Set Buttons so that dialog box doesn't dismiss when neutral button is hit.
            Button posButton = bBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negButton = bBuilder.getButton(AlertDialog.BUTTON_NEGATIVE);
            Button neutralButton = bBuilder.getButton(AlertDialog.BUTTON_NEUTRAL);
            posButton.setOnClickListener(view -> {
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
            });
            negButton.setOnClickListener(view -> bBuilder.dismiss());
            neutralButton.setOnClickListener(view -> {
                Arrays.fill(checkedCategories, Boolean.FALSE);
                Arrays.fill(newCheckedCategories, Boolean.FALSE);
                for(int i = 0; i < newCategoryItems.length; i++){
                    ((AlertDialog) dialog).getListView().setItemChecked(i, false);
                }
            });

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


    private List<Report> applyFilter(Collection<Report> nonFilteredReportMap){
        //Get Filter Options from Stored Preferences
        final String MY_PREFS_NAME = "MyFilterPreferences";
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        boolean [] whatDataWillWeSearch = new boolean[13];
        List<Report> reportObjectList = new ArrayList<>(nonFilteredReportMap);
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
         *  6 == Report Status Option 5 ()
         *  7 == Location Data
         *  8 == Start Date
         *  9 == End Date
         *  10 == Media Allowed
         *  11 == Category Data
         *  12 == Assigned Admin Data
         */
        Boolean byDateData = prefs.getBoolean("dateOption", true);
        Boolean reportStatusData1 = prefs.getBoolean("reportStatusOptionNo1", true);
        Boolean reportStatusData2 = prefs.getBoolean("reportStatusOptionNo2", true);
        Boolean reportStatusData3 = prefs.getBoolean("reportStatusOptionNo3", true);
        Boolean reportStatusData4 = prefs.getBoolean("reportStatusOptionNo4", true);
        Boolean reportStatusData5 = prefs.getBoolean("reportStatusOptionNo5", true);
        String locationData = prefs.getString("locationOption", null);
        Long startDateData = prefs.getLong("startDateOption", 0);
        Long endDateData = prefs.getLong("endDateOption", 0);
        Boolean mediaAllowedData = prefs.getBoolean("mediaAllowedOption", true);
        String categoryData = prefs.getString("categoryOption", null);
        //TODO Assigned Admin SharedPref
        String assignedAdminData = "";

        whatDataWillWeSearch[1] = byDateData;
        whatDataWillWeSearch[2] = !reportStatusData1;
        whatDataWillWeSearch[3] = !reportStatusData2;
        whatDataWillWeSearch[4] = !reportStatusData3;
        whatDataWillWeSearch[5] = !reportStatusData4;
        whatDataWillWeSearch[6] = !reportStatusData4;

        if(locationData != null)
            whatDataWillWeSearch[7] = !locationData.isEmpty();
        else{
            locationData = "";
            whatDataWillWeSearch[7] = false;
        }

        if(startDateData != 0)
            whatDataWillWeSearch[8] = true;
        else{
            startDateData = new Long(0);
            whatDataWillWeSearch[8] = false;
        }

        if(endDateData != 0)
            whatDataWillWeSearch[9] = true;
        else{
            endDateData = new Long(0);
            whatDataWillWeSearch[9] = false;
        }

        whatDataWillWeSearch[10] = !mediaAllowedData;

        if(categoryData != null){
            //Create full category String to test for equality
            final String[] categoryItems = getResources().getStringArray(R.array.category_item);
            List<String> temp = new ArrayList<>();
            Collections.addAll(temp, categoryItems);

            String tempData = getStringBuilder(temp).toString();

            whatDataWillWeSearch[11] = !categoryData.equals(tempData);
        }
        else{
            final String[] categoryItems = getResources().getStringArray(R.array.category_item);
            List<String> temp = new ArrayList<>();
            Collections.addAll(temp, categoryItems);

            categoryData = getStringBuilder(temp).toString();
            whatDataWillWeSearch[11] = false; // String has All Categories
        }

        whatDataWillWeSearch[12] = false;

        //TODO add Assigned Admin If Else
        //Check to see if items exist in the HashMap

        //Add all items in HashMap to List of ReportObjects
        /*for (String key: nonFilteredReportMap.keySet()) {
            System.out.println("key : " + key);
            reportObjectList.add(nonFilteredReportMap.get(key));
        }*/

        //Do Filter
        for(int i = 0; i < reportObjectList.size(); i++){
            boolean passReportCheck = false;
            if(checkReportStatusData(whatDataWillWeSearch[2], reportStatusData1, reportObjectList.get(i).status, 0))
                passReportCheck = true;

            if(checkReportStatusData(whatDataWillWeSearch[3], reportStatusData2, reportObjectList.get(i).status, 1))
                passReportCheck = true;

            if(checkReportStatusData(whatDataWillWeSearch[4], reportStatusData3, reportObjectList.get(i).status, 2))
                passReportCheck = true;

            if(checkReportStatusData(whatDataWillWeSearch[5], reportStatusData4, reportObjectList.get(i).status, 3))
                passReportCheck = true;

            if(checkReportStatusData(whatDataWillWeSearch[6], reportStatusData5, reportObjectList.get(i).status, 4))
                passReportCheck = true;

            /*System.out.println("PASSED TEST 1 = " + reportObjectList.get(i).reportId);
            System.out.println("TEST 2 RESULT = " + checkLocationData(whatDataWillWeSearch[7], locationData, reportObjectList.get(i).location));
            System.out.println("TEST 2 Value = " + whatDataWillWeSearch[7]);
            System.out.println("TEST 3 RESULT = " + checkSubmitDate(whatDataWillWeSearch[8], startDateData, reportObjectList.get(i).creationTimestamp, true));
            System.out.println("TEST 4 RESULT = " + checkSubmitDate(whatDataWillWeSearch[9], endDateData, reportObjectList.get(i).creationTimestamp, false));
            System.out.println("TEST 5 RESULT = " + checkMediaAllowed(whatDataWillWeSearch[10], reportObjectList.get(i).media));
            System.out.println("TEST 6 RESULT = " + checkCategoryData(whatDataWillWeSearch[11], categoryData, reportObjectList.get(i).categories));
            System.out.println("TEST 7 RESULT = " + checkAssignedAdminData(whatDataWillWeSearch[12], assignedAdminData, reportObjectList.get(i).assignedTo));*/

            if(!passReportCheck
                    || !checkLocationData(whatDataWillWeSearch[7], locationData, reportObjectList.get(i).location)
                    || !checkSubmitDate(whatDataWillWeSearch[8], startDateData, reportObjectList.get(i).creationTimestamp, true)
                    || !checkSubmitDate(whatDataWillWeSearch[9], endDateData, reportObjectList.get(i).creationTimestamp, false)
                    || !checkMediaAllowed(whatDataWillWeSearch[10], reportObjectList.get(i).media)
                    || !checkCategoryData(whatDataWillWeSearch[11], categoryData, reportObjectList.get(i).categories)
                    || !checkAssignedAdminData(whatDataWillWeSearch[12], assignedAdminData, reportObjectList.get(i).assignedTo))
                continue; // Go to next item. ReportObject failed to pass filter.

            //Passed Filter. Add to new ReportObject List.
            sortedReportObjectList.add(reportObjectList.get(i));
        }
        //TODO Finished Filtering Into sortedReportObjectList. Now Sort List. First Sort by Threat Level. Then Sort by Date.
        sortedReportObjectList = sortReportListByDate(whatDataWillWeSearch[1], sortedReportObjectList);
        return sortedReportObjectList;
    }

    private boolean checkReportStatusData(boolean arr, Boolean data, String reportData, int state){
        if(arr) //unchecked value
            return false;
        else{
            if(data && reportData.equals("Open"))
                return state == 0;
            else if(data && reportData.equals("In Progress"))
                return state == 1;
            else if(data && reportData.equals("Closed"))
                return state == 2;
            else if(data && reportData.equals("Resolved"))
                return state == 3;
            else if(data && reportData.equals("New"))
                return state == 4;
            return false;
        }
    }

    private boolean checkLocationData(boolean arr, String data, String reportData){
        if(!arr)
            return true; // don't need to check. as it is empty.
        else
            return reportData.toLowerCase().contains(data.toLowerCase());
    }

    private boolean checkSubmitDate(boolean arr, Long data, Long reportData, boolean startOrEnd){
        if(!arr) {
            return true; // don't need to check. as it is empty.
        }
        else {
            //Compare Values Based on Start/End Date data vs. Report Submission Date Data
            if (startOrEnd) //Is Start Date
                return data <= reportData;
            else if (!startOrEnd)//Is End Date
                return data >= reportData;
            else
                return false;
        }
    }

    private boolean checkMediaAllowed(boolean arr, String reportData){
        if(!arr)
            return true;
        else
            return reportData.isEmpty();
    }

    private boolean checkCategoryData(boolean arr, String data, List<String> reportData){
        final String[] categoryItems = getResources().getStringArray(R.array.category_item);
        boolean[] checkedCategories = getPreChecked(categoryItems, data);

        if(!arr)
            return true;
        //Check the Items to find matching Categories.
        for (int i = 0; i < categoryItems.length; i++) {
            //Check if Preference items == Report Data Category
            if(reportData.contains(categoryItems[i])){
                //Check if a Value that matches is checked.
                if(checkedCategories[i])
                    return true;
            }
        }
        //Invalid Report. Nothing was checked in preferences. return false;
        return false;
    }

    private boolean checkAssignedAdminData(boolean arr, String data, List<String> reportData){
        //TODO whole thing. ALSO ASK IF ASSIGNED ADMINS SHOULD BE A LIST<STRING> or JUST STRING???????????
        return true;
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
                if(checkSubmitDate(true,sortedReportList.get(j).creationTimestamp, reportList.get(i).creationTimestamp,arr)) {
                    sortedReportList.add(j, reportList.get(i));
                    set = 1;
                    break;
                }
            }
            if(set == 0)
                sortedReportList.add(size, reportList.get(i));

            size = size + 1;
            set = 0;
        }
        return sortedReportList;
    }
}