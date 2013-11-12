package com.mytutor.session;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.mytutor.search.SearchParams;

public class ServerSession 
implements 
	Session,
	GooglePlayServicesClient.ConnectionCallbacks,
    GooglePlayServicesClient.OnConnectionFailedListener		
{	
	// The location client
	private LocationClient locClient_;
	
    // Location information
    private double lat_;
    private double lon_;
    private double alt_;
    
    static ServerSession singleton_;
    
    static final String logName_ = "ServerSession";
	
    protected ServerSession(Context context) {
        
        // Set up ourselves as a location client, and connect
        locClient_ = new LocationClient(context, this, this);
        
        // Connect the client.
        Log.d(logName_, "connecting...");
        locClient_.connect();
    }
    
    public static ServerSession getInstance() throws Exception{
    	if(null == singleton_){
    		throw new Exception("singleton_ not set.  Call create first!");
    	}
    	
    	return singleton_;
    }
    
    public static ServerSession create(Context context){
    	if(null == singleton_){
    		singleton_ = new ServerSession(context);
    	}
    	
    	return singleton_;
    }
	

	@Override
	public boolean validate_username(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validate_password(String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SessionStateEnum register_user(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SessionStateEnum login(String name, String password) {
		return SessionStateEnum.OK;
	}

	@Override
	public SessionStateEnum change_password(String username,
			String old_password, String new_password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SessionStateEnum search(SearchParams[] params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.d(logName_, "onConnectionFailed");
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		Log.d(logName_, "onConnected");
		
		// Get the last location we are at
		Location loc = locClient_.getLastLocation();
		Log.d(logName_, "Location: " + loc.toString());
		
		setLat(loc.getLatitude());
		setLon(loc.getLongitude());
		setAlt(loc.getAltitude());
	}

	@Override
	public void onDisconnected() {
		Log.d(logName_, "onDisconnected");
		setLat(0.0);
		setLon(0.0);
		setAlt(0.0);
		
	}


	public double getLat() {
		return lat_;
	}

	private void setLat(double lat_) {
		this.lat_ = lat_;
	}

	public double getLon() {
		return lon_;
	}

	private void setLon(double lon_) {
		this.lon_ = lon_;
	}

	public double getAlt() {
		return alt_;
	}

	private void setAlt(double alt_) {
		this.alt_ = alt_;
	}

}
