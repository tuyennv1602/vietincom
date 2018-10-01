package com.app.vietincome.network;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

	private static Retrofit getClient(String url) {
		return new Retrofit.Builder()
				.baseUrl(url)
				.client(httpClient(createLogging()))
				.addConverterFactory(GsonConverterFactory.create())
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.build();
	}

	private static ApiInterface getService(String url) {
		return getClient(url).create(ApiInterface.class);
	}

	private static HttpLoggingInterceptor createLogging() {
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		logging.setLevel(HttpLoggingInterceptor.Level.BODY);
		return logging;
	}

	private static OkHttpClient httpClient(HttpLoggingInterceptor logging) {
		OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
		httpClient.addInterceptor(chain -> {
			Request original = getOriginalRequest(chain);
			return chain.proceed(original);
		});
		httpClient.addInterceptor(logging);
		httpClient.connectTimeout(90, TimeUnit.SECONDS);
		return httpClient.build();
	}


	private static Request getOriginalRequest(Interceptor.Chain chain) {
		return chain.request();
	}

	public static ApiInterface getNewsService() {
		return getService(AppConfig.NEWS_API);
	}

	public static ApiInterface getAllCoinService() {
		return getService(AppConfig.ALL_COIN_API);
	}

	public static ApiInterface getRateService() {
		return getService(AppConfig.RATE);
	}

	public static ApiInterface getChartService() {
		return getService(AppConfig.CHART_COIN);
	}

	public static ApiInterface getEventService(){
		return getService(AppConfig.EVENTS);
	}
}
