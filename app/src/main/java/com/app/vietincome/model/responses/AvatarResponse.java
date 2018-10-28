package com.app.vietincome.model.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AvatarResponse extends BaseResponse {

	@SerializedName("data")
	@Expose
	private Avatar avatar;

	public Avatar getAvatar() {
		return avatar;
	}

	public void setAvatar(Avatar avatar) {
		this.avatar = avatar;
	}

	public class Avatar {
		@SerializedName("filename")
		@Expose
		private String fileName;

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
	}


}
