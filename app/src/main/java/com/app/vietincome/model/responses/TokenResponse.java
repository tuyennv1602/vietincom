package com.app.vietincome.model.responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenResponse implements Parcelable {

	@SerializedName("access_token")
	@Expose
	private String accessToken;
	@SerializedName("expires_in")
	@Expose
	private Integer expiresIn;
	@SerializedName("token_type")
	@Expose
	private String tokenType;

	private long expireAt;

	public final static Parcelable.Creator<TokenResponse> CREATOR = new Creator<TokenResponse>() {


		@SuppressWarnings({
				"unchecked"
		})
		public TokenResponse createFromParcel(Parcel in) {
			return new TokenResponse(in);
		}

		public TokenResponse[] newArray(int size) {
			return (new TokenResponse[size]);
		}

	};

	protected TokenResponse(Parcel in) {
		this.accessToken = ((String) in.readValue((String.class.getClassLoader())));
		this.expiresIn = ((Integer) in.readValue((Integer.class.getClassLoader())));
		this.tokenType = ((String) in.readValue((String.class.getClassLoader())));
		this.expireAt = ((Long) in.readValue((Long.class.getClassLoader())));
	}

	public TokenResponse() {
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Integer getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Integer expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public long getExpireAt() {
		return expireAt;
	}

	public void setExpireAt(long expireAt) {
		this.expireAt = expireAt;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(accessToken);
		dest.writeValue(expiresIn);
		dest.writeValue(tokenType);
		dest.writeValue(expireAt);
	}

	public int describeContents() {
		return 0;
	}

}
