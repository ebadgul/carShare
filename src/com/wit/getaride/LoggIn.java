package com.wit.getaride;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoggIn extends Activity {
	EditText un;
	EditText pw;
	public static Activity alogin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logg_in);
	
		un = (EditText)findViewById(R.id.userName);
		pw = (EditText)findViewById(R.id.password);
	 
	    alogin = this;
	}
	
	public void toSignup(View v){
		startActivity (new Intent(getApplicationContext(), Signup.class));
		
	}

	
	public void loggin(View v){
		
		ParseUser.logInInBackground(un.getText().toString(), pw.getText().toString(), new LogInCallback() {
			  public void done(ParseUser user, ParseException e) {
			    if (user != null) {
			      Log.v("signin", "Signin sucessful");
			      	GetArideApp app = new GetArideApp();
					app.thisUser= app.toUser(ParseUser.getCurrentUser());
					app.currentParseUser = ParseUser.getCurrentUser();
			      startActivity (new Intent(getApplicationContext(), MainActivity.class));
			      finish();
			    } else {
			      // Signup failed. Look at the ParseException to see what happened.
			    	 Log.v("signin", "Signin faild");
			    	 Toast.makeText(getBaseContext(), "Incorrect Username or Password.",
			  			   Toast.LENGTH_LONG).show();
			    }
			  }
			});
		
		
	}
	
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.logg_in, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
