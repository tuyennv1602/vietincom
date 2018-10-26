package com.app.vietincome.network;

import com.app.vietincome.manager.AppPreference;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

	private static final String AUTHENTICATION = "session_id";

	private static Retrofit getClient(String url) {
		return new Retrofit.Builder()
				.baseUrl(url)
				.client(httpClient(createLogging(), false))
				.addConverterFactory(GsonConverterFactory.create())
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.build();
	}

	private static ApiInterface getService(String url) {
		return getClient(url).create(ApiInterface.class);
	}

	private static ApiV2Interface getService(){
		return getClient().create(ApiV2Interface.class);
	}

	private static HttpLoggingInterceptor createLogging() {
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
		logging.setLevel(HttpLoggingInterceptor.Level.BODY);
		return logging;
	}

	public static Retrofit getClient() {
		return new Retrofit.Builder()
				.baseUrl(AppConfig.API_V2)
				.client(httpClient(createLogging(), true))
				.addConverterFactory(GsonConverterFactory.create())
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.build();
	}

	private static OkHttpClient httpClient(HttpLoggingInterceptor logging, boolean checkToken) {
		OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
		httpClient.addInterceptor(chain -> {
			Request original = chain.request();
			Request request = (checkToken && AppPreference.INSTANCE.getProfile() != null)
					? getRequestToken(original)
					: getRequest(original);
			return chain.proceed(request);
		});
		httpClient.addInterceptor(logging);
		httpClient.connectTimeout(90, TimeUnit.SECONDS);
		return httpClient.build();
	}

	private static Request getRequestToken(Request original) {
		return original.newBuilder()
				.header(AUTHENTICATION, AppPreference.INSTANCE.getProfile().getSessionId())
				.method(original.method(), original.body())
				.build();
	}

	private static Request getRequest(Request original) {
		return original.newBuilder()
				.method(original.method(), original.body())
				.build();
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

	public static ApiInterface getEventService() {
		return getService(AppConfig.EVENTS);
	}

	public static ApiV2Interface getApiV2Service(){
		return getService();
	}
}
