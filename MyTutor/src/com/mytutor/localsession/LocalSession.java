package com.mytutor.localsession;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.mytutor.SearchParams;
import com.mytutor.Session;

public class LocalSession extends SQLiteOpenHelper implements Session {

	static CursorFactory cursor_factory;
	
	public LocalSession(Context context) {
		// Call the parent class
		super(context, "session_database", cursor_factory, 1);
		
	}
	
	@Override
	public boolean validate_username(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validate_password(String password) {
		// TODO Auto-generated method stub
		return false;
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

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}