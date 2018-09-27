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
	@SerializedName("page")
	@Expose
	private Integer page;
	@SerializedName("max")
	@Expose
	private Integer max;
	@SerializedName("total_count")
	@Expose
	private Integer totalCount;
	@SerializedName("page_count")
	@Expose
	private Integer pageCount;
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
		this.page = ((Integer) in.readValue((Integer.class.getClassLoader())));
		this.max = ((Integer) in.readValue((Integer.class.getClassLoader())));
		this.totalCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
		this.pageCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
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

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
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
