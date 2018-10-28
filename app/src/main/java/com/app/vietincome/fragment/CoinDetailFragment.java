package com.app.vietincome.fragment;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.adapter.ChartViewPagerAdapter;
import com.app.vietincome.adapter.CoinInfoAdapter;
import com.app.vietincome.adapter.CoinNewsAdapter;
import com.app.vietincome.adapter.TopMarketAdapter;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.EventBusListener;
import com.app.vietincome.manager.interfaces.ItemClickListener;
import com.app.vietincome.model.CoinInfo;
import com.app.vietincome.model.Currency;
import com.app.vietincome.model.Data;
import com.app.vietincome.model.Market;
import com.app.vietincome.model.News;
import com.app.vietincome.model.USD;
import com.app.vietincome.utils.CommonUtil;
import com.app.vietincome.view.CustomItemDecoration;
import com.app.vietincome.view.HighLightTextView;
import com.app.vietincome.view.NavigationTopBar;
import com.app.vietincome.view.NoneSwipeViewpager;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class CoinDetailFragment extends BaseFragment implements ItemClickListener {

	@BindView(R.id.layoutRoot)
	LinearLayout layoutRoot;

	@BindView(R.id.tvPrice)
	TextView tvPrice;

	@BindView(R.id.tvBitcoin)
	TextView tvBitcoin;

	@BindView(R.id.tvSymbol)
	TextView tvSymbol;

	@BindView(R.id.tvEndPrice)
	TextView tvEndPrice;

	@BindView(R.id.viewPagerChart)
	NoneSwipeViewpager viewPagerChart;

	@BindView(R.id.tabLayoutTime)
	TabLayout tabLayoutTime;

	@BindView(R.id.rcvCoinInfo)
	RecyclerView rcvCoinInfo;

	@BindView(R.id.tvCoinInfo)
	TextView tvCoinInfor;

	@BindView(R.id.tvNews)
	TextView tvNews;

	@BindView(R.id.rcvNews)
	RecyclerView rcvNews;

	@BindView(R.id.tvShowNews)
	TextView tvShowNews;

	@BindView(R.id.imgNotification)
	ImageView imgNotification;

	@BindView(R.id.tvAlert)
	TextView tvAlert;

	@BindView(R.id.tvNoAlert)
	TextView tvNoAlert;

	@BindView(R.id.tvCreateAlert)
	HighLightTextView tvCreateAlert;

	@BindView(R.id.tvTopMarket)
	TextView tvTopMarket;

	@BindView(R.id.rcvTopMarket)
	RecyclerView rcvTopMarket;

	@BindView(R.id.tvRank)
	TextView tvRank;

	@BindView(R.id.tvExchange)
	TextView tvExchange;

	@BindView(R.id.tvPair)
	TextView tvPair;

	@BindView(R.id.tvPriceTop)
	TextView tvPriceTop;

	@BindView(R.id.tvVolume)
	TextView tvVolume;

	private Data data;
	private Currency currency = AppPreference.INSTANCE.getCurrency();
	private double rate = 1;
	private ChartViewPagerAdapter chartViewPagerAdapter;
	private ArrayList<CoinInfo> coinInfos;
	private CoinInfoAdapter coinInfoAdapter;
	private ArrayList<News> news = AppPreference.INSTANCE.getNews();
	private CoinNewsAdapter coinNewsAdapter;
	private ArrayList<Market> markets;
	private TopMarketAdapter topMarketAdapter;
	private boolean isLineChart = true;
	private boolean isShowPrice;

	public static CoinDetailFragment newInstance(Data data, boolean isShowPrice) {
		CoinDetailFragment fragment = new CoinDetailFragment();
		fragment.data = data;
		fragment.rate = AppPreference.INSTANCE.getRate();
		fragment.isShowPrice = isShowPrice;
		return fragment;
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_coin_detail;
	}

	@Override
	public void onFragmentReady(View view) {
		onUpdatedTheme();
		initPrice();
		initChartView();
		initCoinInfor();
		initNews();
		initTopMarket();
		tvNoAlert.setText(getString(R.string.no_alert, data.getName()));
	}

	@Override
	public void onLeftClicked() {
		super.onLeftClicked();
		goBack();
	}

	private void initPrice() {
		USD usd = data.getQuotes().getUSD();
		double priceValue;
		if (isShowPrice) {
			priceValue = AppPreference.INSTANCE.getCurrency().getCode().equals("USD") ? usd.getPrice() : usd.getPrice() * rate;
		} else {
			priceValue = AppPreference.INSTANCE.getCurrency().getCode().equals("USD") ? usd.getVolume24h() : usd.getVolume24h() * rate;
		}
		String price;
		String path;
		String endPrice = "";
		if (priceValue < 1.0) {
			path = String.format(Locale.US, "%.4f", priceValue);
			price = String.valueOf(path.charAt(0));
			endPrice = path.substring(1);
		} else if (priceValue < 1000) {
			path = String.format(Locale.US, "%.2f", priceValue);
			price = path.substring(0, path.length() - 3);
			endPrice = path.substring(path.length() - 3);
		} else {
			DecimalFormat dFormat = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
			dFormat.applyPattern("###,###");
			price = dFormat.format(priceValue);
		}
		tvPrice.setText(price);
		tvSymbol.setText(currency.getSymbol());
		tvEndPrice.setText(endPrice);
		double btcPrice = isShowPrice ? data.getQuotes().getBTC().getPrice() : data.getQuotes().getBTC().getVolume24h();
		StringBuilder bitcoin = new StringBuilder();
		bitcoin.append(getContext().getString(R.string.bitcoin));
		bitcoin.append(CommonUtil.formatCurrency(btcPrice, false));
		tvBitcoin.setText(bitcoin.toString());
	}

	private void initChartView() {
		if (chartViewPagerAdapter == null) {
			chartViewPagerAdapter = new ChartViewPagerAdapter(getContext(), getChildFragmentManager(), data.getSymbol(),
					isShowPrice ? data.getQuotes().getUSD().getPrice() : data.getQuotes().getUSD().getVolume24h(),
					rate,
					isShowPrice);
		}
		viewPagerChart.setAdapter(chartViewPagerAdapter);
		viewPagerChart.setOffscreenPageLimit(6);
		tabLayoutTime.setupWithViewPager(viewPagerChart);
	}

	private void initCoinInfor() {
		Currency currency = AppPreference.INSTANCE.getCurrency();
		if (coinInfos == null) {
			coinInfos = new ArrayList<>();
			coinInfos.add(new CoinInfo("Rank", "#" + data.getRank()));
			DecimalFormat dFormat = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
			dFormat.applyPattern("###,###,###,###");
			if (currency.getCode().equals("USD")) {
				coinInfos.add(new CoinInfo("Market Cap", currency.getSymbol() + dFormat.format(data.getQuotes().getUSD().getMarketCap())));
				coinInfos.add(new CoinInfo("Volume (24h)", currency.getSymbol() + dFormat.format(data.getQuotes().getUSD().getVolume24h())));
				coinInfos.add(new CoinInfo("Available Supply", currency.getSymbol() + dFormat.format(data.getCirculatingSupply()) + " " + data.getSymbol()));
				coinInfos.add(new CoinInfo("Total Supply", currency.getSymbol() + dFormat.format(data.getTotalSupply()) + " " + data.getSymbol()));
			} else {
				coinInfos.add(new CoinInfo("Market Cap", currency.getSymbol() + dFormat.format(data.getQuotes().getUSD().getMarketCap() * rate)));
				coinInfos.add(new CoinInfo("Volume (24h)", currency.getSymbol() + dFormat.format(data.getQuotes().getUSD().getVolume24h() * rate)));
				coinInfos.add(new CoinInfo("Available Supply", currency.getSymbol() + dFormat.format(data.getCirculatingSupply() * rate) + " " + data.getSymbol()));
				coinInfos.add(new CoinInfo("Total Supply", currency.getSymbol() + dFormat.format(data.getTotalSupply() * rate) + " " + data.getSymbol()));
			}
		}
		if (coinInfoAdapter == null) {
			coinInfoAdapter = new CoinInfoAdapter(coinInfos);
		}
		rcvCoinInfo.setLayoutManager(new LinearLayoutManager(getContext()));
		rcvCoinInfo.setHasFixedSize(true);
		rcvCoinInfo.setAdapter(coinInfoAdapter);
	}

	private void initNews() {
		if (coinNewsAdapter == null) {
			coinNewsAdapter = new CoinNewsAdapter(news, isDarkTheme, this);
		}
		rcvNews.setHasFixedSize(true);
		rcvNews.setLayoutManager(new LinearLayoutManager(getContext()));
		rcvNews.addItemDecoration(new CustomItemDecoration(1));
		rcvNews.setAdapter(coinNewsAdapter);
	}

	private void initTopMarket() {
		if (markets == null) {
			markets = new ArrayList<>();
		}
		if (topMarketAdapter == null) {
			topMarketAdapter = new TopMarketAdapter(markets, isDarkTheme);
		}
		rcvTopMarket.setLayoutManager(new LinearLayoutManager(getContext()));
		rcvTopMarket.setHasFixedSize(true);
		rcvTopMarket.addItemDecoration(new CustomItemDecoration(1));
		rcvTopMarket.setAdapter(topMarketAdapter);
	}


	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
		navitop.showImgLeft(true);
		navitop.showImgRight(false);
		navitop.setImgLeft(R.drawable.close);
		navitop.setImgRight(R.drawable.icn_linechart);
		navitop.setSubTitle(data.getSymbol());
		navitop.setTvTitle(data.getName());
		navitop.changeLeftPadding(18);
	}

	@Override
	public void onRightClicked() {
		super.onRightClicked();
		navigationTopBar.setImgRight(isLineChart ? R.drawable.icn_candlestick : R.drawable.icn_linechart);
		EventBus.getDefault().post(new EventBusListener.SwitchChart(isLineChart));
		isLineChart = !isLineChart;
	}

	@Override
	public void onUpdatedTheme() {
		tabLayoutTime.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
		tabLayoutTime.setSelectedTabIndicatorColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		tabLayoutTime.setTabTextColors(
				isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray), isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image)
		);
		layoutRoot.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
		setTextColor(tvPrice);
		setTextColor(tvBitcoin);
		setTextColor(tvSymbol);
		setTextColor(tvEndPrice);
		rcvCoinInfo.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
		tvCoinInfor.setBackgroundColor(isDarkTheme ? getColor(R.color.black_background) : getColor(R.color.gray_background));
		tvNews.setBackgroundColor(isDarkTheme ? getColor(R.color.black_background) : getColor(R.color.gray_background));
		rcvNews.setBackgroundColor(isDarkTheme ? getColor(R.color.black) : getColor(R.color.color_line));
		tvShowNews.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
		tvShowNews.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		tvAlert.setBackgroundColor(isDarkTheme ? getColor(R.color.black_background) : getColor(R.color.gray_background));
		setTextColorGray(tvAlert);
		setTextColorGray(tvNoAlert);
		imgNotification.setColorFilter(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
		tvCreateAlert.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		tvTopMarket.setBackgroundColor(isDarkTheme ? getColor(R.color.black_background) : getColor(R.color.gray_background));
		rcvTopMarket.setBackgroundColor(isDarkTheme ? getColor(R.color.black) : getColor(R.color.color_line));
		setTextColorGray(tvRank);
		setTextColorGray(tvExchange);
		setTextColorGray(tvPair);
		setTextColorGray(tvPriceTop);
		setTextColorGray(tvVolume);
		tvCoinInfor.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		tvNews.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		tvTopMarket.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));

	}

	private void setTextColorGray(TextView tv) {
		tv.setTextColor(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
	}

	@OnClick(R.id.tvShowNews)
	void showAllNews() {
		pushFragment(NewsFragment.newInstance(news), R.anim.slide_up, R.anim.slide_down);
	}

	@Override
	public void onItemClicked(int position) {
		AppPreference.INSTANCE.addNewsRead(news.get(position).getId());
		coinNewsAdapter.notifyItemChanged(position);
		openLink(news.get(position).getUrl());
	}
}
