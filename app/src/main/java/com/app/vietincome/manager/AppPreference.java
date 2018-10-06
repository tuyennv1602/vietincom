package com.app.vietincome.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.app.vietincome.model.Currency;
import com.app.vietincome.model.News;
import com.app.vietincome.model.Portfolio;
import com.app.vietincome.model.responses.TokenResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public enum AppPreference {

	INSTANCE;

	private static final String KEY_THEME = "theme";
	private static final String KEY_READ = "read";
	private static final String KEY_NUMNEWS = "numnews";
	private static final String KEY_FAVOURITE = "favourite";
	private static final String KEY_CURRENCY = "currency";
	private static final String KEY_VOLUME = "volume";
	private static final String KEY_HORIZONTAL = "horizontal";
	private static final String KEY_VERTICAL = "vertical";
	private static final String KEY_NEWS = "news";
	private static final String KEY_TOKEN = "token";
	private static final String KEY_EVENTS = "events";
	private static final String KEY_PORTFOLIO = "portfolio";

	public boolean darkTheme;
	private SharedPreferences preferences;
	public Gson mGson;
	public int numNews;
	public int numEvents;
	private ArrayList<Integer> newsRead = new ArrayList<>();
	private ArrayList<Integer> favouriteCoin = new ArrayList<>();
	private ArrayList<News> news = new ArrayList<>();
	private Currency currency;
	private TokenResponse token;
	private boolean isVolume;
	private boolean isHorizontal;
	private boolean isVertical;
	private ArrayList<Portfolio> portfolios = new ArrayList<>();

	public static void init(Context context) {
		INSTANCE.preferences =
				PreferenceManager.getDefaultSharedPreferences(context);
		INSTANCE.mGson = new GsonBuilder().create();
		INSTANCE.readData();
	}

	private void readData() {
		darkTheme = preferences.getBoolean(KEY_THEME, true);
		numNews = preferences.getInt(KEY_NUMNEWS, 0);
		numEvents = preferences.getInt(KEY_EVENTS, 0);
		currency = mGson.fromJson(preferences.getString(KEY_CURRENCY, null), Currency.class);
		token = mGson.fromJson(preferences.getString(KEY_TOKEN, null), TokenResponse.class);
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
		String newsData = preferences.getString(KEY_NEWS, null);
		if (newsData != null) {
			Type type = new TypeToken<ArrayList<News>>() {
			}.getType();
			news = mGson.fromJson(newsData, type);
		}
		String portData = preferences.getString(KEY_PORTFOLIO, null);
		if (portData != null) {
			Type type = new TypeToken<ArrayList<Portfolio>>() {
			}.getType();
			portfolios = mGson.fromJson(portData, type);
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

	public int getNumEvents() {
		return numEvents;
	}

	public void setNumEvents() {
		numEvents++;
		preferences.edit().putInt(KEY_EVENTS, numEvents).apply();
	}

	public void clearNumEvents() {
		numEvents = 0;
		preferences.edit().putInt(KEY_EVENTS, numEvents).apply();
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

	public Currency getCurrency() {
		if (this.currency == null) {
			return new Currency("USD", "$", "");
		}
		return this.currency;
	}

	public TokenResponse getToken() {
		return token;
	}

	public void setToken(TokenResponse token) {
		this.token = token;
		preferences.edit().putString(KEY_TOKEN, mGson.toJson(token)).apply();
	}

	public ArrayList<News> getNews() {
		return news;
	}

	public void setNews(ArrayList<News> news) {
		this.news = news;
		preferences.edit().putString(KEY_NEWS, mGson.toJson(news)).apply();
	}

	public ArrayList<Portfolio> getPortfolios() {
		return portfolios;
	}

	public Portfolio getPortfolioById(int id) {
		for (Portfolio item : portfolios) {
			if (item.getId() == id) {
				return item;
			}
		}
		return new Portfolio();
	}

	public void addPortfolio(Portfolio portfolio) {
		if (portfolios.size() == 0) {
			portfolios.add(portfolio);
		} else {
			int position = -1;
			for (int i = 0; i < portfolios.size(); i++) {
				if (portfolios.get(i).getId() == portfolio.getId()) {
					position = i;
					break;
				}
			}
			if (position == -1) {
				portfolios.add(portfolio);
			} else {
				portfolios.set(position, portfolio);
			}
		}
		preferences.edit().putString(KEY_PORTFOLIO, mGson.toJson(portfolios)).apply();
	}

	public void removePortfolio(Portfolio portfolio){
		for(int i = portfolios.size() - 1; i >= 0; i--){
			if(portfolio.getId() == portfolios.get(i).getId()){
				this.portfolios.remove(i);
				break;
			}
		}
		preferences.edit().putString(KEY_PORTFOLIO, mGson.toJson(portfolios)).apply();
	}

}
