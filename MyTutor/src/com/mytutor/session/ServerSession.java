package com.mytutor.session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.mytutor.authentication.AuthenticationParams;
import com.mytutor.profile.Profile;
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
    
    private static ServerSession singleton_;
    
    private static final String logName_ = "ServerSession";
    
    private static Map<String, Map<String, String>> categoryCache_;
    private static Map<String, String> subcatToCatMap_;
    private static Map<String, String> subcatIdToName_;
    
    private static AccountManager accountManager_;
    
    private static String authenticationToken_;
    
    private static Profile profile_;
	
    protected ServerSession(Context context, Activity activity) {
        
        subcatToCatMap_ = new HashMap<String, String>();
        subcatIdToName_ = new HashMap<String, String>();
        
        // Set up ourselves as a location client, and connect
        locClient_ = new LocationClient(context, this, this);
        
        // Connect the location client.
        Log.d(logName_, "connecting...");
        locClient_.connect();
        
        
        // Get our authentication token if we have a login account
        // Get our account
        accountManager_ = AccountManager.get(activity);
        final AccountManagerFuture<Bundle> future = 
                accountManager_.getAuthTokenByFeatures(
                    AuthenticationParams.ACCOUNT_TYPE,                  // account type
                    AuthenticationParams.AUTHTOKEN_TYPE_FULL_ACCESS,    // auth token type
                    null,                                               // features
                    activity,                                            // activity
                    null,                                               // addAccountOptions
                    null,                                               // getauthtokenoptions
                    new AccountManagerCallback<Bundle>() {              // callback
                        @Override
                        public void run(AccountManagerFuture<Bundle> future) {
                            Bundle bnd = null;
                            try {
                                bnd = future.getResult();
                                authenticationToken_ = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                                Log.d("SearchParams", "Got authentication token: "+ authenticationToken_);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
            , null); 
        
        // Wait on the future
        try {
            future.getResult();
        } catch (OperationCanceledException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (AuthenticatorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        // Grab the main categories and subcategories in a cache
        List<String> mainCat = getMainCategories();
        categoryCache_ = new HashMap<String, Map<String, String>>();
        for(String cat : mainCat) {
            Map<String, String> subcatMap = getSubCategories(cat);
            
            // Fill the map of subcategories -> main categories
            for(String subcat : subcatMap.keySet()) {
                subcatToCatMap_.put(subcat, cat);
            }
            
            categoryCache_.put(cat,  subcatMap);
        }        
    }
    
    public static ServerSession create() throws Exception{
    	if(null == singleton_){
    		throw new Exception("singleton_ not set.  Call create first!");
    	}
    	
    	return singleton_;
    }
    
    public static ServerSession create(Context context, Activity activity){
    	if(null == singleton_){
    		singleton_ = new ServerSession(context, activity);
    		
            // Get our profile
            profile_ = getProfile(authenticationToken_);
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
                   String mainCategory = json_data.getString("name");
                   Log.d("SearchResults","Got main category: " + mainCategory);
                   ret.add(mainCategory); //save to string list
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
	                   
	                   String name = json_data.getString("name");
	                   String id   = json_data.getString("categoryId");
	                   ret.put(name, id);
	                   
	                   // reverse map
	                   subcatIdToName_.put(id,  name);
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
	   
	   public static Profile getMyProfile() {
	       return profile_;
	   }
	   
	   public static Profile getProfile(String email) {
           Log.d("ServerSession::getProfile", "Getting Profile for: " + email);
     
           //populate post parameter with main category selection
           ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
           postParameters.add(new BasicNameValuePair("email", email));
              
           // Return value
           Profile ret = new Profile();
           
           //send off http request to php script on omega
           try {
               String response = CustomHttpClient.executeHttpPost("http://omega.uta.edu/~jwe0053/getAllInfo.php", postParameters);
         
               // store the result returned by PHP script that runs MySQL query
               Log.d("getProfile", "getSubCategories results: "+ response.toString());
                  
               // parse request into the profile
               ret.deserialize(email, response.toString());
           }
           catch (Exception e) {
               Log.e("log_tag","Error in http connection!!" + e.toString());     
           }   
           
           return ret;
	   }
	   
	   public String getSubcategoryNameFromId(String id) {
	       return subcatIdToName_.get(id);
	   }

	   public String getCategory(String subcat_id) {
	       return subcatToCatMap_.get(getSubcategoryNameFromId(subcat_id));
	   }
}
