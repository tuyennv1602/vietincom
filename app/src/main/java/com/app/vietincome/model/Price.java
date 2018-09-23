package com.app.vietincome.model;

public class Price {

	private long time;
	private float price;

	public long getTime() {
		return time;
	}

	public float getPrice() {
		return price;
	}

	public Price(long time, float price) {
		this.time = time;
		this.price = price;
	}

	public Price(float price) {
		this.price = price;
	}
}
