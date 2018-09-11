package com.app.vietincome.fragment;

import android.view.View;

import com.app.vietincome.R;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.EventBusListener;
import com.app.vietincome.view.NavigationTopBar;

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
