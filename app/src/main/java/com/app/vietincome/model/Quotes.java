package com.app.vietincome.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Quotes implements Parcelable
{

	@SerializedName("USD")
	@Expose
	private USD uSD;

	@SerializedName("BTC")
	@Expose
	private USD bTC;

	public final static Parcelable.Creator<Quotes> CREATOR = new Creator<Quotes>() {


		@SuppressWarnings({
				"unchecked"
		})
		public Quotes createFromParcel(Parcel in) {
			return new Quotes(in);
		}

		public Quotes[] newArray(int size) {
			return (new Quotes[size]);
		}

	}
			;

	protected Quotes(Parcel in) {
		this.uSD = ((USD) in.readValue((USD.class.getClassLoader())));
		this.bTC =  ((USD) in.readValue((USD.class.getClassLoader())));
	}

	public Quotes() {
	}

	public USD getUSD() {
		return uSD;
	}

	public void setUSD(USD uSD) {
		this.uSD = uSD;
	}

	public USD getBTC() {
		return bTC;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(uSD);
		dest.writeValue(bTC);
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
		return String.valueOf(getUSD().getPrice()) + "/" + String.valueOf(getBTC().getPrice());
	}
}
