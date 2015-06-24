package com.tuandn.connectfb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

/**
 * Created by Anh Trung on 6/22/2015.
 */
public class MainActivity extends Activity {
    private LoginButton loginButton;
    private Button post;
    private SharedPreferences mSharedPreferences;
    private EditText username;
    private ShareDialog shareDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        shareDialog = new ShareDialog(MainActivity.this);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        post = (Button) findViewById(R.id.post);

        displayUsername();
    }

    private void displayUsername() {
        Context context = getApplicationContext();
        mSharedPreferences = context.getSharedPreferences("",Context.MODE_PRIVATE);
        String defaultValue = getResources().getString(R.string.user_name);
        String username = mSharedPreferences.getString(getString(R.string.user_name), defaultValue);

        TextView welcome = (TextView) findViewById(R.id.user_name);
        welcome.setText("Hello, " + username);
    }

    public void postOnWall(View v){

        EditText status = (EditText) findViewById(R.id.et_status);
        String status_content = status.getText().toString();

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("")
                    .setContentDescription("")
                    .setContentUrl(null)
                    .build();

            shareDialog.show(linkContent);
        }

    }

    public void ShowFriendList(View v){
        Intent i = new Intent(MainActivity.this,DisplayFriendListActivity.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

