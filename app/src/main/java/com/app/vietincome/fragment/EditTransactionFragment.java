package com.app.vietincome.fragment;

import android.app.DatePickerDialog;
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
import com.app.vietincome.manager.EventBusListener;
import com.app.vietincome.model.Portfolio;
import com.app.vietincome.model.Transaction;
import com.app.vietincome.utils.CommonUtil;
import com.app.vietincome.view.HighLightTextView;
import com.app.vietincome.view.NavigationTopBar;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import info.hoang8f.android.segmented.SegmentedGroup;

public class EditTransactionFragment extends BaseFragment {

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

	private Portfolio portfolio;
	private Transaction item;
	private int position;

	public static EditTransactionFragment newInstance(Portfolio portfolio, Transaction item, int position) {
		EditTransactionFragment fragment = new EditTransactionFragment();
		fragment.portfolio = portfolio;
		fragment.item = item;
		fragment.position = position;
		return fragment;
	}
	@Override
	public int getLayoutId() {
		return R.layout.fragment_add_transaction;
	}

	@Override
	public void onFragmentReady(View view) {
		onUpdatedTheme();
		if(item.isBuy()) {
			btnBuy.setChecked(true);
		}else{
			btnSell.setChecked(true);
		}
		changeSegmentColor();
		sgmGroup.setOnCheckedChangeListener((radioGroup, i) -> {
			changeSegmentColor();
		});
		rdGroupCurrency.setOnCheckedChangeListener((radioGroup, i) -> {
			fillData();
		});
		tvTradeTime.setText(item.getDateAdd());
		edtQuantity.setText(String.valueOf(item.getQuantity()));
		fillData();
	}

	private void fillData() {
		if (btnUSD.isChecked()) {
			tvPrice.setText("Price (USD)");
			tvTotalPrice.setText("Total (USD)");
			double price = item.getPriceUSD();
			String value = String.format(Locale.US, "%.4f", price);
			edtPrice.setText(value);
		} else {
			tvPrice.setText("Price (BTC)");
			tvTotalPrice.setText("Total (BTC)");
			edtPrice.setText(String.format(Locale.US, "%.8f", item.getPriceBTC()));
		}
		tvTotalPriceValue.setText(new StringBuilder().append(canculateTotal()).append(btnUSD.isChecked() ? " $" : " ฿").toString());
	}

	private String canculateTotal() {
		if (edtQuantity.getText().toString().isEmpty() || edtPrice.getText().toString().isEmpty()) {
			return "0.0";
		} else {
			float total = (float) (Float.valueOf(edtPrice.getText().toString().trim())) * Float.valueOf(edtQuantity.getText().toString());
			return CommonUtil.formatCurrency(total, btnUSD.isChecked());
		}
	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
		navitop.setTvTitle("Edit transaction (" + portfolio.getSymbol() + ")");
		navitop.showImgLeft(true);
		navitop.setImgLeft(R.drawable.back);
		navitop.showImgRight(false);
		navitop.changeFontTitle(R.font.helvetica_neue);
	}

	private void changeSegmentColor(){
		if(btnBuy.isChecked()){
			sgmGroup.setTintColor(getColor(R.color.green));
		}else{
			sgmGroup.setTintColor(getColor(R.color.red));
		}
	}

	@OnTextChanged({R.id.edtQuantity, R.id.edtPrice})
	void onChangeQuantity(CharSequence text) {
		tvTotalPriceValue.setText(new StringBuilder().append(canculateTotal()).append(btnUSD.isChecked() ? " $" : " ฿").toString());
	}

	@OnClick(R.id.tvTradeTime)
	void selectTradeDate(){
		final Calendar c = Calendar.getInstance();
		DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, year, month, day) -> {
			String result = (day / 10 == 0 ? "0" + day : day)
					+ "/" + ((month + 1) / 10 == 0 ? "0" + (month + 1) : month + 1) + "/" + year;
			tvTradeTime.setText(result);
		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		datePickerDialog.show();
	}

	@OnClick(R.id.tvSave)
	void saveTransaction() {
		if(checkFillData()){
			item.setBuy(btnBuy.isChecked());
			item.setDateAdd(tvTradeTime.getText().toString());
			if(btnUSD.isChecked()){
				item.setPriceUSD(Double.valueOf(edtPrice.getText().toString()));
				item.setPriceBTC(portfolio.getQuotes().getBTC().getPrice());
			}else{
				item.setPriceBTC(Double.valueOf(edtPrice.getText().toString()));
				item.setPriceUSD(portfolio.getQuotes().getUSD().getPrice());
			}
			item.setQuantity(Integer.valueOf(edtQuantity.getText().toString()));
			portfolio.getTransactions().set(position, item);
			EventBus.getDefault().post(new EventBusListener.AddTransaction(item));
			goBack();
		}
	}

	@Override
	public void onLeftClicked() {
		super.onLeftClicked();
		goBack();
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
		tvSave.setTextColor(isDarkTheme ? getColor(R.color.black) : getColor(R.color.white));
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

}
