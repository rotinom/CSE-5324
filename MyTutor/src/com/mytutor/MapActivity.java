package com.mytutor;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import android.app.Activity;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


public class MapActivity 
extends 
	Activity 
implements
	GooglePlayServicesClient.ConnectionCallbacks,
    GooglePlayServicesClient.OnConnectionFailedListener	
{
	// The map
	private GoogleMap map_;
	
	// The location client
	private LocationClient locClient_;
	
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Set up ourselves as a location client, and connect
        locClient_ = new LocationClient(this, this, this);
          
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(
    		WindowManager.LayoutParams.FLAG_FULLSCREEN, 
    		WindowManager.LayoutParams.FLAG_FULLSCREEN
		);
        
        // Set up the map itself
        setContentView(R.layout.activity_map);     
        map_ = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map_.setMyLocationEnabled(true);
    }
    

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.d("MapActivity", "onConnectionFailed");
		
		/*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (result.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                result.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
//            /*
//             * If no resolution is available, display a dialog to the
//             * user with the error.
//             */
//            showErrorDialog(result.getErrorCode());
        }
		
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		Log.d("MapActivity", "onConnected");
		
		// Get the last location we are at
		Location loc = locClient_.getLastLocation();
		Log.d("MapActivity", "Location: " + loc.toString());
		
		// Animate the camera and move it to the current location
		map_.animateCamera(
			CameraUpdateFactory.newLatLngZoom(
				new LatLng(
					loc.getLatitude(), 
					loc.getLongitude()
				), 
				14.0f
			)
		);
		
		
        // You can customize the marker image using images bundled with
        // your app, or dynamically generated bitmaps. 
        map_.addMarker(
    		new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mytutor_icon))
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(loc.getLatitude()+0.001, loc.getLongitude()+0.001)));	
	}
	

	@Override
	public void onDisconnected() {
		Log.d("MapActivity", "onDisconnected");
		// TODO Auto-generated method stub
	}
	
	
    /*
     * Called when the Activity becomes visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        Log.d("MapActivity", "connecting...");
        locClient_.connect();
    }

    
    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        locClient_.disconnect();
        super.onStop();
    }
}


