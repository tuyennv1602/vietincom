package com.app.vietincome.network;

import com.app.vietincome.model.Coin;
import com.app.vietincome.model.responses.CoinResponse;
import com.app.vietincome.model.responses.TokenResponse;
import com.app.vietincome.model.responses.ChartResponse;
import com.app.vietincome.model.responses.EventResponse;
import com.app.vietincome.model.responses.GlobalResponse;
import com.app.vietincome.model.responses.RateResponse;
import com.app.vietincome.model.responses.NewsResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
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

	@GET("v1/events?max=150&showMetadata=true&page=1")
	Call<EventResponse> getEvent(@Query("access_token") String accessToken,
	                             @Query("dateRangeStart") String dateStart,
	                             @Query("dateRangeEnd") String dateEnd,
	                             @Query("coins") String coins);

	@GET("v1/events?max=150&showMetadata=true")
	Observable<EventResponse> getEventInPage(@Query("access_token") String accessToken,
	                             @Query("dateRangeStart") String dateStart,
	                             @Query("dateRangeEnd") String dateEnd,
	                             @Query("page") int page,
	                             @Query("coins") String coins);

	@GET("oauth/v2/token?grant_type=client_credentials")
	Call<TokenResponse> getToken(@Query("client_id") String clientId,
	                             @Query("client_secret") String clientSecret);

	@GET("v1/coins")
	Call<List<Coin>> getCoins(@Query("access_token") String accessToken);

	@GET("listings/")
	Call<CoinResponse> getAllCoinId();

	@GET("ticker/{id}/?convert=BTC&structure=array")
	Call<CoinResponse> getCoinDetail(@Path("id") int id);

	@GET("ticker/{id}/?convert=BTC&structure=array")
	Observable<CoinResponse> getCoinDetailObservable(@Path("id") int id);

}
