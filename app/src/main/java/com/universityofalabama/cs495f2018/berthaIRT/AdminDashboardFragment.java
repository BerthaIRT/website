package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.UpdateAttributesHandler;

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


        view.findViewById(R.id.dashboard_button_editemblem).setOnClickListener(v1 -> actionEditEmblem());

        view.findViewById(R.id.dashboard_button_registration).setOnClickListener(v1 -> actionChangeRegistration());

        view.findViewById(R.id.dashboard_button_editmyname).setOnClickListener(v1 -> actionEditAdminName());

        view.findViewById(R.id.dashboard_button_removeadmin).setOnClickListener(v1 -> actionRemoveAdmin());

        return view;
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

    public void actionEditAdminName() {
        dashboardDialog = Util.getInputDialog(getContext(),"Your Full Name", null, Client.currentUserName,"Update", x->{
            actionUpdateAttribute("name", x);
        });
        dashboardDialog.show();
    }

    private void actionChangeInstitutionName(String s) {
        //TODO change on server
        Toast.makeText(getActivity(),"Inst name " + s, Toast.LENGTH_SHORT).show();
    }

    private void actionEditEmblem() {
        //TODO change on server
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

    private void actionResetPassword() {
        Client.net.secureSend("admin/resetpassword", null, (r)-> actionLogOut());
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

    private void actionInviteNewAdmin(String s) {
        Client.net.secureSend("admin/inviteadmin", s, (r)-> Util.showOkDialog(getActivity(), "Success",
                "An email has been sent to " + s + ".  The new administrator will log in with the supplied credentials.", null));
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

    }

    private void finishRemoveAdmin(List<String> s) {
        Toast.makeText(getActivity(),"t " + s, Toast.LENGTH_SHORT).show();
    }
}
