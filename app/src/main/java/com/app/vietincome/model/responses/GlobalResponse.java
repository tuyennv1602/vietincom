package com.app.vietincome.model.responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.app.vietincome.model.Data;
import com.app.vietincome.model.Metadata;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GlobalResponse implements Parcelable
{

	@SerializedName("data")
	@Expose
	private Data data;
	@SerializedName("metadata")
	@Expose
	private Metadata metadata;
	public final static Parcelable.Creator<GlobalResponse> CREATOR = new Creator<GlobalResponse>() {


		@SuppressWarnings({
				"unchecked"
		})
		public GlobalResponse createFromParcel(Parcel in) {
			return new GlobalResponse(in);
		}

		public GlobalResponse[] newArray(int size) {
			return (new GlobalResponse[size]);
		}

	}
			;

	protected GlobalResponse(Parcel in) {
		this.data = ((Data) in.readValue((Data.class.getClassLoader())));
		this.metadata = ((Metadata) in.readValue((Metadata.class.getClassLoader())));
	}

	public GlobalResponse() {
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(data);
		dest.writeValue(metadata);
	}

	public int describeContents() {
		return 0;
	}

}
