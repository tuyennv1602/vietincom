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

	@SerializedName("id")
	@Expose
	private Integer id;
	@SerializedName("type")
	@Expose
	private String type;

	@SerializedName("url")
	@Expose
	private String url;
	@SerializedName("status")
	@Expose
	private String status;
	@SerializedName("title")
	@Expose
	private String title;

	@SerializedName("date")
	@Expose
	private String date;
	@SerializedName("modified")
	@Expose
	private String modified;

	@SerializedName("tags")
	@Expose
	private ArrayList<Tag> tags = null;
	@SerializedName("author")
	@Expose
	private Author author;

	@SerializedName("attachments")
	@Expose
	private ArrayList<Attachment> attachments = null;

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
		this.modified = ((String) in.readValue((String.class.getClassLoader())));
		in.readList(this.tags, (Tag.class.getClassLoader()));
		this.author = ((Author) in.readValue((Author.class.getClassLoader())));
		in.readList(this.attachments, (Attachment.class.getClassLoader()));
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

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
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


	public ArrayList<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(ArrayList<Attachment> attachments) {
		this.attachments = attachments;
	}


	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(id);
		dest.writeValue(type);
		dest.writeValue(url);
		dest.writeValue(status);
		dest.writeValue(title);
		dest.writeValue(date);
		dest.writeValue(modified);
		dest.writeList(tags);
		dest.writeValue(author);
		dest.writeList(attachments);
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
