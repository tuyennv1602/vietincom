package com.app.vietincome.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.activity.ParentActivity;
import com.app.vietincome.adapter.PortAdapter;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.EventBusListener;
import com.app.vietincome.manager.interfaces.ItemClickListener;
import com.app.vietincome.model.Data;
import com.app.vietincome.model.Portfolio;
import com.app.vietincome.model.Transaction;
import com.app.vietincome.model.responses.CoinResponse;
import com.app.vietincome.network.ApiClient;
import com.app.vietincome.utils.CommonUtil;
import com.app.vietincome.utils.Constant;
import com.app.vietincome.view.CustomItemDecoration;
import com.app.vietincome.view.HighLightTextView;
import com.app.vietincome.view.NavigationTopBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Locale;

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

	@BindView(R.id.layoutListPortfolio)
	LinearLayout layoutPortfolio;

	@BindView(R.id.layoutIntro)
	LinearLayout layoutIntro;

	private ArrayList<Data> coins;
	private ArrayList<Portfolio> portfolios = AppPreference.INSTANCE.getPortfolios();
	private PortAdapter portAdapter;
	private boolean isUSD = true;

	public static PortfolioFragment newInstance() {
		PortfolioFragment fragment = new PortfolioFragment();
		return fragment;
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onUpdatePortfolio(EventBusListener.UpdatePortfolio event) {
		int position = portfolios.indexOf(event.portfolio);
		portfolios.set(position, event.portfolio);
		portAdapter.notifyItemChanged(position);
		AppPreference.INSTANCE.addPortfolio(event.portfolio);
		setupCommonData();
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onAddPortfolio(EventBusListener.AddPortfolio event) {
		AppPreference.INSTANCE.addPortfolio(event.portfolio);
		portAdapter.notifyDataSetChanged();
		if (layoutIntro.getVisibility() == View.VISIBLE) {
			layoutIntro.setVisibility(View.GONE);
			layoutPortfolio.setVisibility(View.VISIBLE);
		}
		setupCommonData();
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onRemoveCoin(EventBusListener.RemoveCoin event){
		AppPreference.INSTANCE.removePortfolio(event.portfolio);
		portAdapter.notifyDataSetChanged();
		setupCommonData();
		if(portfolios.size() == 0){
			layoutIntro.setVisibility(View.VISIBLE);
			layoutPortfolio.setVisibility(View.GONE);
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_portfolio;
	}

	@Override
	public void onFragmentReady(View view) {
		getCoinsId();
		if (portfolios == null) {
			portfolios = new ArrayList<>();
		}
		if (portAdapter == null) {
			portAdapter = new PortAdapter(portfolios, this);
		}
		onUpdatedTheme();
		rcvPortfolio.setLayoutManager(new LinearLayoutManager(getContext()));
		rcvPortfolio.setHasFixedSize(true);
		rcvPortfolio.addItemDecoration(new CustomItemDecoration(1));
		rcvPortfolio.setHasTransientState(true);
		rcvPortfolio.setAdapter(portAdapter);
		if (portfolios.size() == 0) {
			layoutIntro.setVisibility(View.VISIBLE);
			layoutPortfolio.setVisibility(View.GONE);
		} else {
			layoutIntro.setVisibility(View.GONE);
			layoutPortfolio.setVisibility(View.VISIBLE);
		}
		setupCommonData();
	}

	private void setupCommonData() {
		double total = getTotal();
		double cost = getCost();
		tvTotalPrice.setText(new StringBuilder().append(isUSD ? "$" : "฿").append(CommonUtil.formatCurrency(total, isUSD)).toString());
		double profit = total - cost;
		float percent = (float) (profit / total) * 100;
		tvProValue.setTextColor(profit > 0 ? getColor(R.color.green) : getColor(R.color.red));
		tvProValue.setText(generateProfitValue(new StringBuilder().append(isUSD ? "$" : "฿").append(CommonUtil.formatCurrency(profit, isUSD)).append(" / ").append(String.format(Locale.US, "%.4f", percent)).append("%").toString()));
	}

	private SpannableStringBuilder generateProfitValue(String text) {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		SpannableString string = new SpannableString(text);
		String dash = "/";
		string.setSpan(new ForegroundColorSpan(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text)), 0, string.toString().indexOf(dash) + dash.length(), 0);
		builder.append(string);
		return builder;
	}

	private double getTotal() {
		double total = 0;
		for (Portfolio item : portfolios) {
			if (isUSD) {
				total += item.getQuotes().getUSD().getPrice() * item.getNumHold();
			} else {
				total += item.getQuotes().getBTC().getPrice() * item.getNumHold();
			}
		}
		return total;
	}

	private double getCost() {
		double total = 0;
		for (Portfolio item : portfolios) {
			for (Transaction transaction : item.getTransactions()) {
				if (isUSD) {
					total += (transaction.getQuantity() * transaction.getPriceUSD());
				} else {
					total += (transaction.getQuantity() * transaction.getPriceBTC());
				}
			}
		}
		return total;
	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
		navitop.setTvTitle(R.string.portfolio);
		navitop.showImgLeft(false);
		navitop.showImgRight(false);
	}

	@Override
	public void onUpdatedTheme() {
		setTextColor(tvCoin);
		setTextColor(tvPrice);
		setTextColor(tvHolding);
		setTextColor(tvCost);
		tvProLoss.setTextColor(isDarkTheme ? getColor(R.color.white) : getColor(R.color.black));
		tvCoinDistribute.setTextColor(isDarkTheme ? getColor(R.color.white) : getColor(R.color.black));
		tvTotalPrice.setTextColor(isDarkTheme ? getColor(R.color.black) : getColor(R.color.white));
		tvAddCoin.setTextColor(isDarkTheme ? getColor(R.color.black) : getColor(R.color.white));
		tvChangeCoin.setTextColor(isDarkTheme ? getColor(R.color.black) : getColor(R.color.white));
		layoutTop.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		tvAddCoin.setBackground(isDarkTheme ? getResources().getDrawable(R.drawable.bg_add_coin_dark) : getResources().getDrawable(R.drawable.bg_add_coin_light));
		setTextViewDrawableColor(tvChangeCoin);
		rcvPortfolio.setBackgroundColor(isDarkTheme ? getColor(R.color.black) : getColor(R.color.color_line));
		portAdapter.setDarkTheme(isDarkTheme);
		portAdapter.notifyDataSetChanged();
	}

	public void setTextColor(TextView textView){
		textView.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
	}

	private void getCoinsId() {
		navigationTopBar.showProgressBar();
		ApiClient.getAllCoinService().getAllCoinId().enqueue(new Callback<CoinResponse>() {
			@Override
			public void onResponse(Call<CoinResponse> call, Response<CoinResponse> response) {
				navigationTopBar.hideProgressBar();
				if (response.isSuccessful()) {
					if (response.body().getMetadata().isSuccess()) {
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
	void onSelectCoin() {
		if (coins == null) return;
		Intent parent = new Intent(getContext(), ParentActivity.class);
		parent.putExtra(Constant.KEY_SCREEN, Constant.SELECT_COIN);
		parent.putExtra("coins", coins);
		startActivity(parent);
	}

	private void setTextViewDrawableColor(TextView textView) {
		Drawable right = ContextCompat.getDrawable(getContext(), R.drawable.next);
		right = DrawableCompat.wrap(right);
		DrawableCompat.setTint(right.mutate(), isDarkTheme ? getColor(R.color.black) : getColor(R.color.white));
		right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());

		Drawable left = ContextCompat.getDrawable(getContext(), R.drawable.previous);
		left = DrawableCompat.wrap(left);
		DrawableCompat.setTint(left.mutate(), isDarkTheme ? getColor(R.color.black) : getColor(R.color.white));
		left.setBounds(0, 0, left.getIntrinsicWidth(), left.getIntrinsicHeight());

		textView.setCompoundDrawables(left, null, right, null);
	}

	@Override
	public void onItemClicked(int position) {
		Intent parent = new Intent(getContext(), ParentActivity.class);
		parent.putExtra(Constant.KEY_SCREEN, Constant.PORTFOLIO_DETAIL);
		parent.putExtra("portId", portfolios.get(position).getId());
		startActivity(parent);
	}

	@OnClick(R.id.tvChangeCoin)
	void onChangeCoin() {
		isUSD = !isUSD;
		tvChangeCoin.setText(isUSD ? "USD" : "BTC");
		setupCommonData();
		portAdapter.changeCurrency();
	}
}
