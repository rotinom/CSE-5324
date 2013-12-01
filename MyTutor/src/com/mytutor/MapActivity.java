package com.mytutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.google.android.gms.ads.*;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mytutor.search.ImageLoader;
import com.mytutor.search.SearchData;
import com.mytutor.search.SearchResults;
import com.mytutor.session.ServerSession;
import com.mytutor.search.SearchParams;

public class MapActivity extends Activity
{
	// The map
	private GoogleMap map_;
	
	private ServerSession session_;
	
	private int animationTarget_;
	
	private RatingBar ratingBar; 
	
	private Map<String,String> subCatLookup_;
	
	private LatLng currentPos;
		
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
             
        SearchData search = null;
		try {
			search = SearchData.getInstance();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
		int sizeOfSearch = search.data.size();
		final String[] name = new String[sizeOfSearch];
		final String[] lastName = new String[sizeOfSearch];
		final String[] profile = new String[sizeOfSearch];
		final String[] email = new String[sizeOfSearch];
		final String[] rating = new String[sizeOfSearch];
		final String[] rate = new String[sizeOfSearch];
		final String[] schedule = new String[sizeOfSearch];
		final String[] stringLat = new String[sizeOfSearch];
		final String[] stringLon = new String[sizeOfSearch];
		
		
        try {
        	// Get our lat/lon
			session_ = ServerSession.create();
			currentPos = new LatLng(session_.getLat(), session_.getLon());
			
			
			// Build a LatLngBounds for all of the pins we will create, and include
			// our current position
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(currentPos);
            
           			
			// Get the search results
			//
			// TODO This should be bundled....
			
			for(int i = 0; i<sizeOfSearch; i++)
			{	
				name[i] = search.data.get(i).get("username").toString();
				lastName[i] = search.data.get(i).get("lastname").toString();
				profile[i] = search.data.get(i).get("profile").toString(); 
				email[i] = search.data.get(i).get("email").toString(); 
				rating[i] = search.data.get(i).get("rating").toString(); 
				rate[i] = search.data.get(i).get("rate").toString(); 
				schedule[i] = search.data.get(i).get("schedule").toString(); 
				stringLat[i] = search.data.get(i).get("lat").toString(); 
				stringLon[i] =  search.data.get(i).get("lon").toString(); 
				
				double markerLat = Double.parseDouble(stringLat[i]);
				double markerLon = Double.parseDouble(stringLon[i]);
												
				LatLng pos = new LatLng(markerLat, markerLon);
				Marker user = map_.addMarker(
			        new MarkerOptions()
        				.position(pos)
        				.title(name[i])
        				.snippet("Rating: " + rating[i] + " Rate: " + rate[i] + " " + i )
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
                
                // TODO: Look up the AdView as a resource and load a request.             
                /*
        	    AdView adView = (AdView)myContentsView.findViewById(R.id.mapAdView);
        	    AdRequest adRequest = new AdRequest.Builder().build();
        	    adView.loadAd(adRequest);
                */
                
                //Split the snippet string on the space character and store the results into an array.
                final String[] snippets = marker.getSnippet().split(" ");    
                
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
                
                /*
                 * TODO: FINISH UP IMAGE LOADING
                 * CURRENT IDEA IMAGE TOO LARGE
                 * 
                ImageView imgView = ((ImageView)myContentsView.findViewById((R.id.mapImage)));
                ImageLoader imageLoader = new ImageLoader(); 
                String url = "http://omega.uta.edu/~jwe0053/picture.php?email="+email[Integer.parseInt(snippets[5])];
                imageLoader.fetchDrawableOnThread(url, imgView);
                */
                
            	map_.setOnInfoWindowClickListener(
						  new OnInfoWindowClickListener(){
						    public void onInfoWindowClick(Marker marker){
						    	
						    	 Intent intent = new Intent(MapActivity.this, com.mytutor.search.ViewProfile.class);
				   	   		      intent.putExtra("username",name[Integer.parseInt(snippets[5])]);
				   	   		      intent.putExtra("lastname",lastName[Integer.parseInt(snippets[5])]);
				   	   		      intent.putExtra("rate",rate[Integer.parseInt(snippets[5])]);
				   	   		      intent.putExtra("rating", rating[Integer.parseInt(snippets[5])]);;
				   	   		      intent.putExtra("schedule",schedule[Integer.parseInt(snippets[5])]);
				   	   		      intent.putExtra("profile",profile[Integer.parseInt(snippets[5])]);
				   	   		      intent.putExtra("email",email[Integer.parseInt(snippets[5])]);
							      startActivity(intent);;
						    }
						  }
				);
					                
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
        
        final Map<String,String> subCatLookup_;
        
        subCatLookup_ = new HashMap<String,String>();
	   
        //allow network requests in main thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy); 
	      
        final Spinner mainCatSpinner_ = (Spinner)  findViewById(R.id.mainCatSpinner);
        final Spinner subCatSpinner_  = (Spinner)  findViewById(R.id.subCatSpinner);
        final Spinner radiusSpinner_  = (Spinner)  findViewById(R.id.searchRadius);
        final EditText zipcodeEdit_   = (EditText) findViewById(R.id.editZipcode);

        // Get the list of the main categories
        List<String> mainCategories = session_.getMainCategories();
        
        //populate spinner for main categories
        ArrayAdapter<String> adp1 = 
            new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mainCategories);
        
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mainCatSpinner_.setAdapter(adp1);
        
        //set up listener to handle main category selection (to populate sub-category spinner)
        mainCatSpinner_.setOnItemSelectedListener(new OnItemSelectedListener() 
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                populateSubCategory(mainCatSpinner_.getItemAtPosition(position).toString());
            }
            
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
        
        String zipCode = SearchParams.getZipcodeFromLla(session_.getLat(), session_.getLon()); 
        zipcodeEdit_.setText(zipCode);
        
        final Button searchButton = (Button) findViewById(R.id.btnSubmit);

        searchButton.setOnClickListener(
			new View.OnClickListener()
			{
    			public void onClick(View v)
    			{
    				//Log.d("SearchActivity", "Got submit button click event");
					Intent intent = new Intent(v.getContext(), com.mytutor.search.SearchResults.class);
					
					String subCat = subCatSpinner_.getSelectedItem().toString();
					String radius = radiusSpinner_.getSelectedItem().toString();
					String zipcode = zipcodeEdit_.getText().toString();
					
					intent.putExtra("lat",session_.getLat());
   	   				intent.putExtra("lon",session_.getLon());
   	   				intent.putExtra("subcat",subCatLookup_.get(subCat));
   	   				intent.putExtra("radius",radius);
   	   				startActivity(intent);
    			}
    		}
		);
    	           
    }
    
    public void populateSubCategory(String mainCategory) {
    	//populate post parameter with main category selection
 	   	ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
 	   	postParameters.add(new BasicNameValuePair("category", mainCategory));
 	   
 	   	// Populate the map
 	   	// subCatLookup_.clear();
 	   	subCatLookup_ = session_.getSubCategories(mainCategory);
 	   
 	   	// Get the keys as a list
 	   	List<String> keys = new ArrayList<String>(subCatLookup_.keySet());
 	   
 	   	// Populate the listview
 	   	ArrayAdapter<String> adp1 = 
 	   			new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, keys);
 	   	adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner subCatSpinner_ = (Spinner)  findViewById(R.id.subCatSpinner);
        subCatSpinner_.setAdapter(adp1);  
    }

}


