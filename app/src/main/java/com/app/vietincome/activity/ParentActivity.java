package com.app.vietincome.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;

import com.app.vietincome.R;
import com.app.vietincome.bases.BaseActivity;
import com.app.vietincome.fragment.CoinDetailFragment;
import com.app.vietincome.fragment.FavoriteFragment;
import com.app.vietincome.fragment.ForgotPasswordFragment;
import com.app.vietincome.fragment.GlobalFragment;
import com.app.vietincome.fragment.PortfolioDetailFragment;
import com.app.vietincome.fragment.SelectCoinFragment;
import com.app.vietincome.fragment.SettingFragment;
import com.app.vietincome.fragment.SignupFragment;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.EventBusListener;
import com.app.vietincome.model.Data;
import com.app.vietincome.model.Portfolio;
import com.app.vietincome.utils.Constant;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Stack;

public class ParentActivity extends BaseActivity {

	private Stack<Fragment> backStack = new Stack<>();

	@Override
	public void initStack() {
		if (backStack == null) {
			backStack = new Stack<>();
		}
		initData(backStack);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventUpdatedTheme(EventBusListener.UpdatedTheme event) {
		isDarkTheme = AppPreference.INSTANCE.isDarkTheme();
		changeStatusBar();
	}

	@Override
	protected void onStart() {
		super.onStart();
		registerEventBus();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unRegisterEventBus();
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_parent;
	}

	@Override
	public void setTheme() {

	}

	@Override
	public void onViewCreated(View view) {
		if (getIntent() != null && getIntent().getExtras() != null) {
			int screen = getIntent().getIntExtra(Constant.KEY_SCREEN, -1);
			if (screen == -1) {
				finish();
			}
			switch (screen) {
				case Constant.COIN_DETAIL: {
					Data data = getIntent().getParcelableExtra("coin");
					pushFragment(CoinDetailFragment.newInstance(data), R.anim.zoom_in, R.anim.zoom_out);
					break;
				}
				case Constant.GLOBAL_MARKET: {
					pushFragment(GlobalFragment.newInstance()
							, R.anim.slide_from_left_to_right_in, R.anim.slide_from_right_out);
					break;
				}
				case Constant.SELECT_COIN: {
					ArrayList<Data> data = getIntent().getParcelableArrayListExtra("coins");
					pushFragment(SelectCoinFragment.newInstance(data), R.anim.slide_from_left_to_right_in, R.anim.slide_from_right_out);
					break;
				}
				case Constant.PORTFOLIO_DETAIL:
					int portId = getIntent().getIntExtra("portId", -1);
					if (portId == -1) finish();
					pushFragment(PortfolioDetailFragment.newInstance(portId), R.anim.slide_from_left_to_right_in, R.anim.slide_from_right_out);
					break;
				case Constant.FAVOURITE_COIN:
					int lastRank = getIntent().getIntExtra(Constant.LAST_RANK, 0);
					pushFragment(FavoriteFragment.newInstance(lastRank), R.anim.slide_from_left_to_right_in, R.anim.slide_from_right_out);
					break;
				case Constant.FORGOT_PASSWORD:
					pushFragment(new ForgotPasswordFragment(), R.anim.slide_from_left_to_right_in, R.anim.slide_from_right_out);
					break;
				case Constant.SIGN_UP:
					pushFragment(new SignupFragment(), R.anim.slide_from_left_to_right_in, R.anim.slide_from_right_out);
					break;
				case Constant.SETTING:
					pushFragment(SettingFragment.newInstance(), R.anim.slide_from_left_to_right_in, R.anim.slide_from_right_out);
					break;
			}
		}
	}
}
