package com.app.vietincome.model.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RateResponse {

	@SerializedName(value = "rate", alternate = {"AUD", "BRL", "CAD", "CHF", "CLP", "CNY", "CZK", "DKK", "EUR", "GBP", "HKD", "HUF", "IDR", "ILS", "INR", "JPY", "KRW", "MXN", "MYR", "NOK", "NZD", "PHP", "PKR", "PLN", "RUB", "SEK", "SGD", "THB", "TRY", "TWD", "ZAR"})
	@Expose
	private double rate;

	public RateResponse(double rate) {
		this.rate = rate;
	}

	public double getRate() {
		return rate;
	}
}
