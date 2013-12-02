package com.mytutor.authentication;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.mytutor.R;
import com.mytutor.session.ServerSession;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity {
	
	
    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";

    public static final String KEY_ERROR_MESSAGE = "ERR_MSG";

    public final static String PARAM_USER_PASS = "USER_PASS";

    private final int REQ_SIGNUP = 1;

    private final String TAG = this.getClass().getSimpleName();

    private AccountManager accountManager_;
    private String mAuthTokenType;
	
	private final static String log_name = "AuthenticatorActivity";

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	
	private ServerSession session_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_authenticate);
		
		setTitle("Authenticate to MyTutor Service");
		
		try {
			session_ = ServerSession.create();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		accountManager_ = AccountManager.get(getBaseContext());
		
		
        mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);
        if (mAuthTokenType == null) {
            mAuthTokenType = AuthenticationParams.AUTHTOKEN_TYPE_FULL_ACCESS;
        }

		// Set up the login form.
		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.authenticate, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}
	
	
	
	private void finishLogin(Intent intent) {
		Log.d(log_name, "Finishing login");
	    String userName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
	    String password = intent.getStringExtra(PARAM_USER_PASS);
	    
	    Log.d(log_name, "Creating account with username/password: " + userName + "/" + password);
	    final Account account = new Account(userName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));
	    
	    
	    if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
	    	Log.d(log_name, "ARG_IS_ADDING_NEW_ACCOUNT: true");
	        String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
	        String authtokenType = mAuthTokenType;
	        
	        
	        // Creating the account on the device and setting the auth token we got
	        // (Not setting the auth token will cause another call to the server to authenticate the user)
	        accountManager_.addAccountExplicitly(account, password, null);
	        accountManager_.setAuthToken(account, authtokenType, authtoken);
	    } else {
	        accountManager_.setPassword(account, password);
	    }
	    setAccountAuthenticatorResult(intent.getExtras());
	    setResult(RESULT_OK, intent);
	    finish();
	}
	
	

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// The ViewPropertyAnimator APIs are not available, so simply show
		// and hide the relevant UI components.
		mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
		mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		
		Intent res_;
		
		@Override
		protected Boolean doInBackground(Void... params) {

			final String userName = ((TextView) findViewById(R.id.email)).getText().toString();
		    final String userPass = ((TextView) findViewById(R.id.password)).getText().toString();
			
	    	String authtoken = session_.login(userName,  userPass);
	    	
	    	if(null == authtoken || "" == authtoken){
	    		return false;
	    	}
	    	
	        res_ = new Intent();
	        res_.putExtra(AccountManager.KEY_ACCOUNT_NAME, userName);
	        res_.putExtra(AccountManager.KEY_ACCOUNT_TYPE, AuthenticationParams.ACCOUNT_TYPE);
	        res_.putExtra(AccountManager.KEY_AUTHTOKEN, authtoken);
	        res_.putExtra(PARAM_USER_PASS, userPass);
	        

	        
	        
			// TODO: register the new account here.
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
	            finishLogin(res_);
	            showProgress(false);
			} else {
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
