package com.app.vietincome.manager;

import com.app.vietincome.model.Data;
import com.app.vietincome.model.News;

import java.util.ArrayList;

public class EventBusListener {

	public static class UpdatedTheme {
	}

	public static class ExpanableView{

	}

	public static class RefreshData {
		public int tab;

		public RefreshData(int tab) {
			this.tab = tab;
		}
	}

	public static class UpdateNews {
	}

	public static class AddCoin {
		public ArrayList<Data> data;
		public int position;

		public AddCoin(ArrayList<Data> data, int position) {
			this.data = data;
			this.position = position;
		}
	}

	public static class AddNews{
		public ArrayList<News> news;
		public int position;

		public AddNews(ArrayList<News> news, int position){
			this.news = news;
			this.position = position;
		}
	}
}
