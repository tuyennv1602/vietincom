package com.app.vietincome.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Transaction implements Parcelable {

	private int id;
	private double priceUSD;
	private double priceBTC;
	private String dateAdd;
	private float quantity;
	private boolean isBuy;

	public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
		@Override
		public Transaction createFromParcel(Parcel in) {
			return new Transaction(in);
		}

		@Override
		public Transaction[] newArray(int size) {
			return new Transaction[size];
		}
	};

	protected Transaction(Parcel in) {
		this.id = ((int) in.readValue(int.class.getClassLoader()));
		this.priceUSD = ((double) in.readValue(double.class.getClassLoader()));
		this.priceBTC = ((double) in.readValue(double.class.getClassLoader()));
		this.dateAdd = ((String) in.readValue(String.class.getClassLoader()));
		this.quantity = ((float) in.readValue(float.class.getClassLoader()));
		this.isBuy = ((boolean) in.readValue(boolean.class.getClassLoader()));
	}

	public Transaction(int id, double priceUSD, double priceBTC, String dateAdd, float quantity, boolean isBuy) {
		this.id = id;
		this.priceUSD = priceUSD;
		this.priceBTC = priceBTC;
		this.dateAdd = dateAdd;
		this.quantity = quantity;
		this.isBuy = isBuy;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDateAdd() {
		return dateAdd;
	}

	public void setDateAdd(String dateAdd) {
		this.dateAdd = dateAdd;
	}

	public float getQuantity() {
		return quantity;
	}

	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

	public boolean isBuy() {
		return isBuy;
	}

	public void setBuy(boolean buy) {
		isBuy = buy;
	}

	public double getPriceUSD() {
		return priceUSD;
	}

	public void setPriceUSD(double priceUSD) {
		this.priceUSD = priceUSD;
	}

	public double getPriceBTC() {
		return priceBTC;
	}

	public void setPriceBTC(double priceBTC) {
		this.priceBTC = priceBTC;
	}

	public static Creator<Transaction> getCREATOR() {
		return CREATOR;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeValue(id);
		parcel.writeValue(priceUSD);
		parcel.writeValue(priceBTC);
		parcel.writeValue(dateAdd);
		parcel.writeValue(quantity);
		parcel.writeValue(isBuy);
	}
}
