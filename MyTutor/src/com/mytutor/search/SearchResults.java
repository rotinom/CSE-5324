package com.mytutor.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mytutor.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

public class SearchResults extends Activity
{

   @Override
   protected void onCreate(Bundle savedInstanceState) 
   {
	   super.onCreate(savedInstanceState);
	   setContentView(R.layout.activity_results);
	   
	   //create empty post parameters for php request
	   ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
	   postParameters.add(new BasicNameValuePair("subcat", getIntent().getExtras().getString("subcat")));
	   postParameters.add(new BasicNameValuePair("radius", getIntent().getExtras().getString("radius")));
	   postParameters.add(new BasicNameValuePair("lat", getIntent().getExtras().getString("lat")));
	   postParameters.add(new BasicNameValuePair("lon", getIntent().getExtras().getString("lon")));
	   String response = null;
          
       // call executeHttpPost method passing necessary parameters 
	   try {
		   //send off http request to php script on omega
		   response = CustomHttpClient.executeHttpPost("http://omega.uta.edu/~jwe0053/search.php", postParameters);
     
		   // store the result returned by PHP script that runs MySQL query
		   String result = response.toString();
		   final ListView resultsView  = (ListView) findViewById(R.id.resultsView);
		   List<Map> data = new ArrayList<Map>();
              
		   //parse json array data
		   try{
			   JSONArray jArray = new JSONArray(result);
			   Log.i("log_tag","data: "+ jArray.toString());
			   for(int i=0;i<jArray.length();i++){
				   JSONObject json_data = jArray.getJSONObject(i);
				   
				   Map map = new HashMap();
				   map.put("image", "image ");
				   float miles = Float.valueOf(json_data.getString("distance") );
				   map.put("distance", (int)miles + " miles ");
				   map.put("username", json_data.getString("firstName") + " ");
				   map.put("rate", "$"+json_data.getString("rate")+"/hr ");
				   map.put("rating", json_data.getString("rating"));
				   data.add(map);   
			   }
			   
			   SimpleAdapter adapter = new SimpleAdapter(this, (List<? extends Map<String, ?>>) data,
			   R.layout.result_item, new String[] { "image", "distance", "username", "rate", "rating" },
			   new int[] { R.id.image, R.id.distance, R.id.username, R.id.rate, R.id.rating });
			   
			   adapter.setViewBinder(new adapterBinder());
			   resultsView.setAdapter(adapter);
		   }
		   catch(JSONException e){
			   Log.e("log_tag", "Error parsing data "+e.toString());
		   }        
	   }
	   catch (Exception e) {
		   Log.e("log_tag","Error in http connection!!" + e.toString());     
	   }
   }
   
   //special adapter for the rating bar
   class adapterBinder implements ViewBinder{
	    @Override
	    public boolean setViewValue(View view, Object data, String textRepresentation) {
	        if(view.getId() == R.id.rating){
	            String stringval = (String) data;
	            float ratingValue = Float.parseFloat(stringval);
	            RatingBar ratingBar = (RatingBar) view;
	            ratingBar.setRating(ratingValue);
	            return true;
	        }
	        else if(view.getId() == R.id.image){ 
	        	//get image
	        	return true;
	        }
	        return false;
	    }
	}
}