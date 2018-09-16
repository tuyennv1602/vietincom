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

	@GET("ticker/?structure=array&limit=100")
	Observable<CoinResponse> getCoinInPage(@Query("start") int start,
	                                       @Query("convert") String convert);

	@GET("ticker/?structure=array&limit=100")
	Call<CoinResponse> getCoinFirstPage(@Query("convert") String convert);

	@GET("price?fsym=USD&tsyms=BTC")
	Call<RateResponse> getRate();
}
