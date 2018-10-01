package com.app.vietincome.fragment;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.model.Data;
import com.app.vietincome.model.Item;
import com.app.vietincome.model.Portfolio;
import com.app.vietincome.model.responses.CoinResponse;
import com.app.vietincome.network.ApiClient;
import com.app.vietincome.utils.CommonUtil;
import com.app.vietincome.utils.DateUtil;
import com.app.vietincome.view.HighLightTextView;
import com.app.vietincome.view.NavigationTopBar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import info.hoang8f.android.segmented.SegmentedGroup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTransactionFragment extends BaseFragment {

	@BindView(R.id.layoutRoot)
	LinearLayout layoutRoot;

	@BindView(R.id.sgmGroup)
	SegmentedGroup sgmGroup;

	@BindView(R.id.rdGroupCurrency)
	RadioGroup rdGroupCurrency;

	@BindView(R.id.btnUSD)
	RadioButton btnUSD;

	@BindView(R.id.btnBTC)
	RadioButton btnBTC;

	@BindView(R.id.btnBuy)
	RadioButton btnBuy;

	@BindView(R.id.btnSell)
	RadioButton btnSell;

	@BindView(R.id.tvCurrency)
	TextView tvCurrency;

	@BindView(R.id.edtQuantity)
	EditText edtQuantity;

	@BindView(R.id.tvPrice)
	TextView tvPrice;

	@BindView(R.id.tvQuantity)
	TextView tvQuantity;

	@BindView(R.id.edtPrice)
	EditText edtPrice;

	@BindView(R.id.tvTradeDate)
	TextView tvTradedate;

	@BindView(R.id.tvTradeTime)
	TextView tvTradeTime;

	@BindView(R.id.tvTotalPrice)
	TextView tvTotalPrice;

	@BindView(R.id.tvTotalValue)
	TextView tvTotalPriceValue;

	@BindView(R.id.tvSave)
	HighLightTextView tvSave;

	private int portId;
	private Item item;
	private String name;
	private String symbol;
	private Data data;

	public static AddTransactionFragment newInstance(Data data) {
		AddTransactionFragment fragment = new AddTransactionFragment();
		fragment.portId = data.getId();
		fragment.name = data.getName();
		fragment.symbol = data.getSymbol();
		return fragment;
	}

	public static AddTransactionFragment newInstance(int portId, String portName, String portSymbol, Item item) {
		AddTransactionFragment fragment = new AddTransactionFragment();
		fragment.portId = portId;
		fragment.name = portName;
		fragment.symbol = portSymbol;
		fragment.item = item;
		return fragment;
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_add_transaction;
	}

	@Override
	public void onFragmentReady(View view) {
		onUpdatedTheme();
		sgmGroup.setOnCheckedChangeListener((radioGroup, i) -> {
		});
		rdGroupCurrency.setOnCheckedChangeListener((radioGroup, i) -> {
			fillData();
		});
		getCoinDetail();
		tvTradeTime.setText(DateUtil.getStrCurrentDate());
	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
		navitop.setTvTitle("Add transaction (" + symbol + ")");
		navitop.showImgLeft(true);
		navitop.setImgLeft(R.drawable.back);
		navitop.showImgRight(false);
		navitop.changeFontTitle(R.font.helvetica_neue);
	}


	@Override
	public void onLeftClicked() {
		super.onLeftClicked();
		goBack();
	}

	@Override
	public void onUpdatedTheme() {
		layoutRoot.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
		sgmGroup.setTintColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		ColorStateList colorStateList = new ColorStateList(
				new int[][]{
						new int[]{-android.R.attr.state_checked},
						new int[]{android.R.attr.state_checked}
				},
				new int[]{
						getColor(R.color.light_gray)
						, isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image),
				}
		);
		btnBTC.setButtonTintList(colorStateList);
		btnUSD.setButtonTintList(colorStateList);
		btnUSD.setTextColor(colorStateList);
		btnBTC.setTextColor(colorStateList);
		setTextColor(tvCurrency);
		setTextColor(tvQuantity);
		edtQuantity.setHintTextColor(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
		edtQuantity.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		edtPrice.setHintTextColor(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
		edtPrice.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		setTextColor(tvPrice);
		setTextColor(tvTradedate);
		setTextColor(tvTradeTime);
		setTextColor(tvTotalPrice);
		setTextColor(tvTotalPriceValue);
		tvSave.setBackground(isDarkTheme ? getResources().getDrawable(R.drawable.bg_add_coin_dark) : getResources().getDrawable(R.drawable.bg_add_coin_light));
		setEditTextDrawableColor(edtPrice);
		setEditTextDrawableColor(edtQuantity);
		setTextViewDrawableColor(tvTradeTime);
	}

	private void setEditTextDrawableColor(EditText editText) {
		Drawable right = ContextCompat.getDrawable(getContext(), R.drawable.pen);
		right = DrawableCompat.wrap(right);
		DrawableCompat.setTint(right.mutate(), isDarkTheme ? getColor(R.color.white) : getColor(R.color.black));
		right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
		editText.setCompoundDrawables(null, null, right, null);
	}

	private void setTextViewDrawableColor(TextView textView) {
		Drawable right = ContextCompat.getDrawable(getContext(), R.drawable.calendar);
		right = DrawableCompat.wrap(right);
		DrawableCompat.setTint(right.mutate(), isDarkTheme ? getColor(R.color.white) : getColor(R.color.black));
		right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
		textView.setCompoundDrawables(null, null, right, null);
	}

	private void getCoinDetail() {
		navigationTopBar.showProgressBar();
		ApiClient.getAllCoinService().getCoinDetail(portId).enqueue(new Callback<CoinResponse>() {
			@Override
			public void onResponse(Call<CoinResponse> call, Response<CoinResponse> response) {
				navigationTopBar.hideProgressBar();
				if (response.isSuccessful()) {
					if (response.body().getMetadata().isSuccess()) {
						AddTransactionFragment.this.data = response.body().getData().get(0);
						fillData();
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

	private void fillData() {
		if (btnUSD.isChecked()) {
			tvPrice.setText("Price (USD)");
			tvTotalPrice.setText("Total (USD)");
			double price = data.getQuotes().getUSD().getPrice();
			String value;
			if (price < 1.0) {
				value = String.format(Locale.US, "%.4f", price);
			} else if (price < 1000) {
				value = String.format(Locale.US, "%.2f", price);
			} else {
				DecimalFormat dFormat = new DecimalFormat("###,###,###,##0.000");
				value = dFormat.format(price);
			}
			edtPrice.setText(value);
		} else {
			tvPrice.setText("Price (BTC)");
			tvTotalPrice.setText("Total (BTC)");
			edtPrice.setText(String.format(Locale.US, "%.8f", data.getQuotes().getBTC().getPrice()));
		}
		tvTotalPriceValue.setText(canculateTotal());
	}

	private String canculateTotal() {
		if (edtQuantity.getText().toString().isEmpty() || edtPrice.getText().toString().isEmpty()) {
			return "0.0" + (btnUSD.isChecked() ? " $" : " ฿");
		} else {
			float total = (float) ((btnUSD.isChecked() ? data.getQuotes().getUSD().getPrice() : data.getQuotes().getBTC().getPrice()) * Float.valueOf(edtQuantity.getText().toString()));
			return CommonUtil.formatCurrency(total, btnUSD.isChecked());
		}

	}

	@OnTextChanged(R.id.edtQuantity)
	void onChangeQuantity(CharSequence text) {
		tvTotalPriceValue.setText(canculateTotal());
	}

	@OnTextChanged(R.id.edtPrice)
	void onChangePrice(CharSequence text) {
		tvTotalPriceValue.setText(canculateTotal());
	}

	@OnClick(R.id.tvSave)
	void saveTransaction() {
		if(checkFillData()){
			Portfolio portfolio = AppPreference.INSTANCE.getPortfolioById(portId);
			ArrayList<Item> items = portfolio.getItems();
			if(items == null){
				items = new ArrayList<>();
			}
			Item newItem = new Item(
					items.size(),
					data.getQuotes().getUSD().getPrice(),
					data.getQuotes().getBTC().getPrice(),
					tvTradeTime.getText().toString(),
					Integer.valueOf(edtQuantity.getText().toString()),
					btnBuy.isChecked());
			items.add(newItem);
			portfolio.setId(portId);
			portfolio.setName(data.getName());
			portfolio.setSymbol(data.getSymbol());
			portfolio.setItems(items);
			portfolio.setQuotes(data.getQuotes());
			AppPreference.INSTANCE.addPortfolio(portfolio);
			showToast("Save successful");
			popFragment(2);
		}
	}

	private boolean checkFillData() {
		if (edtQuantity.getText().toString().isEmpty()) {
			showAlert("Message", "Please enter quantity");
			return false;
		}
		if (edtPrice.getText().toString().isEmpty()) {
			showAlert("Message", "Please enter price");
			return false;
		}
		return true;
	}
}
