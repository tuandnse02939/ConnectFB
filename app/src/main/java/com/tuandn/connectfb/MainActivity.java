package com.tuandn.connectfb;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import Utils.Utils;

/**
 * Created by Anh Trung on 6/22/2015.
 */
public class MainActivity extends Activity {
    private Button post;
    private SharedPreferences mSharedPreferences;
    private EditText username;
    private ShareDialog shareDialog;
    private LoginManager loginManager;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        shareDialog = new ShareDialog(MainActivity.this);
        mContext = MainActivity.this;

        post = (Button) findViewById(R.id.post);

        displayUsername();
    }

    private void displayUsername() {
        Profile profile = Profile.getCurrentProfile();
        if(profile == null){
            Toast.makeText(getApplication(),"Can't load Profile",Toast.LENGTH_LONG).show();
        }
        else {
            TextView welcome = (TextView) findViewById(R.id.user_name);
            welcome.setText("Hello, " + profile.getName());
        }
    }

    public void logout(View v){
        loginManager = LoginManager.getInstance();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getString(R.string.logout));
        builder.setMessage(mContext.getString(R.string.logout_message));
        builder.setPositiveButton(mContext.getString(R.string.ok),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        loginManager.logOut();
                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
        builder.setCancelable(true);
        builder.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        builder.show();

    }

    public void postOnWall(View v){
        //Check if you are connecting to Internet
        if(Utils.isConnectingToInternet(getApplicationContext())){
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle("")
                        .setContentDescription("")
                        .setContentUrl(null)
                        .build();

                shareDialog.show(linkContent);
            }
        }
        else {
            Toast.makeText(getApplicationContext(),"You are not connecting to the Internet",Toast.LENGTH_LONG).show();
        }
    }

    public void ShowFriendList(View v){
        if(Utils.isConnectingToInternet(getApplicationContext())){
            Intent i = new Intent(MainActivity.this,DisplayFriendListActivity.class);
            startActivity(i);
        }
        else {
            Toast.makeText(getApplicationContext(),"You are not connecting to the Internet",Toast.LENGTH_LONG).show();
        }
    }
}

