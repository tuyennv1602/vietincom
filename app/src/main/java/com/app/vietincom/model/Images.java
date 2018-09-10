package com.app.vietincom.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Images implements Parcelable
{

	@SerializedName("full")
	@Expose
	private Image full;
	public final static Parcelable.Creator<Images> CREATOR = new Creator<Images>() {


		@SuppressWarnings({
				"unchecked"
		})
		public Images createFromParcel(Parcel in) {
			return new Images(in);
		}

		public Images[] newArray(int size) {
			return (new Images[size]);
		}

	}
			;

	protected Images(Parcel in) {
		this.full = ((Image) in.readValue((Image.class.getClassLoader())));
	}

	public Images() {
	}

	public Image getFull() {
		return full;
	}

	public void setFull(Image full) {
		this.full = full;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(full);
	}

	public int describeContents() {
		return 0;
	}

}