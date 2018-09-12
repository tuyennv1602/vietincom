package com.app.vietincome.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Metadata implements Parcelable {

	@SerializedName("timestamp")
	@Expose
	private Integer timestamp;
	@SerializedName("num_cryptocurrencies")
	@Expose
	private Integer numCryptocurrencies;
	@SerializedName("error")
	@Expose
	private String error;
	public final static Parcelable.Creator<Metadata> CREATOR = new Creator<Metadata>() {


		@SuppressWarnings({
				"unchecked"
		})
		public Metadata createFromParcel(Parcel in) {
			return new Metadata(in);
		}

		public Metadata[] newArray(int size) {
			return (new Metadata[size]);
		}

	};

	protected Metadata(Parcel in) {
		this.timestamp = ((Integer) in.readValue((Integer.class.getClassLoader())));
		this.numCryptocurrencies = ((Integer) in.readValue((Integer.class.getClassLoader())));
		this.error = ((String) in.readValue((String.class.getClassLoader())));
	}

	public Metadata() {
	}

	public Integer getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Integer timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getNumCryptocurrencies() {
		return numCryptocurrencies;
	}

	public void setNumCryptocurrencies(Integer numCryptocurrencies) {
		this.numCryptocurrencies = numCryptocurrencies;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(timestamp);
		dest.writeValue(numCryptocurrencies);
		dest.writeValue(error);
	}

	public int describeContents() {
		return 0;
	}

	public boolean isSuccess(){
		return error == null;
	}

}
