package com.app.vietincome.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class USD implements Parcelable
{

	@SerializedName("price")
	@Expose
	public double price;
	@SerializedName("volume_24h")
	@Expose
	public double volume24h;
	@SerializedName("market_cap")
	@Expose
	public double marketCap;
	@SerializedName("percent_change_1h")
	@Expose
	public double percentChange1h;
	@SerializedName("percent_change_24h")
	@Expose
	public double percentChange24h;
	@SerializedName("percent_change_7d")
	@Expose
	public double percentChange7d;
	public final static Parcelable.Creator<USD> CREATOR = new Creator<USD>() {


		@SuppressWarnings({
				"unchecked"
		})
		public USD createFromParcel(Parcel in) {
			return new USD(in);
		}

		public USD[] newArray(int size) {
			return (new USD[size]);
		}

	}
			;

	protected USD(Parcel in) {
		this.price = ((double) in.readValue((double.class.getClassLoader())));
		this.volume24h = ((double) in.readValue((double.class.getClassLoader())));
		this.marketCap = ((double) in.readValue((double.class.getClassLoader())));
		this.percentChange1h = ((double) in.readValue((double.class.getClassLoader())));
		this.percentChange24h = ((double) in.readValue((double.class.getClassLoader())));
		this.percentChange7d = ((double) in.readValue((double.class.getClassLoader())));
	}

	public USD() {
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getVolume24h() {
		return volume24h;
	}

	public void setVolume24h(double volume24h) {
		this.volume24h = volume24h;
	}

	public double getMarketCap() {
		return marketCap;
	}

	public void setMarketCap(double marketCap) {
		this.marketCap = marketCap;
	}

	public String getPercentChange1h() {
		if(percentChange1h == 0) return "__  ";
		if(isPlus(percentChange1h)){
			return "+".concat(String.valueOf(percentChange1h)).concat("%");
		}
		return String.valueOf(percentChange1h).concat("%");
	}

	public void setPercentChange1h(double percentChange1h) {
		this.percentChange1h = percentChange1h;
	}

	public String getPercentChange24h() {
		if(percentChange24h == 0) return "__  ";
		if(isPlus(percentChange24h)){
			return "+".concat(String.valueOf(percentChange24h)).concat("%");
		}
		return String.valueOf(percentChange24h).concat("%");
	}

	public void setPercentChange24h(double percentChange24h) {
		this.percentChange24h = percentChange24h;
	}

	public String getPercentChange7d() {
		if(percentChange7d == 0) return  "__  ";
		if(isPlus(percentChange7d)){
			return "+".concat(String.valueOf(percentChange7d)).concat("%");
		}
		return String.valueOf(percentChange7d).concat("%");	}

	public void setPercentChange7d(double percentChange7d) {
		this.percentChange7d = percentChange7d;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(price);
		dest.writeValue(volume24h);
		dest.writeValue(marketCap);
		dest.writeValue(percentChange1h);
		dest.writeValue(percentChange24h);
		dest.writeValue(percentChange7d);
	}

	public int describeContents() {
		return 0;
	}

	public boolean isPlus(double value){
		return !String.valueOf(value).contains("-");
	}

}