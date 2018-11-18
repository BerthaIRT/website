package com.universityofalabama.cs495f2018.berthaIRT.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.universityofalabama.cs495f2018.berthaIRT.BerthaNet;
import com.universityofalabama.cs495f2018.berthaIRT.Client;
import com.universityofalabama.cs495f2018.berthaIRT.Log;
import com.universityofalabama.cs495f2018.berthaIRT.R;
import com.universityofalabama.cs495f2018.berthaIRT.Report;
import com.universityofalabama.cs495f2018.berthaIRT.adapter.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

class LinearLayoutManagerWrapper extends LinearLayoutManager {

    public LinearLayoutManagerWrapper(Context context) {
        super(context);
    }

    public LinearLayoutManagerWrapper(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public LinearLayoutManagerWrapper(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }
}

public class MessagesFragment extends Fragment {

    static JsonObject reportToRefresh;
    private EditText editMessageText;
    private MessageAdapter adapter;
    private RecyclerView rv;
    List<Log> messageList = Client.activeReport.getMessages();

    ImageButton msgSendButton;
    static AsyncTask<Void, Void, Void> makeTask(Context ctx, BerthaNet.NetSendInterface i){
        return new AsyncTask<Void, Void, Void>()  {
            @Override
            protected Void doInBackground(Void... voids) {
                boolean test = true;
                while(test){
                    System.out.println("HI");
                    Client.net.netSend(ctx, "/report/refresh", reportToRefresh.toString(), r->{
                        if(!r.equals("false")) return;
                        Client.lastUpdated = new Long(r);
                        Client.net.getGroupReports(ctx, i::onResult);
                    });
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
                return null;
            }
        };
    }

    public MessagesFragment(){
        reportToRefresh = new JsonObject();
        reportToRefresh.addProperty("groupID", Client.userGroup.getGroupID());
        reportToRefresh.addProperty("reportID", Client.activeReport.getReportID());
        reportToRefresh.addProperty("lastUpdated", new Long(0));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater flater, ViewGroup tainer, Bundle savedInstanceState){

        AsyncTask task = makeTask(getContext(), x->{
            triggerRefreshActiveReport();
        });

        task.execute(null, null, null); //no idea wtf this is

        View v = flater.inflate(R.layout.fragment_messages, tainer, false);
        rv = v.findViewById(R.id.chat_recycler_view);

        MessageAdapter.RecyclerViewClickListener listener = (view, position) -> {
//            if(messageList.get(position).sendingError) {
//                resendMessage(position);
//            }
        };

        adapter = new MessageAdapter(getContext(), messageList,listener);
        rv.setLayoutManager(new LinearLayoutManagerWrapper(getContext()));
        rv.setAdapter(adapter);

        editMessageText = v.findViewById(R.id.input_chat_message);

        msgSendButton = v.findViewById(R.id.button_chat_send);
        msgSendButton.setOnClickListener(view -> sendMessage());

        editMessageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editMessageText.getText().length() > 0)
                    msgSendButton.setAlpha(1.0f);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        return v;
    }

    private void triggerRefreshActiveReport() {
        Client.activeReport = Client.reportMap.get(Client.activeReport.getReportID());
        populateMessage(Client.activeReport.getMessages());
    }

    @Override
    public void onResume(){
        super.onResume();
        //populateMessage(Client.activeReport.getMessages());
    }

    private void populateMessage(List<Log> m) {
        adapter.notifyItemRangeRemoved(0, messageList.size());
        if(m.size() == 0) {
            messageList.clear();
            messageList.addAll(m);
            adapter.notifyDataSetChanged();
        }
        else{
            messageList.add(m.get(m.size()-1)); //check
            adapter.notifyItemInserted(messageList.size());
            rv.smoothScrollToPosition(messageList.size() - 1);
        }
    }


    private void sendMessage() {
        String msgContent = editMessageText.getText().toString();
        if (!TextUtils.isEmpty(msgContent)) {
            Log m = new Log();
            m.sender = Client.currentUserName;
            m.tStamp = System.currentTimeMillis();
            m.logText = msgContent;
            List<Log> tempList = new ArrayList<>(messageList);
            tempList.add(m);
            Client.activeReport.setMessages(tempList);

            Client.net.secureSend(getContext(), "/report/update", Client.net.gson.toJson(Client.activeReport), r->{
                Client.activeReport = Client.net.gson.fromJson(r, Report.class);
                populateMessage(Client.activeReport.getMessages());
            });
/*            //If there was a problem updating the report then set the error message
            if(!//Client.updateReportMap()) {
                message.sendingError = true;
                messageList.set(messageList.size() - 1, message);
                adapter.notifyDataSetChanged();
            }*/

//            //makes the last sent items time visible
//            for(int i = messageList.size() - 2; i >= 0; i --) {
//                //gets that item
//                Log temp = messageList.get(i);
//                //make sure it belongs to the currentUser
//                if(temp.sender.equals(Client.currentUserName)) {
//                    messageList.set(i, temp);
//                    adapter.notifyDataSetChanged();
//                    break;
//                }
//            }

            //replies back with the same for now
            //Message msgDto1 = new Message(msgContent,"31321");
            //messageList.add(msgDto1);

            //temp if until the messages are sent
            if(messageList.size() > 0) {
                adapter.notifyItemInserted(messageList.size() - 1);
                rv.smoothScrollToPosition(messageList.size() - 1);
            }
            editMessageText.setText("");
            msgSendButton.setAlpha(0.4f);
        }
    }

//    public void resendMessage(int position) {
//        Toast.makeText(getActivity(),"RESEND", Toast.LENGTH_SHORT).show();
//
//        Log message = messageList.get(position);
//        //message.sendingError = false;
//
//        //get the current report Object and add the new message
//        Client.activeReport.messages.add(message);
//
//        //If there was a problem updating the report then set the error message back
//        /*if(!//Client.updateReportMap(Client.activeReport))
//            message.sendingError = true;*/
//
//        messageList.set(position, message);
//        adapter.notifyDataSetChanged();
//    }

//    public void recieveMessage() {
//        //TODO Scott look at
//        Client.net.secureSend("message/get", null, (r)->{
//            JsonObject jay = Client.net.jp.parse(r).getAsJsonObject();
//            for(Map.Entry<String, JsonElement> e : jay.entrySet())
//                messageList.add(Client.net.gson.fromJson(e.getValue().getAsString(), Log.class));
//        });
//    }


}
