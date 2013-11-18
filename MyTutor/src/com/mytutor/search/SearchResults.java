package com.mytutor.search;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.mytutor.MapActivity;
import com.mytutor.R;

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
	       Time start = new Time();
	       Time stop = new Time();
	       start.setToNow();
		   response = CustomHttpClient.executeHttpPost("http://omega.uta.edu/~jwe0053/search.php", postParameters);
		   stop.setToNow();
		   Log.d("SearchResults","search.php took: " + (stop.toMillis(true)-start.toMillis(true)) + " milliseconds");
     
		   // store the result returned by PHP script that runs MySQL query
		   String result = response.toString();
		   final ListView resultsView  = (ListView) findViewById(R.id.resultsView);
		   
		   SearchData search = SearchData.getInstance(); 
		   ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
              
		   //parse json array data
		   try{
			   JSONArray jArray = new JSONArray(result);
			   Log.i("log_tag","data: "+ jArray.toString());
			   for(int i=0;i<jArray.length();i++){
				   JSONObject json_data = jArray.getJSONObject(i);
				   HashMap<String, String> map = new HashMap<String, String>();
				   map.put("email", json_data.getString("emailAddress") );
				   float miles = Float.valueOf(json_data.getString("distance") );
				   map.put("distance", (int)miles + " miles ");
				   map.put("username", json_data.getString("firstName") + " ");
				   map.put("lastname", json_data.getString("lastName") + " ");
				   map.put("rate", "$"+json_data.getString("rate")+"/hr ");
				   map.put("rating", json_data.getString("rating"));
				   map.put("lat", json_data.getString("lat"));
				   map.put("lon", json_data.getString("lon"));
				   map.put("schedule", json_data.getString("schedule"));
				   map.put("profile", json_data.getString("profile"));
				   data.add(map); 
				   search.data.add(map);
			   }
			   
			   // Getting adapter by passing xml data ArrayList
			   LazyAdapter adapter=new LazyAdapter(this, data);
			   resultsView.setAdapter(adapter);
			   
			   // Click event for single list row
			   resultsView.setOnItemClickListener(new OnItemClickListener() {
				   @Override
				   public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
				      Object listItem = resultsView.getItemAtPosition(position);
				      try{
				          for(int i=0; i<SearchData.getInstance().data.size(); i++)
				          {
				    	      HashMap<String, String> item = (HashMap<String, String>)SearchData.getInstance().data.get(i);
				    	      TextView emailView = (TextView)view.findViewById(R.id.email);
				    	      String emailString = emailView.getText().toString();
				    	      if(emailString == item.get("email"))
				    	      {
				    	    	  Log.i("SearchResults", "iterate email:"+item.get("email"));
				    	    	  //populate intent with user data and launch results

							      Intent intent = new Intent(SearchResults.this, com.mytutor.search.ViewProfile.class);
				   	   		      intent.putExtra("username",item.get("username"));
				   	   		      intent.putExtra("lastname",item.get("lastname"));
				   	   		      intent.putExtra("rate",item.get("rate"));
				   	   		      intent.putExtra("rating",item.get("rating"));
				   	   		      intent.putExtra("schedule",item.get("schedule"));
				   	   		      intent.putExtra("profile",item.get("profile"));
				   	   		      intent.putExtra("email",item.get("email"));
							      startActivity(intent);
				    	      }
				    	  
				          }
				      }catch (Exception e) {
						   Log.e("log_tag","Cannot find data entry for item selection" + e.toString());     
					   }
 
				   } 
				});
		   }
		   catch(JSONException e){
			   Log.e("log_tag", "Error parsing data "+e.toString());
		   }        
	   }
	   catch (Exception e) {
		   Log.e("log_tag","Error in http connection!!" + e.toString());     
	   }	   
   } 

 
   // Create a menu
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
       // Inflate the menu; this adds items to the action bar if it is present.
       getMenuInflater().inflate(R.menu.search_results, menu);
       return true;
   }
   
   
   public boolean onOptionsItemSelected (MenuItem item){
       Log.d("SearchResults", "Got a menu click event");
       
       switch (item.getItemId()) {
                   
           case R.id.action_show_results_in_map:
               Log.d("SearchResults", "Got show results in map event");
               Intent intent = new Intent(this, MapActivity.class);
               this.startActivity(intent);
               return true;
                   
           default:
               return super.onOptionsItemSelected(item);
       }
   }
   
}