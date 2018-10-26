package com.app.vietincome.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.activity.ParentActivity;
import com.app.vietincome.adapter.HomeViewpagerAdapter;
import com.app.vietincome.adapter.TopViewPagerAdapter;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.EventBusListener;
import com.app.vietincome.model.Currency;
import com.app.vietincome.model.Data;
import com.app.vietincome.model.Portfolio;
import com.app.vietincome.model.responses.CoinResponse;
import com.app.vietincome.model.responses.RateResponse;
import com.app.vietincome.network.ApiClient;
import com.app.vietincome.utils.Constant;
import com.app.vietincome.view.NavigationTopBar;
import com.app.vietincome.view.NoneSwipeViewpager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllCoinFragment extends BaseFragment {

	@BindView(R.id.tabLayoutTop)
	TabLayout tabLayoutTop;

	@BindView(R.id.viewPagerTop)
	NoneSwipeViewpager viewPagerTop;

	private TopViewPagerAdapter topViewPagerAdapter;
	private int selectedTab = Constant.TAB_MARKET;
	private CompositeDisposable disposable;
	private ArrayList<Data> gainerCoins = new ArrayList<>();
	public static ArrayList<Data> allCoins = new ArrayList<>();
	private Currency currency = AppPreference.INSTANCE.getCurrency();
	private boolean isFirstLoad = true;
	private int start = 1;
	private int perPage = 100;
	private ArrayList<Portfolio> portfolios = new ArrayList<>();
	private ArrayList<Data> favoriteCoins = new ArrayList<>();

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventRefreshData(EventBusListener.RefreshData event) {
		if (event.tab == Constant.TAB_ALL_COIN) {
			start = 1;
			if (!currency.getCode().equals(AppPreference.INSTANCE.getCurrency().getCode())) {
				currency = AppPreference.INSTANCE.getCurrency();
				getData(true);
			} else {
				getCoins();
				getGainerCoin();
			}
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventExpand(EventBusListener.ExpanableView event) {
		Log.d("Main", "onEventExpand: ");
		if (disposable != null && !disposable.isDisposed()) {
			disposable.dispose();
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_allcoin;
	}

	@Override
	public void onFragmentReady(View view) {
		onUpdatedTheme();
		getData(true);
		if (topViewPagerAdapter == null) {
			topViewPagerAdapter = new TopViewPagerAdapter(getContext(), getChildFragmentManager());
		}
		viewPagerTop.setAdapter(topViewPagerAdapter);
		tabLayoutTop.setupWithViewPager(viewPagerTop);
		for (int i = 0; i < tabLayoutTop.getTabCount(); i++) {
			TabLayout.Tab tab = tabLayoutTop.getTabAt(i);
			Objects.requireNonNull(tab).setCustomView(topViewPagerAdapter.getTabView(i, selectedTab, isDarkTheme));
		}
		viewPagerTop.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				if (position == selectedTab) return;
				EventBus.getDefault().post(new EventBusListener.SearchCoin(null, true, selectedTab));
				selectedTab = position;
				navigationTopBar.closeSearch();
				hideKeyboard();
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
		viewPagerTop.setCurrentItem(selectedTab);
		viewPagerTop.setOffscreenPageLimit(3);
		tabLayoutTop.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				changeHighLightTab(tab, true);
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
				changeHighLightTab(tab, false);
			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {
			}
		});
	}

	private void changeHighLightTab(TabLayout.Tab tab, boolean isSelected) {
		LinearLayout layout = (LinearLayout) tab.getCustomView();
		if (isSelected) {
			((ImageView) layout.getChildAt(0)).setColorFilter(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
			((TextView) layout.getChildAt(1)).setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		} else {
			((ImageView) layout.getChildAt(0)).setColorFilter(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
			((TextView) layout.getChildAt(1)).setTextColor(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
		}
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
	public void onLeftClicked() {
		super.onLeftClicked();
		Intent parent = new Intent(getContext(), ParentActivity.class);
		parent.putExtra(Constant.KEY_SCREEN, Constant.GLOBAL_MARKET);
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
		EventBus.getDefault().post(new EventBusListener.SearchCoin(null, true, selectedTab));
	}

	@Override
	public void onRightClicked() {
		super.onRightClicked();
		Intent parent = new Intent(getContext(), ParentActivity.class);
		parent.putExtra(Constant.KEY_SCREEN, Constant.FAVOURITE_COIN);
		parent.putExtra(Constant.LAST_RANK, allCoins.get(allCoins.size() - 1).getRank());
		startActivity(parent);
	}

	@Override
	public void onAdditionRightClicked() {
		super.onAdditionRightClicked();
		showKeyboard(navigationTopBar.getEdtSearch());
		navigationTopBar.openSearch();
	}

	@SuppressLint("CheckResult")
	public void searchCoin(String key) {
		Observable.fromIterable(selectedTab == Constant.TAB_GAINER ? gainerCoins : allCoins)
				.observeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.filter(data -> data.getName().toLowerCase().contains(key.toLowerCase()) || data.getSymbol().toLowerCase().contains(key.toLowerCase()))
				.toList()
				.subscribe(data -> {
					EventBus.getDefault().post(new EventBusListener.SearchCoin((ArrayList<Data>) data, false, selectedTab));
				}, throwable -> {
				});
	}

	@Override
	public void onUpdatedTheme() {
		tabLayoutTop.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
		tabLayoutTop.setSelectedTabIndicatorColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
	}

	public void getRate() {
		navigationTopBar.showProgressBar();
		ApiClient.getRateService().getRate(currency.getCode()).enqueue(new Callback<RateResponse>() {
			@Override
			public void onResponse(Call<RateResponse> call, Response<RateResponse> response) {
				if (response.isSuccessful()) {
					AppPreference.INSTANCE.setRate(response.body().getRate());
					getCoins();
					getGainerCoin();
				}
			}

			@Override
			public void onFailure(Call<RateResponse> call, Throwable t) {
				navigationTopBar.hideProgressBar();
				showAlert("Failure", "Get Rate: " + t.getMessage());
			}
		});
	}

	public void getData(boolean isUpdateRate) {
		if (!currency.getCode().equals("USD") || isUpdateRate) {
			getRate();
		} else {
			getCoins();
			getGainerCoin();
		}
	}

	public void getGainerCoin() {
		navigationTopBar.showProgressBar();
		ApiClient.getAllCoinService().getGainerChange().enqueue(new Callback<CoinResponse>() {
			@Override
			public void onResponse(Call<CoinResponse> call, Response<CoinResponse> response) {
				navigationTopBar.hideProgressBar();
				if (response.isSuccessful()) {
					if (response.body().getMetadata().isSuccess()) {
						EventBus.getDefault().post(new EventBusListener.UpdateCoin(response.body().getData(), navigationTopBar.isSearch(), true));
						gainerCoins.clear();
						gainerCoins.addAll(response.body().getData());
					}
				}
			}

			@Override
			public void onFailure(Call<CoinResponse> call, Throwable t) {
				navigationTopBar.hideProgressBar();
				showAlert("Failure", "Get Coins: " + t.getMessage());
			}
		});
	}

	public void getCoins() {
		portfolios.addAll(AppPreference.INSTANCE.getPortfolios());
		favoriteCoins.addAll(AppPreference.INSTANCE.getFavouriteCoin());
		navigationTopBar.showProgressBar();
		ApiClient.getAllCoinService().getCoinFirstPage().enqueue(new Callback<CoinResponse>() {
			@Override
			public void onResponse(Call<CoinResponse> call, Response<CoinResponse> response) {
				navigationTopBar.hideProgressBar();
				if (response.isSuccessful()) {
					if (response.body().getMetadata().isSuccess()) {
						EventBus.getDefault().post(new EventBusListener.UpdateCoin(response.body().getData(), navigationTopBar.isSearch(), false));
						allCoins.clear();
						allCoins.addAll(response.body().getData());
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
				showAlert("Failure", "Get Coins: " + t.getMessage());
			}
		});
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

	private void updateFavorite(ArrayList<Data> coins) {
		if (favoriteCoins.size() == 0) return;
		for (Data data : coins) {
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
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(coinResponse -> {
					start += perPage;
					allCoins.addAll(coinResponse.getData());
					EventBus.getDefault().post(new EventBusListener.UpdateCoin(coinResponse.getData(), navigationTopBar.isSearch(), false));
					updatePortfolioId(coinResponse.getData());
					updateFavorite(coinResponse.getData());
				}, throwable -> showAlert("Failed", "Get Coins: " + throwable.getMessage()));
		disposable.add(subscribe);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!isFirstLoad) {
			start = 1;
			getCoins();
			getGainerCoin();
		}
		isFirstLoad = false;
	}

	@Override
	public void onPause() {
		super.onPause();
		if (disposable != null && !disposable.isDisposed()) {
			disposable.dispose();
		}
	}
}
