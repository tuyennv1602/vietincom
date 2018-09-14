package com.app.vietincome.network;

import com.app.vietincome.model.responses.CoinResponse;
import com.app.vietincome.model.responses.NewsResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

	@GET("api/get_recent_posts/")
	Call<NewsResponse> getNews();

	@GET("ticker/?convert=BTC&structure=array&limit=100")
	Observable<CoinResponse> getCoinInPage(@Query("start") int start);

	@GET("ticker/?convert=BTC&structure=array&limit=100")
	Call<CoinResponse> getCoinFirstPage();
}
