package com.app.vietincome.network;

import com.app.vietincome.model.responses.CoinResponse;
import com.app.vietincome.model.responses.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

	@GET("api/get_recent_posts/")
	Call<NewsResponse> getNews();

	@GET("ticker/")
	Call<CoinResponse> getAllCoin();
}
