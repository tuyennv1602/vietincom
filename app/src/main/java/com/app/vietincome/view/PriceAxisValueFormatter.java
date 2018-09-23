package com.app.vietincome.view;

import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.model.Currency;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

public class PriceAxisValueFormatter implements IAxisValueFormatter {

	private DecimalFormat mFormat;
	private double rate;

	public PriceAxisValueFormatter(double rate) {
		this.rate = rate;
	}

	@Override
	public String getFormattedValue(float value, AxisBase axis) {
		Currency currency = AppPreference.INSTANCE.getCurrency();
		double price;
		if(currency.getCode().equals("USD")){
			price = value;
		}else{
			price = value * rate;
		}
		if (price < 1000) {
			mFormat = new DecimalFormat("###,###,###,##0.00");
		} else {
			mFormat = new DecimalFormat("###,###,###,###");
		}
		return currency.getSymbol() + mFormat.format(price);
	}
}
