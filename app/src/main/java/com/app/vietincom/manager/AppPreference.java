package com.app.vietincom.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public enum AppPreference {

	INSTANCE;

	private static final String KEY_ACCESS_TOKEN = "access_token";
	private static final String KEY_THEME = "theme";
	private static final String KEY_READ = "read";

	public String token = "";
	public boolean darkTheme = true;
	private SharedPreferences preferences;
	public Gson mGson;
	private ArrayList<Integer> newsRead = new ArrayList<>();

	public static void init(Context context) {
		INSTANCE.preferences =
				PreferenceManager.getDefaultSharedPreferences(context);
		INSTANCE.mGson = new GsonBuilder().create();
		INSTANCE.readData();
	}

	private void readData() {
		token = preferences.getString(KEY_ACCESS_TOKEN, "");
		darkTheme = preferences.getBoolean(KEY_THEME, true);
		String stringData = preferences.getString(KEY_READ, null);
		if (stringData != null) {
			Type type = new TypeToken<ArrayList<Integer>>() {
			}.getType();
			newsRead = mGson.fromJson(stringData, type);
		}
	}

	public void changeTheme() {
		darkTheme = !darkTheme;
		preferences.edit().putBoolean(KEY_THEME, darkTheme).apply();
	}

	public boolean isDarkTheme() {
		return darkTheme;
	}

	public ArrayList<Integer> getNewsRead() {
		return newsRead;
	}

	public void addNewsRead(Integer newsId){
		newsRead.add(newsId);
		preferences.edit().putString(KEY_READ, mGson.toJson(newsRead)).apply();
	}
}
