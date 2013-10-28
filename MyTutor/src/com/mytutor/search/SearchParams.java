package com.mytutor.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.mytutor.R;
import com.mytutor.search.CustomHttpClient;


public class SearchParams extends Activity
{

   @Override
   protected void onCreate(Bundle savedInstanceState) 
   {
	   //allow network requests in main thread
	   StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	   StrictMode.setThreadPolicy(policy); 
	   
	   super.onCreate(savedInstanceState);
	   setContentView(R.layout.activity_search);

	   //create empty post parameters for php request
	   ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
	   String response = null;
          
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
			   final Spinner mainSpinner = (Spinner) findViewById(R.id.mainCatSpinner);
			   ArrayAdapter<String> adp1=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,mainCategories);
			   adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			   mainSpinner.setAdapter(adp1);	
			   //set up listener to handle main category selection (to populate sub-category spinner)
			   mainSpinner.setOnItemSelectedListener(new OnItemSelectedListener() 
			   {
			       @Override
			       public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) 
			       {
			    	   populateSubCategory(mainSpinner.getItemAtPosition(position).toString());
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
   }
   
   public void populateSubCategory(String mainCategory) {
	   
	   //populate post parameter with main category selection
	   ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
	   postParameters.add(new BasicNameValuePair("category", mainCategory));
	   
	   String response = null;
          
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
			   }
			   final Spinner subSpinner = (Spinner) findViewById(R.id.subCatSpinner);
			   ArrayAdapter<String> adp1=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,mainCategories);
			   adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			   subSpinner.setAdapter(adp1);			   
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
