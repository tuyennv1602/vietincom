package com.app.vietincome.model;

public class Volume {
	private long time;
	private float price;

	public Volume(long time, float price) {
		this.price = price;
		this.time = time;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
