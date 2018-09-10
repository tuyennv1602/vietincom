package com.app.vietincom.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.app.vietincom.manager.AppPreference;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class News implements Parcelable {

	@SerializedName("id")
	@Expose
	private Integer id;
	@SerializedName("type")
	@Expose
	private String type;
	@SerializedName("slug")
	@Expose
	private String slug;
	@SerializedName("url")
	@Expose
	private String url;
	@SerializedName("status")
	@Expose
	private String status;
	@SerializedName("title")
	@Expose
	private String title;
	@SerializedName("title_plain")
	@Expose
	private String titlePlain;
	@SerializedName("content")
	@Expose
	private String content;
	@SerializedName("date")
	@Expose
	private String date;
	@SerializedName("modified")
	@Expose
	private String modified;
	@SerializedName("categories")
	@Expose
	private ArrayList<Category> categories = null;
	@SerializedName("tags")
	@Expose
	private ArrayList<Tag> tags = null;
	@SerializedName("author")
	@Expose
	private Author author;
	@SerializedName("comments")
	@Expose
	private ArrayList<Object> comments = null;
	@SerializedName("attachments")
	@Expose
	private ArrayList<Attachment> attachments = null;
	@SerializedName("comment_count")
	@Expose
	private Integer commentCount;
	@SerializedName("comment_status")
	@Expose
	private String commentStatus;
	@SerializedName("thumbnail")
	@Expose
	private String thumbnail;

	@SerializedName("thumbnail_size")
	@Expose
	private String thumbnailSize;
	@SerializedName("thumbnail_images")
	@Expose
	private Images thumbnailImages;
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
		this.slug = ((String) in.readValue((String.class.getClassLoader())));
		this.url = ((String) in.readValue((String.class.getClassLoader())));
		this.status = ((String) in.readValue((String.class.getClassLoader())));
		this.title = ((String) in.readValue((String.class.getClassLoader())));
		this.titlePlain = ((String) in.readValue((String.class.getClassLoader())));
		this.content = ((String) in.readValue((String.class.getClassLoader())));
		this.date = ((String) in.readValue((String.class.getClassLoader())));
		this.modified = ((String) in.readValue((String.class.getClassLoader())));
		in.readList(this.categories, (Category.class.getClassLoader()));
		in.readList(this.tags, (Tag.class.getClassLoader()));
		this.author = ((Author) in.readValue((Author.class.getClassLoader())));
		in.readList(this.comments, (java.lang.Object.class.getClassLoader()));
		in.readList(this.attachments, (Attachment.class.getClassLoader()));
		this.commentCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
		this.commentStatus = ((String) in.readValue((String.class.getClassLoader())));
		this.thumbnail = ((String) in.readValue((String.class.getClassLoader())));
		this.thumbnailSize = ((String) in.readValue((String.class.getClassLoader())));
		this.thumbnailImages = ((Images) in.readValue((Images.class.getClassLoader())));
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

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
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

	public String getTitlePlain() {
		return titlePlain;
	}

	public void setTitlePlain(String titlePlain) {
		this.titlePlain = titlePlain;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public ArrayList<Category> getCategories() {
		return categories;
	}

	public void setCategories(ArrayList<Category> categories) {
		this.categories = categories;
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

	public ArrayList<Object> getComments() {
		return comments;
	}

	public void setComments(ArrayList<Object> comments) {
		this.comments = comments;
	}

	public ArrayList<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(ArrayList<Attachment> attachments) {
		this.attachments = attachments;
	}

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	public String getCommentStatus() {
		return commentStatus;
	}

	public void setCommentStatus(String commentStatus) {
		this.commentStatus = commentStatus;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}


	public String getThumbnailSize() {
		return thumbnailSize;
	}

	public void setThumbnailSize(String thumbnailSize) {
		this.thumbnailSize = thumbnailSize;
	}

	public Images getThumbnailImages() {
		return thumbnailImages;
	}

	public void setThumbnailImages(Images thumbnailImages) {
		this.thumbnailImages = thumbnailImages;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(id);
		dest.writeValue(type);
		dest.writeValue(slug);
		dest.writeValue(url);
		dest.writeValue(status);
		dest.writeValue(title);
		dest.writeValue(titlePlain);
		dest.writeValue(content);
		dest.writeValue(date);
		dest.writeValue(modified);
		dest.writeList(categories);
		dest.writeList(tags);
		dest.writeValue(author);
		dest.writeList(comments);
		dest.writeList(attachments);
		dest.writeValue(commentCount);
		dest.writeValue(commentStatus);
		dest.writeValue(thumbnail);
		dest.writeValue(thumbnailSize);
		dest.writeValue(thumbnailImages);
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

	public void setRead(){
		isRead();
	}

}
