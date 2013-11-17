package com.mytutor;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

public class ProfileActivity extends Activity {

    private Uri mImageCaptureUri;

    private final static int PICK_FROM_CAMERA = 0;

    private final static String log_name = "ProfileActivity";
    
    private Bitmap photo_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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
}
