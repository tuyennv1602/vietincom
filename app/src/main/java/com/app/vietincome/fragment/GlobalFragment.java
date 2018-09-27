package com.app.vietincome.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.activity.ParentActivity;
import com.app.vietincome.adapter.ChartViewPagerAdapter;
import com.app.vietincome.adapter.TopCoinAdapter;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.interfaces.ItemClickListener;
import com.app.vietincome.model.Currency;
import com.app.vietincome.model.Data;
import com.app.vietincome.model.responses.CoinResponse;
import com.app.vietincome.model.responses.GlobalResponse;
import com.app.vietincome.network.ApiClient;
import com.app.vietincome.utils.Constant;
import com.app.vietincome.view.CustomItemDecoration;
import com.app.vietincome.view.NavigationTopBar;
import com.app.vietincome.view.NoneSwipeViewpager;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GlobalFragment extends BaseFragment {

	@BindView(R.id.viewPagerChart)
	NoneSwipeViewpager viewPagerChart;

	@BindView(R.id.tabLayoutTime)
	TabLayout tabLayoutTime;

	@BindView(R.id.layoutMarketCap)
	LinearLayout layoutMarketCap;

	@BindView(R.id.layoutRoot)
	LinearLayout layoutRoot;

	@BindView(R.id.view1)
	View view1;

	@BindView(R.id.view2)
	View view2;


	@BindView(R.id.tvMarketShare)
	TextView tvMarketShare;

	@BindView(R.id.chartMarket)
	PieChart chartMarket;

	@BindView(R.id.tvMarketValue)
	TextView tvMarketValue;

	@BindView(R.id.tvRankMarket)
	TextView tvRankMarket;

	@BindView(R.id.tvNameMarket)
	TextView tvNameMarket;

	@BindView(R.id.tvMarketCap)
	TextView tvMarketCap;

	@BindView(R.id.tvPercentMarket)
	TextView tvPercentMarket;

	@BindView(R.id.tvRankVolume)
	TextView tvRankVolume;

	@BindView(R.id.tvNameVolume)
	TextView tvNameVolume;

	@BindView(R.id.tvVolume)
	TextView tvVolume;

	@BindView(R.id.tvPercentVolume)
	TextView tvPercentVolume;

	@BindView(R.id.rcvMarketCap)
	RecyclerView rcvMarketCap;

	@BindView(R.id.rcvVolume)
	RecyclerView rcvVolume;

	@BindView(R.id.chartVolume)
	PieChart chartVolume;

	@BindView(R.id.layoutVolume)
	LinearLayout layoutVolume;

	@BindView(R.id.tvVolumeValue)
	TextView tvVolumeValue;

	@BindView(R.id.tvShowMarketCap)
	TextView tvShowMarketCap;

	@BindView(R.id.tvShowVolume)
	TextView tvShowVolume;

	private ChartViewPagerAdapter chartViewPagerAdapter;
	private Currency currency = AppPreference.INSTANCE.getCurrency();
	private double rate;
	private Data global;
	private DecimalFormat dfm = new DecimalFormat("###,###,###,###");
	private TopCoinAdapter topMarketAdapter;
	private TopCoinAdapter topVolumeAdapter;
	private ArrayList<Data> topMarkets;
	private ArrayList<Data> topVolumes;
	private boolean showMarketCap = false;
	private boolean showVolume = false;

	public static GlobalFragment newInstance(double rate) {
		GlobalFragment fragment = new GlobalFragment();
		fragment.rate = rate;
		return fragment;
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_global;
	}

	@Override
	public void onFragmentReady(View view) {
		onUpdatedTheme();
		initChartView();
		stylePieChart(chartMarket);
		stylePieChart(chartVolume);
	}

	@Override
	public void onResume() {
		super.onResume();
		getGlobalData();
	}

	private void initData() {
		tvMarketValue.setText("$" + dfm.format(global.getQuotes().getUSD().getTotalMarketCap().longValue()));
		tvVolumeValue.setText("$" + dfm.format(global.getQuotes().getUSD().getTotalVolume24h().longValue()));
//		if (currency.getCode().equals("USD")) {
//			tvMarketValue.setText(currency.getSymbol() + dfm.format(global.getQuotes().getUSD().getTotalMarketCap().longValue()));
//			tvVolumeValue.setText(currency.getSymbol() + dfm.format(global.getQuotes().getUSD().getTotalVolume24h().longValue()));
//		}else{
//			tvMarketValue.setText(currency.getSymbol() + dfm.format(global.getQuotes().getUSD().getTotalMarketCap().longValue() * rate));
//			tvVolumeValue.setText(currency.getSymbol() + dfm.format(global.getQuotes().getUSD().getTotalVolume24h().longValue() * rate));
//		}
		if(topMarkets == null){
			topMarkets = new ArrayList<>();
		}
		if(topMarketAdapter == null){
			topMarketAdapter = new TopCoinAdapter(Constant.MARKET_CAP, rate, topMarkets, global.getQuotes().getUSD().getTotalMarketCap(), position -> openCoinDetail(topMarkets.get(position)));
		}
		rcvMarketCap.setLayoutManager(new LinearLayoutManager(getContext()));
		rcvMarketCap.setHasFixedSize(true);
		rcvMarketCap.addItemDecoration(new CustomItemDecoration(1));
		rcvMarketCap.setAdapter(topMarketAdapter);
		if(topVolumes == null){
			topVolumes = new ArrayList<>();
		}
		if(topVolumeAdapter == null){
			topVolumeAdapter = new TopCoinAdapter(Constant.VOLUME_24H, rate, topVolumes, global.getQuotes().getUSD().getTotalVolume24h(), position -> openCoinDetail(topVolumes.get(position)));
		}
		rcvVolume.setLayoutManager(new LinearLayoutManager(getContext()));
		rcvVolume.setHasFixedSize(true);
		rcvVolume.addItemDecoration(new CustomItemDecoration(1));
		rcvVolume.setAdapter(topVolumeAdapter);
	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
		navitop.showImgLeft(false);
		navitop.showImgRight(true);
		navitop.setImgRight(R.drawable.wifi);
		navitop.setTvTitle("Global Market");
	}

	@Override
	public void onRightClicked() {
		super.onRightClicked();
		goBack();
	}

	@OnClick(R.id.tvShowMarketCap)
	void toggleMarketCap(){
		showMarketCap = !showMarketCap;
		tvShowMarketCap.setText(showMarketCap ? "Show less..." : "Show more...");
		topMarketAdapter.toggleShowMore();
	}

	@OnClick(R.id.tvShowVolume)
	void toggleVolume(){
		showVolume = !showVolume;
		tvShowVolume.setText(showVolume ? "Show less..." : "Show more...");
		topVolumeAdapter.toggleShowMore();
	}

	private void initChartView() {
		if (chartViewPagerAdapter == null) {
			chartViewPagerAdapter = new ChartViewPagerAdapter(getContext(), getChildFragmentManager(), "global", rate);
		}
		viewPagerChart.setAdapter(chartViewPagerAdapter);
		viewPagerChart.setOffscreenPageLimit(6);
		tabLayoutTime.setupWithViewPager(viewPagerChart);
	}

	private void stylePieChart(PieChart mChart) {
		mChart.getDescription().setEnabled(false);
		mChart.setUsePercentValues(true);
		mChart.getLegend().setEnabled(false);
		mChart.setDragDecelerationFrictionCoef(0.95f);
		mChart.setDrawHoleEnabled(false);
		mChart.setDrawCenterText(false);
		mChart.setRotationAngle(270);
		mChart.setRotationEnabled(false);
		mChart.setHighlightPerTapEnabled(true);
		mChart.animateY(500);
		mChart.highlightValues(null);
	}

	private PieData setData(int type, ArrayList<Data> dataCoin) {
		ArrayList<PieEntry> entries = new ArrayList<>();
		if(type == Constant.MARKET_CAP) {
			float first = (float) ((dataCoin.get(0).getQuotes().getUSD().getMarketCap() / global.getQuotes().getUSD().getTotalMarketCap()) * 100);
			float second = (float) ((dataCoin.get(1).getQuotes().getUSD().getMarketCap() / global.getQuotes().getUSD().getTotalMarketCap()) * 100);
			float third = (float) ((dataCoin.get(2).getQuotes().getUSD().getMarketCap() / global.getQuotes().getUSD().getTotalMarketCap()) * 100);
			float end = 100 - (first + second + third);
			entries.add(new PieEntry(first, dataCoin.get(0).getSymbol()));
			entries.add(new PieEntry(second, dataCoin.get(1).getSymbol()));
			entries.add(new PieEntry(third, dataCoin.get(2).getSymbol()));
			entries.add(new PieEntry(end, "Others"));

		}else{
			float first = (float) ((dataCoin.get(0).getQuotes().getUSD().getVolume24h() / global.getQuotes().getUSD().getTotalVolume24h()) * 100);
			float second = (float) ((dataCoin.get(1).getQuotes().getUSD().getVolume24h() / global.getQuotes().getUSD().getTotalVolume24h()) * 100);
			float third = (float) ((dataCoin.get(2).getQuotes().getUSD().getVolume24h() / global.getQuotes().getUSD().getTotalVolume24h()) * 100);
			float end = 100 - (first + second + third);
			entries.add(new PieEntry(first, dataCoin.get(0).getSymbol()));
			entries.add(new PieEntry(second, dataCoin.get(1).getSymbol()));
			entries.add(new PieEntry(third, dataCoin.get(2).getSymbol()));
			entries.add(new PieEntry(end, "Others"));
		}
		PieDataSet dataSet = new PieDataSet(entries, "");
		dataSet.setDrawIcons(false);
		dataSet.setSliceSpace(2f);
		dataSet.setSelectionShift(5f);
		// add a lot of colors

		ArrayList<Integer> colors = new ArrayList<>();
		colors.add(Color.parseColor("#e14f32"));
		colors.add(Color.parseColor("#d6be28"));
		colors.add(Color.parseColor("#2e7dd7"));
		colors.add(Color.parseColor("#2c2c3d"));
		dataSet.setColors(colors);
		//dataSet.setSelectionShift(0f);

		PieData data = new PieData(dataSet);
		data.setValueFormatter(new PercentFormatter());
		data.setValueTextSize(15f);
		data.setValueTextColor(Color.WHITE);
		return data;
	}

	private void getTopMarketCap(){
		ApiClient.getAllCoinService().getTopMarketCap().enqueue(new Callback<CoinResponse>() {
			@Override
			public void onResponse(Call<CoinResponse> call, Response<CoinResponse> response) {
				navigationTopBar.hideProgressBar();
				if(response.isSuccessful()){
					if(response.body().getMetadata().isSuccess()){
						chartMarket.setData(setData(Constant.MARKET_CAP, response.body().getData()));
						chartMarket.invalidate();
						topMarkets.clear();
						topMarkets.addAll(response.body().getData());
						topMarketAdapter.setTopCoinData(topMarkets);
					}
				}
			}

			@Override
			public void onFailure(Call<CoinResponse> call, Throwable t) {
				navigationTopBar.hideProgressBar();
				showAlert("Failure", "Get TopMarkets: " +t.getMessage());
			}
		});
	}

	private void getTopVolume(){
		ApiClient.getAllCoinService().getTopVolume().enqueue(new Callback<CoinResponse>() {
			@Override
			public void onResponse(Call<CoinResponse> call, Response<CoinResponse> response) {
				navigationTopBar.hideProgressBar();
				if(response.isSuccessful()){
					if(response.body().getMetadata().isSuccess()){
						chartVolume.setData(setData(Constant.VOLUME_24H, response.body().getData()));
						chartVolume.invalidate();
						topVolumes.clear();
						topVolumes.addAll(response.body().getData());
						topVolumeAdapter.setTopCoinData(topVolumes);
					}
				}
			}

			@Override
			public void onFailure(Call<CoinResponse> call, Throwable t) {
				navigationTopBar.hideProgressBar();
				showAlert("Failure", "Get TopVolumes: " +t.getMessage());
			}
		});
	}

	private void getGlobalData(){
		navigationTopBar.showProgressBar();
		ApiClient.getAllCoinService().getGlobalData().enqueue(new Callback<GlobalResponse>() {
			@Override
			public void onResponse(Call<GlobalResponse> call, Response<GlobalResponse> response) {
				if(response.isSuccessful()){
					if(response.body().getMetadata().isSuccess()){
						GlobalFragment.this.global = response.body().getData();
						initData();
						getTopMarketCap();
						getTopVolume();
					}
				}
			}

			@Override
			public void onFailure(Call<GlobalResponse> call, Throwable t) {
				navigationTopBar.hideProgressBar();
				showAlert("Failure","Get Global Data: " + t.getMessage());
			}
		});
	}

	@Override
	public void onUpdatedTheme() {
		layoutVolume.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		layoutMarketCap.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		layoutRoot.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
		view1.setBackgroundColor(isDarkTheme ? getColor(R.color.black_background) : getColor(R.color.gray_background));
		view2.setBackgroundColor(isDarkTheme ? getColor(R.color.black_background) : getColor(R.color.gray_background));
		tabLayoutTime.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
		tabLayoutTime.setSelectedTabIndicatorColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		tabLayoutTime.setTabTextColors(
				isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray), isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image)
		);
		tvMarketShare.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		setTextColorGray(tvRankMarket);
		setTextColorGray(tvNameMarket);
		setTextColorGray(tvMarketCap);
		setTextColorGray(tvPercentMarket);
		setTextColorGray(tvRankVolume);
		setTextColorGray(tvNameVolume);
		setTextColorGray(tvVolume);
		setTextColorGray(tvPercentVolume);
		rcvMarketCap.setBackgroundColor(isDarkTheme ? getColor(R.color.black) : getColor(R.color.color_line));
		rcvVolume.setBackgroundColor(isDarkTheme ? getColor(R.color.black) : getColor(R.color.color_line));
		tvShowMarketCap.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		tvShowVolume.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
	}

	private void setTextColorGray(TextView tv) {
		tv.setTextColor(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
	}

	private void openCoinDetail(Data data){
		Intent parent = new Intent(getContext(), ParentActivity.class);
		parent.putExtra("coin", data);
		parent.putExtra(Constant.KEY_SCREEN, Constant.COIN_DETAIL);
		parent.putExtra(Constant.KEY_RATE, rate);
		startActivity(parent);
	}
}
