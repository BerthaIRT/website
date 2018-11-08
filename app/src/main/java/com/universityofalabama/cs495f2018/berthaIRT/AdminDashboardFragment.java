package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardFragment extends Fragment {
    View view;

    public AdminDashboardFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater flater, ViewGroup tainer, Bundle savedInstanceState){
        view = flater.inflate(R.layout.fragment_admin_dashboard, tainer, false);

        view.findViewById(R.id.dashboard_button_metrics).setOnClickListener(v1 ->
                startActivity(new Intent(getActivity(), MetricsActivity.class)));

        view.findViewById(R.id.dashboard_button_editinstitutionname).setOnClickListener(v1 ->
                Util.showInputDialog(getActivity(),"New Institution name ", null, "Change", this::actionChangeInstitutionName));


        view.findViewById(R.id.dashboard_button_editemblem).setOnClickListener(v1 -> actionEditEmblem());

        view.findViewById(R.id.dashboard_button_registration).setOnClickListener(v1 -> actionChangeRegistration());

        view.findViewById(R.id.dashboard_button_editmyname).setOnClickListener(v1 ->
                Util.showInputDialog(getActivity(),"New name ", null, "Change", this::actionChangeName));

        view.findViewById(R.id.dashboard_button_resetpassword).setOnClickListener(v1 -> actionChangePassword());

        view.findViewById(R.id.dashboard_button_logout).setOnClickListener(v1 ->
                Util.showYesNoDialog(getActivity(),"Are you sure you want to Logout?", "",
                "Logout", "Cancel", this::actionLogOut, null));

        view.findViewById(R.id.dashboard_button_inviteadmin).setOnClickListener(v1 -> actionInviteNewAdmin());

        view.findViewById(R.id.dashboard_button_removeadmin).setOnClickListener(v1 -> actionRemoveAdmin());

        return view;
    }

    private void actionChangeInstitutionName(String s) {
        Toast.makeText(getActivity(),"Inst name " + s, Toast.LENGTH_SHORT).show();
    }

    private void actionEditEmblem() {
        Toast.makeText(getActivity(),"Emblem", Toast.LENGTH_SHORT).show();
    }

    //Currently working on
    private void actionChangeRegistration() {
        TextView tvRegistration = view.findViewById(R.id.dashboard_button_registration);
        String message = "You are about to CLOSE your group to new members.  No one may use your institution's access code until you reopen.";
        if(tvRegistration.getText() == "Open Registration") message = "You are about to OPEN your group to new members and your access code will become active.";
        Util.showYesNoDialog(getActivity(),"Changing Registration", message,
                "Confirm", "Cancel", this::toggleRegistration, null);
    }

    private void toggleRegistration() {
        Client.net.secureSend("admin/toggleregistration", null, (r)->{
            if(r.equals("Closed"))
                ((TextView) view.findViewById(R.id.dashboard_button_registration)).setText("Open Registration");
            else
                ((TextView) view.findViewById(R.id.dashboard_button_registration)).setText("Close Registration");

        });
    }

    private void actionChangeName(String s) {
        Toast.makeText(getActivity(),"t " + s, Toast.LENGTH_SHORT).show();
    }

    private void actionChangePassword() {
        Toast.makeText(getActivity(),"Pass", Toast.LENGTH_SHORT).show();
    }

    private void actionLogOut(){
        //TODO delete from shared preferences

        startActivity(new Intent(getActivity(), AdminLoginActivity.class));
        getActivity().finish();
    }

    private void actionInviteNewAdmin() {
        Toast.makeText(getActivity(),"Invite", Toast.LENGTH_SHORT).show();
    }

    private void actionRemoveAdmin() {
        List<String> admins = new ArrayList<>();
        List<Boolean> adminsChecked = new ArrayList<>();
        admins.add("Jake");
        admins.add("Johnathan");
        admins.add("Scott");
        admins.add("Not Jim");
        adminsChecked.add(true);
        adminsChecked.add(true);
        adminsChecked.add(true);
        adminsChecked.add(true);
        Util.showSelectCategoriesDialog(getActivity(), adminsChecked, admins, this::finishRemoveAdmin);
        /*Toast.makeText(getActivity(),"Remove", Toast.LENGTH_SHORT).show();
        List<String> admins = new ArrayList<>();
        List<Boolean> adminsChecked = new ArrayList<>();
*//*        admins.add("Jake");
        admins.add("Johnathan");
        admins.add("Scott");
        admins.add("Not Jim");
        adminsChecked.add(true);
        adminsChecked.add(true);
        adminsChecked.add(true);
        adminsChecked.add(true);*//*
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.checkbox_view_recycler, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AppCompatAlertDialogStyle);
        //AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Cancel", null);
        builder.setNeutralButton("Clear All", null);
        builder.setView(dialoglayout);
        builder.show();
        RecyclerView rvTest = dialoglayout.findViewById(R.id.rec_view);
        CheckBoxAdapter cbAdapter = new CheckBoxAdapter(getActivity(),admins,adminsChecked);
        rvTest.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvTest.setAdapter(cbAdapter);*/
    }

    private void finishRemoveAdmin(List<String> s) {
        Toast.makeText(getActivity(),"t " + s, Toast.LENGTH_SHORT).show();
    }
}
