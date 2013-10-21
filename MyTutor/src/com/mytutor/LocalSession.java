package com.mytutor;


import java.security.SecureRandom;
import java.util.*;

import com.mytutor.Session.SessionStateEnum;


public class LocalSession implements Session {

	//private Map<String> userNames_ = new HashMap<String>();
	
	public LocalSession() {
	}
	
	@Override
	public boolean validate_username(String name) {
		return false;
		//return !userNames_.contains(name);
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
		
		// Create a 256-bit salt
		SecureRandom sr = new SecureRandom();
		byte[] salt = new byte[132];
		sr.nextBytes(salt);
		
		
		
		// A-OK
		return SessionStateEnum.OK;
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
