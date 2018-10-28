package com.app.vietincome.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.vietincome.R;
import com.app.vietincome.adapter.CoinAdapter;
import com.app.vietincome.bases.BaseDialogFragment;
import com.app.vietincome.manager.interfaces.ItemClickListener;
import com.app.vietincome.manager.interfaces.OnSelectedCoin;
import com.app.vietincome.model.Coin;
import com.app.vietincome.view.CustomItemDecoration;
import com.app.vietincome.view.HighLightTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CoinDialog extends BaseDialogFragment implements ItemClickListener {

	@BindView(R.id.rcvCoin)
	RecyclerView rcvCoin;

	@BindView(R.id.layoutRoot)
	RelativeLayout layoutRoot;

	@BindView(R.id.tvChooseCoin)
	TextView tvChooseCoin;

	@BindView(R.id.edtSearch)
	EditText edtSearch;

	@BindView(R.id.imgClose)
	ImageView imgClose;

	@BindView(R.id.tvCancel)
	HighLightTextView tvCancel;

	private OnSelectedCoin onSelectedCoin;
	private ArrayList<Coin> coins, searchCoins;
	private CoinAdapter coinAdapter;

	public static CoinDialog newIntance(ArrayList<Coin> coins, OnSelectedCoin onSelectedCoin) {
		CoinDialog dialog = new CoinDialog();
		Bundle args = new Bundle();
		dialog.setArguments(args);
		dialog.onSelectedCoin = onSelectedCoin;
		dialog.coins = coins;
		dialog.searchCoins = coins;
		return dialog;
	}


	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setAttribute(dialog, true, R.style.ZoomDialogAnimation, 10, 100, 10, 100);
		return dialog;
	}

	@Override
	public int getLayoutId() {
		return R.layout.dialog_coin;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		tvChooseCoin.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		tvCancel.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		layoutRoot.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
		rcvCoin.setBackgroundColor(isDarkTheme ? getColor(R.color.black) : getColor(R.color.color_line));
		edtSearch.setHintTextColor(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
		edtSearch.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		imgClose.setColorFilter(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		if (coins == null) {
			coins = new ArrayList<>();
		}
		if (coinAdapter == null) {
			coinAdapter = new CoinAdapter(coins, this);
		}
		rcvCoin.setLayoutManager(new LinearLayoutManager(getContext()));
		rcvCoin.setHasFixedSize(true);
		rcvCoin.addItemDecoration(new CustomItemDecoration(1));
		rcvCoin.setAdapter(coinAdapter);
	}


	@Override
	public void onItemClicked(int position) {
		if (onSelectedCoin != null) {
			onSelectedCoin.onSelectedCoin(searchCoins.get(position).getId(), searchCoins.get(position).getName());
			getDialog().dismiss();
		}
	}

	@OnTextChanged(R.id.edtSearch)
	void onSearchChanged(CharSequence text) {
		if (text.toString().isEmpty()) {
			imgClose.setVisibility(View.GONE);
		} else {
			imgClose.setVisibility(View.VISIBLE);
		}
		searchCoin(text.toString());
	}

	@OnClick(R.id.tvCancel)
	void onCancel() {
		if (onSelectedCoin != null) {
			onSelectedCoin.onCancel();
		}
		getDialog().dismiss();
	}

	@OnClick(R.id.imgClose)
	void onClearSearch() {
		edtSearch.setText("");
	}

	@SuppressLint("CheckResult")
	public void searchCoin(String key) {
		Observable.fromIterable(coins)
				.observeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.filter(data -> data.getName().toLowerCase().contains(key.toLowerCase()) || data.getSymbol().toLowerCase().contains(key.toLowerCase()))
				.toList()
				.subscribe(data -> {
					searchCoins = (ArrayList<Coin>) data;
					coinAdapter.setCoins(searchCoins);
				});
	}

}
