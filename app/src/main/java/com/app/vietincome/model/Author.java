package com.app.vietincome.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Author implements Parcelable {

	@SerializedName("user_id")
	@Expose
	private String userId;
	@SerializedName("user_login")
	@Expose
	private String userLogin;
	@SerializedName("display_name")
	@Expose
	private String displayName;
	@SerializedName("user_email")
	@Expose
	private String userEmail;
	@SerializedName("user_nicename")
	@Expose
	private String userNicename;
	public final static Parcelable.Creator<Author> CREATOR = new Creator<Author>() {


		@SuppressWarnings({
				"unchecked"
		})
		public Author createFromParcel(Parcel in) {
			return new Author(in);
		}

		public Author[] newArray(int size) {
			return (new Author[size]);
		}

	};

	protected Author(Parcel in) {
		this.userId = ((String) in.readValue((String.class.getClassLoader())));
		this.userLogin = ((String) in.readValue((String.class.getClassLoader())));
		this.displayName = ((String) in.readValue((String.class.getClassLoader())));
		this.userEmail = ((String) in.readValue((String.class.getClassLoader())));
		this.userNicename = ((String) in.readValue((String.class.getClassLoader())));
	}

	public Author() {
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserNicename() {
		return userNicename;
	}

	public void setUserNicename(String userNicename) {
		this.userNicename = userNicename;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(userId);
		dest.writeValue(userLogin);
		dest.writeValue(displayName);
		dest.writeValue(userEmail);
		dest.writeValue(userNicename);
	}

	public int describeContents() {
		return 0;
	}

}