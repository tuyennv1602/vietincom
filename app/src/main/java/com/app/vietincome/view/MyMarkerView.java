package com.app.vietincome.view;

import android.content.Context;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.manager.AppPreference;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

public class MyMarkerView extends MarkerView {
	private TextView tvPrice;
	private TextView tvTime;
	private IAxisValueFormatter xAxisValueFormatter;

	public MyMarkerView(Context context,IAxisValueFormatter xAxisValueFormatter) {
		super(context, R.layout.custom_marker_view);
		this.xAxisValueFormatter = xAxisValueFormatter;
		tvPrice = findViewById(R.id.tvPrice);
		tvTime = findViewById(R.id.tvTime);
	}

	// callbacks everytime the MarkerView is redrawn, can be used to update the
	// content (user-interface)
	@Override
	public void refreshContent(Entry e, Highlight highlight) {
		tvPrice.setText(AppPreference.INSTANCE.getCurrency().getSymbol() + Utils.formatNumber(e.getY(), 0, true));
		tvTime.setText(xAxisValueFormatter.getFormattedValue(e.getX(), null));
		super.refreshContent(e, highlight);
	}

	@Override
	public MPPointF getOffset() {
		return new MPPointF(-(getWidth() / 2), -getHeight());
	}


}
