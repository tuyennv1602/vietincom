package com.app.vietincome.model.responses;

import com.app.vietincome.model.Profile;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserResponse extends BaseResponse {

	@SerializedName("data")
	@Expose
	private Profile profile;

	public Profile getProfile() {
		return profile;
	}
}
