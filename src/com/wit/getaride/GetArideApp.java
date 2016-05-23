package com.wit.getaride;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import android.app.ActionBar.LayoutParams;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class GetArideApp extends Application{
	public static User thisUser = new User("Current User", new Location(""), new Location(""),"45468768");
	public static ParseUser currentParseUser;
	public static double searchRadius=0.02;
	private MarkerOptions markerOptions;
	private Marker marker;
	private static List <User> allUsers    = new ArrayList<User>();
	@Override
	  public void onCreate()
	  {
	    super.onCreate();
	    Log.v("MainActivity", "GetAride App Started");
	    
	    
	    Parse.enableLocalDatastore(this);
	    
	    Parse.initialize(this, "OfhVidObwK5CQ0iAmdpJyvgH6ysikqBoulzCAPlF", "LwWP2IPu6qZbjOs511iQq6axSGzIoeEtmFejFp3F");
	    
	    
	   
	    MyPhoneListener phoneListenre = new MyPhoneListener();
		TelephonyManager telephonyManager = 
				(TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(phoneListenre,PhoneStateListener.LISTEN_CALL_STATE);
	 
		
	  
	  }
	

	public void putDp(final ImageView iv, String userNick) throws ParseException{
		
		
		 ParseUser currUser;
		 if(userNick.equals("")){currUser = currentParseUser;}
		 else{
			 ParseQuery<ParseUser> query = ParseUser.getQuery();
				query.whereEqualTo("username", userNick);
				
					List<ParseUser>pusers= query.find();
					currUser=pusers.get(0);
				
		 }
		 
		 
		 if(currUser.get("photo")!=null){
		        ParseFile profile = (ParseFile)currUser.get("photo");
		        profile.getDataInBackground(new GetDataCallback() {
		        	
		          public void done(byte[] data, ParseException e) {
		            if (e == null) {
		              // data has the bytes for the resume
		            	
		            	iv.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
		            	
		            	
		            } else {
		              // something went wrong
		            	Log.v("downloaded img",""+"-"+data.length);
		            }
		          }
		       
		        });
		        }
	    
		
		
	}
	

	
	public String calculateDistance(double lat1,double lat2, double lng1,double lng2) {
        int Radius=6371;//radius of earth in Km         

        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
        Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult= Radius*c;
        double km=valueResult/1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec =  Integer.valueOf(newFormat.format(km*1000));
        double meter=valueResult%1000;
        int  meterInDec= Integer.valueOf(newFormat.format(meter*1000));
        Log.v("Radius Value",""+valueResult+"   KM  "+kmInDec+" Meter   "+meterInDec);

        //Radius * c;
        return meterInDec+" meters";
        
     }
	
	// toUser() converts ParseUser object 	to a User object
		public User toUser(ParseUser puser){
			
			String un= puser.getUsername();
			ParseGeoPoint loc=puser.getParseGeoPoint("location");
			ParseGeoPoint dest=puser.getParseGeoPoint("destination");
			Location l = new Location("");
			Location d = new Location("");
			if(dest!=null && loc!=null){
			l.setLatitude(loc.getLatitude()); l.setLongitude(loc.getLongitude());
			d.setLatitude(dest.getLatitude()); d.setLongitude(dest.getLongitude());
			}
			String phone = puser.getString("phone");
			
			User user = new User(un,l,d , phone);
			user.hasCar = puser.getBoolean("hasCar");
			return user;
			
		}
		
		public void setPoint(String key, double lat, double lng){
			
			ParseUser user = currentParseUser;
			Log.v("current user",user.getUsername());
			ParseGeoPoint loc = new ParseGeoPoint(lat, lng);
			
			user.put(key, loc);
			user.saveInBackground(new SaveCallback(){

				@Override
				public void done(ParseException e) {
					if(e==null){
						Log.v("setDest", "successful");
					}
					else{
						
						Log.v("setDest", "failed :"+e);
					}
					
				}
				});
			
		}
		
		
		
		private class MyPhoneListener extends PhoneStateListener {
			 
			private boolean onCall = false;
	 
			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
	 
				switch (state) {
				case TelephonyManager.CALL_STATE_RINGING:
					// phone ringing...
					Toast.makeText(getApplicationContext(), incomingNumber + " calls you", 
							Toast.LENGTH_LONG).show();
					break;
				
				case TelephonyManager.CALL_STATE_OFFHOOK:
					// one call exists that is dialing, active, or on hold
					Toast.makeText(getApplicationContext(), "on call...", 
							Toast.LENGTH_LONG).show();
					//because user answers the incoming call
					onCall = true;
					break;

				case TelephonyManager.CALL_STATE_IDLE:
					// in initialization of the class and at the end of phone call 
					
					// detect flag from CALL_STATE_OFFHOOK
					if (onCall == true) {
						Toast.makeText(getApplicationContext(), "resuming app", 
								Toast.LENGTH_LONG).show();
	 
						// restart our application
//						Intent restart = getBaseContext().getPackageManager().
//							getLaunchIntentForPackage(getBaseContext().getPackageName());
//						restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//						startActivity(restart);
	 
						onCall = false;
					}
					break;
				default:
					break;
				}
				
			}
		}
	
}
