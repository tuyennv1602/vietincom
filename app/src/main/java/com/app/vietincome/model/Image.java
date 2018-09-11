package com.app.vietincome.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image implements Parcelable
{

	@SerializedName("url")
	@Expose
	private String url;
	@SerializedName("width")
	@Expose
	private Integer width;
	@SerializedName("height")
	@Expose
	private Integer height;
	public final static Parcelable.Creator<Image> CREATOR = new Creator<Image>() {


		@SuppressWarnings({
				"unchecked"
		})
		public Image createFromParcel(Parcel in) {
			return new Image(in);
		}

		public Image[] newArray(int size) {
			return (new Image[size]);
		}

	}
			;

	protected Image(Parcel in) {
		this.url = ((String) in.readValue((String.class.getClassLoader())));
		this.width = ((Integer) in.readValue((Integer.class.getClassLoader())));
		this.height = ((Integer) in.readValue((Integer.class.getClassLoader())));
	}

	public Image() {
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(url);
		dest.writeValue(width);
		dest.writeValue(height);
	}

	public int describeContents() {
		return 0;
	}

}