package com.app.vietincom.fragment;

import android.view.View;

import com.app.vietincom.R;
import com.app.vietincom.bases.BaseFragment;
import com.app.vietincom.manager.AppPreference;
import com.app.vietincom.manager.EventBusListener;
import com.app.vietincom.view.NavigationTopBar;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

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
	public void onUpdatedTheme() {
	}

	@OnClick(R.id.btnChangeTheme)
	void changeTheme(){
		AppPreference.INSTANCE.changeTheme();
		EventBus.getDefault().post(new EventBusListener.UpdatedTheme());
	}
}
