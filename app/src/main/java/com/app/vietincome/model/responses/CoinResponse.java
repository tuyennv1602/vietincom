package com.app.vietincome.model.responses;

import android.os.Parcel;
import android.os.Parcelable;
import com.app.vietincome.model.Metadata;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CoinResponse implements Parcelable {


	@SerializedName("data")
	@Expose
	private JsonElement data;
	@SerializedName("metadata")
	@Expose
	private Metadata metadata;
	public final static Parcelable.Creator<CoinResponse> CREATOR = new Creator<CoinResponse>() {


		@SuppressWarnings({
				"unchecked"
		})
		public CoinResponse createFromParcel(Parcel in) {
			return new CoinResponse(in);
		}

		public CoinResponse[] newArray(int size) {
			return (new CoinResponse[size]);
		}

	};

	protected CoinResponse(Parcel in) {
		this.data = ((JsonElement) in.readValue((JsonElement.class.getClassLoader())));
		this.metadata = ((Metadata) in.readValue((Metadata.class.getClassLoader())));
	}

	public CoinResponse() {
	}

	public JsonElement getData() {
		return data;
	}

	public void setData(JsonElement data) {
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
