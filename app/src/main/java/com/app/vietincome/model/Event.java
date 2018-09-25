package com.app.vietincome.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Event implements Parcelable {

	@SerializedName("id")
	@Expose
	private Integer id;
	@SerializedName("title")
	@Expose
	private String title;
	@SerializedName("date_event")
	@Expose
	private String dateEvent;
	@SerializedName("created_date")
	@Expose
	private String createdDate;
	@SerializedName("description")
	@Expose
	private String description;
	@SerializedName("proof")
	@Expose
	private String proof;
	@SerializedName("source")
	@Expose
	private String source;
	@SerializedName("is_hot")
	@Expose
	private Boolean isHot;
	@SerializedName("vote_count")
	@Expose
	private Integer voteCount;
	@SerializedName("positive_vote_count")
	@Expose
	private Integer positiveVoteCount;
	@SerializedName("percentage")
	@Expose
	private Integer percentage;

	@SerializedName("tip_symbol")
	@Expose
	private String tipSymbol;
	@SerializedName("tip_adress")
	@Expose
	private String tipAdress;
	@SerializedName("twitter_account")
	@Expose
	private String twitterAccount;
	@SerializedName("can_occur_before")
	@Expose
	private Boolean canOccurBefore;
	@SerializedName("categories")
	@Expose
	private List<Category> categories = null;
	public final static Parcelable.Creator<Event> CREATOR = new Creator<Event>() {


		@SuppressWarnings({
				"unchecked"
		})
		public Event createFromParcel(Parcel in) {
			return new Event(in);
		}

		public Event[] newArray(int size) {
			return (new Event[size]);
		}

	};

	protected Event(Parcel in) {
		this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
		this.title = ((String) in.readValue((String.class.getClassLoader())));
		this.dateEvent = ((String) in.readValue((String.class.getClassLoader())));
		this.createdDate = ((String) in.readValue((String.class.getClassLoader())));
		this.description = ((String) in.readValue((String.class.getClassLoader())));
		this.proof = ((String) in.readValue((String.class.getClassLoader())));
		this.source = ((String) in.readValue((String.class.getClassLoader())));
		this.isHot = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
		this.voteCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
		this.positiveVoteCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
		this.percentage = ((Integer) in.readValue((Integer.class.getClassLoader())));
		this.tipSymbol = ((String) in.readValue((String.class.getClassLoader())));
		this.tipAdress = ((String) in.readValue((String.class.getClassLoader())));
		this.twitterAccount = ((String) in.readValue((String.class.getClassLoader())));
		this.canOccurBefore = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
		in.readList(this.categories, (Category.class.getClassLoader()));
	}

	public Event() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDateEvent() {
		return dateEvent;
	}

	public void setDateEvent(String dateEvent) {
		this.dateEvent = dateEvent;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProof() {
		return proof;
	}

	public void setProof(String proof) {
		this.proof = proof;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Boolean getIsHot() {
		return isHot;
	}

	public void setIsHot(Boolean isHot) {
		this.isHot = isHot;
	}

	public Integer getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(Integer voteCount) {
		this.voteCount = voteCount;
	}

	public Integer getPositiveVoteCount() {
		return positiveVoteCount;
	}

	public void setPositiveVoteCount(Integer positiveVoteCount) {
		this.positiveVoteCount = positiveVoteCount;
	}

	public Integer getPercentage() {
		return percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

	public String getTipSymbol() {
		return tipSymbol;
	}

	public void setTipSymbol(String tipSymbol) {
		this.tipSymbol = tipSymbol;
	}

	public String getTipAdress() {
		return tipAdress;
	}

	public void setTipAdress(String tipAdress) {
		this.tipAdress = tipAdress;
	}

	public String getTwitterAccount() {
		return twitterAccount;
	}

	public void setTwitterAccount(String twitterAccount) {
		this.twitterAccount = twitterAccount;
	}

	public Boolean getCanOccurBefore() {
		return canOccurBefore;
	}

	public void setCanOccurBefore(Boolean canOccurBefore) {
		this.canOccurBefore = canOccurBefore;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(id);
		dest.writeValue(title);
		dest.writeValue(dateEvent);
		dest.writeValue(createdDate);
		dest.writeValue(description);
		dest.writeValue(proof);
		dest.writeValue(source);
		dest.writeValue(isHot);
		dest.writeValue(voteCount);
		dest.writeValue(positiveVoteCount);
		dest.writeValue(percentage);
		dest.writeValue(tipSymbol);
		dest.writeValue(tipAdress);
		dest.writeValue(twitterAccount);
		dest.writeValue(canOccurBefore);
		dest.writeList(categories);
	}

	public int describeContents() {
		return 0;
	}

}
