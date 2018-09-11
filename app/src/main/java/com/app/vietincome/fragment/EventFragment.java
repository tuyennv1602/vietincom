package com.app.vietincome.fragment;

import android.view.View;

import com.app.vietincome.R;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.manager.EventBusListener;
import com.app.vietincome.view.NavigationTopBar;

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
