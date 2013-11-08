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
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.mytutor.R;
import com.mytutor.search.CustomHttpClient;
import com.mytutor.session.ServerSession;


public class SearchParams extends Activity 
{
   private Map<String,String> subCatLookup_;
   ServerSession session_;
 
   	@Override
   	protected void onCreate(Bundle savedInstanceState) 
   	{
   		try {
   			session_ = ServerSession.getInstance();
   		} catch (Exception e1) {
   			// TODO Auto-generated catch block
   			e1.printStackTrace();
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

	   //create empty post parameters for php request
	   ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
	   String response;
	   
	   
          
       // call executeHttpPost method passing necessary parameters 
	   try {
		   //send off http request to php script on omega
		   response = CustomHttpClient.executeHttpPost("http://omega.uta.edu/~jwe0053/mainCategories.php", postParameters);
     
		   // store the result returned by PHP script that runs MySQL query
		   String result = response.toString();
		   //Log.e("log_tag", "DB Result "+ result);
		   List<String> mainCategories = new ArrayList<String>();
              
		   //parse json array data
		   try{
			   JSONArray jArray = new JSONArray(result);
			   for(int i=0;i<jArray.length();i++){
				   JSONObject json_data = jArray.getJSONObject(i);
				   //Log.i("log_tag","name: "+json_data.getString("name"));
				   //Get an output to the screen
				   mainCategories.add(json_data.getString("name")); //save to string list
			   }

			   //populate spinner for main categories
			   ArrayAdapter<String> adp1=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,mainCategories);
			   adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			   mainCatSpinner_.setAdapter(adp1);	
			   //set up listener to handle main category selection (to populate sub-category spinner)
			   mainCatSpinner_.setOnItemSelectedListener(new OnItemSelectedListener() 
			   {
			       @Override
			       public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) 
			       {
			    	   populateSubCategory(mainCatSpinner_.getItemAtPosition(position).toString());
			       }

			       @Override
			       public void onNothingSelected(AdapterView<?> parentView) 
			       {}
			   });
			   
		   }
		   catch(JSONException e){
			   Log.e("log_tag", "Error parsing data "+e.toString());
		   }        
	   }
	   catch (Exception e) {
		   Log.e("log_tag","Error in http connection!!" + e.toString());     
	   }
	   
       // Set up handler for the profile button on the bottom of the activity
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
   		        HttpGet httpGet = new HttpGet("http://maps.google.com/maps/api/geocode/json?address=" + zipcode +"&sensor=false");
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

	   final Spinner subCatSpinner_  = (Spinner)  findViewById(R.id.subCatSpinner);
	   //populate post parameter with main category selection
	   ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
	   postParameters.add(new BasicNameValuePair("category", mainCategory));
	   
	   String response = null;
	   subCatLookup_.clear();
          
	   //send off http request to php script on omega
	   try {
		   response = CustomHttpClient.executeHttpPost("http://omega.uta.edu/~jwe0053/subCategories.php", postParameters);
     
		   // store the result returned by PHP script that runs MySQL query
		   String result = response.toString();
		   //Log.e("log_tag", "DB Result "+ result);
		   List<String> mainCategories = new ArrayList<String>();
              
		   //parse request for sub categories
		   try{
			   JSONArray jArray = new JSONArray(result);
			   for(int i=0;i<jArray.length();i++){
				   JSONObject json_data = jArray.getJSONObject(i);
				   //Log.i("log_tag","name: "+json_data.getString("name"));
				   //Get an output to the screen
				   mainCategories.add(json_data.getString("name"));
				   subCatLookup_.put(json_data.getString("name"), json_data.getString("categoryId"));
			   }
			   ArrayAdapter<String> adp1=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,mainCategories);
			   adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			   subCatSpinner_.setAdapter(adp1);			   
		   }
		   catch(JSONException e){
			   Log.e("log_tag", "Error parsing data "+e.toString());
		   }
	   }
	   catch (Exception e) {
		   Log.e("log_tag","Error in http connection!!" + e.toString());     
	   }   
   }
   
   
 
   public String getZipcodeFromLla(double lat, double lon) {
       
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
