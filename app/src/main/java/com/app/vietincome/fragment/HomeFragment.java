package com.app.vietincome.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
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
import com.app.vietincome.manager.interfaces.ItemClickCancelListener;
import com.app.vietincome.manager.interfaces.ItemClickListener;
import com.app.vietincome.model.Currency;
import com.app.vietincome.model.Data;
import com.app.vietincome.model.Portfolio;
import com.app.vietincome.model.responses.CoinResponse;
import com.app.vietincome.model.responses.GlobalResponse;
import com.app.vietincome.model.responses.RateResponse;
import com.app.vietincome.network.ApiClient;
import com.app.vietincome.network.ApiInterface;
import com.app.vietincome.utils.Constant;
import com.app.vietincome.view.CustomItemDecoration;
import com.app.vietincome.view.NavigationTopBar;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment implements ItemClickListener, AddFavoriteListener {

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

	@BindView(R.id.appbarLayout)
	AppBarLayout appBarLayout;

	public static ArrayList<Data> allCoins;
	private ArrayList<Data> coinSearched = new ArrayList<>();
	private AllCoinAdapter allCoinAdapter;
	private int start = 1;
	private int perPage = 100;
	private boolean isBTC = false;
	private boolean isSortUp = true;
	private double rate = 1;
	private boolean isLoading = true;
	private Currency currency = AppPreference.INSTANCE.getCurrency();
	private boolean isFirstLoad = true;
	private CompositeDisposable disposable;
	private ArrayList<Portfolio> portfolios = new ArrayList<>();
	private ArrayList<Data> favoriteCoins = new ArrayList<>();

	public static HomeFragment newInstance() {
		HomeFragment fragment = new HomeFragment();
		return fragment;
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventRefreshData(EventBusListener.RefreshData event) {
		if (event.tab == Constant.TAB_ALL_COIN) {
			start = 1;
			if (!currency.getCode().equals(AppPreference.INSTANCE.getCurrency().getCode())) {
				currency = AppPreference.INSTANCE.getCurrency();
				getData(true);
			} else {
				getCoins(allCoins.size() == 0);
			}
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventExpand(EventBusListener.ExpanableView event) {
		appBarLayout.setExpanded(true);
		if (disposable != null && !disposable.isDisposed()) {
			disposable.dispose();
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_home;
	}

	@SuppressLint("CheckResult")
	@Override
	public void onFragmentReady(View view) {
		if (allCoins == null) {
			allCoins = new ArrayList<>();
		}
		if (allCoinAdapter == null) {
			allCoinAdapter = new AllCoinAdapter(allCoins, this, this);
		}
		onUpdatedTheme();
		rcvAllCoin.setLayoutManager(new LinearLayoutManager(getContext()));
		rcvAllCoin.addItemDecoration(new CustomItemDecoration(2));
		rcvAllCoin.setDemoShimmerDuration(1000000);
		rcvAllCoin.setAdapter(allCoinAdapter);
		getData(true);
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
	public void onLeftClicked() {
		super.onLeftClicked();
		if (isLoading) return;
		Intent parent = new Intent(getContext(), ParentActivity.class);
		parent.putExtra(Constant.KEY_SCREEN, Constant.GLOBAL_MARKET);
		parent.putExtra(Constant.KEY_RATE, rate);
		startActivity(parent);
	}

	@Override
	public void onSearchDone() {
		super.onSearchDone();
		hideKeyboard();
	}

	@Override
	public void onSearchChanged(String key) {
		super.onSearchChanged(key);
		searchCoin(key);
	}

	@Override
	public void onCloseSearch() {
		super.onCloseSearch();
		hideKeyboard();
		allCoinAdapter.setCoins(allCoins);
	}

	@Override
	public void onRightClicked() {
		super.onRightClicked();
		if (isLoading) return;
		Intent parent = new Intent(getContext(), ParentActivity.class);
		parent.putExtra(Constant.KEY_SCREEN, Constant.FAVOURITE_COIN);
		parent.putExtra(Constant.KEY_RATE, rate);
		parent.putExtra(Constant.LAST_RANK, allCoins.get(allCoins.size() - 1).getRank());
		startActivity(parent);
	}

	@Override
	public void onAdditionRightClicked() {
		super.onAdditionRightClicked();
		if (isLoading) return;
		showKeyboard(navigationTopBar.getEdtSearch());
		navigationTopBar.openSearch();
	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
		navitop.setTvTitle(R.string.all_coin);
		navitop.setImgRight(R.drawable.favourite);
		navitop.setImgLeft(R.drawable.pie_chart);
		navitop.showAdditionalRight(true);
		navitop.playTitleAnimation();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!isFirstLoad) {
			start = 1;
			getCoins(allCoins.size() == 0);
		}
		isFirstLoad = false;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
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
		allCoinAdapter.notifyDataSetChanged();
	}

	public void getRate() {
		isLoading = true;
		navigationTopBar.showProgressBar();
		rcvAllCoin.showShimmerAdapter();
		ApiClient.getRateService().getRate(currency.getCode()).enqueue(new Callback<RateResponse>() {
			@Override
			public void onResponse(Call<RateResponse> call, Response<RateResponse> response) {
				if (response.isSuccessful()) {
					HomeFragment.this.rate = response.body().getRate();
					allCoinAdapter.setRate(HomeFragment.this.rate);
					Log.d("__coin", "onResponse: " + HomeFragment.this.rate);
					getCoins(false);
				}
			}

			@Override
			public void onFailure(Call<RateResponse> call, Throwable t) {
				navigationTopBar.hideProgressBar();
				rcvAllCoin.hideShimmerAdapter();
				showAlert("Failure", "Get Rate: " + t.getMessage());
			}
		});
	}

	public void getData(boolean isUpdateRate) {
		tvCurrency.setText(currency.getSymbol());
		if (!currency.getCode().equals("USD") || isUpdateRate) {
			if (isBTC) {
				allCoinAdapter.changeCurrency();
			}
			getRate();
		} else {
			getCoins(true);
		}
	}

	public void getCoins(boolean showShimmer) {
		portfolios.addAll(AppPreference.INSTANCE.getPortfolios());
		favoriteCoins.addAll(AppPreference.INSTANCE.getFavouriteCoin());
		isLoading = true;
		navigationTopBar.showProgressBar();
		if (showShimmer) {
			rcvAllCoin.showShimmerAdapter();
		}
		ApiClient.getAllCoinService().getCoinFirstPage().enqueue(new Callback<CoinResponse>() {
			@Override
			public void onResponse(Call<CoinResponse> call, Response<CoinResponse> response) {
				navigationTopBar.hideProgressBar();
				rcvAllCoin.hideShimmerAdapter();
				isLoading = false;
				if (response.isSuccessful()) {
					if (response.body().getMetadata().isSuccess()) {
						allCoins.clear();
						allCoins.addAll(response.body().getData());
						Log.d("__coin", "onResponse: " + response.body().getData().get(0).getRank());
						if (!navigationTopBar.isSearch()) {
							allCoinAdapter.setCoins(allCoins);
						}
						start += perPage;
						if (response.body().getData().size() == perPage) {
							getNextPage(response.body().getMetadata().getNumCryptocurrencies());
						}
						updatePortfolioId(response.body().getData());
						updateFavorite(response.body().getData());
					}
				}
			}

			@Override
			public void onFailure(Call<CoinResponse> call, Throwable t) {
				navigationTopBar.hideProgressBar();
				rcvAllCoin.hideShimmerAdapter();
				showAlert("Failure", "Get Coins: " + t.getMessage());
			}
		});
	}

	@SuppressLint("CheckResult")
	public void searchCoin(String key) {
		Observable.fromIterable(allCoins)
				.observeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.filter(data -> data.getName().toLowerCase().contains(key.toLowerCase()) || data.getSymbol().toLowerCase().contains(key.toLowerCase()))
				.toList()
				.subscribe(data -> {
					coinSearched = (ArrayList<Data>) data;
					allCoinAdapter.setCoins(coinSearched);
				}, throwable ->{});
	}

	@SuppressLint("CheckResult")
	private void updatePortfolioId(ArrayList<Data> coins) {
		if (portfolios.size() == 0) return;
		for (Data data : coins) {
			for (int i = portfolios.size() - 1; i >= 0; i--) {
				if (data.getId() == portfolios.get(i).getId()) {
					Portfolio portfolio = portfolios.get(i);
					portfolio.setQuotes(data.getQuotes());
					EventBus.getDefault().post(new EventBusListener.UpdatePortfolio(portfolio));
					portfolios.remove(i);
					break;
				}
			}
		}
	}

	private void updateFavorite(ArrayList<Data> coins){
		if(favoriteCoins.size() == 0) return;
		for(Data data : coins){
			for (int i = favoriteCoins.size() - 1; i >= 0; i--) {
				if (data.getId() == favoriteCoins.get(i).getId()) {
					AppPreference.INSTANCE.updateFavorite(data);
					favoriteCoins.remove(i);
					break;
				}
			}
		}
	}

	@SuppressLint("CheckResult")
	private void getNextPage(int totalItem) {
		totalItem = totalItem - perPage;
		int numPage = totalItem / perPage;
		if (totalItem % perPage != 0) {
			numPage += 1;
		}
		disposable = new CompositeDisposable();
		Disposable subscribe = Observable.range(1, numPage)
				.subscribeOn(Schedulers.io())
				.concatMap((Function<Integer, ObservableSource<CoinResponse>>) integer -> ApiClient.getAllCoinService().getCoinInPage((integer * perPage) + 1))
				.doOnError(throwable -> {
					Log.d("__home", "getNextPage: error");
				})
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(coinResponse -> {
					start += perPage;
					allCoins.addAll(coinResponse.getData());
					if (!navigationTopBar.isSearch()) {
						allCoinAdapter.notifyItemRangeChanged(coinResponse.getData().get(0).getRank() - 1, allCoins.size());
					}
					updatePortfolioId(coinResponse.getData());
					updateFavorite(coinResponse.getData());
					Log.d("__coin", "onResponse: " + coinResponse.getData().get(0).getRank());
				}, throwable -> showAlert("Failed", "Get Coins: " + throwable.getMessage()));
		disposable.add(subscribe);
	}

	@Override
	public void onItemClicked(int position) {
		if (isLoading) return;
		Intent parent = new Intent(getContext(), ParentActivity.class);
		parent.putExtra("coin", navigationTopBar.isSearch() ? coinSearched.get(position) : allCoins.get(position));
		parent.putExtra(Constant.KEY_SCREEN, Constant.COIN_DETAIL);
		parent.putExtra(Constant.KEY_RATE, rate);
		startActivity(parent);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (disposable != null && !disposable.isDisposed()) {
			disposable.dispose();
		}
	}

	@Override
	public void onChangeFavorite(int position) {
		if (allCoins.get(position).isFavourite()) {
			AppPreference.INSTANCE.removeFavourite(allCoins.get(position));
		} else {
			AppPreference.INSTANCE.addFavourite(allCoins.get(position));
		}
	}
}
