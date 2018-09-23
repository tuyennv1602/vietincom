package com.app.vietincome.network;

import com.app.vietincome.model.responses.ChartResponse;
import com.app.vietincome.model.responses.CoinResponse;
import com.app.vietincome.model.responses.GlobalResponse;
import com.app.vietincome.model.responses.RateResponse;
import com.app.vietincome.model.responses.NewsResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Path;
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

	@GET("{symbol}")
	Call<ChartResponse> getAllHistory(@Path("symbol") String symbol);

	@GET("{day}day/{symbol}")
	Call<ChartResponse> getHistoryByDay(@Path("day") int day,
	                                    @Path("symbol") String symbol);

	@GET("global")
	Call<GlobalResponse> getGlobalData();

	@GET("ticker/?limit=10&sort=market_cap&structure=array&convert=BTC")
	Call<CoinResponse> getTopMarketCap();

	@GET("ticker/?limit=10&sort=volume_24h&structure=array&convert=BTC")
	Call<CoinResponse> getTopVolume();
}
