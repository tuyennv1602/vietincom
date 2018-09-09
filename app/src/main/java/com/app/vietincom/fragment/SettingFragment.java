package com.app.vietincom.fragment;

import android.view.View;

import com.app.vietincom.R;
import com.app.vietincom.bases.BaseFragment;
import com.app.vietincom.view.NavigationTopBar;

public class SettingFragment extends BaseFragment {

	public static SettingFragment newInstance(){
		SettingFragment fragment = new SettingFragment();
		return fragment;
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_setting;
	}

	@Override
	public void onFragmentReady(View view) {

	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
		navitop.setTvTitle(R.string.setting);
		navitop.showImgLeft(false);
		navitop.showImgRight(false);
	}

	@Override
	public void updateData() {

	}
}
