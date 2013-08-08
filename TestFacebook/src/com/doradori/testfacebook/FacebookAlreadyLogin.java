package com.doradori.testfacebook;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

public class FacebookAlreadyLogin extends Activity {

	private TextView userInfoTextView;
	String userInfo = null;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facebook_already_login);
		
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		userInfo = intent.getStringExtra("userInfo");
		

		
		final LoginButton fb_btn = (LoginButton)findViewById(R.id.fbButton2);
		fb_btn.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
			
			@Override
			public void onUserInfoFetched(GraphUser user) {
				// TODO Auto-generated method stub
			}
		});
		
		userInfoTextView = (TextView)findViewById(R.id.userInfoTextView);
		userInfoTextView.setVisibility(View.VISIBLE);
		Log.d("userInfo:", userInfo);
		if (!userInfo.isEmpty()) {
			userInfoTextView.setText(userInfo);
		}
	}
	
	
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
		
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (session.isOpened()) {

			
		} else if (session.isClosed()) {
			userInfoTextView.setVisibility(View.INVISIBLE);
			// Session Closed, return to MainActivity
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(intent);
			finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

}
