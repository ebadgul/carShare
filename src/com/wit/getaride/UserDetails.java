package com.wit.getaride;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UserDetails extends Base {

	private MapFragment mapFr;
	private GoogleMap map;
	private Location currentLocation;
	private LocationManager manager;
	private GoogleApiClient client;
	private MarkerOptions markerOptions;
	private Marker marker;
	public static User selectedUser;
	private Button btn_call;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_details);
		
		mapFr = (MapFragment) getFragmentManager().findFragmentById(R.id.map2);
		map = (mapFr).getMap();
		map.setMyLocationEnabled(true);	
		
		btn_call = (Button)findViewById(R.id.button1);
		
		
		btn_call.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				if(selectedUser.contact==null){
					Toast.makeText(getApplicationContext(),"User's contact is not available",
							Toast.LENGTH_LONG).show();
					return;
				}
				try{
					String uri = selectedUser.contact;
					 Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+uri));
					 startActivity(intent);
					 
					 
				}catch(Exception e) {
					Toast.makeText(getApplicationContext(),"Your call has failed...",
						Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			}	
			
		});
	
		
		if (marker != null) {
			marker.remove();
		}
	
		
		double selectedUserLat = selectedUser.loc.getLatitude();
		double selectedUserLng =selectedUser.loc.getLongitude();
		double thisUserLat = app.thisUser.loc.getLatitude();
		double thisUserLng = app.thisUser.loc.getLongitude();
		
		String goingTo ="";
		try {
			 goingTo = getAddress(selectedUser.dest.getLatitude(), selectedUser.dest.getLongitude());
		} catch (IOException e) {
			e.printStackTrace();
		}
		String distance = app.calculateDistance(selectedUserLat, thisUserLat, 
											selectedUserLng, thisUserLng);
		TextView name = (TextView) findViewById(R.id.name);
		TextView dest = (TextView)findViewById(R.id.destination);
		TextView distn = (TextView)findViewById(R.id.distance);
		TextView cont =(TextView)findViewById(R.id.conatct);
		dest.setText(goingTo);
		name.setText(selectedUser.userNick);
		distn.setText(distance);
		cont.setText(selectedUser.contact);
	
	///// below : adding marker to selected user's location
		LatLng selectedUserPosition = new LatLng(selectedUserLat,selectedUserLng);
		markerOptions = new MarkerOptions().position(selectedUserPosition)
				.title(selectedUser.userNick)
				.snippet("Goint to "+goingTo+", Contact "+selectedUser.contact).draggable(false);
		map.getUiSettings().setZoomControlsEnabled(true);
		map.getUiSettings().setCompassEnabled(true);
		if(selectedUser.hasCar)
		markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_pin));
		else
		markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.hand_pin));
		
		marker = map.addMarker(markerOptions);
		map.moveCamera(CameraUpdateFactory.newLatLng(selectedUserPosition));
		map.animateCamera(CameraUpdateFactory.zoomTo(15));
		
	
	}

	
	public String getAddress(double lat, double lng) throws IOException{
		
		Geocoder gc = new Geocoder(this);
		List<Address> list = gc.getFromLocation(lat, lng, 1);
		Address address = list.get(0);
		return address.getAddressLine(0);
	}
	
	

}
