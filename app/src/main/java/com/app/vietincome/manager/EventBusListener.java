package com.app.vietincome.manager;

public class EventBusListener {

	public static class UpdatedTheme{}

	public static class RefreshData{
		public int tab;
		public RefreshData(int tab){
			this.tab = tab;
		}
	}

	public static class UpdateNews{}
}
