package com.app.vietincome.model;

public class Volume {
	private long price;

	public Volume(long price) {
		this.price = price;
	}

	public long getPrice() {
		return price / 1000000;
	}

	public void setPrice(long price) {
		this.price = price;
	}
}
