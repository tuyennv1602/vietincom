package com.app.vietincome.network;

import com.app.vietincome.model.responses.BaseResponse;
import com.app.vietincome.model.responses.UserResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiV2Interface {

	@POST("login.php")
	Call<UserResponse> login(@Body RequestBody body);

	@POST("forgotpassword.php")
	Call<BaseResponse> forgotPassword(@Body RequestBody body);

	@POST("signup.php")
	Call<UserResponse> signUp(@Body RequestBody body);

	@POST("logout.php")
	Call<BaseResponse> logout();
}
