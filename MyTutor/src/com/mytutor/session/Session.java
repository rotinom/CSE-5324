package com.mytutor.session;

import android.widget.ImageView;

import com.mytutor.search.SearchParams;

public interface Session {

	/**
	 * This function will allow the caller to determine if the username 
	 * is unused
	 * 
	 * NOTE - You are not required to be logged int o run this function
	 * 
	 * @param name The username to validate
	 * @return true if the username is available, false otherwise
	 */
	boolean validate_username(String name);
	
	/**
	 * This function is used to ensure that a password is valid against the
	 * complexity requirements of the system
	 * 
	 * NOTE - You are not required to be logged in to run this function
	 * 
	 * @param password The password to validate
	 * @return true if the password is complex enough, false otherwise
	 */
	boolean validate_password(String password);
	
	/**
	 * Register the user in the system
	 * 
	 * @param username The username to register
	 * @param password The user's password
	 * @return
	 */
	boolean register_user(String username, String password);
	
	
	/**
	 * This function will allow the session to log in.
	 * 
	 * @param name The username to login with
	 * @param password The user's password
	 * @return OK if login was successful, LoginFailed otherwise
	 */
	String login(String name, String password);
	
	public void getProfilePicAsync(String email, ImageView dest);
	
	
}
