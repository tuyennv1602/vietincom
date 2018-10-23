package com.app.vietincome.fragment;

import android.view.View;

import com.app.vietincome.R;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.view.NavigationTopBar;

public class ProfileFragment extends BaseFragment {

	public static ProfileFragment newInstance() {
		ProfileFragment fragment = new ProfileFragment();
		return fragment;
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_profile;
	}

	@Override
	public void onFragmentReady(View view) {

	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
		navitop.showImgRight(false);
		navitop.showImgLeft(false);
		navitop.setTvTitle("My profile");
	}

	@Override
	public void onUpdatedTheme() {

	}
}
