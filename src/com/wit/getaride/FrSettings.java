package com.wit.getaride;

import java.io.ByteArrayOutputStream;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class FrSettings extends Fragment{
	
	private Intent mRequestFileIntent;
    private ParcelFileDescriptor mInputPFD;
    private ImageView imgView;
    private ImageView uploadImage;
    private EditText et;
    private EditText etPhone;
    private String imgDecodableString="";
    private Spinner spRadius;
    private Switch swt;
    private int radiusKm;
    private Double searchRad;
    private GetArideApp app = new GetArideApp();
	public FrSettings() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fr_settings, container, false);
		
		mRequestFileIntent = new Intent(Intent.ACTION_PICK);
        mRequestFileIntent.setType("image/jpg");
        
        imgView = (ImageView) v.findViewById(R.id.profileImage);
        et = (EditText)v.findViewById(R.id.editUserName);
        etPhone=(EditText)v.findViewById(R.id.phone);
        spRadius= (Spinner)v.findViewById(R.id.spinner1);
        uploadImage = (ImageView)v.findViewById(R.id.uploadImage);
//        ImageButton b1 = (ImageButton)v.findViewById(R.id.imageButton1);
//        ImageButton b2 = (ImageButton)v.findViewById(R.id.imageButton2);
        Button svChangs = (Button)v.findViewById(R.id.button2);
        swt = (Switch)v.findViewById(R.id.switch1);
        
//        b1.setOnClickListener(this);
//        b2.setOnClickListener(this);
        
        svChangs.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				ParseUser currUser = app.currentParseUser;
				String newUsername = et.getText().toString();
				
				if(newUsername.equals("")){
					Toast.makeText(getActivity(), "Username cannot be empty",
							Toast.LENGTH_SHORT).show();
					return;
				}else{
					
					if(swt.isChecked()){
						currUser.put("hasCar", true);
						app.thisUser.hasCar=true;
					}
					else{
					app.thisUser.hasCar=false;
					currUser.put("hasCar", false);}
					currUser.put("name",et.getText().toString());
					currUser.put("phone", etPhone.getText().toString());
					currUser.put("radius", ""+searchRad);
					
				}
				currUser.saveInBackground();
				app.searchRadius = searchRad;
				// Show a simple toast message
				Toast.makeText(getActivity(), "changes saved",
						Toast.LENGTH_SHORT).show();
				Log.v("search radius", ""+app.searchRadius);
				
			}
        });
        uploadImage.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				startActivityForResult(mRequestFileIntent, 0);
				
			}
        });
		
        if(app.currentParseUser.getString("name")!=null)
        {
        	et.setText(app.currentParseUser.getString("name"));}
        
        if(app.currentParseUser.getString("phone")!=null){
        	etPhone.setText(app.currentParseUser.getString("phone"));}
        if(app.currentParseUser.getBoolean("hasCar")){
        	swt.setChecked(true);
        }else{swt.setChecked(false);}
       
        if(app.currentParseUser.getString("radius")==null){
        	spRadius.setSelection(0);
    	   //app.searchRadius = 0.02;
    	   
       }
        else{
        	String rd = app.currentParseUser.getString("radius");
        	
        	switch(rd){
        	case "0.02":
        		spRadius.setSelection(0);
        	break;
        	case "0.04":
            	spRadius.setSelection(1);
            	break;
        	case "0.06":
            	spRadius.setSelection(2);
            	break;
        	
        	}
        }
        
        spRadius.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				switch(position){
				
				case 0:
					radiusKm = 1;
					searchRad=0.02;
					break;
				case 1:
					radiusKm =	2;
					searchRad=0.04;
					break;
				case 2:
					radiusKm =	3;
					searchRad=0.06;
					
				
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}});
        
//        et.setEnabled(false);
//        etPhone.setEnabled(false);
      // bitmap = imgView.getDrawingCache();
       try {
		app.putDp(imgView,"");
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
        
		return v;
	}
	


	@Override
	public void onActivityResult(int requestCode, int resultCode,
	        Intent returnIntent) {
	    // If the selection didn't work
	    if (resultCode != getActivity().RESULT_OK) {
	        // Exit without doing anything else
	        return;
	    } else {
	        // Get the file's content URI from the incoming Intent
	        Uri returnUri = returnIntent.getData();
	        String[] filePathColumn = { MediaStore.Images.Media.DATA };
	        
	        // Get the cursor
	        Cursor cursor = getActivity().getContentResolver().query(returnUri,
	                filePathColumn, null, null, null);
	        // Move to first row
	        cursor.moveToFirst();

	        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	        imgDecodableString = cursor.getString(columnIndex);
	        cursor.close();
	       Bitmap bitmap = BitmapFactory.decodeFile(imgDecodableString);
	        
	        float scaleWidth = (float) 0.5;
	        float scaleHeight = (float) 0.5;
	        Matrix matrix = new Matrix();
//	        matrix.postRotate(45);
	        matrix.postScale(scaleWidth, scaleHeight);
	        
	        Bitmap resized = Bitmap.createBitmap(bitmap, 0, 0,
	                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	       // Bitmap resized= Bitmap.createScaledBitmap(bitmap, 250, 250, true);
			// Convert it to byte
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			// Compress image to lower quality scale 1 - 100
			resized.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			byte[] image = stream.toByteArray();

			// Create the ParseFile
			ParseFile file = new ParseFile("profile.jpg", image);
			// Upload the image into Parse Cloud
			file.saveInBackground();
			ParseUser currUser = ParseUser.getCurrentUser();
			// Create a column named "ImageName" and set the string
			currUser.put("photo", file);
			currUser.saveInBackground();
	        // Set the Image in ImageView after decoding the String
	     
			imgView.setImageBitmap(BitmapFactory
	                .decodeFile(imgDecodableString));
	        
	        
	        
	    }
	}
	

//	@Override
//	public void onClick(View v) {
//		switch(v.getId()){
//		case R.id.imageButton1:
//		et.setEnabled(true);
//		etPhone.setEnabled(false);
//		break;
//		case R.id.imageButton2:
//			etPhone.setEnabled(true);
//			et.setEnabled(false);
//		break;
//		
//		}
//		
//	}
	
	
	

}
