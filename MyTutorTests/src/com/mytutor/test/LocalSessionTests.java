package com.mytutor.test;

import com.mytutor.LocalSession;

import junit.framework.TestCase;

public class LocalSessionTests extends TestCase {

	private LocalSession localSession_ = new LocalSession();
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testLocalSession() {
		fail("Not yet implemented");
	}

	public void testValidate_username() {
		assertTrue(localSession_.validate_username("foo"));
		assertTrue(localSession_.validate_username("bar"));
		assertTrue(localSession_.validate_username("alice"));
		assertTrue(localSession_.validate_username("bob"));
	}

	public void testValidate_password() {
		fail("Not yet implemented");
	}

	public void testLogin() {
		fail("Not yet implemented");
	}

	public void testChange_password() {
		fail("Not yet implemented");
	}

	public void testSearch() {
		fail("Not yet implemented");
	}

}
