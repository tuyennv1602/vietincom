package com.app.vietincome.network;

import com.app.vietincome.model.responses.CoinResponse;
import com.app.vietincome.model.responses.RateResponse;
import com.app.vietincome.model.responses.NewsResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

	@GET("api/get_recent_posts/")
	Call<NewsResponse> getNews();

	@GET("api/get_recent_posts/")
	Observable<NewsResponse> getNewsInPage(@Query("page") int page);

	@GET("ticker/?structure=array&limit=100&convert=BTC")
	Observable<CoinResponse> getCoinInPage(@Query("start") int start);

	@GET("ticker/?structure=array&limit=100&convert=BTC")
	Call<CoinResponse> getCoinFirstPage();

	@GET("price?fsym=USD")
	Call<RateResponse> getRate(@Query("tsyms") String toSymbol);
}
