package com.mytutor;

import android.app.Activity;
import android.os.Bundle;

/**
 * Activity which displays an account register screen to the user.
 */
public class RegisterActivity extends Activity {

	// Values for email and password at the time of the login attempt.
	//private String mEmail;
	//private String mPassword;

	// UI references.
	//private EditText mEmailView;
	//private EditText mPasswordView;
	//private View mRegisterFormView;
	//private View mRegisterStatusView;
	//private TextView mRegisterStatusMessageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);


	}
}
