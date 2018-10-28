package com.app.vietincome.fragment;

import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.app.vietincome.R;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.EventBusListener;
import com.app.vietincome.view.NavigationTopBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class EmptyFragment extends BaseFragment {

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventProfile(EventBusListener.ProfileListener event){
		updateView();
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_empty;
	}

	@Override
	public void onFragmentReady(View view) {
		initView();
	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {

	}

	@Override
	public void onUpdatedTheme() {

	}

	private void initView() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.addToBackStack(null);
		ft.replace(R.id.empty, AppPreference.INSTANCE.getProfile() != null ? ProfileFragment.newInstance(true) : new LoginFragment());
		ft.commit();
	}

	private void updateView(){
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.addToBackStack(null);
		ft.replace(R.id.empty, AppPreference.INSTANCE.getProfile() != null ? ProfileFragment.newInstance(false) : new LoginFragment());
		ft.commit();
	}

}
