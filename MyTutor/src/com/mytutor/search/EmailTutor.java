package com.mytutor.search;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.mytutor.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EmailTutor {
   	public EmailTutor()
   	{
   		
   	}
    
    public static void showEmailDialog(final Context mContext, final SharedPreferences.Editor editor,
    		                           final String toName, final String toAddress, 
    		                           final String fromName, final String fromAddress) {
    	final Dialog dialog = new Dialog(mContext);
        dialog.setTitle("Email " + "tutorName");
        
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        View v = inflater.inflate(R.layout.email_popup, null);
        View innerView = v.findViewById(R.id.LinearLayout);
        
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(innerView);
        final AlertDialog alertDialog = builder.create();
        final EditText message = (EditText) v.findViewById(R.id.comments);

        final Button submitButton = (Button) v.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				//submit review to db
				alertDialog.dismiss();
	
				 //create empty post parameters for php request
				 ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				 postParameters.add(new BasicNameValuePair("toName", toName));
				 postParameters.add(new BasicNameValuePair("toAddress", toAddress));
				 postParameters.add(new BasicNameValuePair("fromName", fromName));
				 postParameters.add(new BasicNameValuePair("fromAddress", fromAddress));
				 postParameters.add(new BasicNameValuePair("body", message.getText().toString()));
				 String response = null;
			          
			     // call executeHttpPost method passing necessary parameters 
				 try {
				  //send off http request to php script on omega
				     Time start = new Time();
			         Time stop = new Time();
				     start.setToNow();
					 response = CustomHttpClient.executeHttpPost("http://omega.uta.edu/~jwe0053/email.php", postParameters);
					 stop.setToNow();
					 Log.d("SearchResults","email.php took: " + (stop.toMillis(true)-start.toMillis(true)) + " milliseconds");
			     
					 // store the result returned by PHP script that runs MySQL query
					 String result = response.toString();
					 
					 new AlertDialog.Builder(mContext)
					 .setTitle("Email")
					 .setMessage(result)
					 .setPositiveButton("Close", new DialogInterface.OnClickListener() {
					     @Override
					     public void onClick(DialogInterface dialog, int which) {
                            //do nothing
					     }
					 })
					 .create()
					 .show();
				 }
				 catch (Exception e) {
					 Log.e("log_tag","Error in http connection!!" + e.toString());     
				 }
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