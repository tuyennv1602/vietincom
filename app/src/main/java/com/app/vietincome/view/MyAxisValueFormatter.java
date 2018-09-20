package com.app.vietincome.view;

import com.app.vietincome.manager.AppPreference;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

public class MyAxisValueFormatter implements IAxisValueFormatter {

	private DecimalFormat mFormat;

	public MyAxisValueFormatter() {
		mFormat = new DecimalFormat("###,###,###,##0.00");
	}

	@Override
	public String getFormattedValue(float value, AxisBase axis) {
		return AppPreference.INSTANCE.getCurrency().getSymbol() + mFormat.format(value);
	}
}
