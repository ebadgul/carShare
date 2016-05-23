package com.wit.getaride;

import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		Thread thread = new Thread(){
			
			@Override
			public void run(){
				
				try {
					sleep(1000);
					
					ParseUser curUser = ParseUser.getCurrentUser();
					if(curUser != null){
						GetArideApp app = new GetArideApp();
						GetArideApp.thisUser= app.toUser(curUser);
						GetArideApp.currentParseUser=curUser;
						Log.v("current user", app.thisUser.userNick);
						startActivity(new Intent(getApplicationContext(), MainActivity.class));
					}
					else{
					startActivity(new Intent(getApplicationContext(), LoggIn.class));
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		thread.start();
	}
	public void onPause(){
		super.onPause();
		finish();
		
	}
}
