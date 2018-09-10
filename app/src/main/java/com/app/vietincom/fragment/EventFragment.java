package com.app.vietincom.fragment;

import android.view.View;

import com.app.vietincom.R;
import com.app.vietincom.bases.BaseFragment;
import com.app.vietincom.manager.EventBusListener;
import com.app.vietincom.view.NavigationTopBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class EventFragment extends BaseFragment {

	public static EventFragment newInstance(){
		EventFragment fragment = new EventFragment();
		return fragment;
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventRefreshData(EventBusListener.RefreshData event) {
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
	public void onUpdatedTheme() {

	}
}
