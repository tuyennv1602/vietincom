package com.app.vietincome.manager;

import android.util.Log;

import com.app.vietincome.model.Notification;
import com.app.vietincome.utils.Constant;
import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

import org.greenrobot.eventbus.EventBus;

public class NotificationReceived implements OneSignal.NotificationReceivedHandler {
	@Override
	public void notificationReceived(OSNotification notification) {
		Log.d("__noti", "notificationReceived: " + notification.payload.additionalData.toString());
		if (notification.isAppInFocus) {
			Notification message = AppPreference.INSTANCE.mGson.fromJson(notification.payload.additionalData.toString()
					, Notification.class);
			if (message.getType() == Constant.TYPE_NEWS) {
				EventBus.getDefault().post(new EventBusListener.UpdateNews());
			}
		}
	}
}
