package com.mytutor.session;

import com.mytutor.search.SearchParams;

public interface Session {
	
	enum SessionStateEnum{
		OK,
		LoginRequired,
		LoginFailed,
		UserNameInUse,
		PasswordNotComplicated
	};

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
	SessionStateEnum register_user(String username, String password);
	
	
	/**
	 * This function will allow the session to log in.
	 * 
	 * @param name The username to login with
	 * @param password The user's password
	 * @return OK if login was successful, LoginFailed otherwise
	 */
	SessionStateEnum login(String name, String password);
	
	/**
	 * Allows the user to change their password
	 * 
	 * @param username The username to login with
	 * @param old_password The users old password
	 * @param new_password The users new password
	 * @return 
	 * 		OK if successful,
	 * 		LoginRequired if the session is not logged in
	 * 		LoginFailed if old_password is incorrect
	 *      PasswordNotComplicated if the password is not complicated enough
	 */
	SessionStateEnum change_password(String username, String old_password, String new_password);
	
	/**
	 * Allows the caller to search the database for relevant information
	 * 
	 * @param params The search parameters to use for the search
	 * @param results[out] The results of the search
	 * 
	 * @return 
	 * 		OK if successful
	 * 		LoginRequired if login has not yet been called
	 */
	SessionStateEnum search(SearchParams[] params);
	
	
	
}
