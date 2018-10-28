package com.app.vietincome.network;

import com.app.vietincome.model.responses.AvatarResponse;
import com.app.vietincome.model.responses.BaseResponse;
import com.app.vietincome.model.responses.UserResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiV2Interface {

	@POST("login.php")
	Call<UserResponse> login(@Body RequestBody body);

	@POST("forgotpassword.php")
	Call<BaseResponse> forgotPassword(@Body RequestBody body);

	@POST("signup.php")
	Call<UserResponse> signUp(@Body RequestBody body);

	@POST("logout.php")
	Call<BaseResponse> logout();

	@POST("becomevip.php")
	Call<BaseResponse> activeVip(@Body RequestBody body);

	@POST("changepassword.php")
	Call<BaseResponse> changePassword(@Body RequestBody body);

	@POST("edit.php")
	Call<BaseResponse> editProfile(@Body RequestBody body);

	@Multipart
	@POST("avatar.php")
	Call<AvatarResponse> uploadAvatar(@Part MultipartBody.Part image);

	@GET("info.php")
	Call<UserResponse> getProfile();
}
