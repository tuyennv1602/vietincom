package com.app.vietincome.model;

public class Market {

	private int rank;
	private String exchange;
	private String pair;
	private Double price;
	private Double volume;

	public Market(int rank, String exchange, String pair, Double price, Double volume) {
		this.rank = rank;
		this.exchange = exchange;
		this.pair = pair;
		this.price = price;
		this.volume = volume;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getPair() {
		return pair;
	}

	public void setPair(String pair) {
		this.pair = pair;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}
}
