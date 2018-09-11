package com.app.vietincome.manager.interfaces;

public class NetworkInterface {
	interface OnService {
		void dataResponse(Object data, boolean success, boolean active);
	}
}
