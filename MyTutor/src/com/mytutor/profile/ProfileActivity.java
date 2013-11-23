package com.mytutor.profile;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.mytutor.R;
import com.mytutor.authentication.AuthenticationHelper;
import com.mytutor.session.ServerSession;

public class ProfileActivity extends Activity {

    private ServerSession session_;
    private AuthenticationHelper ah_;

    private final static int PICK_FROM_CAMERA = 0;

    private final static String log_name = "ProfileActivity";
    
    private AccountManager accountManager_;
    
    private Bitmap photo_;
    
    private View topLayout_;
    private View statusLayout_;
    
    private ProfileTask profileTask_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        // Get our top layout
        topLayout_ = findViewById(R.id.profile_grid_layout);
        statusLayout_ = findViewById(R.id.profile_status);
        
        // get account manager
        accountManager_ = AccountManager.get(this);
        
        // Get the server session
        try {
            session_ = ServerSession.create();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        

        
        


        
        
        ListView lv = (ListView)findViewById(R.id.categoryListView);
        
        
        
        ////////////////
        // Test
        HashMap<String, String> datamap = new HashMap<String, String>();
        datamap.put("main", "main1");
        datamap.put("subcategory", "sub1");
        
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        
        for(int i = 0; i < 25; i += 1) {
            data.add(datamap);
        }
        ////////////////
        
        
        lv.setAdapter(new ProfileCategoryAdapter(this, data)); 
        
        
        TextView statusMessage = (TextView)findViewById(R.id.profile_status_message);
        statusMessage.setText("Retrieving profile");
        showProgress(true);
        profileTask_ = new ProfileTask();
        profileTask_.execute((Void) null);
    }
    
    
    
    
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

            statusLayout_.setVisibility(View.VISIBLE);
            statusLayout_.animate().setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            statusLayout_.setVisibility(show ? View.VISIBLE
                                    : View.GONE);
                        }
                    });

            topLayout_.setVisibility(View.VISIBLE);
            topLayout_.animate().setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            topLayout_.setVisibility(show ? View.GONE
                                    : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            statusLayout_.setVisibility(show ? View.VISIBLE : View.GONE);
            topLayout_.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    
    
    
    
    
    
    
    
    
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    public void onSelectPicture(View view) {
        Log.d(log_name, "onSelectPicture");
         Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
         startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }
        
        if(requestCode != PICK_FROM_CAMERA) {
            return;
        }
        
        
        
        ImageButton profileButton = (ImageButton)findViewById(R.id.button_profile_pic);
        photo_ = (Bitmap) data.getExtras().get("data");
        
        
        
        // Rescale the bitmap
        int height = photo_.getHeight();
        int width  = photo_.getWidth();
        
        // Figure out the longest dimension (height or width)
        // and scale it accordingly
        int newHeight;
        int newWidth;
        if(height > width) {
            Log.d(log_name, "Portrait");
            newHeight = profileButton.getMeasuredHeight();
            newWidth = (int)((double)newHeight * ((double)width / (double)height));
        }
        else {
            newWidth = profileButton.getMeasuredWidth();
            newHeight = (int)((double)newWidth * ((double)height / (double)width));
        }
        
        // Save to a member variable, because I can't figure out how to suck the 
        // image out of the button in onSaveInstanceState
        photo_ = Bitmap.createScaledBitmap(photo_, newWidth, newHeight, false);
        profileButton.setImageBitmap(photo_);
    }
    
    
    
    // Save the state when we rotate
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
     // Encode the bitmap into Base64
        if(null != photo_) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();  
            photo_.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] image = stream.toByteArray();
            String photoString = Base64.encodeToString(image, Base64.DEFAULT);
            
            outState.putString("photo", photoString);
        }
    }

    // Deserialize the state when we have redrawn after a rotate
    @Override  
    public void onRestoreInstanceState(Bundle savedInstanceState) {  
        super.onRestoreInstanceState(savedInstanceState);
        
        if(savedInstanceState.containsKey("photo")) {
        
            String photoString = savedInstanceState.getString("photo");
            byte[] image = Base64.decode(photoString, Base64.DEFAULT);
            photo_ = BitmapFactory.decodeByteArray(image, 0, image.length);
            
            ImageButton profileButton = (ImageButton)findViewById(R.id.button_profile_pic);
            profileButton.setImageBitmap(photo_);
        }
    }
    
    
    
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class ProfileTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {

            String token = new String();
            
            
            
//            // Wait for our authentication to come back
//            try {
//                future.wait();
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
            
            session_.getProfile(token);
            

//            try {
//                // Simulate network access.
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                return false;
//            }
//
//            for (String credential : DUMMY_CREDENTIALS) {
//                String[] pieces = credential.split(":");
//                if (pieces[0].equals(mEmail)) {
//                    // Account exists, return true if the password matches.
//                    return pieces[1].equals(mPassword);
//                }
//            }
//
//            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
//            mAuthTask = null;
            showProgress(false);

//            if (success) {
//                finish();
//            } else {
//                mPasswordView
//                        .setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
//            }
        }

        @Override
        protected void onCancelled() {
//            mAuthTask = null;
            showProgress(false);
        }
    }    
    
    
    
}
