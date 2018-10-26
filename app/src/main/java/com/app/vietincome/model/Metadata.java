package com.app.vietincome.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Metadata implements Parcelable {

	@SerializedName("timestamp")
	@Expose
	private int timestamp;
	@SerializedName("num_cryptocurrencies")
	@Expose
	private int numCryptocurrencies;
	@SerializedName("error")
	@Expose
	private String error;
	@SerializedName("page")
	@Expose
	private int page;
	@SerializedName("max")
	@Expose
	private int max;
	@SerializedName("total_count")
	@Expose
	private int totalCount;
	@SerializedName("page_count")
	@Expose
	private int pageCount;
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
		this.timestamp = ((int) in.readValue((int.class.getClassLoader())));
		this.numCryptocurrencies = ((int) in.readValue((int.class.getClassLoader())));
		this.error = ((String) in.readValue((String.class.getClassLoader())));
		this.page = ((int) in.readValue((int.class.getClassLoader())));
		this.max = ((int) in.readValue((int.class.getClassLoader())));
		this.totalCount = ((int) in.readValue((int.class.getClassLoader())));
		this.pageCount = ((int) in.readValue((int.class.getClassLoader())));
	}

	public Metadata() {
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	public int getNumCryptocurrencies() {
		return numCryptocurrencies;
	}

	public void setNumCryptocurrencies(int numCryptocurrencies) {
		this.numCryptocurrencies = numCryptocurrencies;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(timestamp);
		dest.writeValue(numCryptocurrencies);
		dest.writeValue(error);
		dest.writeValue(page);
		dest.writeValue(max);
		dest.writeValue(totalCount);
		dest.writeValue(pageCount);
	}

	public int describeContents() {
		return 0;
	}

	public boolean isSuccess(){
		return error == null;
	}

}
