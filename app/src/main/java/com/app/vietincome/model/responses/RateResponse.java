package com.app.vietincome.model.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RateResponse {

	@SerializedName("BTC")
	@Expose
	private double rate;

	public double getRate() {
		return rate;
	}
}
