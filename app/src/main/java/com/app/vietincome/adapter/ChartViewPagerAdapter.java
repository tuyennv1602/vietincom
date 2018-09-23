package com.app.vietincome.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.vietincome.fragment.CoinChartFragment;

import java.util.ArrayList;
import java.util.Arrays;

public class ChartViewPagerAdapter extends FragmentStatePagerAdapter {
	private Context context;
	private ArrayList<Fragment> fragments;
	private ArrayList<String> labelTime = new ArrayList<>(Arrays.asList("24H", "7D", "1M", "3M", "1Y", "ALL"));

	public ChartViewPagerAdapter(Context context, FragmentManager fm, String coinId, double rate) {
		super(fm);
		this.context  = context;
		if (fragments == null) {
			fragments = new ArrayList<>();
			for(int i = 0; i < labelTime.size(); i++){
				fragments.add(CoinChartFragment.newInstance(coinId, i, rate));
			}
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
