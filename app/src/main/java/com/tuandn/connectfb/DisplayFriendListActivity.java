package com.tuandn.connectfb;

import android.app.Activity;
import android.app.ListActivity;
import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.FriendListAdapter;
import Model.Friend;
import Model.FriendWrapper;

/**
 * Created by Anh Trung on 6/24/2015.
 */
public class DisplayFriendListActivity extends ListActivity {

    private ArrayList<Friend> friendList;
    private FriendListAdapter adapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.friendlist_activity);


        displayFriendList();
    }

    private void displayFriendList() {
        friendList = new ArrayList<Friend>();

        //Read Friend Data
        String url = "/me/taggable_friends";
        getFriends(url);

        //Fake data
        Friend f = new Friend("Tuan DN","https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xfa1/v/t1.0-1/p50x50/11011055_10153055682684098_2654723766583976976_n.jpg?oh=173d1cf50d33cd1325acd9d8a0899e85&oe=56236021&__gda__=1441240232_cac831540a532d86bf9a5f44bc1f43a9");
        friendList.add(f);
        Friend f2 = new Friend("Linh DQ","https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xfa1/v/t1.0-1/c0.0.50.50/p50x50/644393_10205781223790610_37187257021574747_n.jpg?oh=1f068b2fca16f2bf00932a1ed471d63e&oe=562A8577&__gda__=1441805260_bcfc02007350acf45c06a28c274be5c4");
        friendList.add(f2);


        setListAdapter(new FriendListAdapter(this, friendList));
    }

    private ArrayList<Friend> getFriendList() {


        return null;
    }

public void getFriends(String url){
    GraphRequestBatch batch = new GraphRequestBatch(new GraphRequest(
            accessToken, url, null, null, new GraphRequest.Callback() {
        @Override
        public void onCompleted(GraphResponse response) {
            String strResponse = response.getRawResponse();
            L.e("getRawResponse : " + strResponse);

            try {
                Friend dataObject = DataParser.parseFacebookOutbox(strResponse);
                if (dataObject != null) {
                    ArrayList<EnOutboxData> listOutboxData = dataObject.getData();
                    if (listOutboxData != null && listOutboxData.size() > 0) {
                        int nSize = listOutboxData.size();
                        L.e("listOutboxData : " + nSize);

                        for (int i = 0; i < nSize; i++) {
                            L.e("_**+*++*++*+*++*+ ::::: " + i);
                            EnOutboxData outboxData = listOutboxData.get(i);
                            if (outboxData != null) {
                                if (isSentFromMe(outboxData.getComments(), mUserID)) {

                                    // check time
                                    if (true) {
                                        if (null == mListToPeople) {
                                            mListToPeople = new ArrayList<EnTOData>();
                                        }
                                        mListToPeople.clear();
                                        mListToPeople.addAll(outboxData.getTo().getData());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                String msg = (e==null)?"ParseJson failed!":e.getMessage();
                Log.e("Exception", msg);
            }
        }
    }));

    batch.addCallback(new GraphRequestBatch.Callback() {
        @Override
        public void onBatchCompleted(GraphRequestBatch graphRequests) {
            // Create Care team member
            new AddMemberFacebookTask(mContext, mListToPeople).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
        }
    });
    batch.executeAsync();
}

}
