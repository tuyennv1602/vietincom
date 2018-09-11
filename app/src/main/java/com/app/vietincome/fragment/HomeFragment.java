package com.app.vietincome.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.manager.EventBusListener;
import com.app.vietincome.view.CustomItemDecoration;
import com.app.vietincome.view.NavigationTopBar;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class HomeFragment extends BaseFragment {

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

	@BindView(R.id.imgCurrency)
	ImageView imgCurrency;

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

	@BindView(R.id.layoutTopAllCoin)
	LinearLayout layoutTopAll;

	@BindView(R.id.rcvAllCoin)
	ShimmerRecyclerView rcvAllCoin;

	public static HomeFragment newInstance(){
		HomeFragment fragment = new HomeFragment();
		return fragment;
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventRefreshData(EventBusListener.RefreshData event) {
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_home;
	}

	@Override
	public void onFragmentReady(View view) {
		rcvAllCoin.setLayoutManager(new LinearLayoutManager(getContext()));
		rcvAllCoin.addItemDecoration(new CustomItemDecoration(2));
		rcvAllCoin.setDemoShimmerDuration(60000);
		onUpdatedTheme();
	}

	@Override
	public void onLeftClicked() {
		super.onLeftClicked();
		hideKeyboard();
	}

	@Override
	public void onSearchDone() {
		super.onSearchDone();
		hideKeyboard();
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
	public void onRightClicked() {
		super.onRightClicked();
	}

	@Override
	public void onAdditionRightClicked() {
		super.onAdditionRightClicked();
		navigationTopBar.openSearch();
	}

	@Override
	public void onUpdatedTheme() {
		layoutTopAll.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
		imgSortName.setColorFilter(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		imgCurrency.setColorFilter(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
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
	}
}
