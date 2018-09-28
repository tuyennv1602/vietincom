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
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.interfaces.ItemClickListener;
import com.app.vietincome.manager.interfaces.OnSelectedCoin;
import com.app.vietincome.model.Coin;
import com.app.vietincome.model.Data;
import com.app.vietincome.network.ApiClient;
import com.app.vietincome.view.CustomItemDecoration;
import com.app.vietincome.view.HighLightTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

	@BindView(R.id.tvLoading)
	TextView tvLoading;

	private OnSelectedCoin onSelectedCoin;
	private ArrayList<Coin> coins, searchCoins;
	private CoinAdapter coinAdapter;

	public static CoinDialog newIntance(OnSelectedCoin onSelectedCoin) {
		CoinDialog dialog = new CoinDialog();
		Bundle args = new Bundle();
		dialog.setArguments(args);
		dialog.onSelectedCoin = onSelectedCoin;
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
		tvLoading.setTextColor(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
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
		getCoins();
	}

	private void getCoins() {
		tvLoading.setVisibility(View.VISIBLE);
		ApiClient.getEventService().getCoins(AppPreference.INSTANCE.getToken().getAccessToken()).enqueue(new Callback<List<Coin>>() {
			@Override
			public void onResponse(Call<List<Coin>> call, Response<List<Coin>> response) {
				tvLoading.setVisibility(View.GONE);
				if (response.isSuccessful()) {
					if (response.body() != null) {
						searchCoins = (ArrayList<Coin>) response.body();
						coins.addAll(response.body());
						coinAdapter.notifyDataSetChanged();
					}
				}
			}

			@Override
			public void onFailure(Call<List<Coin>> call, Throwable t) {
				tvLoading.setVisibility(View.GONE);
			}
		});

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
				.filter(data -> data.getName().contains(key) || data.getSymbol().contains(key.toUpperCase()))
				.toList()
				.subscribe(data -> {
					searchCoins = (ArrayList<Coin>) data;
					coinAdapter.setCoins(searchCoins);
				});
	}

}
