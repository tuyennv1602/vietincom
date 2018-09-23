package com.app.vietincome.model.responses;

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

	public ChartResponse() {
		prices = new ArrayList<>();
		volumes = new ArrayList<>();
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
			_volumes.add(new Volume(Long.parseLong(volumes.get(i).get(0))));
		}
		return _volumes;
	}

}
