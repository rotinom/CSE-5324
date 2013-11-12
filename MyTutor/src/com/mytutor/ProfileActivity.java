package com.mytutor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

public class ProfileActivity extends Activity {

    private Uri mImageCaptureUri;

    private final static int PICK_FROM_CAMERA = 0;

    private final static String log_name = "ProfileActivity";

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
        
        ImageButton profilePic = (ImageButton)findViewById(R.id.button_profile_pic);
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        profilePic.setImageBitmap(photo);
        

    }


}
