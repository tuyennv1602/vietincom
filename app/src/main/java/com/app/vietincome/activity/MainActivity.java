package com.app.vietincome.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.adapter.HomeViewpagerAdapter;
import com.app.vietincome.bases.BaseActivity;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.EventBusListener;
import com.app.vietincome.utils.Constant;
import com.app.vietincome.view.NoneSwipeViewpager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;
import java.util.Stack;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

	@BindView(R.id.viewPagerMain)
	NoneSwipeViewpager viewPagerMain;

	@BindView(R.id.tabLayoutBottom)
	TabLayout tabLayoutBottom;

	@BindView(R.id.main_content)
	RelativeLayout mainLayout;

	private HomeViewpagerAdapter homeViewPagerAdapter;
	private int selectedTab = 2;
	private Stack<Fragment> backStack = new Stack<>();
	private int numNews = AppPreference.INSTANCE.getNumNews();
	private int numEvents = AppPreference.INSTANCE.getNumEvents();

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventUpdatedTheme(EventBusListener.UpdatedTheme event) {
		isDarkTheme = AppPreference.INSTANCE.isDarkTheme();
		setTheme();
		changeStatusBar();
		for (int i = 0; i < tabLayoutBottom.getTabCount(); i++) {
			TabLayout.Tab tab = tabLayoutBottom.getTabAt(i);
			changeHighLightTab(tab, i == selectedTab);
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventUpdateNews(EventBusListener.UpdateNews event) {
		numNews ++;
		setBadge(Constant.TAB_NEWS, numNews);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventUpdateEvents(EventBusListener.UpdateEvent event){
		numEvents ++ ;
		setBadge(Constant.TAB_EVENT, numEvents);
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
	public void initStack() {
		if (backStack == null) {
			backStack = new Stack<>();
		}
		initData(backStack);
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_main;
	}

	@Override
	public void setTheme() {
		tabLayoutBottom.setBackgroundColor(isDarkTheme ? getColorRes(R.color.dark_background) : getColorRes(R.color.light_background));
		mainLayout.setBackgroundColor(isDarkTheme ? getColorRes(R.color.dark_background) : getColorRes(R.color.light_background));
		tabLayoutBottom.setSelectedTabIndicatorColor(getColorRes(R.color.dark_tab));
	}

	@Override
	public void onViewCreated(View view) {
		setBackPressListener(true);
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
				if(selectedTab == Constant.TAB_ALL_COIN){
					EventBus.getDefault().post(new EventBusListener.RefreshData(Constant.TAB_ALL_COIN));
				}else if(selectedTab  == Constant.TAB_EVENT) {
					EventBus.getDefault().post(new EventBusListener.RefreshData(Constant.TAB_EVENT));
					EventBus.getDefault().post(new EventBusListener.ExpanableView());
				}else{
					EventBus.getDefault().post(new EventBusListener.ExpanableView());
				}
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
				clearNews();
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
				changeHighLightTab(tab, false);
			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {
				clearNews();
			}
		});
		setBadge(Constant.TAB_NEWS, numNews);
		setBadge(Constant.TAB_EVENT, numEvents);
	}

	private void changeHighLightTab(TabLayout.Tab tab, boolean isSelected) {
		RelativeLayout layout = (RelativeLayout) tab.getCustomView();
		if(isSelected){
			((TextView) layout.getChildAt(0)).setTextColor(getColorRes(R.color.dark_tab));
			((ImageView) layout.getChildAt(1)).setColorFilter(getColorRes(R.color.dark_tab));
			(layout.getChildAt(2)).setVisibility(View.GONE);
		}else{
			((TextView) layout.getChildAt(0)).setTextColor(isDarkTheme ? getColorRes(R.color.dark_gray) : getColorRes(R.color.light_gray));
			((ImageView) layout.getChildAt(1)).setColorFilter(isDarkTheme ? getColorRes(R.color.dark_gray) : getColorRes(R.color.light_gray));
		}
	}

	private void setBadge(int tabPosition, int numBadger){
		TabLayout.Tab tab = tabLayoutBottom.getTabAt(tabPosition);
		RelativeLayout layout = (RelativeLayout) tab.getCustomView();
		TextView textView = (TextView) layout.getChildAt(2);
		textView.setVisibility(numBadger > 0 ? View.VISIBLE : View.GONE);
		textView.setText(String.valueOf(numBadger));
	}

	private void clearNews(){
		if(selectedTab == Constant.TAB_NEWS){
			numNews = 0;
			AppPreference.INSTANCE.clearNumNews();
			setBadge(Constant.TAB_NEWS, numNews);
		}else if(selectedTab == Constant.TAB_EVENT){
			numEvents = 0;
			AppPreference.INSTANCE.clearNumEvents();
			setBadge(Constant.TAB_EVENT, numEvents);
		}
	}

}
