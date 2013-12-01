package com.mytutor.search;

import com.mytutor.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class TutorReview {
   	public TutorReview()
   	{
   		
   	}
    
    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
    	final Dialog dialog = new Dialog(mContext);
        dialog.setTitle("Rate " + "tutorName");
        
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        View v = inflater.inflate(R.layout.rating_popup, null);
        View innerView = v.findViewById(R.id.LinearLayout);
        
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(innerView);
        final AlertDialog alertDialog = builder.create();
        final RatingBar ratingBar = (RatingBar) v.findViewById(R.id.rating);
    	final TextView ratingView = (TextView) v.findViewById(R.id.ratingLabel);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            	ratingView.setText(String.format("(%.1f)", rating));
            	ratingBar.setRating(rating);
        }});

        final Button submitButton = (Button) v.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				//submit review to db
				alertDialog.dismiss();
				
				//todo submit rating to database with comment
			}
        });

        final Button cancelButton = (Button) v.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	alertDialog.dismiss();
            }
        }); 
        alertDialog.show();
    }
}