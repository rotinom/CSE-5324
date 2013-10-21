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

	public void testSalt(){
		String salt1 = Password.salt();
		String salt2 = Password.salt();
		assertNotSame(salt1, salt2);
	}
	
	public void testHash() {
		try{
			String salt = Password.salt();
			
			// Verify that passwords are repeatable
			String pw1 = Password.hash("password1", salt);
			String pw2 = Password.hash("password1", salt);
			assertEquals(pw1,  pw2);			
		}
		catch(Exception e){
			fail("Caught an unexpected exception");
		}
	}

	public void testAuthenticate() {
		// Verify that the given password will rehash with a stored password
		String salt = Password.salt();
		try{
			String pw = Password.hash("password1", salt);
			assertTrue(Password.authenticate("password1",pw));
		}
		catch(Exception e){
			fail("Caught an unexpected exception");
		}
	}

}
