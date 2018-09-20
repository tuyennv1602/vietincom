package com.app.vietincome.fragment;

import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.adapter.ChartViewPagerAdapter;
import com.app.vietincome.adapter.CoinInfoAdapter;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.model.CoinInfo;
import com.app.vietincome.model.Currency;
import com.app.vietincome.model.Data;
import com.app.vietincome.model.USD;
import com.app.vietincome.view.NavigationTopBar;
import com.app.vietincome.view.NoneSwipeViewpager;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;

public class CoinDetailFragment extends BaseFragment {

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

	private Data data;
	private Currency currency = AppPreference.INSTANCE.getCurrency();
	private double rate;
	private ChartViewPagerAdapter chartViewPagerAdapter;
	private ArrayList<CoinInfo> coinInfos;
	private CoinInfoAdapter coinInfoAdapter;

	public static CoinDetailFragment newInstance(Data data, double rate) {
		CoinDetailFragment fragment = new CoinDetailFragment();
		fragment.data = data;
		fragment.rate = rate;
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
	}

	@Override
	public void onLeftClicked() {
		super.onLeftClicked();
		goBack(R.anim.zoom_out);
	}

	private void initPrice() {
		USD usd = data.getQuotes().getUSD();
		double priceValue = AppPreference.INSTANCE.getCurrency().getCode().equals("USD") ? usd.getPrice() : usd.getPrice() * rate;
		String price;
		String path;
		String endPrice = "";
		if (usd.getPrice() < 1.0) {
			path = String.format("%.4f", priceValue);
			price = String.valueOf(path.charAt(0));
			endPrice = path.substring(1);
		} else if (usd.getPrice() < 1000) {
			path = String.format("%.2f", priceValue);
			price = path.substring(0, path.length() - 3);
			endPrice = path.substring(path.length() - 3);
		} else {
			DecimalFormat dFormat = new DecimalFormat("###,###");
			price = dFormat.format(priceValue);
		}
		tvPrice.setText(price);
		tvSymbol.setText(currency.getSymbol());
		tvEndPrice.setText(endPrice);

		StringBuilder bitcoin = new StringBuilder();
		bitcoin.append(getContext().getString(R.string.bitcoin));
		bitcoin.append(String.format("%.8f", (data.getQuotes().getBTC().getPrice())));
		tvBitcoin.setText(bitcoin.toString());
	}

	private void initChartView() {
		if (chartViewPagerAdapter == null) {
			chartViewPagerAdapter = new ChartViewPagerAdapter(getContext(), getChildFragmentManager(), data.getName());
		}
		viewPagerChart.setAdapter(chartViewPagerAdapter);
		tabLayoutTime.setupWithViewPager(viewPagerChart);
	}

	private void initCoinInfor() {
		Currency currency = AppPreference.INSTANCE.getCurrency();
		if (coinInfos == null) {
			coinInfos = new ArrayList<>();
			coinInfos.add(new CoinInfo("Rank", "#" + data.getRank()));
			DecimalFormat dFormat = new DecimalFormat("###,###,###,###");
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

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
		navitop.showImgLeft(true);
		navitop.showImgRight(false);
		navitop.setImgLeft(R.drawable.close);
		navitop.setSubTitle(data.getSymbol());
		navitop.setTvTitle(data.getName());
		navitop.changeLeftPadding(18);
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
		tvCoinInfor.setTextColor(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
		tvNews.setBackgroundColor(isDarkTheme ? getColor(R.color.black_background) : getColor(R.color.gray_background));
		tvNews.setTextColor(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
	}

}
