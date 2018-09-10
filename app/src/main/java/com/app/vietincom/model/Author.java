package com.app.vietincom.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Author implements Parcelable {

	@SerializedName("id")
	@Expose
	private Integer id;
	@SerializedName("slug")
	@Expose
	private String slug;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("first_name")
	@Expose
	private String firstName;
	@SerializedName("last_name")
	@Expose
	private String lastName;
	@SerializedName("nickname")
	@Expose
	private String nickname;
	@SerializedName("url")
	@Expose
	private String url;
	@SerializedName("description")
	@Expose
	private String description;
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
		this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
		this.slug = ((String) in.readValue((String.class.getClassLoader())));
		this.name = ((String) in.readValue((String.class.getClassLoader())));
		this.firstName = ((String) in.readValue((String.class.getClassLoader())));
		this.lastName = ((String) in.readValue((String.class.getClassLoader())));
		this.nickname = ((String) in.readValue((String.class.getClassLoader())));
		this.url = ((String) in.readValue((String.class.getClassLoader())));
		this.description = ((String) in.readValue((String.class.getClassLoader())));
	}

	public Author() {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(id);
		dest.writeValue(slug);
		dest.writeValue(name);
		dest.writeValue(firstName);
		dest.writeValue(lastName);
		dest.writeValue(nickname);
		dest.writeValue(url);
		dest.writeValue(description);
	}

	public int describeContents() {
		return 0;
	}

}
