package com.app.vietincome.network;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
	private static Retrofit retrofit = null;

	private static Retrofit getClient(String url) {
		if (retrofit == null) {
			retrofit = new Retrofit.Builder()
					.baseUrl(url)
					.client(httpClient(createLogging()))
					.addConverterFactory(GsonConverterFactory.create())
					.build();

		}
		return retrofit;
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
		httpClient.connectTimeout(60, TimeUnit.SECONDS);
		return httpClient.build();
	}


	private static Request getOriginalRequest(Interceptor.Chain chain) {
		return chain.request();
	}

	public static ApiInterface getNewsService(){
		return getService(AppConfig.NEWS_API);
	}
}
