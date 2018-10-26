package com.app.vietincome.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.fragment.GainerFragment;
import com.app.vietincome.fragment.MarketCapFragment;
import com.app.vietincome.fragment.VolumeFragment;
import com.app.vietincome.utils.Constant;

public class TopViewPagerAdapter extends FragmentStatePagerAdapter {
	private Context context;

	public TopViewPagerAdapter(Context context,FragmentManager fm) {
		super(fm);
		this.context = context;
	}

	@Override
	public Fragment getItem(int i) {
		if(i == Constant.TAB_MARKET) return new MarketCapFragment();
		if(i == Constant.TAB_VOLUME) return new VolumeFragment();
		return new GainerFragment();
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		if (position == Constant.TAB_MARKET) return "Market Cap";
		else if (position == Constant.TAB_VOLUME) return "Volume";
		else return "Gainers";
	}

	public int getIcon(int position) {
		if (position == Constant.TAB_MARKET) return R.drawable.money;
		else if (position == Constant.TAB_VOLUME) return R.drawable.volume;
		else return R.drawable.gainer;
	}

	public View getTabView(int position, int selectedTab, boolean isDarkTheme) {
		View view = LayoutInflater.from(context).inflate(R.layout.item_tab_top, null).findViewById(R.id.layoutMain);
		TextView textView = view.findViewById(R.id.tvTitleTabHome);
		textView.setText(getPageTitle(position));
		ImageView imgIcon = view.findViewById(R.id.imgTabHome);
		imgIcon.setImageResource(getIcon(position));
		if (position == selectedTab) {
			imgIcon.setColorFilter(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
			textView.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
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
