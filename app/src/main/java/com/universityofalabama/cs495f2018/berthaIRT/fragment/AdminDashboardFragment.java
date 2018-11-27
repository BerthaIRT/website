package com.universityofalabama.cs495f2018.berthaIRT.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.UpdateAttributesHandler;
import com.google.gson.JsonObject;
import com.universityofalabama.cs495f2018.berthaIRT.AdminLoginActivity;
import com.universityofalabama.cs495f2018.berthaIRT.Client;
import com.universityofalabama.cs495f2018.berthaIRT.Interface;
import com.universityofalabama.cs495f2018.berthaIRT.MetricsActivity;
import com.universityofalabama.cs495f2018.berthaIRT.R;
import com.universityofalabama.cs495f2018.berthaIRT.Util;
import com.universityofalabama.cs495f2018.berthaIRT.adapter.AddRemoveAdapter;
import com.universityofalabama.cs495f2018.berthaIRT.dialog.AddRemoveDialog;
import com.universityofalabama.cs495f2018.berthaIRT.dialog.CheckboxDialog;
import com.universityofalabama.cs495f2018.berthaIRT.dialog.InputDialog;
import com.universityofalabama.cs495f2018.berthaIRT.dialog.YesNoDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class AdminDashboardFragment extends Fragment {
    View view;
    AddRemoveDialog d;

    public AdminDashboardFragment(){

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater flater, ViewGroup tainer, Bundle savedInstanceState){
        view = flater.inflate(R.layout.fragment_admin_dashboard, tainer, false);

        view.findViewById(R.id.dashboard_button_metrics).setOnClickListener(v1 ->
                startActivity(new Intent(getActivity(), MetricsActivity.class)));
//
        //not a user attribute
//        view.findViewById(R.id.dashboard_button_editinstitutionname).setOnClickListener(v1 ->
//                Util.showInputDialog(getContext(),"Your Institution Name", null, Client.currentUserGroupID,"Update", x-> actionUpdateAttribute("institution", x)) );

//        view.findViewById(R.id.dashboard_button_editemblem).setOnClickListener(v1 -> actionEditEmblem());
//
        if(Client.userGroupStatus.equals("Closed")) ((TextView) view.findViewById(R.id.dashboard_button_registration)).setText("Open Registration");
        view.findViewById(R.id.dashboard_button_registration).setOnClickListener(v1 -> actionToggleRegistration());

        view.findViewById(R.id.dashboard_button_editmyname).setOnClickListener(v1 ->
                new InputDialog(getContext(),"Your Full Name", Client.userName, x -> actionUpdateAttribute("name", x)).show());

//        view.findViewById(R.id.dashboard_button_resetpassword).setOnClickListener(v1 ->
//                new YesNoDialog(getActivity(), "Are you sure?", "A temporary code for you to reset your password will be sent to your email and you will be logged out.", new Interface.YesNoHandler() {
//                    @Override
//                    public void onYesClicked() { actionResetPassword(); }
//                    @Override
//                    public void onNoClicked() { }
//                }).show());

        view.findViewById(R.id.dashboard_button_logout).setOnClickListener(v1 ->
                new YesNoDialog(getActivity(),"Are you sure you want to Logout?", "", new Interface.YesNoHandler() {
                    @Override
                    public void onYesClicked() { actionLogOut(); }
                    @Override
                    public void onNoClicked() { }
                }).show());

//        view.findViewById(R.id.dashboard_button_addremoveadmin).setOnClickListener(v1 -> actionAddRemoveAdmin());

        ((TextView) view.findViewById(R.id.dashboard_alt_name)).setText(Client.userName);
        ((TextView) view.findViewById(R.id.dashboard_alt_institution)).setText(Client.userGroupName);
        ((TextView) view.findViewById(R.id.dashboard_alt_accesscode)).setText(Client.userGroupID.toString());
        return view;
    }

    public void actionToggleRegistration() {
        Client.net.toggleRegistration(getContext(), (r)->{
            Toast.makeText(getContext(), "Registration set to " + r, Toast.LENGTH_SHORT).show();
            if(r.equals("Closed")) ((TextView) view.findViewById(R.id.dashboard_button_registration)).setText("Open Registration");
            else ((TextView) view.findViewById(R.id.dashboard_button_registration)).setText("Close Registration");
            Client.userGroupStatus = r;
        });
    }

    public void actionUpdateAttribute(String attribute, String value){
        CognitoUserAttributes attribs = new CognitoUserAttributes();
        attribs.addAttribute(attribute, value);
        Client.net.pool.getCurrentUser().updateAttributesInBackground(attribs, new UpdateAttributesHandler() {
            @Override
            public void onSuccess(List<CognitoUserCodeDeliveryDetails> attributesVerificationList) {
                Toast.makeText(getContext(), "Update successful.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(getContext(), "Unable to update attribute.", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    //Currently working on
//    private void actionChangeRegistration() {
//        TextView tvRegistration = view.findViewById(R.id.dashboard_button_registration);
//        String message = "You are about to CLOSE your group to new members.  No one may use your institution's access code until you reopen.";
//        if(tvRegistration.getText() == "Open Registration") message = "You are about to OPEN your group to new members and your access code will become active.";
//        new YesNoDialog(getActivity(),"Changing Registration", message, new Interface.YesNoHandler() {
//            @Override
//            public void onYesClicked() { toggleRegistration(); }
//            @Override
//            public void onNoClicked() { }
//        }).show();
//    }
//
//    private void toggleRegistration() {
//        /*Client.net.secureSend("admin/toggleregistration", null, (r)->{
//            if(r.equals("Closed"))
//                ((TextView) view.findViewById(R.id.dashboard_button_registration)).setText("Open Registration");
//            else
//                ((TextView) view.findViewById(R.id.dashboard_button_registration)).setText("Close Registration");
//
//        });*/
//    }
//
//
//    private void actionChangeInstitutionName(String s) {
//        //TODO change on server
//        Toast.makeText(getActivity(),"Inst name " + s, Toast.LENGTH_SHORT).show();
//    }
//
//    private void actionEditEmblem() {
//        //TODO change on server
//        Toast.makeText(getActivity(),"Emblem", Toast.LENGTH_SHORT).show();
//    }
//
    private void actionLogOut(){
        startActivity(new Intent(getActivity(), AdminLoginActivity.class));
        Objects.requireNonNull(getActivity()).finish();
    }

//    private void actionResetPassword() {
//
//    }

/*    private void actionAddRemoveAdmin() {
        //Get the admins and display dialog
        List<String> admins = new ArrayList<>();
        d = new AddRemoveDialog(getActivity(), Client.userGroup.getAdmins(), this::actionAddAdmin, this::actionRemoveAdmin, null);
        d.show();
        ((EditText) Objects.requireNonNull(d.findViewById(R.id.addremove_input))).setHint("Admin Email");
    }

    private void actionAddAdmin(String admin) {
        new YesNoDialog(getActivity(), "Are you sure? ", "About to add " + admin + " as an Admin?", new Interface.YesNoHandler() {
            @Override
            public void onYesClicked() {
                Client.net.secureSend(getContext(), "/group/join/admin", admin, x->
                        ((AddRemoveAdapter) ((RecyclerView) Objects.requireNonNull(d.findViewById(R.id.addremove_rv))).getAdapter()).addToList(admin));
            }

            @Override
            public void onNoClicked() {
            }
        }).show();
    }

    private void actionRemoveAdmin(String admin) {
        new YesNoDialog(getActivity(),"Are you sure?", "About to remove " + admin + " as an Admin?", new Interface.YesNoHandler() {
            @Override
            public void onYesClicked() {
                Client.net.secureSend(getContext(), "/group/remove/admin", admin,null);
            }

            @Override
            public void onNoClicked() {
                //add that admin back to the list
                ((AddRemoveAdapter) ((RecyclerView) Objects.requireNonNull(d.findViewById(R.id.addremove_rv))).getAdapter()).addToList(admin);
            }
        }).show();
    }*/
}
