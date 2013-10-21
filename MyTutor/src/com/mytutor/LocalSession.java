package com.mytutor;


import java.security.SecureRandom;
import java.util.*;

import com.mytutor.Session.SessionStateEnum;
import com.mytutor.session.Password;


public class LocalSession implements Session {

	private Map<String, String> userNames_ = new HashMap<String, String>();
	
	public LocalSession() {
	}
	
	@Override
	public boolean validate_username(String name) {
		return !userNames_.containsKey(name);
	}

	/**
	 * Only checks length >= 6
	 */
	@Override
	public boolean validate_password(String password) {
		return password.length() >= 6;
	}
	
	/**
	 * Register the user in the system
	 * 
	 * @param username The username to register
	 * @param password The user's password
	 * @return
	 */
	public SessionStateEnum register_user(String username, String password){
		
		// Valid username?
		if(!validate_username(username)){
			return SessionStateEnum.UserNameInUse;
		}
		
		// Valid password?
		if(!validate_password(password)){
			return SessionStateEnum.PasswordNotComplicated;
		}
		
		SessionStateEnum ret = SessionStateEnum.OK;
		
		// Create a 256-bit salt
		String salt = Password.salt();
		String hashed_password = Password.hash(password, salt);
		
		// Save off the username and password
		userNames_.put(username,  hashed_password);
		
		
		// A-OK
		return ret;
	}

	@Override
	public SessionStateEnum login(String name, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SessionStateEnum change_password(String username,
			String old_password, String new_password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SessionStateEnum search(SearchParams[] params) {
		// TODO Auto-generated method stub
		return null;
	}
}
