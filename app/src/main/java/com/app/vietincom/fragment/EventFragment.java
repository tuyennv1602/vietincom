package com.app.vietincom.fragment;

import android.view.View;

import com.app.vietincom.R;
import com.app.vietincom.bases.BaseFragment;
import com.app.vietincom.view.NavigationTopBar;

public class EventFragment extends BaseFragment {

	public static EventFragment newInstance(){
		EventFragment fragment = new EventFragment();
		return fragment;
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_event;
	}

	@Override
	public void onFragmentReady(View view) {

	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
		navitop.setTvTitle(R.string.event);
		navitop.showImgLeft(false);
	}

	@Override
	public void onRightClicked() {
		super.onRightClicked();
		navigationTopBar.openSearch();
	}

	@Override
	public void updateData() {

	}
}
