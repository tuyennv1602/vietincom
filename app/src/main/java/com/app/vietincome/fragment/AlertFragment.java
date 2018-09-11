package com.app.vietincome.fragment;

import android.view.View;

import com.app.vietincome.R;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.view.NavigationTopBar;

public class AlertFragment extends BaseFragment {

	public static AlertFragment newInstance(){
		AlertFragment fragment = new AlertFragment();
		return fragment;
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_alert;
	}

	@Override
	public void onFragmentReady(View view) {

	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
		navitop.setTvTitle(R.string.alert);
		navitop.showImgLeft(false);
		navitop.showImgRight(false);
	}

	@Override
	public void onUpdatedTheme() {

	}
}
