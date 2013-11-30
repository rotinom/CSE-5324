package com.mytutor.authentication;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class AuthenticationHelper {

	private AccountManager accountManager_;
	
	Context context_;
	static String token_;
	
	static private String log_name = "AuthenticationHelper";
	
	public AuthenticationHelper(Context context) {
		// Set up our object
		context_ = context;
		accountManager_ = AccountManager.get(context_);
	}
	
	/**
	 * Removes all AccountManager accounts from the system
	 */
	public void remove_all_of_our_accounts(){
		Account[] accounts = accountManager_.getAccounts();
		Log.d(log_name, "Found " + accounts.length + " accounts");
		
		// If we have an account of our type, then return success
		for(Account account : accounts){
			Log.d(log_name, "Found account: " + account);
			Log.d(log_name, AuthenticationParams.ACCOUNT_TYPE);
			Log.d(log_name, account.type);
			if(account.type.equals(AuthenticationParams.ACCOUNT_TYPE)){
				Log.d(log_name, "Found account!!!: " + account);
				AccountManagerFuture<Boolean> ret = accountManager_.removeAccount(account, null, null);
				Log.d(log_name, "removeAccount returned: " + ret);
				
			}
		}
		token_ = null;
	}
	   
	public void logout(){
		token_ = null;
		remove_all_of_our_accounts();
	}
	
	public boolean has_account(){
		Account[] accounts = accountManager_.getAccounts();
		Log.d(log_name, "Discovered " + accounts.length + " accounts ");
		
		// If we have an account of our type, then return success
		for(Account account : accounts){
			Log.d(log_name, "Found account: " + account);
			
			if(account.type.equals(AuthenticationParams.ACCOUNT_TYPE)){
				return true;
			}
		}
		return false;
	}
	

	
	
	public String login(Activity activity) {
		
		if(null != token_){
			return token_;
		}
		
	    
      // Get our authentication token if we have a login account
      final AccountManagerFuture<Bundle> future = 
              accountManager_.getAuthTokenByFeatures(
                  AuthenticationParams.ACCOUNT_TYPE,                  // account type
                  AuthenticationParams.AUTHTOKEN_TYPE_FULL_ACCESS,    // auth token type
                  null,                                               // features
                  activity,                                            // activity
                  null,                                               // addAccountOptions
                  null,                                               // getauthtokenoptions
                  new AccountManagerCallback<Bundle>() {              // callback
                      @Override
                      public void run(AccountManagerFuture<Bundle> future) {
                          Bundle bnd = null;
                          try {
                              bnd = future.getResult();
                              token_ = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                              Log.d("SearchParams", "Got authentication token: "+ token_);

                          } catch (Exception e) {
                              e.printStackTrace();
                          }
                      }
                  }
          , null); 
     
      
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
			    try {
			        Bundle bnd = future.getResult();
		        } catch (Exception e) {
			            e.printStackTrace();
		        }
		    }
		});
		t.start();
      
		return token_;
      
      
      // handler
	
//	    Account accounts[] = accountManager_.getAccountsByType(AuthenticationParams.ACCOUNT_TYPE);
//	    
//        final AccountManagerFuture<Bundle> future = 
//                accountManager_.getAuthToken(
//                        accounts[0], 
//                        AuthenticationParams.AUTHTOKEN_TYPE_FULL_ACCESS, 
//                        null, 
//                        activity, 
//                        null, 
//                        null);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Bundle bnd = future.getResult();
//
//                    final String token_ = bnd.getString(AccountManager.KEY_AUTHTOKEN);
//                    Log.d("udinic", "GetToken Bundle is " + bnd);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
	}
	
	public String getToken() throws Exception {
		if(null == token_){
			throw new Exception("We don't have a valid security token");
		}
	    return token_;
	}
	
	public boolean has_authtoken() {
        return token_ != null;
    }	
}