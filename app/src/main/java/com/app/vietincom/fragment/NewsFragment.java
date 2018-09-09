package com.app.vietincom.fragment;

import android.view.View;

import com.app.vietincom.R;
import com.app.vietincom.bases.BaseFragment;
import com.app.vietincom.view.NavigationTopBar;

public class NewsFragment extends BaseFragment {

	public static NewsFragment newInstance(){
		NewsFragment fragment = new NewsFragment();
		return fragment;
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_news;
	}

	@Override
	public void onFragmentReady(View view) {

	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
		navitop.setTvTitle(R.string.news);
		navitop.showImgLeft(false);
		navitop.showImgRight(false);
	}

	@Override
	public void updateData() {

	}
}
