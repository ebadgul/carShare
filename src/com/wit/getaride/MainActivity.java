package com.wit.getaride;

import java.util.ArrayList;

import views.SlidingTabLayout;
import android.app.ActionBar;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseUser;

public class MainActivity extends Base{
	private MapFragment mapFr;
	ActionBar actionBar;
	ViewPager viewPager;
	SlidingTabLayout mSlidingtabs;
	private GoogleMap map;
	private Location currentLocation;
	private Button btnConfirm;
	private LocationManager manager;
	private GoogleApiClient client;
	private MarkerOptions markerOptions;
	private Marker marker;
	public static FragmentManager fragmentManager;
	private String imgDecodableString="";
	private Intent mRequestFileIntent;
    private ParcelFileDescriptor mInputPFD;
	private SharedPreference sp = new SharedPreference();
	public GetArideApp app = (GetArideApp)getApplication();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		checkGPS();
		fragmentManager = getSupportFragmentManager();
		viewPager = (ViewPager)findViewById(R.id.pager);
		viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
		
		mSlidingtabs = (SlidingTabLayout)findViewById(R.id.sliding_tabs);
		mSlidingtabs.setViewPager(viewPager);
		mSlidingtabs.setDistributeEvenly(true);
		
		
		
		if(app.currentParseUser.getString("radius")!=null)
			app.searchRadius = Double.parseDouble(ParseUser.getCurrentUser().getString("radius"));
		
		ArrayList<Destination> dests = sp.loadDestinations(this);
		
		if(dests==null){
			sp.addDestination(this, new Destination("Waterford City", 52.259538, -7.105995));
		}
		
	}
	


public void onConfirmPressed(View view){
	
	Double lat = app.thisUser.dest.getLongitude();
	if(lat == 0){
		
		Toast.makeText(this, "Please select your destination",
				   Toast.LENGTH_LONG).show();
		return;
	}
	
	startActivity (new Intent(this, RideList.class));

}

public void onSavePressed(View view){
	
	EditText destName = (EditText)findViewById(R.id.etDestName);
	double lat = app.thisUser.dest.getLatitude();
	double lng = app.thisUser.dest.getLongitude();
	String name = destName.getText().toString();
	
	if(name.equals("")||lat==0){
		Toast.makeText(this, "Please choose a destination and enter a name",
				   Toast.LENGTH_LONG).show();
		return;
	}
	Log.v("", String.valueOf(lat));
	Destination destination = new Destination(name, lat, lng);
	sp.addDestination(this, destination);
	destName.setText("");
	Toast.makeText(this, "The Destination has been saved",
			   Toast.LENGTH_LONG).show();
}



public void logout(View v){
	
	ParseUser.logOut();
	
	startActivity (new Intent(this, LoggIn.class));
	finish();
}



	////////////////==================

class MyAdapter extends FragmentPagerAdapter{

	String[] tabText = getResources().getStringArray(R.array.tabTitles);
	public MyAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		
		Fragment fr =null;
		if(arg0 == 0){
			fr = new FrSetDestination();
		}
		if(arg0 == 1){
			fr = new FrSettings();
		}
		if(arg0 == 2){
			fr = new FrSavedDestinations();
		}
		
		
		
		
		return fr;
	}
	
	@Override
	public CharSequence getPageTitle(int position){
		
		return tabText[position];
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}
	
	
	
}

	

}

