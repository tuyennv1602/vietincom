package com.app.vietincome.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.vietincome.fragment.LineChartFragment;

import java.util.ArrayList;
import java.util.Arrays;

public class ChartViewPagerAdapter extends FragmentStatePagerAdapter {
	private Context context;
	private ArrayList<Fragment> fragments;
	private ArrayList<String> labelTime = new ArrayList<>(Arrays.asList("1H", "24H", "7D", "1M", "3M", "1Y", "ALL"));

	public ChartViewPagerAdapter(Context context, FragmentManager fm, String coinId) {
		super(fm);
		this.context  = context;
		if (fragments == null) {
			fragments = new ArrayList<>();
			fragments.add(LineChartFragment.newInstance(coinId, labelTime.get(0)));
			fragments.add(LineChartFragment.newInstance(coinId, labelTime.get(1)));
			fragments.add(LineChartFragment.newInstance(coinId, labelTime.get(2)));
			fragments.add(LineChartFragment.newInstance(coinId, labelTime.get(3)));
			fragments.add(LineChartFragment.newInstance(coinId, labelTime.get(4)));
			fragments.add(LineChartFragment.newInstance(coinId, labelTime.get(5)));
			fragments.add(LineChartFragment.newInstance(coinId, labelTime.get(6)));
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
		return labelTime.get(position);
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

}
