package com.app.vietincome.model.responses;

import com.app.vietincome.model.News;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;

public class NewsResponse implements ObservableSource<NewsResponse> {
	@SerializedName("status")
	@Expose
	private String status;

	@SerializedName("count")
	@Expose
	private int count;

	@SerializedName("max_page")
	@Expose
	private int maxPages;

	@SerializedName("current_page")
	@Expose
	private int currentPage;

	@SerializedName("posts")
	@Expose
	private ArrayList<News> news;

	public boolean isSuccess() {
		if(status == null) return false;
		return status.equals("ok");
	}

	public ArrayList<News> getNews() {
		return news;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getMaxPages() {
		return maxPages;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public void setMaxPages(int maxPages) {
		this.maxPages = maxPages;
	}

	public void setNews(ArrayList<News> news) {
		this.news = news;
	}

	@Override
	public void subscribe(Observer<? super NewsResponse> observer) {

	}
}
