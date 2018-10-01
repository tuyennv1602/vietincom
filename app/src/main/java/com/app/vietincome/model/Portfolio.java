package com.app.vietincome.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Portfolio implements Parcelable {

	private int id;
	private String name;
	private String symbol;
	private Quotes quotes;
	private ArrayList<Item> items;

	public final static Parcelable.Creator<Portfolio> CREATOR = new Creator<Portfolio>() {

		@SuppressWarnings({
				"unchecked"
		})
		public Portfolio createFromParcel(Parcel in) {
			return new Portfolio(in);
		}

		public Portfolio[] newArray(int size) {
			return (new Portfolio[size]);
		}

	};

	protected Portfolio(Parcel in) {
		this.id = ((Integer) in.readValue(Integer.class.getClassLoader()));
		this.name = ((String) in.readValue(String.class.getClassLoader()));
		this.symbol = ((String) in.readValue(String.class.getClassLoader()));
		this.quotes = ((Quotes) in.readValue(Quotes.class.getClassLoader()));
		in.readList(this.items, (Item.class.getClassLoader()));
	}

	public Portfolio(){}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Quotes getQuotes() {
		return quotes;
	}

	public void setQuotes(Quotes quotes) {
		this.quotes = quotes;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	public static Creator<Portfolio> getCREATOR() {
		return CREATOR;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(id);
		dest.writeValue(name);
		dest.writeValue(symbol);
		dest.writeValue(quotes);
		dest.writeValue(items);
	}

	public int describeContents() {
		return 0;
	}


}
