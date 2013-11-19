package com.mytutor.search;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.mytutor.R;
import com.mytutor.session.ServerSession;


public class SearchParams extends Activity 
{
   private Map<String,String> subCatLookup_;
   private ServerSession session_;
 
   	@Override
   	protected void onCreate(Bundle savedInstanceState) 
   	{
   		try {
   			session_ = ServerSession.create();
   		} catch (Exception e1) {
   			// Do nothing
   		}
   		
   		
	   subCatLookup_ = new HashMap<String,String>();
	   
	   //allow network requests in main thread
	   StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	   StrictMode.setThreadPolicy(policy); 
	   
	   super.onCreate(savedInstanceState);
	   setContentView(R.layout.activity_search);
	   
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
	   
	   
	   
       // Set up handler for the submit button on the bottom of the activity
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
   				   				
   				//turn zip into lat/lon
   		    	//send request to google map api via http client
   	           Time start = new Time();
   	           Time stop = new Time();
   	           start.setToNow();
               HttpGet httpGet = new HttpGet("http://maps.google.com/maps/api/geocode/json?address=" + zipcode +"&sensor=false");
   	           stop.setToNow();
   	           Log.d("SearchResults","google geocoding took: " + (stop.toMillis(true)-start.toMillis(true)) + " milliseconds");

   		        HttpClient client = new DefaultHttpClient();
   		        HttpResponse response;
   		        StringBuilder stringBuilder = new StringBuilder();

   		        //try and execute request and get response
   		        try {
   		            response = client.execute(httpGet);
   		            HttpEntity entity = response.getEntity();
   		            InputStream stream = entity.getContent();
   		            int b;
   		            while ((b = stream.read()) != -1) {
   		                stringBuilder.append((char) b);
   		            }
   		        } catch (ClientProtocolException e) {
   		            } catch (IOException e) {
   		        }
   		        
   		        //Log.e("log_tag","Zipcode lookup: " + stringBuilder.toString());

   		        String lat_string = "";
   		        String lon_string = "";
   		        //try parsing json encoded text
   		        try {
   		        	JSONObject request = new JSONObject(stringBuilder.toString());
   		            JSONObject results = request.getJSONArray("results").getJSONObject(0);
   		            JSONObject geometry = results.getJSONObject("geometry");
   		            JSONObject location = geometry.getJSONObject("location");
   		            
   		            lat_string = location.getString("lat");
   		            lon_string = location.getString("lng");
   		            //Log.d("test", "lat: " + lat_string + " lon:" + lon_string);
   		            
   	   		        //populate intent with user data and launch results
   	   				intent.putExtra("lat",lat_string);
   	   				intent.putExtra("lon",lon_string);
   	   				intent.putExtra("subcat",subCatLookup_.get(subCat));
   	   				intent.putExtra("radius",radius);
   	   				startActivity(intent);
   		            
   		        } catch (JSONException e) {
   		            e.printStackTrace();
   		        }
   			}
   		  }
		); 
       
       
       	if(null != session_){
       		String zipcode = this.getZipcodeFromLla(session_.getLat(), session_.getLon());
       		zipcodeEdit_.setText(zipcode);
       	}   
   }
   
   public void populateSubCategory(String mainCategory) {
	   
	   //populate post parameter with main category selection
	   ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
	   postParameters.add(new BasicNameValuePair("category", mainCategory));
	   
	   // Populate the map
	   subCatLookup_.clear();
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
   
   
  
   
 
   public static String getZipcodeFromLla(double lat, double lon) {
       
       //turn lat/lon into zip
	   //send request to google map api via http client
	   HttpGet httpGet = new HttpGet("http://maps.google.com/maps/api/geocode/json?latlng="+
			   lat + "," + lon + "&sensor=false");
	   
	   HttpClient client = new DefaultHttpClient();
	   HttpResponse response;
	   StringBuilder stringBuilder = new StringBuilder();

	   //try and execute request and get response
	   try {
	       response = client.execute(httpGet);
	       HttpEntity entity = response.getEntity();
	       InputStream stream = entity.getContent();
	       int b;
	       while ((b = stream.read()) != -1) {
	           stringBuilder.append((char) b);
	        }
	    } catch (ClientProtocolException e) {
	        } catch (IOException e) {
	    }
	   
	   String zipcode_string = "";
	   //try parsing json encoded text
	   try {
	       JSONObject request = new JSONObject(stringBuilder.toString());
	       JSONArray resultsArray = request.getJSONArray("results");
	       for (int i = 0; i < resultsArray.length(); i++) {

	          JSONObject resultsObject = resultsArray.getJSONObject(i);
	          JSONArray addressArray = resultsObject.getJSONArray("address_components");        
	          
	          for (int j = 0; j < addressArray.length(); j++) {
	              String postalcode = ((JSONArray)((JSONObject)addressArray.get(j)).get("types")).getString(0);
                  if (postalcode.compareTo("postal_code") == 0) {
                	  zipcode_string = ((JSONObject)addressArray.get(j)).getString("long_name");
//        	          EditText zipcodeEdit_   = (EditText) findViewById(R.id.editZipcode);
//        	          zipcodeEdit_.setText(zipcode_string); 
                }
	          }
	      }
	    } catch (JSONException e) {
	        e.printStackTrace();
	    }
	   
	   return zipcode_string;
   }
   
}
