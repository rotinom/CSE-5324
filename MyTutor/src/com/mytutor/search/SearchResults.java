package com.mytutor.search;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mytutor.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
		   ArrayList<String> list = new ArrayList<String>();
              
		   //parse json array data
		   try{
			   JSONArray jArray = new JSONArray(result);
			   //Log.i("log_tag","data: "+ jArray.toString());
			   for(int i=0;i<jArray.length();i++){
				   JSONObject json_data = jArray.getJSONObject(i);
				   //Log.i("log_tag","name: "+json_data.getString("firstName"));
				   list.add(json_data.getString("firstName"));
			   }
			   resultsView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, list));
		   
		   }
		   catch(JSONException e){
			   Log.e("log_tag", "Error parsing data "+e.toString());
		   }        
	   }
	   catch (Exception e) {
		   Log.e("log_tag","Error in http connection!!" + e.toString());     
	   }
	   
   }
}