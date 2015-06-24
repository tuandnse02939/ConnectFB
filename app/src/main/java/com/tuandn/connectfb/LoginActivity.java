package com.tuandn.connectfb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class LoginActivity extends Activity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.login_screen);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends","publish_actions","user_posts","me/feed");
//        loginButton.setPublishPermissions("publish_actions","basic_info");

//        Check if another already logged in
        if(AccessToken.getCurrentAccessToken() != null){
//            Start Main Activity
            Intent i = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(i);
            finish();

        }
        else {
//             Callback registration
            callbackManager = CallbackManager.Factory.create();
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    accessToken = loginResult.getAccessToken();
                    Profile profile = Profile.getCurrentProfile();
//                    Toast.makeText(getApplicationContext(), profile.getName(), Toast.LENGTH_LONG).show();

                    Context context = getApplicationContext();
                    mSharedPreferences = context.getSharedPreferences("",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString(getString(R.string.user_name), profile.getName());
                    editor.commit();

                    Intent i = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();

                }

                @Override
                public void onCancel() {
                    Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(FacebookException exception) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}