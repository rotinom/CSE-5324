package com.mytutor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.mytutor.authentication.AuthenticationHelper;
import com.mytutor.search.SearchData;
import com.mytutor.session.ServerSession;
 
public class SplashScreen extends Activity {
 
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1500;
    
    private AuthenticationHelper ah_;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        ah_ = new AuthenticationHelper(this);
 
        //new Handler().postDelayed(new Runnable() {
 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
 
            //@Override
            //public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                //Intent i = new Intent(SplashScreen.this, MainActivity.class);
                //SplashScreen.this.startActivity(i);
 
                // close this activity
                //SplashScreen.this.finish();
            //}
        //}, SPLASH_TIME_OUT);
        
        //Protocol.getInstance(this);
        
        
        SearchData search = SearchData.create();

        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    
                    // Init the singleton
                    ServerSession.create(getApplicationContext(), SplashScreen.this);
                    
                    // If we have an account, then login
                    if(ah_.has_account()) {
                        ah_.login(SplashScreen.this);
                    }
                    
                    sleep(SPLASH_TIME_OUT);
                    
                } catch (Exception e) {
                    Log.e(getClass().getName(), e.toString());
                } finally {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));

                    // close this activity
                    finish();
                }
            }
        };

        welcomeThread.start();
    }
 
}
