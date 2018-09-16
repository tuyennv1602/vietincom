package com.app.vietincome.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Currency implements Parcelable {

	@SerializedName("code")
	@Expose
	private String code;
	@SerializedName("symbol")
	@Expose
	private String symbol;
	@SerializedName("flag")
	@Expose
	private String flag;

	private boolean isSelected;

	public final static Parcelable.Creator<Currency> CREATOR = new Creator<Currency>() {


		@SuppressWarnings({
				"unchecked"
		})
		public Currency createFromParcel(Parcel in) {
			return new Currency(in);
		}

		public Currency[] newArray(int size) {
			return (new Currency[size]);
		}

	};

	protected Currency(Parcel in) {
		this.code = ((String) in.readValue((String.class.getClassLoader())));
		this.symbol = ((String) in.readValue((String.class.getClassLoader())));
		this.flag = ((String) in.readValue((String.class.getClassLoader())));
	}

	public Currency(String code, String symbol, String flag) {
		this.code = code;
		this.symbol = symbol;
		this.flag = flag;
	}

	public Currency() {
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(code);
		dest.writeValue(symbol);
		dest.writeValue(flag);
	}

	public int describeContents() {
		return 0;
	}

}