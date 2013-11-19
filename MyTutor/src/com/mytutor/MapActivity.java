package com.mytutor;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
	
	private RatingBar ratingBar; 
		
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
			LatLng currentPos = new LatLng(session_.getLat(), session_.getLon());
			
			
			// Build a LatLngBounds for all of the pins we will create, and include
			// our current position
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(currentPos);
            
			
			// Get the search results
			//
			// TODO This should be bundled....
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
				
				
				LatLng pos = new LatLng(markerLat, markerLon);
				Marker user = map_.addMarker(
			        new MarkerOptions()
        				.position(pos)
        				.title(name)
        				.snippet("Rating: " + rating + " Rate: " + rate)
        				.icon(BitmapDescriptorFactory.fromResource(R.drawable.custom_marker))
				 );
								
				// Add the position to our builder
				builder.include(pos);
			}
			
			// Build the bounding box with the appropriate pixel margin
			final int MAP_MARGIN_IN_PIXELS =  250;
			LatLngBounds bounds = builder.build();
			final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, MAP_MARGIN_IN_PIXELS);

			
			
	       // Set the zoom after the map has been laid out
	       final View map_view = findViewById(R.id.map);
	       final ViewTreeObserver map_observer = map_view.getViewTreeObserver();
	       map_observer.addOnGlobalLayoutListener(
	           new OnGlobalLayoutListener() {
	               @Override
	               public void onGlobalLayout() {
	                   map_.moveCamera(cu);
	               }
	           }
	       );
			
			
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
                
                //Split the snippet string on the space character and store the results into an array.
                String[] snippets = marker.getSnippet().split(" ");    
                
                TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
                tvTitle.setText(marker.getTitle());
                
                // Log.d("Rate: " , snippets[2]);
                // Log.d("Rate Csot: " , snippets[3]); 
                TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.snippet));
                tvSnippet.setText(snippets[2] + " " + snippets[3]);

                // Set the rating for the rating bar.
                // Log.d("Rating: " , snippets[1]);  
                ratingBar = (RatingBar)myContentsView.findViewById(R.id.tutorRatingMap);
                ratingBar.setRating(Float.parseFloat(snippets[1])); 
                
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


