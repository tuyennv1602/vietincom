package com.app.vietincome.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.adapter.CurrencyAdapter;
import com.app.vietincome.bases.BaseDialogFragment;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.EventBusListener;
import com.app.vietincome.manager.interfaces.ItemClickListener;
import com.app.vietincome.model.Currency;
import com.app.vietincome.utils.CommonUtil;
import com.app.vietincome.view.HighLightTextView;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class CurrencyDialog extends BaseDialogFragment implements ItemClickListener {

	@BindView(R.id.layoutRoot)
	RelativeLayout layoutRoot;

	@BindView(R.id.tvCancel)
	HighLightTextView tvCancel;

	@BindView(R.id.tvSelect)
	HighLightTextView tvSelect;

	@BindView(R.id.rcvCurrency)
	RecyclerView rcvCurrency;

	@BindView(R.id.tvCurrency)
	TextView tvCurrency;

	private ArrayList<Currency> currencies;
	private CurrencyAdapter currencyAdapter;
	private Currency currency;
	private OnSelectedCurrency onSelectedCurrency;

	public static CurrencyDialog newInstance(OnSelectedCurrency onSelectedCurrency){
		CurrencyDialog dialog = new CurrencyDialog();
		Bundle args = new Bundle();
		dialog.setArguments(args);
		dialog.onSelectedCurrency = onSelectedCurrency;
		return dialog;

	}

	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setAttribute(dialog, true, R.style.ZoomDialogAnimation, 15, 100, 15, 100);
		return dialog;
	}

	@Override
	public int getLayoutId() {
		return R.layout.dialog_currency;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		tvCurrency.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		tvCancel.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		tvSelect.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		layoutRoot.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
		rcvCurrency.setBackgroundColor(isDarkTheme ? getColor(R.color.black) : getColor(R.color.color_line));
		currencies = AppPreference.INSTANCE.mGson.fromJson(
				CommonUtil.getStringFromAssets(getContext(), "common-currency.json"),
				new TypeToken<ArrayList<Currency>>() {
				}.getType());
		currency = AppPreference.INSTANCE.getCurrency();
		for (int i = 0; i < currencies.size(); i++) {
			if (currencies.get(i).getCode().equals(currency.getCode())) {
				currencies.get(i).setSelected(true);
			} else {
				currencies.get(i).setSelected(false);
			}
		}
		rcvCurrency.setLayoutManager(new LinearLayoutManager(getContext()));
		rcvCurrency.setNestedScrollingEnabled(false);
		if (currencyAdapter == null) {
			currencyAdapter = new CurrencyAdapter(currencies, this);
		}
		rcvCurrency.setAdapter(currencyAdapter);
	}

	@OnClick(R.id.tvCancel)
	void onCancel() {
		getDialog().dismiss();
	}

	@OnClick(R.id.tvSelect)
	void onSelected(){
		AppPreference.INSTANCE.setCurrency(currency);
		if(onSelectedCurrency != null){
			EventBus.getDefault().post(new EventBusListener.ChangeCurrency());
			onSelectedCurrency.onCurrencyValue(currency.getCode());
			getDialog().dismiss();
		}
	}

	@Override
	public void onItemClicked(int position) {
		currencyAdapter.changeSelected(position);
		currency = currencies.get(position);
	}

	public interface OnSelectedCurrency{
		void onCurrencyValue(String currency);
	}
}
