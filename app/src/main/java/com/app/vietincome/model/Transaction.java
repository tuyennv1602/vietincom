package com.app.vietincome.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Transaction implements Parcelable {

	private int id;
	private double priceUSD;
	private double priceBTC;
	private String dateAdd;
	private int quantity;
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
		this.id = ((Integer) in.readValue(Integer.class.getClassLoader()));
		this.priceUSD = ((Double) in.readValue(Double.class.getClassLoader()));
		this.priceBTC = ((Double) in.readValue(Double.class.getClassLoader()));
		this.dateAdd = ((String) in.readValue(String.class.getClassLoader()));
		this.quantity = ((Integer) in.readValue(Integer.class.getClassLoader()));
		this.isBuy = ((Boolean) in.readValue(Boolean.class.getClassLoader()));
	}

	public Transaction(int id, double priceUSD, double priceBTC, String dateAdd, int quantity, boolean isBuy) {
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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
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
