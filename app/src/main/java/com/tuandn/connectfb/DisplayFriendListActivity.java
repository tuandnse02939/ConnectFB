package com.tuandn.connectfb;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.pm.PackageInstaller;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
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
        //Read Friend Data
        String url = "/me/taggable_friends";
        getFriends(url);
    }

    public void getFriends(String url) {

        GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/taggable_friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        String dataResponse = response.toString();
                        dataResponse = cutString(dataResponse);
                        try {
                            friendList = new ArrayList<Friend>();
                            JSONObject jsonObject2 = new JSONObject(dataResponse);
                            JSONArray jsonArray = jsonObject2.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String name = jsonObject.getString("name");
                                JSONObject picture = jsonObject.getJSONObject("picture");
                                JSONObject jsonObject3 = picture.getJSONObject("data");
                                String image = jsonObject3.getString("url");
                                Friend f = new Friend();
                                f.setName(name);
                                f.setImage(image);
                                friendList.add(f);
                            }


                        } catch (Exception e) {

                        }
                        setListAdapter(new FriendListAdapter(getApplicationContext(), friendList));
                    }
                }

        ).executeAsync();

    }

    private String cutString(String dataResponse) {
        int check = 0;
        for (int i = 0; i < dataResponse.length(); i++) {
            if (dataResponse.charAt(i) == '{')
            {
                check++;
                if(check==2){
                    dataResponse = dataResponse.substring(i,dataResponse.length()-1);
                    return dataResponse;
                }
            }
        }
        return null;
    }
}