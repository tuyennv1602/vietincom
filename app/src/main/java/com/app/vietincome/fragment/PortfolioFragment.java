package com.app.vietincome.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.activity.ParentActivity;
import com.app.vietincome.adapter.PortAdapter;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.interfaces.ItemClickListener;
import com.app.vietincome.model.Data;
import com.app.vietincome.model.Item;
import com.app.vietincome.model.Portfolio;
import com.app.vietincome.model.responses.CoinResponse;
import com.app.vietincome.network.ApiClient;
import com.app.vietincome.utils.Constant;
import com.app.vietincome.view.CustomItemDecoration;
import com.app.vietincome.view.HighLightTextView;
import com.app.vietincome.view.NavigationTopBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PortfolioFragment extends BaseFragment implements ItemClickListener {

	@BindView(R.id.tvChangeCoin)
	HighLightTextView tvChangeCoin;

	@BindView(R.id.layoutTop)
	RelativeLayout layoutTop;

	@BindView(R.id.tvTotalPrice)
	TextView tvTotalPrice;

	@BindView(R.id.tvProfitLoss)
	TextView tvProLoss;

	@BindView(R.id.tvProfitValue)
	TextView tvProValue;

	@BindView(R.id.tvCoinDistribute)
	TextView tvCoinDistribute;

	@BindView(R.id.tvCoin)
	TextView tvCoin;

	@BindView(R.id.tvPrice)
	TextView tvPrice;

	@BindView(R.id.tvHolding)
	TextView tvHolding;

	@BindView(R.id.tvCost)
	TextView tvCost;

	@BindView(R.id.tvAddCoin)
	HighLightTextView tvAddCoin;

	@BindView(R.id.rcvPortfolio)
	RecyclerView rcvPortfolio;

	private ArrayList<Data> coins;
	private ArrayList<Portfolio> portfolios = AppPreference.INSTANCE.getPortfolios();
	private PortAdapter portAdapter;

	public static PortfolioFragment newInstance(){
		PortfolioFragment fragment = new PortfolioFragment();
		return fragment;
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_portfolio;
	}

	@Override
	public void onFragmentReady(View view) {
		getCoinsId();
		if(portfolios == null){
			portfolios = new ArrayList<>();
		}
		if(portAdapter == null){
			portAdapter  = new PortAdapter(portfolios, this);
		}
		rcvPortfolio.setLayoutManager(new LinearLayoutManager(getContext()));
		rcvPortfolio.setHasFixedSize(true);
		rcvPortfolio.addItemDecoration(new CustomItemDecoration(1));
		rcvPortfolio.setAdapter(portAdapter);
		onUpdatedTheme();
	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
		navitop.setTvTitle(R.string.portfolio);
		navitop.showImgLeft(false);
		navitop.showImgRight(false);
	}

	@Override
	public void onUpdatedTheme() {
		setTextColor(tvProLoss);
		setTextColor(tvCoinDistribute);
		setTextColor(tvCoin);
		setTextColor(tvPrice);
		setTextColor(tvHolding);
		setTextColor(tvCost);
		layoutTop.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_image): getColor(R.color.light_image));
		tvAddCoin.setBackground(isDarkTheme ? getResources().getDrawable(R.drawable.bg_add_coin_dark) : getResources().getDrawable(R.drawable.bg_add_coin_light));
		setTextViewDrawableColor(tvChangeCoin);
		rcvPortfolio.setBackgroundColor(isDarkTheme ? getColor(R.color.black) : getColor(R.color.color_line));
		portAdapter.setDarkTheme(isDarkTheme);
		portAdapter.notifyDataSetChanged();
	}

	private void getCoinsId(){
		navigationTopBar.showProgressBar();
		ApiClient.getAllCoinService().getAllCoinId().enqueue(new Callback<CoinResponse>() {
			@Override
			public void onResponse(Call<CoinResponse> call, Response<CoinResponse> response) {
				navigationTopBar.hideProgressBar();
				if(response.isSuccessful()){
					if(response.body().getMetadata().isSuccess()){
						coins = response.body().getData();
					}
				}
			}

			@Override
			public void onFailure(Call<CoinResponse> call, Throwable t) {
				navigationTopBar.hideProgressBar();
				showAlert("Failure", t.getMessage());
			}
		});
	}

	@OnClick(R.id.tvAddCoin)
	void onSelectCoin(){
		if(coins == null) return;
		Intent parent = new Intent(getContext(), ParentActivity.class);
		parent.putExtra(Constant.KEY_SCREEN, Constant.SELECT_COIN);
		parent.putExtra("coins", coins);
		startActivity(parent);
	}

	private void setTextViewDrawableColor(TextView textView) {
		Drawable right = ContextCompat.getDrawable(getContext(), R.drawable.next);
		right = DrawableCompat.wrap(right);
		DrawableCompat.setTint(right.mutate(), getColor(R.color.white));
		right.setBounds( 0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());

		Drawable left = ContextCompat.getDrawable(getContext(), R.drawable.previous);
		left = DrawableCompat.wrap(left);
		DrawableCompat.setTint(left.mutate(), getColor(R.color.white));
		left.setBounds( 0, 0, left.getIntrinsicWidth(), left.getIntrinsicHeight());

		textView.setCompoundDrawables(left, null, right, null);
	}

	@Override
	public void onItemClicked(int position) {

	}
}
