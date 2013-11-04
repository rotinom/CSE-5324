package com.mytutor.authentication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyTutorAuthenticationService extends Service {

	@Override
    public IBinder onBind(Intent intent) {
		MyTutorAuthenticator authenticator = new MyTutorAuthenticator(this);
        return authenticator.getIBinder();
    }

}
