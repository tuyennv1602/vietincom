package com.app.vietincome.view;

import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.model.Currency;
import com.app.vietincome.utils.CommonUtil;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;
import java.util.Locale;

public class PriceAxisValueFormatter implements IAxisValueFormatter {

	private DecimalFormat mFormat = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
	private double rate;
	private boolean isShowPrice;

	public PriceAxisValueFormatter(double rate, boolean isShowPrice) {
		this.rate = rate;
		this.isShowPrice = isShowPrice;
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
			mFormat.applyPattern("###,##0.00");
		} else {
			mFormat.applyPattern("###,###,###,###");
		}
		return currency.getSymbol() + (isShowPrice ?  mFormat.format(price) : withSuffix(price));
	}

	public String withSuffix(double price) {
		if (price < 1000) return CommonUtil.formatCurrency(price, 1, null);
		int exp = (int) (Math.log(price) / Math.log(1000));
		return String.format(Locale.US, "%.2f%c",
				price / Math.pow(1000, exp),
				"KMBTPE".charAt(exp - 1));
	}

}
