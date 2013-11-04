package com.mytutor.authentication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;

public class AuthenticationManager {

	public static final String log_title = "AuthenticationManager";
	
	public static boolean  isLoggedIn(Context context){
		
        // Get an account manager
        AccountManager am = AccountManager.get(context);
        Account[] accounts = am.getAccounts();
        
        Log.d(log_title, "Found " + accounts.length + " accounts");
        for(int i = 0; i < accounts.length; ++i){
        	Log.d(log_title, "Found Account for: " + accounts[i].toString());
        }
        
        return true;
	}
	
}
