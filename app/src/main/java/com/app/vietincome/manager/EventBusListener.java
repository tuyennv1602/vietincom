package com.app.vietincome.manager;

import com.app.vietincome.model.Data;
import com.app.vietincome.model.Event;
import com.app.vietincome.model.News;
import com.app.vietincome.model.Portfolio;
import com.app.vietincome.model.Transaction;

import java.util.ArrayList;

public class EventBusListener {

	public static class UpdatedTheme {
	}

	public static class ExpanableView {

	}

	public static class RefreshData {
		public int tab;

		public RefreshData(int tab) {
			this.tab = tab;
		}
	}

	public static class UpdateNews {
	}

	public static class UpdateEvent {

	}

	public static class UpdatePortfolio {
		public Portfolio portfolio;

		public UpdatePortfolio(Portfolio portfolio) {
			this.portfolio = portfolio;
		}
	}

	public static class AddPortfolio {
		public Portfolio portfolio;

		public AddPortfolio(Portfolio portfolio) {
			this.portfolio = portfolio;
		}
	}

	public static class AddTransaction {
		public Transaction transaction;

		public AddTransaction(Transaction transaction) {
			this.transaction = transaction;
		}
	}

	public static class RemoveCoin {
		public Portfolio portfolio;

		public RemoveCoin(Portfolio portfolio) {
			this.portfolio = portfolio;
		}
	}

	public static class SwitchChart {
		public boolean isLineChart;

		public SwitchChart(boolean isLineChart) {
			this.isLineChart = isLineChart;
		}
	}

	public static class UpdateCoin {
		public ArrayList<Data> data;
		public boolean isGainerTab;
		public boolean isSearch;

		public UpdateCoin(ArrayList<Data> data, boolean isSearch, boolean isGainerTab) {
			this.isSearch = isSearch;
			this.data = data;
			this.isGainerTab = isGainerTab;
		}
	}

	public static class SearchCoin {
		public ArrayList<Data> data;
		public boolean isClosed;
		public int tab;

		public SearchCoin(ArrayList<Data> data, boolean isClosed, int tab) {
			this.data = data;
			this.isClosed = isClosed;
			this.tab = tab;
		}
	}

	public static class ChangeCurrency{

	}

	public static class ProfileListener{

	}
}
