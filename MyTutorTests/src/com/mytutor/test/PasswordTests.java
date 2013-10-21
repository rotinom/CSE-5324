package com.mytutor.test;



import android.util.Log;

import com.mytutor.session.Password;

import junit.framework.TestCase;

public class PasswordTests extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testHash() {
		try{
			String salt = Password.salt();
			
			// Verify that passwords are repeatable
			String pw1 = Password.hash("password1", salt);
			String pw2 = Password.hash("password1", salt);
			assertEquals(pw1,  pw2);
			
			// Verify that the given password will rehash with a stored
			// password
			assertTrue(Password.authenticate("password1",pw1));
			assertTrue(Password.authenticate("password1",pw2));
			
		}
		catch(Exception e){
			
		}
	}

	public void testAuthenticate() {
		fail("Not yet implemented");
	}

}
