package com.app.vietincome.fragment;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.adapter.TransactionAdapter;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.EventBusListener;
import com.app.vietincome.manager.interfaces.AlertListener;
import com.app.vietincome.manager.interfaces.ItemClickListener;
import com.app.vietincome.manager.interfaces.OnDeleteItemListener;
import com.app.vietincome.model.Data;
import com.app.vietincome.model.Portfolio;
import com.app.vietincome.model.Transaction;
import com.app.vietincome.utils.CommonUtil;
import com.app.vietincome.view.CustomItemDecoration;
import com.app.vietincome.view.HighLightTextView;
import com.app.vietincome.view.NavigationTopBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class PortfolioDetailFragment extends BaseFragment implements ItemClickListener, OnDeleteItemListener {

	@BindView(R.id.tvChangeCoin)
	HighLightTextView tvChangeCoin;

	@BindView(R.id.layoutTop)
	RelativeLayout layoutTop;

	@BindView(R.id.tvTotalPrice)
	TextView tvTotalPrice;

	@BindView(R.id.tvAddCoin)
	HighLightTextView tvAddCoin;

	@BindView(R.id.tvAllHolding)
	TextView tvAllHolding;

	@BindView(R.id.tvCurrentPrice)
	TextView tvCurrentPrice;

	@BindView(R.id.tvWallet)
	TextView tvWallet;

	@BindView(R.id.tvHolding)
	TextView tvHolding;

	@BindView(R.id.tvPrice)
	TextView tvPrice;

	@BindView(R.id.tvTotal)
	TextView tvTotal;

	@BindView(R.id.tvProfit)
	TextView tvProfit;

	@BindView(R.id.rcvTransaction)
	RecyclerView rcvTransaction;

	@BindView(R.id.layoutRoot)
	LinearLayout layoutRoot;

	private Portfolio portfolio;
	private int portId;
	private ArrayList<Transaction> transactions;
	private boolean isUSD = true;
	private TransactionAdapter transactionAdapter;

	public static PortfolioDetailFragment newInstance(int portId) {
		PortfolioDetailFragment fragment = new PortfolioDetailFragment();
		fragment.portId = portId;
		fragment.portfolio = AppPreference.INSTANCE.getPortfolioById(portId);
		return fragment;
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onAddTransaction(EventBusListener.AddTransaction event) {
		this.portfolio = AppPreference.INSTANCE.getPortfolioById(portId);
		transactionAdapter.setTotalPrice(getTotalPrice());
		transactionAdapter.notifyDataSetChanged();
		setupCommonData();
		EventBus.getDefault().post(new EventBusListener.UpdatePortfolio(portfolio));
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_portfolio_detail;
	}

	@Override
	public void onFragmentReady(View view) {
		onUpdatedTheme();
		setupCommonData();
		transactions = portfolio.getTransactions();
		if (transactionAdapter == null) {
			transactionAdapter = new TransactionAdapter(transactions, getTotalPrice(), getCurrentPrice(), this, this);
		}
		rcvTransaction.setLayoutManager(new LinearLayoutManager(getContext()));
		rcvTransaction.addItemDecoration(new CustomItemDecoration(1));
		rcvTransaction.setHasFixedSize(true);
		rcvTransaction.setAdapter(transactionAdapter);
	}

	private void setupCommonData() {
		tvAllHolding.setText(new StringBuilder().append("Holdings: ").append(portfolio.getNumHold()).toString());
		StringBuilder strCurrentPrice = new StringBuilder();
		strCurrentPrice.append("Price: ");
		strCurrentPrice.append(isUSD ? "$" : "฿");
		strCurrentPrice.append(CommonUtil.formatCurrency(getCurrentPrice(), isUSD));
		tvCurrentPrice.setText(strCurrentPrice.toString());
		tvTotalPrice.setText(new StringBuilder().append(isUSD ? "$" : "฿").append(CommonUtil.formatCurrency(getTotalPrice(), isUSD)).toString());
	}

	private double getCurrentPrice() {
		return isUSD ? portfolio.getQuotes().getUSD().getPrice() : portfolio.getQuotes().getBTC().getPrice();
	}

	private double getTotalPrice() {
		return portfolio.getNumHold() * (isUSD ? portfolio.getQuotes().getUSD().getPrice() : portfolio.getQuotes().getBTC().getPrice());
	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
		navitop.setTvTitle(portfolio.getName() + "(" + portfolio.getSymbol() + ")");
		navitop.setImgLeft(R.drawable.back);
		navitop.setImgRight(R.drawable.add);
		navitop.changeFontTitle(R.font.roboto_regular);
	}

	@Override
	public void onLeftClicked() {
		super.onLeftClicked();
		goBack();
	}

	@Override
	public void onRightClicked() {
		super.onRightClicked();
		actionAddCoin();
	}

	@OnClick(R.id.tvChangeCoin)
	void onChangeCoin() {
		isUSD = !isUSD;
		tvChangeCoin.setText(isUSD ? "USD" : "BTC");
		setupCommonData();
		transactionAdapter.changeCurrency();
		transactionAdapter.setTotalPrice(getTotalPrice());
		transactionAdapter.setCurrentPrice(getCurrentPrice());
		transactionAdapter.notifyDataSetChanged();
	}

	@OnClick(R.id.tvAddCoin)
	void addCoin() {
		actionAddCoin();
	}

	private void actionAddCoin() {
		Data data = new Data();
		data.setId(portfolio.getId());
		data.setName(portfolio.getName());
		data.setSymbol(portfolio.getSymbol());
		pushFragment(AddTransactionFragment.newInstance(data, false), R.anim.slide_from_left_to_right_in, R.anim.slide_from_right_out);
	}

	@Override
	public void onUpdatedTheme() {
		layoutRoot.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
		layoutTop.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		tvAddCoin.setBackground(isDarkTheme ? getResources().getDrawable(R.drawable.bg_add_coin_dark) : getResources().getDrawable(R.drawable.bg_add_coin_light));
		tvAllHolding.setTextColor(isDarkTheme ? getColor(R.color.white) : getColor(R.color.black));
		tvCurrentPrice.setTextColor(isDarkTheme ? getColor(R.color.white) : getColor(R.color.black));
		setTextColor(tvHolding);
		setTextColor(tvPrice);
		setTextColor(tvTotal);
		setTextColor(tvProfit);
		tvChangeCoin.setTextColor(isDarkTheme ? getColor(R.color.black) : getColor(R.color.white));
		tvAddCoin.setTextColor(isDarkTheme ? getColor(R.color.black) : getColor(R.color.white));
		tvTotalPrice.setTextColor(isDarkTheme ? getColor(R.color.black) : getColor(R.color.white));
		rcvTransaction.setBackgroundColor(isDarkTheme ? getColor(R.color.black) : getColor(R.color.color_line));
		setTextViewDrawableColor(tvChangeCoin);
	}

	public void setTextColor(TextView textView) {
		textView.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
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
		pushFragment(EditTransactionFragment.newInstance(portfolio, transactions.get(position), position), R.anim.slide_from_left_to_right_in, R.anim.slide_from_right_out);
	}

	@Override
	public void onItemDeleted(int position) {
		showAlert("Message", "Do you want to detele this transaction?", "YES", "NO", new AlertListener() {
			@Override
			public void onClickedOk() {
				PortfolioDetailFragment.this.portfolio.getTransactions().remove(position);
				if(PortfolioDetailFragment.this.portfolio.getTransactions().size() == 0){
					EventBus.getDefault().post(new EventBusListener.RemoveCoin(PortfolioDetailFragment.this.portfolio));
					goBack();
					return;
				}
				AppPreference.INSTANCE.addPortfolio(portfolio);
				transactionAdapter.notifyDataSetChanged();
				setupCommonData();
			}

			@Override
			public void onClickedCancel() {

			}
		});

	}
}
