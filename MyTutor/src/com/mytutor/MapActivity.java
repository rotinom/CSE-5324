package com.mytutor;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


public class MapActivity extends Activity {

	
	private GoogleMap map_;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        
        setContentView(R.layout.activity_map);
        
        map_ = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map_.setMyLocationEnabled(true);
        
        // Ballpark for DFW area.  Need to get current LLA but interface is complex
        // and I'm tired.
        map_.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(33, -97.4), 10));
    }
}


