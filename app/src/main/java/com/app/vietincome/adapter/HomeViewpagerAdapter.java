package com.app.vietincome.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.fragment.AlertFragment;
import com.app.vietincome.fragment.EventFragment;
import com.app.vietincome.fragment.HomeFragment;
import com.app.vietincome.fragment.NewsFragment;
import com.app.vietincome.fragment.SettingFragment;
import com.app.vietincome.utils.Constant;

import java.util.ArrayList;

public class HomeViewpagerAdapter extends FragmentStatePagerAdapter {

	private Context context;
	private ArrayList<Fragment> fragments;

	public HomeViewpagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		this.context = context;
		if (fragments == null) {
			fragments = new ArrayList<>();
			fragments.add(NewsFragment.newInstance());
			fragments.add(EventFragment.newInstance());
			fragments.add(HomeFragment.newInstance());
			fragments.add(AlertFragment.newInstance());
			fragments.add(SettingFragment.newInstance());
		}
	}

	@Override
	public Fragment getItem(int i) {
		return fragments.get(i);
	}

	@Override
	public int getCount() {
		return fragments == null ? 0 : fragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		if (position == Constant.TAB_NEWS) return this.context.getString(R.string.news);
		else if (position == Constant.TAB_EVENT) return this.context.getString(R.string.event);
		else if (position == Constant.TAB_ALL_COIN) return this.context.getString(R.string.all_coin);
		else if (position == Constant.TAB_ALERTS) return this.context.getString(R.string.alert);
		else return this.context.getString(R.string.setting);
	}

	public int getIcon(int position) {
		if (position == Constant.TAB_NEWS) return R.drawable.news;
		else if (position == Constant.TAB_EVENT) return R.drawable.event;
		else if (position == Constant.TAB_ALL_COIN) return R.drawable.analytic;
		else if (position == Constant.TAB_ALERTS) return R.drawable.notification;
		else return R.drawable.settings;
	}

	public View getTabView(int position, int selectedTab, boolean isDarkTheme) {
		View view = LayoutInflater.from(context).inflate(R.layout.item_tab_home, null);
		TextView textView = view.findViewById(R.id.tvTitleTabHome);
		textView.setText(getPageTitle(position));
		ImageView imgIcon = view.findViewById(R.id.imgTabHome);
		imgIcon.setImageResource(getIcon(position));
		if (position == selectedTab) {
			imgIcon.setColorFilter(isDarkTheme ? getColor(R.color.dark_tab) : getColor(R.color.light_tab));
			textView.setTextColor(isDarkTheme ? getColor(R.color.dark_tab) : getColor(R.color.light_tab));
		} else {
			imgIcon.setColorFilter(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
			textView.setTextColor(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
		}
		return view;
	}

	private int getColor(int color) {
		return ContextCompat.getColor(context, color);
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
}