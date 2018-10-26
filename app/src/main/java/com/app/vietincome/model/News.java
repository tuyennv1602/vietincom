package com.app.vietincome.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.model.responses.NewsResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;

public class News implements Parcelable, ObservableSource<News> {

	@SerializedName("post_id")
	@Expose
	private Integer id;
	@SerializedName("post_type")
	@Expose
	private String type;

	@SerializedName("post_url")
	@Expose
	private String url;
	@SerializedName("post_status")
	@Expose
	private String status;
	@SerializedName("post_title")
	@Expose
	private String title;

	@SerializedName("post_date")
	@Expose
	private String date;

	@SerializedName("post_tags")
	@Expose
	private ArrayList<Tag> tags = null;
	@SerializedName("post_author")
	@Expose
	private Author author;

	@SerializedName("post_thumbnail")
	@Expose
	private String thumbnail;

	public final static Parcelable.Creator<News> CREATOR = new Creator<News>() {

		@SuppressWarnings({
				"unchecked"
		})
		public News createFromParcel(Parcel in) {
			return new News(in);
		}

		public News[] newArray(int size) {
			return (new News[size]);
		}

	};

	protected News(Parcel in) {
		this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
		this.type = ((String) in.readValue((String.class.getClassLoader())));
		this.url = ((String) in.readValue((String.class.getClassLoader())));
		this.status = ((String) in.readValue((String.class.getClassLoader())));
		this.title = ((String) in.readValue((String.class.getClassLoader())));
		this.date = ((String) in.readValue((String.class.getClassLoader())));
		in.readList(this.tags, (Tag.class.getClassLoader()));
		this.author = ((Author) in.readValue((Author.class.getClassLoader())));
		this.thumbnail = ((String) in.readValue(String.class.getClassLoader()));
	}

	public News() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public ArrayList<Tag> getTags() {
		return tags;
	}

	public void setTags(ArrayList<Tag> tags) {
		this.tags = tags;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(id);
		dest.writeValue(type);
		dest.writeValue(url);
		dest.writeValue(status);
		dest.writeValue(title);
		dest.writeValue(date);
		dest.writeList(tags);
		dest.writeValue(author);
		dest.writeValue(thumbnail);
	}

	public int describeContents() {
		return 0;
	}

	public boolean isRead() {
		for (Integer integer : AppPreference.INSTANCE.getNewsRead()) {
			if (integer == id) return true;
		}
		return false;
	}

	@Override
	public void subscribe(Observer<? super News> observer) {

	}
}
