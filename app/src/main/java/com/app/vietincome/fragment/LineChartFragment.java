package com.app.vietincome.fragment;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.model.Data;
import com.app.vietincome.model.History;
import com.app.vietincome.utils.Constant;
import com.app.vietincome.view.MyAxisValueFormatter;
import com.app.vietincome.view.MyMarkerView;
import com.app.vietincome.view.NavigationTopBar;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class LineChartFragment extends BaseFragment {

	@BindView(R.id.lineChart)
	LineChart lineChart;

	@BindView(R.id.tvExchange)
	TextView tvExchange;

	@BindView(R.id.tvForTime)
	TextView tvForTime;

	@BindView(R.id.tvLow)
	TextView tvLow;

	@BindView(R.id.tvHigh)
	TextView tvHigh;

	@BindView(R.id.tvHighValue)
	TextView tvHighValue;

	@BindView(R.id.tvLowValue)
	TextView tvLowValue;

	@BindView(R.id.imgDot)
	ImageView imgDot;

	private String coinId;
	private int time;
	private ArrayList<History> data;
	private boolean isDarkTheme = AppPreference.INSTANCE.isDarkTheme();
	private boolean isVerticalLine = AppPreference.INSTANCE.isVertical();
	private boolean isHorizontalLine = AppPreference.INSTANCE.isHorizontal();

	public static LineChartFragment newInstance(String coinId, int time) {
		LineChartFragment fragment = new LineChartFragment();
		fragment.coinId = coinId;
		fragment.time = time;
		return fragment;
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_line_chart;
	}

	@Override
	public void onFragmentReady(View view) {
		onUpdatedTheme();
		initLabel();
		styleChart();
		setData(45, 100);
		lineChart.animateXY(1000, 1000);
		lineChart.setPaddingRelative(0, 0, 0, 5);
		lineChart.invalidate();
	}

	private void initLabel() {
		switch (time) {
			case Constant.HIS_24H: {
				tvForTime.setText("for 24 hours");
				break;
			}
			case Constant.HIS_7D: {
				tvForTime.setText("for 7 days");
				break;
			}
			case Constant.HIS_1M: {
				tvForTime.setText("for 1 month");
				break;
			}
			case Constant.HIS_3M: {
				tvForTime.setText("for 3 months");
				break;
			}
			case Constant.HIS_1Y: {
				tvForTime.setText("for 1 year");
				break;
			}
			case Constant.HIS_ALL: {
				tvForTime.setText("for all");
				break;
			}
		}
	}

	private void styleChart() {
		// no description text
		lineChart.getDescription().setEnabled(false);
		// enable touch gestures
		lineChart.setTouchEnabled(true);
		lineChart.getLegend().setEnabled(false);

		// enable dragging
		lineChart.setDragEnabled(true);
		lineChart.setScaleEnabled(false);

		MyAxisValueFormatter custom = new MyAxisValueFormatter();

		YAxis y = lineChart.getAxisRight();
		y.setLabelCount(4, false);
		y.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
		y.setDrawGridLines(isVerticalLine);
		y.setTextSize(12);
		y.setValueFormatter(custom);
		y.setAxisLineColor(Color.GRAY);

		lineChart.getAxisLeft().setEnabled(false);

		XAxis x = lineChart.getXAxis();
		x.setLabelCount(4, false);
		x.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		x.setTextSize(12);
		x.setDrawGridLines(isHorizontalLine);
		x.setAxisLineColor(Color.GRAY);
		x.setPosition(XAxis.XAxisPosition.BOTTOM);

		MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view);
		mv.setChartView(lineChart); // For bounds control
		lineChart.setMarker(mv); // Set the marker to the chart

	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {

	}

	@Override
	public void onUpdatedTheme() {
		tvForTime.setTextColor(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
		tvLow.setTextColor(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
		tvHigh.setTextColor(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
		imgDot.setColorFilter(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
	}

	private void setData(int count, float range) {

		ArrayList<Entry> yVals = new ArrayList<>();

		for (int i = 0; i < count; i++) {
			float mult = (range + 1);
			float val = (float) (Math.random() * mult) + 20;// + (float)
			// ((mult *
			// 0.1) / 10);
			yVals.add(new Entry(i, val));
		}

		LineDataSet set1;

		if (lineChart.getData() != null &&
				lineChart.getData().getDataSetCount() > 0) {
			set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
			set1.setValues(yVals);
			lineChart.getData().notifyDataChanged();
			lineChart.notifyDataSetChanged();
		} else {
			// create a dataset and give it a type
			set1 = new LineDataSet(yVals, "");
			set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
			set1.setCubicIntensity(0.2f);
			//set1.setDrawFilled(true);
			set1.setDrawCircles(false);
			set1.setLineWidth(1.8f);
			set1.setCircleRadius(4f);
			set1.setCircleColor(getColor(R.color.green));
			set1.setColor(getColor(R.color.green));
			set1.setFillAlpha(100);
			set1.setHighLightColor(Color.GRAY);
			set1.enableDashedHighlightLine(10f, 5f, 0f);
			set1.setDrawHorizontalHighlightIndicator(false);
			set1.setFillFormatter(new IFillFormatter() {
				@Override
				public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
					return -10;
				}
			});

			// create a data object with the datasets
			LineData data = new LineData(set1);
			data.setValueTextSize(9f);
			data.setDrawValues(false);
			// set data
			lineChart.setData(data);
		}
	}
}
