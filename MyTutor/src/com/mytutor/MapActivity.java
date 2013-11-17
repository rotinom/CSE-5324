package com.mytutor;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mytutor.search.SearchData;
import com.mytutor.session.ServerSession;


public class MapActivity extends Activity
{
	// The map
	private GoogleMap map_;
	
	private ServerSession session_;
	
	private int animationTarget_;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_map);
        

       // Get the target height for the animation target
       final View tv = findViewById(R.id.ExpandingLinearLayout);
       final ViewTreeObserver observer = tv.getViewTreeObserver();
       observer.addOnGlobalLayoutListener(
           new OnGlobalLayoutListener() {
               @Override
               public void onGlobalLayout() {
                   animationTarget_ = tv.getHeight();
                   Log.d("MapActivity", "Detected height to be: " + animationTarget_);
                   tv.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                   // Set the initial height
                   tv.getLayoutParams().height = 0;
               }
           }
       );
       
       
        
//    // Hide the keyboard
//       getWindow().setSoftInputMode(
//           WindowManager.LayoutParams.soft_input);
        
          


        //Remove notification bar
        this.getWindow().setFlags(
    		WindowManager.LayoutParams.FLAG_FULLSCREEN, 
    		WindowManager.LayoutParams.FLAG_FULLSCREEN
		);
        
        // Set up the map itself
        
        map_ = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        
        // Show our location, but hide the button
        map_.setMyLocationEnabled(true);
        map_.getUiSettings().setMyLocationButtonEnabled(false);
        
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
                
              //  ImageView imgView = ((ImageView)myContentsView.findViewById((R.id.imgStar)));
               
                return myContentsView;
            }
        });
    }
    
    public void onClickMyLocation(View view) {
        Log.d("MapActivity", "Got My Location");
        
        // Get our lat/lon
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
    }
    
//    
//    boolean keyboardShown_ = false;
//    AnimationListener keyboardHider = new AnimationListener() {
//        // ...
//        @Override
//        public void onAnimationEnd(Animation anim) {
//            Log.d("MapActivity", "Animation Ended");
//            if(keyboardShown_) {
//                Log.d("MapActivity", "Hiding keyboard");
//                
////                // Hide the keyboard
////                getWindow().setSoftInputMode(
////                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//                
//                InputMethodManager imm = 
//                        (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(findViewById(android.R.id.content).getWindowToken() , 0);
//            }
//            else {
//                Log.d("MapActivity", "Showing keyboard");
//                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
////                InputMethodManager imm = 
////                        (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//////                imm.showSoftInput(view, flags, new ResultReader(Ö))
////                    imm.showSoftInputFromInputMethod(findViewById(android.R.id.content), 0);
//                    
//                    
//            }
//            keyboardShown_ = !keyboardShown_;
//        }
//
//        @Override
//        public void onAnimationRepeat(Animation arg0) {
//        }
//
//        @Override
//        public void onAnimationStart(Animation arg0) {
//        }
//
//      };
    
    
    boolean expanding_ = false;
    
    public void onClickSearchButton(View view) {
        Log.d("MapActivity", "Got Search Button Click");
        View viewToAnimate = findViewById(R.id.ExpandingLinearLayout);
        
        
        expanding_ = !expanding_;
        
//        animationTarget_ = 550;
//        Log.d("MapActivity", "Setting height to: " + animationTarget_);
        DropDownAnimation dda = new DropDownAnimation(viewToAnimate, animationTarget_, expanding_);
        dda.setDuration(500);
        dda.setStartOffset(0);
//        dda.setAnimationListener(keyboardHider);
        
        
        
        Log.d("MapActivity", "Starting animation");
        viewToAnimate.startAnimation(dda);
       
        // Invalidate the parent's view, so that the animation starts
        // right away
        View viewToInvalidate = findViewById(R.id.MapRelativeLayout);
        viewToInvalidate.invalidate();
    }
    
    
    
}


