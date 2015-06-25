package com.tuandn.connectfb;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.android.imageloader.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.FriendListAdapter;
import Model.Friend;

/**
 * Created by Anh Trung on 6/24/2015.
 */
public class DisplayFriendListActivity extends ListActivity {

    private static final String FriendLimit = "250";

    private ArrayList<Friend> friendList;
    private FriendListAdapter adapter;
    private ListView mListView;
    private boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Display Friend List
        displayFriendList();
    }

    private void displayFriendList() {
        //Read Friend Data
        String url = "/me/taggable_friends";
        getFriends(url);
    }

    public void getFriends(String url) {
        Bundle bundle = new Bundle();
        bundle.putString("limit",FriendLimit);
        GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                url,
                bundle,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        String dataResponse = response.toString();
                        dataResponse = cutString(dataResponse);
                        try {
                            friendList = new ArrayList<Friend>();
                            JSONObject jsonObject2 = new JSONObject(dataResponse);
                            JSONArray user_data = jsonObject2.getJSONArray("data");
                            for (int i = 0; i < user_data.length(); i++) {
                                JSONObject jsonObject = user_data.getJSONObject(i);
                                //Get name of friend
                                String name = jsonObject.getString("name");
                                JSONObject picture = jsonObject.getJSONObject("picture");
                                JSONObject picture_data = picture.getJSONObject("data");
                                //Get url of friend's image
                                String image = picture_data.getString("url");
                                Friend f = new Friend();
                                f.setName(name);
                                f.setImage(image);
                                friendList.add(f);
                            }
                        } catch (Exception e) {
                        }
                        setListAdapter(new FriendListAdapter(DisplayFriendListActivity.this, friendList));
                    }
                }

        ).executeAsync();

//        GraphRequestBatch batch = new GraphRequestBatch(new GraphRequest(AccessToken.getCurrentAccessToken(),url,null,null))
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