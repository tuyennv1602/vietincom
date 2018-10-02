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

	public static class UpdateEvent{

	}

	public static class UpdatePortfolio{
		public Portfolio portfolio;

		public UpdatePortfolio(Portfolio portfolio){
			this.portfolio = portfolio;
		}
	}

	public static class AddPortfolio{
		public Portfolio portfolio;

		public AddPortfolio(Portfolio portfolio){
			this.portfolio = portfolio;
		}
	}

	public static class AddTransaction{
		public Transaction transaction;

		public AddTransaction(Transaction transaction){
			this.transaction = transaction;
		}
	}
}
