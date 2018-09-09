package com.app.vietincom.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.vietincom.R;
import com.app.vietincom.adapter.HomeViewpagerAdapter;
import com.app.vietincom.bases.BaseActivity;
import com.app.vietincom.utils.Constant;
import com.app.vietincom.view.NoneSwipeViewpager;

import java.util.Objects;

import butterknife.BindView;

public class MainActivity extends BaseActivity {


	@BindView(R.id.viewPagerMain)
	NoneSwipeViewpager viewPagerMain;

	@BindView(R.id.tabLayoutBottom)
	TabLayout tabLayoutBottom;

	private HomeViewpagerAdapter homeViewPagerAdapter;
	private int selectedTab = 2;

	@Override
	public int animationIn() {
		return 0;
	}

	@Override
	public int animationOut() {
		return 0;
	}

	@Override
	public void initStack() {

	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_main;
	}

	@Override
	public void setTheme() {
		tabLayoutBottom.setBackgroundColor(isDarkTheme ? getColorRes(R.color.dark_background) : getColorRes(R.color.light_background));
		tabLayoutBottom.setSelectedTabIndicatorColor(isDarkTheme ? getColorRes(R.color.white) : getColorRes(R.color.black));
	}

	@Override
	public void onViewCreated(View view) {
		if (homeViewPagerAdapter == null) {
			homeViewPagerAdapter = new HomeViewpagerAdapter(this, getSupportFragmentManager());
		}
		viewPagerMain.setAdapter(homeViewPagerAdapter);
		tabLayoutBottom.setupWithViewPager(viewPagerMain);
		for (int i = 0; i < tabLayoutBottom.getTabCount(); i++) {
			TabLayout.Tab tab = tabLayoutBottom.getTabAt(i);
			Objects.requireNonNull(tab).setCustomView(homeViewPagerAdapter.getTabView(i, selectedTab, isDarkTheme));
		}
		viewPagerMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				if (position == selectedTab) return;
				selectedTab = position;
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
		viewPagerMain.setCurrentItem(selectedTab);
		viewPagerMain.setOffscreenPageLimit(5);
		tabLayoutBottom.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				changeHighLightTab(tab, true);
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
				changeHighLightTab(tab, false);
			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});
	}

	private void changeHighLightTab(TabLayout.Tab tab, boolean isSelected) {
		RelativeLayout layout = (RelativeLayout) tab.getCustomView();
		if(isSelected){
			((TextView) layout.getChildAt(0)).setTextColor(isDarkTheme ? getColorRes(R.color.white) : getColorRes(R.color.black));
			((ImageView) layout.getChildAt(1)).setColorFilter(isDarkTheme ? getColorRes(R.color.white) : getColorRes(R.color.black));
			(layout.getChildAt(2)).setVisibility(View.GONE);
		}else{
			((TextView) layout.getChildAt(0)).setTextColor(getColorRes(R.color.text_gray));
			((ImageView) layout.getChildAt(1)).setColorFilter(getColorRes(R.color.gray_image));
		}
	}

	private void setBadge(int tabPosition, int numBadger){
		TabLayout.Tab tab = tabLayoutBottom.getTabAt(tabPosition);
		RelativeLayout layout = (RelativeLayout) tab.getCustomView();
		TextView textView = (TextView) layout.getChildAt(2);
		textView.setVisibility(numBadger > 0 ? View.VISIBLE : View.GONE);
		textView.setText(String.valueOf(numBadger));
	}

}
