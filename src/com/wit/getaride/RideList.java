package com.wit.getaride;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;



public class RideList extends Base implements OnItemClickListener{
	private ListView listView;
	public List <User> users    = new ArrayList<User>();
	private List <User> allUsers    = new ArrayList<User>();
	//private GetArideApp app = (GetArideApp)getApplication();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ride_list);
		
		
		List<User>allUsers = getMatching();
		
		Log.v("after getMatching", ""+allUsers.size());
		
		//app.toUser(app.currentParseUser)
		users = filterUsers(allUsers, app.thisUser);
		
		
	
		listView = (ListView)findViewById(R.id.rideList);
		UserAdapter adapter = new UserAdapter(this, users);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		
		allUsers.clear();
	}
	
// filterUsers() filters the list of users fetched from cloud based on their 
// current location and destination in relation to the current user. filtered
	public List<User> filterUsers(List<User> allusers, User thisUser){
		
		
		List<User> filteredUsers = new ArrayList<User>();
		double thisLocLat = thisUser.loc.getLatitude();
		double thisLocLng = thisUser.loc.getLongitude();
		double thisDestLat = thisUser.dest.getLatitude();
		double thisDestLng = thisUser.dest.getLongitude();
		
	for(int i=0; i<allusers.size(); i++){
		
			
			double locLat = allusers.get(i).loc.getLatitude();
			double locLng = allusers.get(i).loc.getLongitude();
			double destLat = allusers.get(i).dest.getLatitude();
			double destLng = allusers.get(i).dest.getLongitude();
			double sr = app.searchRadius;
		
			if(locLat>=(thisLocLat-sr) && locLat<=(thisLocLat+sr)&& locLng>=(thisLocLng-sr)&& locLng<=(thisLocLng+sr)
				&& destLat>=(thisDestLat-sr)&& destLat<=(thisDestLat+sr)&& 	destLng>=(thisDestLng-sr)&& destLng<=(thisDestLng+sr)
					)
			{
				filteredUsers.add(allusers.get(i));
			}
		
		}
		return filteredUsers;
	}
	
// getMatching()  fetches list of users from cloud based on current user's hasCar status	
	public List<User> getMatching(){
		
		final List<User> users = new ArrayList<User>();
		
		boolean hasCar = app.thisUser.hasCar?false:true;
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("hasCar", hasCar);
		/*query.findInBackground(new FindCallback<ParseUser>() {
		  public void done(List<ParseUser> pusers, ParseException e) {
		    if (e == null) {
		    	
		    	for(int i=0; i<pusers.size(); i++){
		    		
		    		users.add(toUser(pusers.get(i)));
		    		Log.v("test", ""+users.get(i).userNick);
		    	}
		    	
		    } else {
		        // Something went wrong.
		    }
		  }

		});*/
		try {
			List<ParseUser>pusers= query.find();
			for(int i=0; i<pusers.size(); i++){
	    		
	    		users.add(app.toUser(pusers.get(i)));
	    		Log.v("test", ""+users.get(i).userNick);
	    	}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Log.v("within getMatching", ""+users.size());
		
		return users;
	}
	

	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View row, int pos, long id) {
		
		UserDetails.selectedUser = users.get(pos);
		Intent intent = new Intent(this, UserDetails.class);
		startActivity(intent);
		
	}
	
	

}
class UserAdapter extends ArrayAdapter<User> {
	private Context context;
	private List<User> users;
	private GetArideApp app = new GetArideApp();
	public UserAdapter(Context context, List<User> users) {
		super(context, R.layout.list_row, users);
		this.context = context;
		this.users = users;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(R.layout.list_row, parent, false);
		User user = users.get(position);
		ImageView profilePic = (ImageView)view.findViewById(R.id.dp);
		ImageView callBtn = (ImageView)view.findViewById(R.id.callBtn);
		TextView user_nick = (TextView) view.findViewById(R.id.userNick);
		TextView dist = (TextView)view.findViewById(R.id.distance);
		TextView contact=(TextView)view.findViewById(R.id.cont);
		ImageView icon = (ImageView)view.findViewById(R.id.smallIcon);
		
		
		
		try {
			app.putDp(profilePic, user.userNick);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final String distance = app.calculateDistance(user.loc.getLatitude(), app.thisUser.loc.getLatitude()
				, user.loc.getLongitude(), app.thisUser.loc.getLongitude());
		Log.v("distance", distance);
		final String contNum =user.contact;
		dist.setText(distance);
		contact.setText(contNum);
		user_nick.setText( user.userNick);
		
		final String uname = user.userNick;
		user_nick.setText(uname);
		ImageView msgBtn = (ImageView)view.findViewById(R.id.btnSms);
		
		final String msgbody;
		if(user.hasCar){
			
			msgbody = uname+" at "+distance+" is offerign a lift.";
		}else{
			msgbody = uname+" at "+distance+" is requesting a lift.";
		}
		
		msgBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendSMS(msgbody, contNum);
			}
		});
		
		
		
		//call number
		callBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(contNum == null){
					Toast.makeText(context.getApplicationContext(),"User's contact is not available",
							Toast.LENGTH_LONG).show();
					Log.v("Contact ", ""+contNum);
					return;
					
				}
				
				try{
					 Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+contNum));
					 context.startActivity(intent);
					 
					 
				}catch(Exception e) {
					Toast.makeText(context.getApplicationContext(),"Your call has failed...",
						Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			}	
		});
		
		if(user.hasCar){
			
			icon.setImageResource(R.drawable.hascar);
		}else if(!user.hasCar){
			
			icon.setImageResource(R.drawable.nocar);
		}
		

		return view;
	}
	
	
	protected void sendSMS(String msgbody, String contact) {
	      Log.i("Send SMS", "");
//	      String name = users.get(position).userNick;
	      
	      Intent smsIntent = new Intent(Intent.ACTION_VIEW);
	      smsIntent.setData(Uri.parse("smsto:"));
	      smsIntent.setType("vnd.android-dir/mms-sms");

	      smsIntent.putExtra("address"  , new String (contact));
	      smsIntent.putExtra("sms_body"  , msgbody );
	      try {
	         context.startActivity(smsIntent);
//	         context.finish();
	         Log.i("Finished sending SMS...", "");
	      } catch (android.content.ActivityNotFoundException ex) {
	         Toast.makeText(getContext(), 
	         "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
	      }
	   }
	
	
	public User getItem(int position){

		return users.get(position);
	}
	@Override
	public int getCount() {
		return users.size();
	}
}
