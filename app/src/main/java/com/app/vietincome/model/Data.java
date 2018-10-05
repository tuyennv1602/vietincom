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
	private Integer id;
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
	private Integer rank;
	@SerializedName("circulating_supply")
	@Expose
	private Double circulatingSupply;
	@SerializedName("total_supply")
	@Expose
	private Double totalSupply;
	@SerializedName("max_supply")
	@Expose
	private Double maxSupply;
	@SerializedName("quotes")
	@Expose
	private Quotes quotes;
	@SerializedName("last_updated")
	@Expose
	private Integer lastUpdated;
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
		this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
		this.name = ((String) in.readValue((String.class.getClassLoader())));
		this.symbol = ((String) in.readValue((String.class.getClassLoader())));
		this.websiteSlug = ((String) in.readValue((String.class.getClassLoader())));
		this.rank = ((Integer) in.readValue((Integer.class.getClassLoader())));
		this.circulatingSupply = ((Double) in.readValue((Double.class.getClassLoader())));
		this.totalSupply = ((Double) in.readValue((Double.class.getClassLoader())));
		this.maxSupply = ((Double) in.readValue((Double.class.getClassLoader())));
		this.quotes = ((Quotes) in.readValue((Quotes.class.getClassLoader())));
		this.lastUpdated = ((Integer) in.readValue((Integer.class.getClassLoader())));
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

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Double getCirculatingSupply() {
		return circulatingSupply;
	}

	public void setCirculatingSupply(Double circulatingSupply) {
		this.circulatingSupply = circulatingSupply;
	}

	public Double getTotalSupply() {
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

	public Integer getLastUpdated() {
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

//	public boolean isFavourite() {
//		for (Integer integer : AppPreference.INSTANCE.getFavouriteCoin()) {
//			if (integer == id) return true;
//		}
//		return false;
//	}

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
