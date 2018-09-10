package com.app.vietincom.network;

import com.app.vietincom.model.responses.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

	@GET("api/get_recent_posts/")
	Call<NewsResponse> getNews();
}
