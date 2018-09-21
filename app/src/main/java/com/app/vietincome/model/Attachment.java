package com.app.vietincome.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attachment  implements Parcelable
{

	@SerializedName("id")
	@Expose
	private Integer id;
	@SerializedName("url")
	@Expose
	private String url;

	public final static Parcelable.Creator<Attachment> CREATOR = new Creator<Attachment>() {


		@SuppressWarnings({
				"unchecked"
		})
		public Attachment createFromParcel(Parcel in) {
			return new Attachment(in);
		}

		public Attachment[] newArray(int size) {
			return (new Attachment[size]);
		}

	}
			;

	protected Attachment(Parcel in) {
		this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
		this.url = ((String) in.readValue((String.class.getClassLoader())));
	}

	public Attachment() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(id);
		dest.writeValue(url);
	}

	public int describeContents() {
		return 0;
	}

}