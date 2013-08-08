package com.doradori.testfacebook;

import java.lang.reflect.Array;
import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;


import com.facebook.*;
import com.facebook.model.*;
import com.facebook.widget.LoginButton;

public class MainActivity extends Activity {

    private UiLifecycleHelper uiHelper;
    private String userInfo = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		final LoginButton fb_btn = (LoginButton)findViewById(R.id.fbButton1);
		fb_btn.setReadPermissions(Arrays.asList("user_location", "user_birthday", "user_likes", "email"));

		
		// Check Facebook Session Status from Cache
		Session session = Session.openActiveSessionFromCache(this);
		if (session != null && session.getState().isOpened()) {
			// Facebook Session restored, Move to FacebookAlreadyLogin Activity
			Intent intent = new Intent(getApplicationContext(), FacebookAlreadyLogin.class);
			startActivity(intent);
			finish();
			Log.d("--------------------------------", "Cache FB OK!");
		} else {
			// Do nothing for now
		}
    }
    
    // For Email Address
    private interface MyGraphEmail extends GraphObject {
        // Getter for the Email field
        String getEmail();
    }
    
    // Get User Info
	private String buildUserInfoDisplay(GraphUser user) {
	    StringBuilder userInfo = new StringBuilder("");

	    userInfo.append(String.format("ID: %s\n\n", 
	        user.getId()));
	    
	    // Example: typed access (name)
	    // - no special permissions required
	    userInfo.append(String.format("Name: %s\n\n", 
	        user.getName()));

	    // Example: typed access (birthday)
	    // - requires user_birthday permission
	    userInfo.append(String.format("Birthday: %s\n\n", 
	        user.getBirthday()));

	    // Example: partially typed access, to location field,
	    // name key (location)
	    // - requires user_location permission
	    userInfo.append(String.format("Location: %s\n\n", 
	        user.getLocation().getProperty("name")));
//
//	    // Example: access via property name (locale)
//	    // - no special permissions required
//	    userInfo.append(String.format("Locale: %s\n\n", 
//	        user.getProperty("locale")));

	    // Example: access via key for array (languages) 
	    // - requires user_likes permission
//	    JSONArray languages = (JSONArray)user.getProperty("languages");
//	    if (languages.length() > 0) {
//	        ArrayList<String> languageNames = new ArrayList<String> ();
//	        for (int i=0; i < languages.length(); i++) {
//	            JSONObject language = languages.optJSONObject(i);
//	            // Add the language name to a list. Use JSON
//	            // methods to get access to the name field. 
//	            languageNames.add(language.optString("name"));
//	        }           
//	        userInfo.append(String.format("Languages: %s\n\n", 
//	        languageNames.toString()));
	    
	    userInfo.append(String.format("Email: %s\n\n", user.getProperty("email")));


	    return userInfo.toString();
	}
    
    
    // ----------------------------------------------------------------------------------
    // Facebook Functions
	private Session.StatusCallback callback = new Session.StatusCallback() {
		
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (session.isOpened()) {
			
			// Request user data and show the results
		    Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

		        @Override
		        public void onCompleted(GraphUser user, Response response) {
		            if (user != null) {
		                // Display the parsed user info
		            	userInfo = buildUserInfoDisplay(user);
		    			Intent intent = new Intent(getApplicationContext(), FacebookAlreadyLogin.class);
		    			intent.putExtra("userInfo", userInfo);
		    			startActivity(intent);
		    			finish();
		            }
		        }
		    });
		} 
	}
	
	// Manage LifeCycle
    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();
    }
    
    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);

    }

//    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

}
