package com.app.vietincome.fragment;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.adapter.AllCoinAdapter;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.interfaces.AddFavoriteListener;
import com.app.vietincome.manager.interfaces.ItemClickListener;
import com.app.vietincome.model.Data;
import com.app.vietincome.model.responses.CoinResponse;
import com.app.vietincome.network.ApiClient;
import com.app.vietincome.view.CustomItemDecoration;
import com.app.vietincome.view.NavigationTopBar;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteFragment extends BaseFragment implements ItemClickListener, AddFavoriteListener {

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

	@BindView(R.id.layoutRoot)
	CoordinatorLayout layoutRoot;

	private double rate = 1;
	public static ArrayList<Data> allCoins;
	private AllCoinAdapter allCoinAdapter;
	private boolean isSortUp = true;
	private boolean isBTC = false;
	private int lastRank;

	public static FavoriteFragment newInstance( int lastRank) {
		FavoriteFragment favoriteFragment = new FavoriteFragment();
		favoriteFragment.rate = AppPreference.INSTANCE.getRate();
		favoriteFragment.lastRank = lastRank;
		return favoriteFragment;
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_favorite;
	}

	@Override
	public void onFragmentReady(View view) {
		onUpdatedTheme();
		if (allCoins == null) {
			allCoins = AppPreference.INSTANCE.getFavouriteCoin();
		}
		if (allCoinAdapter == null) {
			allCoinAdapter = new AllCoinAdapter(allCoins, true,this, this);
			allCoinAdapter.setDarkTheme(isDarkTheme);
			allCoinAdapter.setRate(rate);
		}
		rcvAllCoin.setLayoutManager(new LinearLayoutManager(getContext()));
		rcvAllCoin.addItemDecoration(new CustomItemDecoration(2));
		rcvAllCoin.hideShimmerAdapter();
		rcvAllCoin.setAdapter(allCoinAdapter);
		checkUpdateData();
	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
		navitop.showImgLeft(true);
		navitop.showImgRight(false);
		navitop.setImgLeft(R.drawable.back);
		navitop.setTvTitle("Favorites");
	}

	@Override
	public void onLeftClicked() {
		super.onLeftClicked();
		goBack();
	}

	@OnClick(R.id.layoutReverse)
	void onReverse() {
		Collections.reverse(allCoins);
		allCoinAdapter.setCoins(allCoins);
		isSortUp = !isSortUp;
		imgSortName.setImageResource(isSortUp ? R.drawable.sort_up : R.drawable.sort_down);
	}

	@OnClick(R.id.layoutPrice)
	void onChangeCurrency() {
		allCoinAdapter.changeCurrency();
		isBTC = !isBTC;
		tvCurrency.setText(isBTC ? getString(R.string.bitcoin) : AppPreference.INSTANCE.getCurrency().getSymbol());
	}

	@Override
	public void onUpdatedTheme() {
		layoutRoot.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
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
	}

	@Override
	public void onItemClicked(int position) {
		pushFragment(CoinDetailFragment.newInstance(allCoins.get(position), true), R.anim.zoom_in, R.anim.zoom_out);
	}

	@Override
	public void onChangeFavorite(int position) {
		AppPreference.INSTANCE.removeFavourite(allCoins.get(position));
		allCoinAdapter.notifyItemRemoved(position);
	}

	private void checkUpdateData() {
		if(allCoins.size() == 0) return;
		if (allCoins.get(allCoins.size() - 1).getRank() < lastRank) {
			return;
		}
		for (int i = allCoins.size() - 1; i >= 0; i--) {
			if(allCoins.get(i).getRank() > lastRank){
				getCoinDetail(allCoins.get(i).getId(), i);
			}else{
				break;
			}
		}
	}

	private void getCoinDetail(int coinId, int position) {
		navigationTopBar.showProgressBar();
		ApiClient.getAllCoinService().getCoinDetail(coinId).enqueue(new Callback<CoinResponse>() {
			@Override
			public void onResponse(Call<CoinResponse> call, Response<CoinResponse> response) {
				navigationTopBar.hideProgressBar();
				if (response.isSuccessful()) {
					if (response.body().getMetadata().isSuccess()) {
						AppPreference.INSTANCE.updateFavorite(response.body().getData().get(0));
						allCoinAdapter.notifyItemChanged(position);
					}
				}
			}

			@Override
			public void onFailure(Call<CoinResponse> call, Throwable t) {
				navigationTopBar.hideProgressBar();
				Log.d("__", "onFailure: " + t.getMessage());
			}
		});
	}

}
