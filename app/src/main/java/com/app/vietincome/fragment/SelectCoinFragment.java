package com.app.vietincome.fragment;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.app.vietincome.R;
import com.app.vietincome.adapter.SelectCoinAdapter;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.manager.interfaces.ItemClickListener;
import com.app.vietincome.model.Data;
import com.app.vietincome.view.CustomItemDecoration;
import com.app.vietincome.view.NavigationTopBar;

import java.util.ArrayList;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SelectCoinFragment extends BaseFragment implements ItemClickListener {

	@BindView(R.id.layoutRoot)
	LinearLayout layoutRoot;

	@BindView(R.id.rcvCoin)
	RecyclerView rcvCoin;

	private ArrayList<Data> coins, searchCoins;
	private SelectCoinAdapter selectCoinAdapter;

	public static SelectCoinFragment newInstance(ArrayList<Data> coins){
		SelectCoinFragment fragment = new SelectCoinFragment();
		fragment.coins = coins;
		fragment.searchCoins = coins;
		return fragment;
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_select_coin;
	}

	@Override
	public void onFragmentReady(View view) {
		onUpdatedTheme();
		if(coins == null){
			coins = new ArrayList<>();
		}
		if(selectCoinAdapter == null){
			selectCoinAdapter = new SelectCoinAdapter(coins, this);
		}
		rcvCoin.setLayoutManager(new LinearLayoutManager(getContext()));
		rcvCoin.setHasFixedSize(true);
		rcvCoin.addItemDecoration(new CustomItemDecoration(1));
		rcvCoin.setAdapter(selectCoinAdapter);
	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
		navitop.setTvTitle("Select coin");
		navitop.showImgLeft(true);
		navitop.setImgLeft(R.drawable.back);
		navitop.changeFontTitle(R.font.helvetica_neue);
	}

	@Override
	public void onLeftClicked() {
		super.onLeftClicked();
		goBack();
	}

	@Override
	public void onSearchDone() {
		super.onSearchDone();
		hideKeyboard();
	}

	@Override
	public void onSearchChanged(String key) {
		super.onSearchChanged(key);
		searchCoin(key);
	}

	@Override
	public void onCloseSearch() {
		super.onCloseSearch();
		hideKeyboard();
	}

	@Override
	public void onRightClicked() {
		super.onRightClicked();
		if (coins == null) return;
		showKeyboard(navigationTopBar.getEdtSearch());
		navigationTopBar.openSearch();
	}

	@Override
	public void onUpdatedTheme() {
		layoutRoot.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
		rcvCoin.setBackgroundColor(isDarkTheme ? getColor(R.color.black) : getColor(R.color.color_line));
	}

	@SuppressLint("CheckResult")
	public void searchCoin(String key) {
		Observable.fromIterable(coins)
				.observeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.filter(data -> data.getName().contains(key) || data.getSymbol().contains(key.toUpperCase()))
				.toList()
				.subscribe(data -> {
					searchCoins = (ArrayList<Data>) data;
					selectCoinAdapter.setCoins(searchCoins);
				});
	}

	@Override
	public void onItemClicked(int position) {
		pushFragment(AddTransactionFragment.newInstance(searchCoins.get(position)), R.anim.slide_from_left_to_right_in, R.anim.slide_from_right_out);
	}
}
