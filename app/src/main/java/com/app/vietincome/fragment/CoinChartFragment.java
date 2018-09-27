package com.app.vietincome.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.model.Price;
import com.app.vietincome.model.Volume;
import com.app.vietincome.model.responses.ChartResponse;
import com.app.vietincome.network.ApiClient;
import com.app.vietincome.utils.CommonUtil;
import com.app.vietincome.utils.Constant;
import com.app.vietincome.utils.DateUtil;
import com.app.vietincome.view.PriceAxisValueFormatter;
import com.app.vietincome.view.MyMarkerView;
import com.app.vietincome.view.NavigationTopBar;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoinChartFragment extends BaseFragment {

	@BindView(R.id.combineChart)
	CombinedChart chart;

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

	@BindView(R.id.layoutExchange)
	LinearLayout layoutExchange;

	@BindView(R.id.layoutNumber)
	LinearLayout layoutNumber;

	private String symbol;
	private int time;
	private boolean isDarkTheme = AppPreference.INSTANCE.isDarkTheme();
	private double rate;

	public static CoinChartFragment newInstance(String symbol, int time, double rate) {
		CoinChartFragment fragment = new CoinChartFragment();
		fragment.symbol = symbol;
		fragment.time = time;
		fragment.rate = rate;
		return fragment;
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_coin_chart;
	}

	@Override
	public void onFragmentReady(View view) {
		onUpdatedTheme();
		styleChart();
		if (symbol.equals("global")) {
			layoutExchange.setVisibility(View.GONE);
			layoutNumber.setVisibility(View.GONE);
		} else {
			initData(this.time);
		}
		setData(new ChartResponse());
	}

	@Override
	public void onStart() {
		super.onStart();
		registerEventBus();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unRegisterEventBus();
	}

	private void initData(int time) {
		switch (time) {
			case Constant.HIS_24H: {
				tvForTime.setText("for 24 hours");
				getHistoryByDay(1);
				break;
			}
			case Constant.HIS_7D: {
				tvForTime.setText("for 7 days");
				getHistoryByDay(7);
				break;
			}
			case Constant.HIS_1M: {
				tvForTime.setText("for 1 month");
				getHistoryByDay(30);
				break;
			}
			case Constant.HIS_3M: {
				tvForTime.setText("for 3 months");
				getHistoryByDay(90);
				break;
			}
			case Constant.HIS_1Y: {
				tvForTime.setText("for 1 year");
				getHistoryByDay(365);
				break;
			}
			case Constant.HIS_ALL: {
				tvForTime.setText("for all time");
				getAllHistory();
				break;
			}
		}
	}

	private void styleChart() {
		// no description text
		chart.getDescription().setEnabled(false);
		// enable touch gestures
		chart.setTouchEnabled(true);
		chart.getLegend().setEnabled(false);
		chart.setDrawValueAboveBar(true);
		// enable dragging
		chart.setScaleEnabled(false);
		chart.fitScreen();
		chart.setDragYEnabled(false);
		chart.setExtraOffsets(0, 0, 0, 10);
		chart.setDrawOrder(new CombinedChart.DrawOrder[]{
				CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE});

		PriceAxisValueFormatter custom = new PriceAxisValueFormatter(rate);

		YAxis yRight = chart.getAxisRight();
		yRight.setLabelCount(4, false);
		yRight.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		yRight.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
		yRight.setDrawGridLines(AppPreference.INSTANCE.isVertical());
		yRight.setTextSize(12);
		yRight.setDrawAxisLine(false);
		yRight.setValueFormatter(custom);
		yRight.setAxisLineColor(Color.GRAY);

		YAxis yLeft = chart.getAxisLeft();
		yLeft.setDrawGridLines(false);
		yLeft.setDrawAxisLine(false);
		yLeft.setDrawLabels(false);
		yLeft.setTextColor(Color.WHITE);
		yLeft.setAxisMinimum(0f);
		yLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

		XAxis x = chart.getXAxis();
		x.setLabelCount(3, false);
		x.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		x.setTextSize(12);
		x.setDrawGridLines(AppPreference.INSTANCE.isHorizontal());
		x.setAxisLineColor(Color.GRAY);
		x.setAvoidFirstLastClipping(true);
		x.setPosition(XAxis.XAxisPosition.BOTTOM);
	}

	private void getAllHistory() {
		ApiClient.getChartService().getAllHistory(symbol).enqueue(new Callback<ChartResponse>() {
			@Override
			public void onResponse(Call<ChartResponse> call, Response<ChartResponse> response) {
				if (response.isSuccessful()) {
					setData(response.body());
				}
			}

			@Override
			public void onFailure(Call<ChartResponse> call, Throwable t) {
				showAlert("Failure","Get History: " + t.getMessage());
			}
		});
	}

	private void getHistoryByDay(int day) {
		ApiClient.getChartService().getHistoryByDay(day, symbol).enqueue(new Callback<ChartResponse>() {
			@Override
			public void onResponse(Call<ChartResponse> call, Response<ChartResponse> response) {
				hideProgressDialog();
				if (response.isSuccessful()) {
					setData(response.body());
					Log.d("__chart", "onResponse: " + response.body().getVolumes().size());
				}
			}

			@Override
			public void onFailure(Call<ChartResponse> call, Throwable t) {
				showAlert("Failure","Get History: " + t.getMessage());
			}
		});
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

	private void setData(ChartResponse chartResponse) {
		CombinedData data = new CombinedData();
		data.setData(generateLineData(chartResponse.getPrices()));
		if(AppPreference.INSTANCE.isVolume()){
			data.setData(generateBarChart(chartResponse.getVolumes()));
			chart.getAxisLeft().setAxisMaximum(data.getYMax(YAxis.AxisDependency.LEFT) * 4);
		}
		tvLowValue.setText(CommonUtil.formatCurrency(data.getYMin(YAxis.AxisDependency.RIGHT), rate, AppPreference.INSTANCE.getCurrency()));
		tvHighValue.setText(CommonUtil.formatCurrency(data.getYMax(YAxis.AxisDependency.RIGHT), rate, AppPreference.INSTANCE.getCurrency()));
		chart.setData(data);
		chart.animateXY(1000, 1000);
		chart.invalidate();
	}

	private LineData generateLineData(ArrayList<Price> prices) {
		ArrayList<Entry> yVals = new ArrayList<>();
		if (prices != null && prices.size() > 0) {
			for (int i = 0; i < prices.size(); i++) {
				yVals.add(new Entry(i, prices.get(i).getPrice()));
			}
		} else {
			for (int i = 0; i <= 100; i++) {
				yVals.add(new Entry(i, 0));
			}
		}
		LineDataSet set1 = new LineDataSet(yVals, "");
		set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
		set1.setCubicIntensity(0.2f);
		set1.setDrawCircles(false);
		set1.setLineWidth(2f);
		set1.setAxisDependency(YAxis.AxisDependency.RIGHT);
		set1.setColor(Color.parseColor("#45d178"));
		set1.setHighLightColor(Color.GRAY);
		set1.setHighlightLineWidth(1.5f);
		set1.enableDashedHighlightLine(10f, 5f, 0f);
		set1.setDrawHorizontalHighlightIndicator(false);
		if (prices != null && prices.size() > 0) {
			chart.getXAxis().setValueFormatter((value, axis) -> {
				if (time == Constant.HIS_24H) {
					return DateUtil.getStringTime(prices.get((int) value).getTime(), DateUtil.FORMAT_HOURS);
				}
				if (time == Constant.HIS_ALL) {
					return DateUtil.getStringTime(prices.get((int) value).getTime(), DateUtil.FORMAT_MONTH_OF_YEAR);
				}
				return DateUtil.getStringTime(prices.get((int) value).getTime(), DateUtil.FORMAT_DAY_OF_MONTH);
			});

		}
		MyMarkerView mv = new MyMarkerView(getContext(), chart.getXAxis().getValueFormatter(), R.layout.custom_marker_view);
		chart.setMarker(mv); // Set the marker to the chart
		// create a data object with the datasets
		LineData data = new LineData(set1);
		data.setDrawValues(false);
		return data;
	}

	private BarData generateBarChart(ArrayList<Volume> volumes) {
		ArrayList<BarEntry> entries = new ArrayList<>();
		if (volumes != null && volumes.size() > 0) {
			for (int i = 0; i < volumes.size(); i++) {
				if (i % 5 == 0)
					entries.add(new BarEntry(i, volumes.get(i).getPrice()));
			}
		}
		BarDataSet set = new BarDataSet(entries, "Bar 1");
		set.setColor(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
		set.setAxisDependency(YAxis.AxisDependency.LEFT);
		BarData data = new BarData(set);
		data.setDrawValues(false);
		data.setBarWidth(2f);
		return data;
	}

}