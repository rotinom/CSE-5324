package com.mytutor.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.mytutor.search.CustomHttpClient;
import com.mytutor.search.ImageLoader;
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
    
    private Map<String, Map<String, String>> categoryCache_;
	
    protected ServerSession(Context context) {
        
        // Set up ourselves as a location client, and connect
        locClient_ = new LocationClient(context, this, this);
        
        // Connect the client.
        Log.d(logName_, "connecting...");
        locClient_.connect();
        
        // Grab the main categories and subcategories in a cache
        List<String> mainCat = getMainCategories();
        for(String cat : mainCat) {
            Map<String, String> subcat = getSubCategories(cat);
            categoryCache_.put(cat,  subcat);
        }
    }
    
    public static ServerSession create() throws Exception{
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
	
	
	public void getProfilePicAsync(String email, ImageView dest) {
        ImageLoader imageLoader = new ImageLoader(); 
        String url = "http://omega.uta.edu/~jwe0053/picture.php?email=" + email;
        imageLoader.fetchDrawableOnThread(url, dest);
	}
	

	
	public List<String> getMainCategories(){
       ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
       List<String> ret = new ArrayList<String>();
       
       // call executeHttpPost method passing necessary parameters 
       try {
           //send off http request to php script on omega
           Time start = new Time();
           Time stop = new Time();
           start.setToNow();
           String response = CustomHttpClient.executeHttpPost("http://omega.uta.edu/~jwe0053/mainCategories.php", postParameters);
           stop.setToNow();
           Log.d("SearchResults","mainCategories.php took: " + (stop.toMillis(true)-start.toMillis(true)) + " milliseconds");
           
           //parse json array data
           try{
               JSONArray jArray = new JSONArray(response.toString());
               for(int i=0;i<jArray.length();i++){
                   JSONObject json_data = jArray.getJSONObject(i);
                   ret.add(json_data.getString("name")); //save to string list
               }               
           }
           catch(JSONException e){
               Log.e("log_tag", "Error parsing data "+e.toString());
           }        
       }
       catch (Exception e) {
           Log.e("log_tag","Error in http connection!!" + e.toString());     
       }
       
       return ret;
   }
	
	
	 /**
	    * Get the subcategories given a main category
	    * @param category The main category
	    * @return Map of category name to id for each subcategory
	    */
	   public Map<String,String> getSubCategories(String category){
	       
	       Map<String,String> ret = new HashMap<String, String>();
	       
	       //populate post parameter with main category selection
	       ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
	       postParameters.add(new BasicNameValuePair("category", category));
	          
	       //send off http request to php script on omega
	       try {
	           String response = CustomHttpClient.executeHttpPost("http://omega.uta.edu/~jwe0053/subCategories.php", postParameters);
	     
	           // store the result returned by PHP script that runs MySQL query
	           String result = response.toString();
	           Log.d("SearchParams", "getSubCategories results: "+ result);
	              
	           //parse request for sub categories
	           try{
	               JSONArray jArray = new JSONArray(result);
	               for(int i=0;i<jArray.length();i++){
	                   JSONObject json_data = jArray.getJSONObject(i);
	                   ret.put(json_data.getString("name"), json_data.getString("categoryId"));
	               }           
	           }
	           catch(JSONException e){
	               Log.e("log_tag", "Error parsing data "+e.toString());
	           }
	       }
	       catch (Exception e) {
	           Log.e("log_tag","Error in http connection!!" + e.toString());     
	       }   
	       
	       return ret;
	   }
	   
	   
	   public Map<String, Map<String, String>> getCategoryCache(){
	       return categoryCache_;
	   }

}
