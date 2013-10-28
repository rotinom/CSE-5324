package com.mytutor;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        // Set up handler for the msg_button on the bottom of the activity
        ImageButton msg_button = (ImageButton)findViewById(R.id.msg_button);

        msg_button.setOnClickListener(
    		new View.OnClickListener()
    		{
    			public void onClick(View v)
    			{
    		    	Log.d("MainActivity", "Got message button click event");
    				Intent intent = new Intent(v.getContext(), MessageActivity.class);
    				v.getContext().startActivity(intent);
    			}
    		}
		);    
        
        
        // Set up handler for the profile button on the bottom of the activity
        ImageButton profile_button = (ImageButton)findViewById(R.id.profile_button);

        profile_button.setOnClickListener(
    		new View.OnClickListener()
    		{
    			public void onClick(View v)
    			{
    		    	Log.d("MainActivity", "Got profile button click event");
    				Intent intent = new Intent(v.getContext(), ProfileActivity.class);
    				v.getContext().startActivity(intent);
    			}
    		}
		); 
        
        
        // Set up handler for the profile button on the bottom of the activity
        ImageButton search_button = (ImageButton)findViewById(R.id.search_button);

        search_button.setOnClickListener(
    		new View.OnClickListener()
    		{
    			public void onClick(View v)
    			{
    		    	Log.d("MainActivity", "Got map button click event");
    				Intent intent = new Intent(v.getContext(), MapActivity.class);
    				v.getContext().startActivity(intent);
    			}
    		}
		);         
        
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected (MenuItem item){
    	Log.d("MainMenu", "Got a menu click event");
    	
		switch (item.getItemId()) {
		   	case R.id.action_login:
				Log.d("MainActivity", "Got login click event");
					Intent intent = new Intent(this, LoginActivity.class);
					startActivity(intent);
					return true;
					
		       default:
		           return super.onOptionsItemSelected(item);
		}
    }
    
    public void onClickSearch(View view) {
    	Log.d("MainActivity", "Got search click event");
		Intent intent = new Intent(this, com.mytutor.search.SearchParams.class);
		startActivity(intent);
    }
    
    public void onClickProfile(View view) {
    	Log.d("MainActivity", "Got profile click event");
		Intent intent = new Intent(this, ProfileActivity.class);
		startActivity(intent);
    }
}
