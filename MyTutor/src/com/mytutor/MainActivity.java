package com.mytutor;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    public void onClickLogin(View view) {
    	Log.d("MainActivity", "Got login click event");
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
    }
    
    public void onClickSearch(View view) {
    	Log.d("MainActivity", "Got search click event");
    }
    
    public void onClickProfile(View view) {
    	Log.d("MainActivity", "Got profile click event");
		Intent intent = new Intent(this, ProfileActivity.class);
		startActivity(intent);
    }
}
