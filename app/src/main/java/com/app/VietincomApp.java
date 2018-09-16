package com.app;

import android.app.Application;
import android.util.Log;

import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.NotificationReceived;
import com.app.vietincome.model.responses.RateResponse;
import com.app.vietincome.network.ApiClient;
import com.onesignal.OneSignal;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
