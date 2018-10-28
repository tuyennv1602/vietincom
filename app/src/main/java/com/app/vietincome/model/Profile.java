package com.app.vietincome.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.app.vietincome.utils.Constant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile implements Parcelable {

	@SerializedName("session_id")
	@Expose
	private String sessionId;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("avatar")
	@Expose
	private String avatar;
	@SerializedName("bio")
	@Expose
	private String bio;
	@SerializedName("vic")
	@Expose
	private String vic;
	@SerializedName("vip")
	@Expose
	private int vip;
	@SerializedName("expired")
	@Expose
	private String expired;
	@SerializedName("invite_code")
	@Expose
	private String inviteCode;
	public final static Parcelable.Creator<Profile> CREATOR = new Creator<Profile>() {


		@SuppressWarnings({
				"unchecked"
		})
		public Profile createFromParcel(Parcel in) {
			return new Profile(in);
		}

		public Profile[] newArray(int size) {
			return (new Profile[size]);
		}

	};

	protected Profile(Parcel in) {
		this.sessionId = ((String) in.readValue((String.class.getClassLoader())));
		this.name = ((String) in.readValue((String.class.getClassLoader())));
		this.avatar = ((String) in.readValue((String.class.getClassLoader())));
		this.bio = ((String) in.readValue((String.class.getClassLoader())));
		this.vic = ((String) in.readValue((String.class.getClassLoader())));
		this.vip = ((int) in.readValue((int.class.getClassLoader())));
		this.expired = ((String) in.readValue((String.class.getClassLoader())));
		this.inviteCode = ((String) in.readValue((String.class.getClassLoader())));
	}

	public Profile() {
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatar() {
		if(avatar == null || avatar.isEmpty()) return "";
		return "https://vietincome.com/apiv2/user/avatar/" + avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getBio() {
		if (bio == null || bio.isEmpty()) return "";
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getVic() {
		if (vic == null || bio.isEmpty()) return "0 VIC";
		return vic + " VIC";
	}

	public String getVip() {
		if (vip == Constant.VIP1) return "VIP1";
		if (vip == Constant.VIP2) return "VIP2";
		if (vip == Constant.VIP3) return "VIP3";
		return "Free";
	}

	public boolean isVip() {
		return vip != 0;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	public String getExpired() {
		return expired;
	}

	public void setExpired(String expired) {
		this.expired = expired;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(sessionId);
		dest.writeValue(name);
		dest.writeValue(avatar);
		dest.writeValue(bio);
		dest.writeValue(vic);
		dest.writeValue(vip);
		dest.writeValue(expired);
		dest.writeValue(inviteCode);
	}

	public int describeContents() {
		return 0;
	}

}