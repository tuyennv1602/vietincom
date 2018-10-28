package com.app.vietincome.model.responses;

import com.app.vietincome.model.MarketCap;
import com.app.vietincome.model.Price;
import com.app.vietincome.model.Volume;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ChartResponse {

	@SerializedName("price")
	@Expose
	private List<List<String>> prices = null;
	@SerializedName("volume")
	@Expose
	private List<List<String>> volumes = null;
	@SerializedName("market_cap")
	@Expose
	private List<List<String>> marketCaps = null;

	public ChartResponse() {
		prices = new ArrayList<>();
		volumes = new ArrayList<>();
		marketCaps = new ArrayList<>();
	}

	public ArrayList<Price> getPrices() {
		ArrayList<Price> _prices = new ArrayList<>();
		for(int i = 0; i < prices.size(); i++){
			_prices.add(new Price(Long.valueOf(prices.get(i).get(0)), Float.valueOf(prices.get(i).get(1))));
		}
		return _prices;
	}

	public ArrayList<Volume> getVolumes() {
		ArrayList<Volume> _volumes = new ArrayList<>();
		for(int i = 0; i < volumes.size(); i++){
			_volumes.add(new Volume(Long.valueOf(volumes.get(i).get(0)), Float.valueOf(volumes.get(i).get(1))));
		}
		return _volumes;
	}

	public ArrayList<MarketCap> getMarketCaps() {
		ArrayList<MarketCap> _marketCaps = new ArrayList<>();
		for(int i = 0; i < marketCaps.size(); i++){
			_marketCaps.add(new MarketCap(Long.valueOf(marketCaps.get(i).get(0)), Float.valueOf(marketCaps.get(i).get(1))));
		}
		return _marketCaps;
	}


}
