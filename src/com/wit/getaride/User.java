package com.wit.getaride;

import android.location.Location;

public class User {

	public Location dest;
	public Location loc;
	public String userNick;
	public String password;
	public String contact;
	public boolean hasCar;
	
	
	public User(String userNick, String password, Location loc, Location dest, String contact, boolean hasCar){	
		this.dest = dest;
		this.loc = loc;
		this.userNick = userNick;
		this.password = password;
		this.hasCar = hasCar;
		this.contact = contact;
	}
	public User(String userNick, Location loc, Location dest, String contact){
		
		this.dest = dest;
		this.loc = loc;
		this.userNick = userNick;
		this.contact = contact;
		this.hasCar = false;
		
	}
	
}
