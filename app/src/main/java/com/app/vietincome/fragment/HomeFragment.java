package com.app.vietincome.fragment;

import android.view.View;

import com.app.vietincome.R;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.manager.EventBusListener;
import com.app.vietincome.view.NavigationTopBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class HomeFragment extends BaseFragment {


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

	}
}
