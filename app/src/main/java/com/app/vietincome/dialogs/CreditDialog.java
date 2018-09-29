package com.app.vietincome.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.bases.BaseDialogFragment;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.view.HighLightTextView;

import butterknife.BindView;
import butterknife.OnClick;

public class CreditDialog extends BaseDialogFragment {

	@BindView(R.id.layoutRoot)
	LinearLayout layoutRoot;

	@BindView(R.id.tvCredit)
	TextView tvCredit;

	@BindView(R.id.tvCreditContent)
	TextView tvCreditContent;

	@BindView(R.id.tvOk)
	HighLightTextView tvOk;

	private boolean isDarkTheme = AppPreference.INSTANCE.isDarkTheme();

	@Override
	public void onStart() {
		super.onStart();
		Dialog dialog = getDialog();
		if (dialog != null) {
			dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setAttribute(dialog, true, R.style.ZoomDialogAnimation);
		return dialog;
	}

	@Override
	public int getLayoutId() {
		return R.layout.dialog_credit;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		layoutRoot.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
		tvCredit.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		tvCreditContent.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		tvOk.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		tvCreditContent.setText(generateContent());
	}

	private SpannableStringBuilder generateContent() {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		SpannableString string = new SpannableString(getString(R.string.credit_content));
		String coinMarketCap = "CoinMarketCap";
		String cryptoCompare = "CryptoCompare";
		string.setSpan(new UnderlineSpan(), string.toString().indexOf(coinMarketCap), string.toString().indexOf(coinMarketCap) + coinMarketCap.length(), 0);
		string.setSpan(new ForegroundColorSpan(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image)), string.toString().indexOf(coinMarketCap), string.toString().indexOf(coinMarketCap) + coinMarketCap.length(), 0);
		string.setSpan(new UnderlineSpan(), string.toString().indexOf(cryptoCompare), string.toString().indexOf(cryptoCompare) + cryptoCompare.length(), 0);
		string.setSpan(new ForegroundColorSpan(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image)), string.toString().indexOf(cryptoCompare), string.toString().indexOf(cryptoCompare) + cryptoCompare.length(), 0);
		builder.append(string);
		return builder;
	}

	@OnClick(R.id.tvOk)
	void onOke(){
		getDialog().dismiss();
	}
}
