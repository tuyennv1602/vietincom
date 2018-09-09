package com.app.vietincom.fragment;

import android.view.View;

import com.app.vietincom.R;
import com.app.vietincom.bases.BaseFragment;
import com.app.vietincom.view.NavigationTopBar;

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
	public void updateData() {

	}
}
