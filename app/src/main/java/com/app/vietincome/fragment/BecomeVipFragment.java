package com.app.vietincome.fragment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.view.HighLightLinearLayout;
import com.app.vietincome.view.HighLightTextView;
import com.app.vietincome.view.NavigationTopBar;

import butterknife.BindView;

public class BecomeVipFragment extends BaseFragment {

	@BindView(R.id.layoutRoot)
	LinearLayout layoutRoot;

	@BindView(R.id.layoutActive1)
	HighLightLinearLayout layoutActive1;

	@BindView(R.id.tvVip1)
	TextView tvVip1;

	@BindView(R.id.tvPrice1)
	TextView tvPrice1;

	@BindView(R.id.tvActive1)
	HighLightTextView tvActive1;

	@BindView(R.id.layoutActive2)
	HighLightLinearLayout layoutActive2;

	@BindView(R.id.tvVip2)
	TextView tvVip2;

	@BindView(R.id.tvPrice2)
	TextView tvPrice2;

	@BindView(R.id.tvActive2)
	HighLightTextView tvActive2;

	@BindView(R.id.layoutActive3)
	HighLightLinearLayout layoutActive3;

	@BindView(R.id.tvVip3)
	TextView tvVip3;

	@BindView(R.id.tvPrice3)
	TextView tvPrice3;

	@BindView(R.id.tvActive3)
	HighLightTextView tvActive3;

	@BindView(R.id.tvNote)
	TextView tvNote;

	@BindView(R.id.tvNoteContent)
	TextView tvNoteContent;

	@Override
	public int getLayoutId() {
		return R.layout.fragment_becomevip;
	}

	@Override
	public void onFragmentReady(View view) {
		onUpdatedTheme();
	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
		navitop.showImgRight(false);
		navitop.setImgLeft(R.drawable.back);
		navitop.setTvTitle(R.string.become_vip);
	}

	@Override
	public void onLeftClicked() {
		super.onLeftClicked();
		goBack();
	}

	@Override
	public void onUpdatedTheme() {
		layoutRoot.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
		setTextColor(tvVip1);
		setTextColor(tvPrice1);
		setTextColor(tvVip2);
		setTextColor(tvPrice2);
		setTextColor(tvVip3);
		setTextColor(tvPrice3);
		setTextColor(tvNote);
		setTextColor(tvNoteContent);
		tvActive1.setBackgroundResource(isDarkTheme ? R.drawable.bg_circle_dark : R.drawable.bg_circle_light);
		tvActive1.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		tvActive2.setBackgroundResource(isDarkTheme ? R.drawable.bg_circle_dark : R.drawable.bg_circle_light);
		tvActive2.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		tvActive3.setBackgroundResource(isDarkTheme ? R.drawable.bg_circle_dark : R.drawable.bg_circle_light);
		tvActive3.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		layoutActive1.setBackgroundResource(isDarkTheme ? R.drawable.bg_border_dark : R.drawable.bg_border_light);
		layoutActive2.setBackgroundResource(isDarkTheme ? R.drawable.bg_border_dark : R.drawable.bg_border_light);
		layoutActive3.setBackgroundResource(isDarkTheme ? R.drawable.bg_border_dark : R.drawable.bg_border_light);
	}
}
