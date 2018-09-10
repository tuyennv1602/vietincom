package com.app.vietincom.model;

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
	@SerializedName("slug")
	@Expose
	private String slug;
	@SerializedName("title")
	@Expose
	private String title;
	@SerializedName("description")
	@Expose
	private String description;
	@SerializedName("caption")
	@Expose
	private String caption;
	@SerializedName("parent")
	@Expose
	private Integer parent;
	@SerializedName("mime_type")
	@Expose
	private String mimeType;
	@SerializedName("images")
	@Expose
	private Images images;
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
		this.slug = ((String) in.readValue((String.class.getClassLoader())));
		this.title = ((String) in.readValue((String.class.getClassLoader())));
		this.description = ((String) in.readValue((String.class.getClassLoader())));
		this.caption = ((String) in.readValue((String.class.getClassLoader())));
		this.parent = ((Integer) in.readValue((Integer.class.getClassLoader())));
		this.mimeType = ((String) in.readValue((String.class.getClassLoader())));
		this.images = ((Images) in.readValue((Images.class.getClassLoader())));
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

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public Images getImages() {
		return images;
	}

	public void setImages(Images images) {
		this.images = images;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(id);
		dest.writeValue(url);
		dest.writeValue(slug);
		dest.writeValue(title);
		dest.writeValue(description);
		dest.writeValue(caption);
		dest.writeValue(parent);
		dest.writeValue(mimeType);
		dest.writeValue(images);
	}

	public int describeContents() {
		return 0;
	}

}