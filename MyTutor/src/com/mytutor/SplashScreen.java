package com.mytutor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
 
public class SplashScreen extends Activity {
 
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1500;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
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
