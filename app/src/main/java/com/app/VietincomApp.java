package com.app;

import android.app.Application;

import com.app.vietincom.manager.AppPreference;
import com.onesignal.OneSignal;

public class VietincomApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		AppPreference.init(this);
		OneSignal.startInit(this)
				.inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
				.unsubscribeWhenNotificationsAreDisabled(true)
				.init();
	}
}
