package com.wit.getaride;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class FrSavedDestinations extends Fragment implements OnItemClickListener{
	
	private SharedPreference sp = new SharedPreference();
	private DestinationAdapter adapter;
	private GetArideApp app = new GetArideApp();
	private ImageView delimg;
	private ArrayList<Destination> dests;
	private ListView listView;
	public FrSavedDestinations() {
		// Required empty public constructor
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fr_saved_destinations, container,
				false);
		
		dests = sp.loadDestinations(getActivity());
		
		
		Button deleteLast = (Button)v.findViewById(R.id.deleteDests);
		
		 listView = (ListView)v.findViewById(R.id.destList);  
         adapter = new DestinationAdapter(getActivity(), dests);
 		
 			listView.setAdapter(adapter);
 			listView.setOnItemClickListener(this);
       
		
 			deleteLast.setOnClickListener(new OnClickListener(){

 				@Override
 				public void onClick(View v) {
 					int count =adapter.getCount();
 					if(count!=0){
 					Destination desti= adapter.getItem(count-1);
 					sp.removeDestination(getActivity(),count-1 );
 					adapter.remove(desti);
 					adapter.notifyDataSetChanged();
 					}
 				}
 			});
		
		return v;
	}
          
	 

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		app.thisUser.dest.setLatitude(adapter.getItem(position).lat);
		app.thisUser.dest.setLongitude(adapter.getItem(position).lng);
		app.setPoint("destination", adapter.getItem(position).lat, adapter.getItem(position).lng);
		
		Log.v("", ""+adapter.getItem(position).lat);
		
		startActivity (new Intent(getActivity(), RideList.class));
		
	}


}
class DestinationAdapter extends ArrayAdapter<Destination> {
	private Context context;
	private List<Destination> dests;
	private ListView mListView;

	public DestinationAdapter(Context context, List<Destination> dests) {
		super(context, R.layout.list_row, dests);
		this.context = context;
		this.dests = dests;
		
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		final Destination destin = this.getItem(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(R.layout.dest_list_row, parent, false);
		Destination dest = dests.get(position);
		TextView dest_name = (TextView) view.findViewById(R.id.userNick);
//		ImageView icon = (ImageView)view.findViewById(R.id.smallIcon);
		

		dest_name.setText( dest.name);
			
//			icon.setImageResource(R.drawable.delete);
			

			
		return view;
		
		
		
		
	}
	
	
	public Destination getItem(int position){

		return dests.get(position);
	}
	@Override
	public int getCount() {
		return dests.size();
	}
}
