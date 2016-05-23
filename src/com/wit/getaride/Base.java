package com.wit.getaride;

import com.parse.ParseUser;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;


public class Base extends FragmentActivity{

	
	public GetArideApp app;
	
	 @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    app = (GetArideApp) getApplication();
	    
	  
	   
	    
	  }
	 
	 
	 @Override
	  protected void onDestroy() {
	    super.onDestroy();
	    
	 }
	 
	 
	 @Override
		public boolean onCreateOptionsMenu(Menu menu) {
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}
	 
	 @Override
	 public boolean onPrepareOptionsMenu(Menu menu){
		 super.onPrepareOptionsMenu(menu);
		 MenuItem home = menu.findItem(R.id.menuHome);
	      MenuItem dest = menu.findItem(R.id.menuDest);
	      MenuItem svdest = menu.findItem(R.id.menuSvDest);
	      MenuItem logout = menu.findItem(R.id.logout);
	      
	      if(this instanceof MainActivity){
	    	  
	    	  home.setEnabled(false);
	    	  dest.setEnabled(false);
	    	  svdest.setEnabled(false);
	      }
//	      if(this instanceof SetDestination){
//	    	  dest.setEnabled(false);
//	      }
	      
	      
		 return true;
	 }
	 
	 public void homeClicked(MenuItem item){
		 
		 startActivity(new Intent(this, MainActivity.class));
	 }
	 
	 public void destClicked(MenuItem item){
		 
//		 startActivity(new Intent(this, SetDestination.class));
	 }
	 public void savedDests(MenuItem item){
		 
		// startActivity (new Intent(this, SavedDestinations.class));
	 }
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			
			int id = item.getItemId();
			if (id == R.id.action_settings) {
				return true;
			}
			return super.onOptionsItemSelected(item);
		}
		
	
	public void openProfile(MenuItem item){
		
		//startActivity (new Intent(this, Profile.class));
		
	}
	
	
	
	//checkGPS checks if gps is on or not, if not show a message with link to phones settings.
	public void checkGPS(){
		LocationManager manager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		boolean enabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if(!enabled) {
		
		  LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
			final View popupMsg = inflater.inflate(R.layout.popup, (ViewGroup)findViewById(R.id.popup_element));
			final PopupWindow window = new PopupWindow(popupMsg, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
			
			Button settings = (Button)popupMsg.findViewById(R.id.btnSettings);
			settings.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					
					window.dismiss();
					Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			    	  startActivity(intent);
					
					
				}
			});
			
			new Handler().postDelayed(new Runnable(){

			    public void run() {
			    	window.showAtLocation(popupMsg, Gravity.CENTER, 0, 0);
			    }

			}, 100L);
		}
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	}
	
	
}

