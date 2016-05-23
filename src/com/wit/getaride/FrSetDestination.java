package com.wit.getaride;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class FrSetDestination extends Fragment implements 
OnMapReadyCallback,ConnectionCallbacks, OnConnectionFailedListener, LocationListener{

	private MapView mapV;
	private GoogleMap map;
	private Location currentLocation;
	private Button btnConfirm;
	private LocationManager manager;
	private GoogleApiClient client;
	private MarkerOptions markerOptions;
	private Marker marker;
	GetArideApp app = new GetArideApp();
	private SharedPreference sp = new SharedPreference();
	
	public FrSetDestination() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v =inflater.inflate(R.layout.fragment_fr_set_destination,
				container, false);
		
		return v;
		
		
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		
		super.onActivityCreated(savedInstanceState);
		
		mapV = (MapView)getActivity().findViewById(R.id.map);
		mapV.onCreate(savedInstanceState);
	    mapV.onResume();// needed to get the map to display immediately

	    try {
	        MapsInitializer.initialize(getActivity().getApplicationContext());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		map = mapV.getMap();
		map.setMyLocationEnabled(true);
		
		//=== API client ========================
		 client = new GoogleApiClient.Builder(getActivity())
		  .addApi(LocationServices.API)
		  .addConnectionCallbacks(this)
		  .addOnConnectionFailedListener(this)
		  .build();
		 client.connect();
		 
		//MapController controller = map.getController();
		manager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500000, 10, this);
		
		
		//====Map Click Listener ===//
		map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng point) {
				Log.v("FrSetDest", "Map clicked");
				if (marker != null) {
					marker.remove();
				}
				markerOptions = new MarkerOptions().position(new LatLng(point.latitude, point.longitude))
						.title("Your Destination")
						.snippet("Tap on another spot to change your destination").draggable(true);
				map.getUiSettings().setZoomControlsEnabled(true);
				map.getUiSettings().setCompassEnabled(true);
				
				markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
				marker = map.addMarker(markerOptions);
				
				app.thisUser.dest.setLatitude(point.latitude);
				app.thisUser.dest.setLongitude(point.longitude);
				
				app.setPoint("destination", point.latitude, point.longitude);
			}

		});//end of setOnMapClickListener
	
		
		
	}// end onActivityCreated() ...
	

	@Override
	public void onLocationChanged(Location location) {
		
		LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
		
		map.moveCamera(CameraUpdateFactory.newLatLng(latlng));
		map.animateCamera(CameraUpdateFactory.zoomTo(15));
		
		currentLocation = location;
		Log.v("Location changed",""+location);
		app.thisUser.loc.setLatitude(currentLocation.getLatitude());
		app.thisUser.loc.setLongitude(currentLocation.getLongitude());
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle connectionHint) {
	//	FusedLocationProviderApi.requestLocationUpdates();
		currentLocation = LocationServices.FusedLocationApi.getLastLocation(client);
		 
		 
	        if (currentLocation != null) {
	          
	            Log.v("onConnected", ""+currentLocation);
	            
	        LatLng latlng =new LatLng((currentLocation.getLatitude()-0.0001398),
    				(currentLocation.getLongitude()+0.0001895));
	            
	            map.moveCamera(CameraUpdateFactory.newLatLng(latlng));
	    		map.animateCamera(CameraUpdateFactory.zoomTo(15));
	            
	    		app.thisUser.loc.setLatitude(currentLocation.getLatitude());
	    		app.thisUser.loc.setLongitude(currentLocation.getLongitude());
	    		
	    		app.setPoint("location", currentLocation.getLatitude(), currentLocation.getLongitude());
	            
	        }
		
	}
	



	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMapReady(final GoogleMap map){
		
	
	}
	
	@Override
	public void onResume(){
		super.onResume();
		markerOptions = new MarkerOptions().position(new LatLng(app.thisUser.dest.getLatitude(), 
				app.thisUser.dest.getLongitude())).title("Your Destination")
				.snippet("Tap on another spot to change your destination").draggable(true);
		markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
		marker = map.addMarker(markerOptions);
		
	}
	

}