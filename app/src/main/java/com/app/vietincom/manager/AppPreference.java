package com.app.vietincom.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public enum AppPreference {

	INSTANCE;

	private static final String KEY_ACCESS_TOKEN = "access_token";
	private static final String KEY_THEME = "theme";

	public String token = "";
	public boolean darkTheme = true;
	private SharedPreferences preferences;
	public Gson mGson;

	public static void init(Context context) {
		INSTANCE.preferences =
				PreferenceManager.getDefaultSharedPreferences(context);
		INSTANCE.mGson = new GsonBuilder().create();
		INSTANCE.readData();
	}

	private void readData() {
		token = preferences.getString(KEY_ACCESS_TOKEN, "");
		darkTheme = preferences.getBoolean(KEY_THEME, true);
	}

	public void changeTheme() {
		darkTheme = !darkTheme;
		preferences.edit().putBoolean(KEY_THEME, darkTheme);
	}

	public boolean isDarkTheme() {
		return darkTheme;
	}

}
