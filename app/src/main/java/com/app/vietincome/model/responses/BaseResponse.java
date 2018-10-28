package com.app.vietincome.model.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseResponse {

	@SerializedName("success")
	@Expose
	private boolean isSuccess;

	@SerializedName("message")
	@Expose
	private String message;

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean success) {
		isSuccess = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isExpired(){
		return message.equals("Your account is expired");
	}
}
