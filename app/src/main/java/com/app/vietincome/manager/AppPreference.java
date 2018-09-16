package com.app.vietincome.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.app.vietincome.model.Currency;
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
	private static final String KEY_NUMNEWS = "numnews";
	private static final String KEY_FAVOURITE = "favourite";
	private static final String KEY_CURRENCY = "currency";
	private static final String KEY_VOLUME = "volume";
	private static final String KEY_HORIZONTAL = "horizontal";
	private static final String KEY_VERTICAL = "vertical";

	public String token = "";
	public boolean darkTheme;
	private SharedPreferences preferences;
	public Gson mGson;
	public int numNews;
	private ArrayList<Integer> newsRead = new ArrayList<>();
	private ArrayList<Integer> favouriteCoin = new ArrayList<>();
	private Currency currency;
	private boolean isVolume;
	private boolean isHorizontal;
	private boolean isVertical;

	public static void init(Context context) {
		INSTANCE.preferences =
				PreferenceManager.getDefaultSharedPreferences(context);
		INSTANCE.mGson = new GsonBuilder().create();
		INSTANCE.readData();
	}

	private void readData() {
		token = preferences.getString(KEY_ACCESS_TOKEN, "");
		darkTheme = preferences.getBoolean(KEY_THEME, true);
		numNews = preferences.getInt(KEY_NUMNEWS, 0);
		currency = mGson.fromJson(preferences.getString(KEY_CURRENCY, null), Currency.class);
		isVolume = preferences.getBoolean(KEY_VOLUME, true);
		isHorizontal = preferences.getBoolean(KEY_HORIZONTAL, true);
		isVertical = preferences.getBoolean(KEY_VERTICAL, true);
		String stringData = preferences.getString(KEY_READ, null);
		if (stringData != null) {
			Type type = new TypeToken<ArrayList<Integer>>() {
			}.getType();
			newsRead = mGson.fromJson(stringData, type);
		}
		String coinData = preferences.getString(KEY_FAVOURITE, null);
		if (coinData != null) {
			Type type = new TypeToken<ArrayList<Integer>>() {
			}.getType();
			favouriteCoin = mGson.fromJson(coinData, type);
		}
	}

	public boolean isVolume() {
		return isVolume;
	}

	public void setVolume() {
		isVolume = !isVolume;
		preferences.edit().putBoolean(KEY_VOLUME, isVolume).apply();
	}

	public boolean isHorizontal() {
		return isHorizontal;
	}

	public void setHorizontal() {
		isHorizontal = !isHorizontal;
		preferences.edit().putBoolean(KEY_HORIZONTAL, isHorizontal).apply();
	}

	public boolean isVertical() {
		return isVertical;
	}

	public void setVertical() {
		isVertical = !isVertical;
		preferences.edit().putBoolean(KEY_VERTICAL, isVertical).apply();
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

	public void addNewsRead(Integer newsId) {
		newsRead.add(newsId);
		preferences.edit().putString(KEY_READ, mGson.toJson(newsRead)).apply();
	}

	public int getNumNews() {
		return numNews;
	}

	public void setNumNews() {
		numNews++;
		preferences.edit().putInt(KEY_NUMNEWS, numNews).apply();
	}

	public void clearNumNews() {
		numNews = 0;
		preferences.edit().putInt(KEY_NUMNEWS, numNews).apply();
	}

	public void addFavourite(Integer coinId) {
		favouriteCoin.add(coinId);
		preferences.edit().putString(KEY_FAVOURITE, mGson.toJson(favouriteCoin)).apply();
	}

	public void removeFavourite(Integer coinId) {
		for (int i = favouriteCoin.size() - 1; i <= 0; i--) {
			if (favouriteCoin.get(i) == coinId) {
				favouriteCoin.remove(i);
				break;
			}
		}
		preferences.edit().putString(KEY_FAVOURITE, mGson.toJson(favouriteCoin)).apply();
	}

	public ArrayList<Integer> getFavouriteCoin() {
		return favouriteCoin;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
		preferences.edit().putString(KEY_CURRENCY, mGson.toJson(currency)).apply();
	}

	public Currency getCurrency(){
		if(this.currency == null){
			return new Currency("USD", "$", "");
		}
		return this.currency;
	}
}
