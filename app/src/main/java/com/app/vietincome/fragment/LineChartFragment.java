package com.app.vietincome.fragment;

import android.view.View;

import com.app.vietincome.R;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.model.Data;
import com.app.vietincome.model.History;
import com.app.vietincome.view.NavigationTopBar;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class LineChartFragment extends BaseFragment {

	@BindView(R.id.lineChart)
	LineChart lineChart;

	private String coinId;
	private String time;
	private ArrayList<History> data;
	private ArrayList<Entry> entries = new ArrayList<Entry>();

	public static LineChartFragment newInstance(String coinId, String time){
		LineChartFragment fragment= new LineChartFragment();
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
		entries.add(new Entry(0, 0));
		entries.add(new Entry(1, 1));
		entries.add(new Entry(2, 3));
		entries.add(new Entry(5, 6));
		entries.add(new Entry(10, 7));
		entries.add(new Entry(100, 50));
		entries.add(new Entry(0, 0));
		entries.add(new Entry(1, 1));
		entries.add(new Entry(2, 3));
		entries.add(new Entry(5, 6));
		entries.add(new Entry(10, 7));
		entries.add(new Entry(100, 50));
		LineDataSet dataSet = new LineDataSet(entries, "Label");
		LineData data = new LineData(dataSet);
		data.setDrawValues(true);
		data.setHighlightEnabled(true);
		lineChart.setAutoScaleMinMaxEnabled(true);
		styleChart();
		lineChart.setData(data);
		lineChart.invalidate();
	}

	private void styleChart(){
		AxisBase yAxisLeft = lineChart.getAxisLeft();
		yAxisLeft.setDrawLabels(false);
		AxisBase yAxisRight = lineChart.getAxisRight();
		yAxisRight.setDrawLabels(true);
		yAxisRight.setDrawGridLines(true);
	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {

	}

	@Override
	public void onUpdatedTheme() {

	}
}
