package com.app;

import android.app.Application;

import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.NotificationReceived;
import com.onesignal.OneSignal;

public class VietincomApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		AppPreference.init(this);
		OneSignal.startInit(this)
				.inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
				.setNotificationReceivedHandler(new NotificationReceived())
				.unsubscribeWhenNotificationsAreDisabled(true)
				.init();
	}
}
