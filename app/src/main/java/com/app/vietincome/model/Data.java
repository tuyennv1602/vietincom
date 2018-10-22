package com.app.vietincome.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.app.vietincome.manager.AppPreference;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Parcelable
{

	@SerializedName("id")
	@Expose
	private int id;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("symbol")
	@Expose
	private String symbol;
	@SerializedName("website_slug")
	@Expose
	private String websiteSlug;
	@SerializedName("rank")
	@Expose
	private int rank;
	@SerializedName("circulating_supply")
	@Expose
	private double circulatingSupply;
	@SerializedName("total_supply")
	@Expose
	private double totalSupply;
	@SerializedName("max_supply")
	@Expose
	private double maxSupply;
	@SerializedName("quotes")
	@Expose
	private Quotes quotes;
	@SerializedName("last_updated")
	@Expose
	private int lastUpdated;
	public final static Parcelable.Creator<Data> CREATOR = new Creator<Data>() {


		@SuppressWarnings({
				"unchecked"
		})
		public Data createFromParcel(Parcel in) {
			return new Data(in);
		}

		public Data [] newArray(int size) {
			return (new Data[size]);
		}

	}
	;

	protected Data(Parcel in) {
		this.id = ((int) in.readValue((int.class.getClassLoader())));
		this.name = ((String) in.readValue((String.class.getClassLoader())));
		this.symbol = ((String) in.readValue((String.class.getClassLoader())));
		this.websiteSlug = ((String) in.readValue((String.class.getClassLoader())));
		this.rank = ((int) in.readValue((int.class.getClassLoader())));
		this.circulatingSupply = ((double) in.readValue((double.class.getClassLoader())));
		this.totalSupply = ((double) in.readValue((double.class.getClassLoader())));
		this.maxSupply = ((double) in.readValue((double.class.getClassLoader())));
		this.quotes = ((Quotes) in.readValue((Quotes.class.getClassLoader())));
		this.lastUpdated = ((int) in.readValue((int.class.getClassLoader())));
	}

	public Data(){
	}

	public int getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getWebsiteSlug() {
		return websiteSlug;
	}

	public void setWebsiteSlug(String websiteSlug) {
		this.websiteSlug = websiteSlug;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public double getCirculatingSupply() {
		return circulatingSupply;
	}

	public void setCirculatingSupply(Double circulatingSupply) {
		this.circulatingSupply = circulatingSupply;
	}

	public double getTotalSupply() {
		return totalSupply;
	}

	public void setTotalSupply(Double totalSupply) {
		this.totalSupply = totalSupply;
	}

	public Object getMaxSupply() {
		return maxSupply;
	}

	public void setMaxSupply(Double maxSupply) {
		this.maxSupply = maxSupply;
	}

	public Quotes getQuotes() {
		return quotes;
	}

	public void setQuotes(Quotes quotes) {
		this.quotes = quotes;
	}

	public int getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Integer lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(id);
		dest.writeValue(name);
		dest.writeValue(symbol);
		dest.writeValue(websiteSlug);
		dest.writeValue(rank);
		dest.writeValue(circulatingSupply);
		dest.writeValue(totalSupply);
		dest.writeValue(maxSupply);
		dest.writeValue(quotes);
		dest.writeValue(lastUpdated);
	}

	public boolean isFavourite() {
		for (Data item : AppPreference.INSTANCE.getFavouriteCoin()) {
			if (item.getId() == id) return true;
		}
		return false;
	}

	public int describeContents() {
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		return this.toString().equals(obj.toString());
	}


	@Override
	public String toString() {
		return String.valueOf(id) + "/" + String.valueOf(quotes.getUSD().price) + "/" + String.valueOf(quotes.getBTC().price);
	}
}
