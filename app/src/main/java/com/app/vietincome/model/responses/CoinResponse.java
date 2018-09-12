package com.app.vietincome.model.responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.app.vietincome.model.Data;
import com.app.vietincome.model.Metadata;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class CoinResponse {

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



}
