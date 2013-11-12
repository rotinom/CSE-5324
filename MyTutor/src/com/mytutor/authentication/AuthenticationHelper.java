package com.mytutor.authentication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.mytutor.R;

public class AuthenticationHelper {

	private AccountManager accountManager_;
	
	Context context_;
	
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
	
	public String get_username(){
		return "";
	}
	
	public String get_password(){
		return "";
	}
	
	
}
