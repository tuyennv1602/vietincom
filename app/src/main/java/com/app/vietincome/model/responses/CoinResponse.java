package com.app.vietincome.model.responses;

import com.app.vietincome.model.Data;
import com.app.vietincome.model.Metadata;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;


public class CoinResponse implements ObservableSource<CoinResponse> {

	@SerializedName("data")
	@Expose
	private ArrayList<Data> data;
	@SerializedName("metadata")
	@Expose
	private Metadata metadata;


	public CoinResponse() {
	}

	public ArrayList<Data> getData() {
		return data;
	}

	public void setData(ArrayList<Data> data) {
		this.data = data;
	}

	public Metadata getMetadata() {
		return metadata;
	}


	@Override
	public void subscribe(Observer<? super CoinResponse> observer) {

	}
}
