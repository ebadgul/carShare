package com.wit.getaride;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class Signup extends Activity {
	
	private EditText et_name;
	private EditText et_userName;
	private EditText et_password;
	private EditText et_rePassword;
	private EditText et_phone;
	
	private String username;
	private String name;
	private String password;
	private String rePassword;
	private String phone;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		
		
		et_name = (EditText)findViewById(R.id.name);
		et_userName = (EditText)findViewById(R.id.userName);
		et_password = (EditText)findViewById(R.id.et_password);
		et_rePassword = (EditText)findViewById(R.id.re_password);
		et_phone = (EditText)findViewById(R.id.phone_number);
		
		
		
		
		et_rePassword.setOnFocusChangeListener(new OnFocusChangeListener(){

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					password = et_password.getText().toString();
					rePassword = et_rePassword.getText().toString();
					if(!password.equals(rePassword)){
						Toast.makeText(getBaseContext(), "Passwords do not match",
					  			   Toast.LENGTH_LONG).show();
						et_password.setBackgroundColor(Color.parseColor("#40F79F81"));
						et_rePassword.setBackgroundColor(Color.parseColor("#40F79F81"));
					}
					else{
						et_password.setBackgroundColor(Color.parseColor("#40E5E5FF"));
						et_rePassword.setBackgroundColor(Color.parseColor("#40E5E5FF"));
						
					}
				}
				
			}
		});
		
		
		
		
		
	}
	
	public void signup(View v){
		
		name = et_name.getText().toString();
		username = et_userName.getText().toString();
		password = et_password.getText().toString();
		rePassword = et_rePassword.getText().toString();
		phone = et_phone.getText().toString();
		
		if(username.equals("")||name.equals("")||password.equals("")
				||rePassword.equals("")||phone.equals("")){
			
			Toast.makeText(getBaseContext(), "All fields must be completed",
		  			   Toast.LENGTH_LONG).show();
			return;
		}
				
		alreadyExist(username);
		
		ParseUser user = new ParseUser();
	    user.setUsername(username);
	    user.setPassword(password);
	    user.put("radius", "0.02");
		user.put("name", et_name.getText().toString());
		user.put("phone", et_phone.getText().toString());
		user.put("hasCar", false);
		user.signUpInBackground(new SignUpCallback(){
	    	
	    	public void done(ParseException e){
	    		if(e==null){
	    			
	    			Log.v("Successful", "sign up");
	    			GetArideApp app = new GetArideApp();
					app.thisUser= app.toUser(ParseUser.getCurrentUser());
					app.currentParseUser = ParseUser.getCurrentUser();
	    			startActivity (new Intent(getApplicationContext(), MainActivity.class));
	    			finish();
	    			LoggIn.alogin.finish();
	    		}
	    		else{
	    			
	    			Toast.makeText(getBaseContext(), "Sign up failed",
				  			   Toast.LENGTH_LONG).show();
	    		}
	    		
	    	}
	    });
		
	}
	
	
	public void alreadyExist(String username){
		
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("username", username);
		query.findInBackground(new FindCallback<ParseUser>() {
			
		  public void done(List<ParseUser> objects, ParseException e) {
		    if (e == null) {
		        if(objects.size()>0){
		        	Toast.makeText(getBaseContext(), "Username already exist",
				  			   Toast.LENGTH_LONG).show();
		        	return;
		        }
		      
		    } else {
		        
		    	Toast.makeText(getBaseContext(), "Problem connecting to database",
			  			   Toast.LENGTH_LONG).show();
		    }
		  }
		});
		
		
		
	}


}
