package com.app.vietincome.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.activity.ParentActivity;
import com.app.vietincome.adapter.AllCoinAdapter;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.EventBusListener;
import com.app.vietincome.manager.interfaces.AddFavoriteListener;
import com.app.vietincome.manager.interfaces.ItemClickListener;
import com.app.vietincome.model.Currency;
import com.app.vietincome.model.Data;
import com.app.vietincome.utils.Constant;
import com.app.vietincome.view.CustomItemDecoration;
import com.app.vietincome.view.NavigationTopBar;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.OnClick;

public class GainerFragment extends BaseFragment implements ItemClickListener, AddFavoriteListener {
	@BindView(R.id.tvSortName)
	TextView tvSortName;

	@BindView(R.id.imgSortName)
	ImageView imgSortName;

	@BindView(R.id.tvName)
	TextView tvName;

	@BindView(R.id.tvPrice)
	TextView tvPrice;

	@BindView(R.id.imgDot)
	ImageView imgDot;

	@BindView(R.id.tvCurrency)
	TextView tvCurrency;

	@BindView(R.id.tv1H)
	TextView tv1H;

	@BindView(R.id.imgSort1H)
	ImageView imgSort1H;

	@BindView(R.id.tv24H)
	TextView tv24H;

	@BindView(R.id.imgSort24H)
	ImageView imgSort24H;

	@BindView(R.id.tv7D)
	TextView tv7D;

	@BindView(R.id.imgSort7D)
	ImageView imgSort7D;

	@BindView(R.id.rcvAllCoin)
	ShimmerRecyclerView rcvAllCoin;

	public static ArrayList<Data> allCoins;
	private ArrayList<Data> coinSearched = new ArrayList<>();
	private AllCoinAdapter allCoinAdapter;
	private boolean isBTC = false;
	private boolean isSortUp = true;
	private boolean isLoading = true;
	private boolean isSearch;
	private Currency currency = AppPreference.INSTANCE.getCurrency();

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onUpdateCoin(EventBusListener.UpdateCoin event) {
		if(!event.isGainerTab) return;
		isLoading = false;
		if(event.isClear){
			allCoins.clear();
		}
		allCoins.addAll(event.data);
		if (!event.isSearch) {
			rcvAllCoin.hideShimmerAdapter();
			allCoinAdapter.notifyDataSetChanged();
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onChangeCurrency(EventBusListener.ChangeCurrency event){
		currency = AppPreference.INSTANCE.getCurrency();
		tvCurrency.setText(currency.getSymbol());
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onSearchedCoin(EventBusListener.SearchCoin event) {
		if(event.tab != Constant.TAB_GAINER) return;
		isSearch = !event.isClosed;
		coinSearched = event.data;
		allCoinAdapter.setCoins(event.isClosed ? allCoins : coinSearched);
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_coin;
	}

	@SuppressLint("CheckResult")
	@Override
	public void onFragmentReady(View view) {
		if (allCoins == null) {
			allCoins = new ArrayList<>();
		}
		if (allCoinAdapter == null) {
			allCoinAdapter = new AllCoinAdapter(allCoins, true,this, this);
		}
		onUpdatedTheme();
		rcvAllCoin.setLayoutManager(new LinearLayoutManager(getContext()));
		rcvAllCoin.addItemDecoration(new CustomItemDecoration(2));
		rcvAllCoin.setDemoShimmerDuration(1000000);
		rcvAllCoin.setAdapter(allCoinAdapter);
		rcvAllCoin.showShimmerAdapter();
		tvCurrency.setText(currency.getSymbol());
		setupUI(view);
	}

	@OnClick(R.id.layoutPrice)
	void onChangeCurrency() {
		allCoinAdapter.changeCurrency();
		isBTC = !isBTC;
		tvCurrency.setText(isBTC ? getString(R.string.bitcoin) : AppPreference.INSTANCE.getCurrency().getSymbol());
	}

	@OnClick(R.id.layoutReverse)
	void onReverse() {
		Collections.reverse(allCoins);
		allCoinAdapter.setCoins(allCoins);
		isSortUp = !isSortUp;
		imgSortName.setImageResource(isSortUp ? R.drawable.sort_up : R.drawable.sort_down);
	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
	}

	@Override
	public void onUpdatedTheme() {
		rcvAllCoin.setBackgroundColor(isDarkTheme ? getColor(R.color.black) : getColor(R.color.color_line));
		imgSortName.setColorFilter(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		tvCurrency.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		imgDot.setColorFilter(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
		imgSort1H.setColorFilter(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		imgSort24H.setColorFilter(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		imgSort7D.setColorFilter(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		tvSortName.setTextColor(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
		tvName.setTextColor(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
		tvPrice.setTextColor(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
		tv1H.setTextColor(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
		tv24H.setTextColor(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
		tv7D.setTextColor(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
		rcvAllCoin.setDemoLayoutReference(isDarkTheme ? R.layout.layout_demo_coin_dark : R.layout.layout_demo_coin_light);
		allCoinAdapter.setDarkTheme(isDarkTheme);
	}


	@Override
	public void onItemClicked(int position) {
		if (isLoading) return;
		Intent parent = new Intent(getContext(), ParentActivity.class);
		parent.putExtra("coin", isSearch ? coinSearched.get(position) : allCoins.get(position));
		parent.putExtra("price", true);
		parent.putExtra(Constant.KEY_SCREEN, Constant.COIN_DETAIL);
		startActivity(parent);
	}

	@Override
	public void onChangeFavorite(int position) {
		if (allCoins.get(position).isFavourite()) {
			AppPreference.INSTANCE.removeFavourite(isSearch ? coinSearched.get(position) : allCoins.get(position));
		} else {
			AppPreference.INSTANCE.addFavourite(isSearch ? coinSearched.get(position) : allCoins.get(position));
		}
	}
}
