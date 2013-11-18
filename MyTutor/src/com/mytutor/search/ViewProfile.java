package com.mytutor.search;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mytutor.R;


public class ViewProfile extends Activity
{

   @Override
   protected void onCreate(Bundle savedInstanceState) 
   {
	   super.onCreate(savedInstanceState);
	   setContentView(R.layout.result_profile);
	   
       ImageView image=(ImageView)findViewById(R.id.image);
       TextView firstName = (TextView)findViewById(R.id.firstName);
       TextView lastName = (TextView)findViewById(R.id.lastName);
       TextView rate = (TextView)findViewById(R.id.rate);
       RatingBar ratingBar = (RatingBar)findViewById(R.id.rating);
       TextView schedule = (TextView)findViewById(R.id.schedule);
       TextView profile = (TextView)findViewById(R.id.comment);
       
       //TextView email = (TextView)findViewById(R.id.email); //hidden?
	   
       ImageLoader imageLoader = new ImageLoader(); 
       String url = "http://omega.uta.edu/~jwe0053/picture.php?email="+getIntent().getExtras().getString("email");
       imageLoader.fetchDrawableOnThread(url, image);
       firstName.setText(getIntent().getExtras().getString("username"));
       lastName.setText(getIntent().getExtras().getString("lastname"));
       rate.setText(getIntent().getExtras().getString("rate"));
       ratingBar.setRating(Float.parseFloat(getIntent().getExtras().getString("rating")));
       schedule.setText(getIntent().getExtras().getString("schedule"));
       profile.setText(getIntent().getExtras().getString("profile"));
       Log.i("SearchResults", "rating=" + getIntent().getExtras().getString("rating"));
   }
}