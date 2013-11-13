package com.mytutor;

import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.*;
import com.mytutor.R;
import com.mytutor.R.drawable;
import com.mytutor.R.id;
import com.mytutor.R.layout;
import com.mytutor.session.ServerSession;
import com.mytutor.search.SearchData;

import android.app.Activity;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


public class MapActivity extends Activity
{
	// The map
	private GoogleMap map_;
	
	private ServerSession session_;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          
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
        
        try {
        	// Get our lat/lon
			session_ = ServerSession.getInstance();
	        double lat = session_.getLat();
	        double lon = session_.getLon();
	        	        
			// Animate the camera and move it to the current location
			map_.animateCamera(
				CameraUpdateFactory.newLatLngZoom(
					new LatLng(
						lat, 
						lon
					), 
					14.0f
				)
			);
			
			SearchData search = SearchData.getInstance(); 
			
			for(int i = 0; i<search.data.size(); i++)
			{	
				String name = search.data.get(i).get("username").toString();
				String rating = search.data.get(i).get("rating").toString(); 
				String rate = search.data.get(i).get("rate").toString(); 
				String stringLat = search.data.get(i).get("lat").toString(); 
				String stringLon =  search.data.get(i).get("lon").toString(); 
				
				double markerLat = Double.parseDouble(stringLat);
				double markerLon = Double.parseDouble(stringLon);
				
				Marker user = map_.addMarker(new MarkerOptions()
				.position(new LatLng(0, 0))
				.title(name)
				.snippet("Rating: " + rating + " Rate/hr: " + rate)
				.position(new LatLng(markerLat, markerLon))
				 );
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
                
        // Set up the custom info window adapter
        map_.setInfoWindowAdapter(new InfoWindowAdapter() {
            
        	// Default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }
             
            // Defines the custom info window.
            @Override
            public View getInfoContents(Marker marker) 
            {
                View myContentsView = getLayoutInflater().inflate(R.layout.activity_map_infowindow, null);
                
                TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
                tvTitle.setText(marker.getTitle());
                
                TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.snippet));
                tvSnippet.setText(marker.getSnippet());
                
                ImageView imgView = ((ImageView)myContentsView.findViewById((R.id.imgStar)));
               
                return myContentsView;
            }
        });
    }
    
}


