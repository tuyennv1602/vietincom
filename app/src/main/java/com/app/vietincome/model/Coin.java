package com.app.vietincome.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coin implements Parcelable
{

	@SerializedName("id")
	@Expose
	private String id;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("symbol")
	@Expose
	private String symbol;
	public final static Parcelable.Creator<Coin> CREATOR = new Creator<Coin>() {


		@SuppressWarnings({
				"unchecked"
		})
		public Coin createFromParcel(Parcel in) {
			return new Coin(in);
		}

		public Coin[] newArray(int size) {
			return (new Coin[size]);
		}

	}
			;

	protected Coin(Parcel in) {
		this.id = ((String) in.readValue((String.class.getClassLoader())));
		this.name = ((String) in.readValue((String.class.getClassLoader())));
		this.symbol = ((String) in.readValue((String.class.getClassLoader())));
	}

	public Coin() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(id);
		dest.writeValue(name);
		dest.writeValue(symbol);
	}

	public int describeContents() {
		return 0;
	}

}

