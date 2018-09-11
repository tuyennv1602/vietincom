package com.app.vietincome.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tag implements Parcelable
{

	@SerializedName("id")
	@Expose
	private Integer id;
	@SerializedName("slug")
	@Expose
	private String slug;
	@SerializedName("title")
	@Expose
	private String title;
	@SerializedName("description")
	@Expose
	private String description;
	@SerializedName("post_count")
	@Expose
	private Integer postCount;
	public final static Parcelable.Creator<Tag> CREATOR = new Creator<Tag>() {


		@SuppressWarnings({
				"unchecked"
		})
		public Tag createFromParcel(Parcel in) {
			return new Tag(in);
		}

		public Tag[] newArray(int size) {
			return (new Tag[size]);
		}

	}
			;

	protected Tag(Parcel in) {
		this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
		this.slug = ((String) in.readValue((String.class.getClassLoader())));
		this.title = ((String) in.readValue((String.class.getClassLoader())));
		this.description = ((String) in.readValue((String.class.getClassLoader())));
		this.postCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
	}

	public Tag() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getPostCount() {
		return postCount;
	}

	public void setPostCount(Integer postCount) {
		this.postCount = postCount;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(id);
		dest.writeValue(slug);
		dest.writeValue(title);
		dest.writeValue(description);
		dest.writeValue(postCount);
	}

	public int describeContents() {
		return 0;
	}

}