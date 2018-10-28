package com.app.vietincome.model;

public class MarketCap {

	private long time;
	private float price;

	public long getTime() {
		return time;
	}

	public float getPrice() {
		return price;
	}

	public MarketCap(long time, float price) {
		this.time = time;
		this.price = price;
	}

	public MarketCap(float price) {
		this.price = price;
	}
}
