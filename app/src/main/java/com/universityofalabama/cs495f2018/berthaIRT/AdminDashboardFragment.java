package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.UpdateAttributesHandler;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AdminDashboardFragment extends Fragment {
    View view;
    AlertDialog dashboardDialog;

    public AdminDashboardFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater flater, ViewGroup tainer, Bundle savedInstanceState){
        view = flater.inflate(R.layout.fragment_admin_dashboard, tainer, false);

        view.findViewById(R.id.dashboard_button_metrics).setOnClickListener(v1 ->
                startActivity(new Intent(getActivity(), MetricsActivity.class)));

        view.findViewById(R.id.dashboard_button_editinstitutionname).setOnClickListener(v1 ->
                Util.showInputDialog(getContext(),"Your Institution Name", null, Client.currentUserGroupID,"Update", x-> actionUpdateAttribute("institution", x)) );

        view.findViewById(R.id.dashboard_button_editemblem).setOnClickListener(v1 -> actionEditEmblem());

        view.findViewById(R.id.dashboard_button_registration).setOnClickListener(v1 -> actionChangeRegistration());

        view.findViewById(R.id.dashboard_button_editmyname).setOnClickListener(v1 ->
                Util.showInputDialog(getContext(),"Your Full Name", null, Client.currentUserName,"Update", x-> actionUpdateAttribute("name", x)) );

        view.findViewById(R.id.dashboard_button_resetpassword).setOnClickListener(v1 ->
                Util.showYesNoDialog(getActivity(), "Are you sure?", "A temporary code for you to reset your password will be sent to your email and you will be logged out.",
                        "Reset", "Cancel", this::actionResetPassword, null));

        view.findViewById(R.id.dashboard_button_logout).setOnClickListener(v1 ->
                Util.showYesNoDialog(getActivity(),"Are you sure you want to Logout?", "",
                        "Logout", "Cancel", this::actionLogOut, null));


        //TEMP to make up admins
        List<String> admins = new ArrayList<>();
        admins.add("John Frank");
        admins.add("Fred Hurts");
        view.findViewById(R.id.dashboard_button_removeadmin).setOnClickListener(v1 ->
                Util.showAddRemoveDialog(getActivity(), /*TODO get list of admins in group*/admins, this::actionRemoveAdmin) );

        updateInfoCard(view.findViewById(R.id.dashboard_alt_name), view.findViewById(R.id.dashboard_alt_institution), view.findViewById(R.id.dashboard_alt_accesscode));

        return view;
    }

    public void updateInfoCard(TextView tvName, TextView tvInstitution, TextView tvGroupID){
        Client.net.netSend(getContext(), "/group/lookup", Client.currentUserGroupID, r->{
            JsonObject jay = Client.net.jp.parse(r).getAsJsonObject();
            tvName.setText(Client.currentUserName);
            tvInstitution.setText(jay.get("groupName").getAsString());
            tvGroupID.setText(Client.currentUserGroupID);
        });
    }

    public void actionUpdateAttribute(String attribute, String value){
        CognitoUserAttributes attribs = new CognitoUserAttributes();
        attribs.addAttribute(attribute, value);
        Client.net.pool.getCurrentUser().updateAttributesInBackground(attribs, new UpdateAttributesHandler() {
            @Override
            public void onSuccess(List<CognitoUserCodeDeliveryDetails> attributesVerificationList) {
                dashboardDialog.dismiss();
                Toast.makeText(getContext(), "Update successful.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(getContext(), "Unable to update attribute.", Toast.LENGTH_SHORT).show();
            }
        });
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
        /*Client.net.secureSend("admin/toggleregistration", null, (r)->{
            if(r.equals("Closed"))
                ((TextView) view.findViewById(R.id.dashboard_button_registration)).setText("Open Registration");
            else
                ((TextView) view.findViewById(R.id.dashboard_button_registration)).setText("Close Registration");

        });*/
    }


    private void actionChangeInstitutionName(String s) {
        //TODO change on server
        Toast.makeText(getActivity(),"Inst name " + s, Toast.LENGTH_SHORT).show();
    }

    private void actionEditEmblem() {
        //TODO change on server
        Toast.makeText(getActivity(),"Emblem", Toast.LENGTH_SHORT).show();
    }

    private void actionLogOut(){
        SharedPreferences prefs = getActivity().getSharedPreferences("LoginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        //Remove Previous Shared Preferences.
        editor.remove("username");
        editor.remove("password");
        editor.apply();

        startActivity(new Intent(getActivity(), AdminLoginActivity.class));
        getActivity().finish();
    }

    private void actionResetPassword() {

    }

    private void actionRemoveAdmin(List<String> admins) {
        //TODO get Admins from server
        //TODO remove admins that are NOT in the returned list.
    }

    private void finishRemoveAdmin(List<String> s) {
        Toast.makeText(getActivity(),"t " + s, Toast.LENGTH_SHORT).show();
    }
}
