package com.app.vietincome.fragment;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.dialogs.CreditDialog;
import com.app.vietincome.dialogs.CurrencyDialog;
import com.app.vietincome.dialogs.FeedbackDialog;
import com.app.vietincome.dialogs.RateDialog;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.EventBusListener;
import com.app.vietincome.view.HighLightTextView;
import com.app.vietincome.view.NavigationTopBar;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingFragment extends BaseFragment implements CurrencyDialog.OnSelectedCurrency {

	@BindView(R.id.imgSend)
	ImageView imgSend;

	@BindView(R.id.swTheme)
	Switch swTheme;

	@BindView(R.id.tvRate)
	TextView tvRate;

	@BindView(R.id.tvTelegram)
	TextView tvTelegram;

	@BindView(R.id.tvGeneral)
	TextView tvGeneral;

	@BindView(R.id.tvCurrency)
	TextView tvCurrency;

	@BindView(R.id.tvDarkTheme)
	TextView tvDarkTheme;

	@BindView(R.id.tvChart)
	TextView tvChart;

	@BindView(R.id.tvShowVolume)
	TextView tvShowVolume;

	@BindView(R.id.tvShowVertical)
	TextView tvShowVertical;

	@BindView(R.id.tvShowHorizontal)
	TextView tvShowHorizontal;

	@BindView(R.id.tvAbout)
	TextView tvAbout;

	@BindView(R.id.swVolume)
	Switch swVolume;

	@BindView(R.id.swHorizontal)
	Switch swHorizontal;

	@BindView(R.id.swVertical)
	Switch swVertical;

	@BindView(R.id.tvCurrencyValue)
	TextView tvCurrencyValue;

	@BindView(R.id.tvDonate)
	HighLightTextView tvDonate;

	@BindView(R.id.tvContact)
	TextView tvContact;

	@BindView(R.id.tvCooperation)
	TextView tvCooperation;

	@BindView(R.id.tvShare)
	TextView tvShare;

	@BindView(R.id.tvCredit)
	TextView tvCredit;

	@BindView(R.id.tvAppInfo)
	HighLightTextView tvAppInfo;

	@BindView(R.id.tvSignal)
	TextView tvSignal;

	@BindView(R.id.tvTelegramPremium)
	HighLightTextView tvTelegramPremium;

	public static SettingFragment newInstance() {
		SettingFragment fragment = new SettingFragment();
		return fragment;
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_setting;
	}

	@Override
	public void onFragmentReady(View view) {
		swTheme.setOnCheckedChangeListener(null);
		swTheme.setChecked(isDarkTheme);
		swTheme.setOnCheckedChangeListener((compoundButton, b) -> {
			AppPreference.INSTANCE.changeTheme();
			EventBus.getDefault().post(new EventBusListener.UpdatedTheme());
		});
		swVolume.setOnCheckedChangeListener(null);
		swVolume.setChecked(AppPreference.INSTANCE.isVolume());
		swVolume.setOnCheckedChangeListener((compoundButton, b) -> {
			AppPreference.INSTANCE.setVolume();
			setSwitchColor(swVolume);
		});
		swHorizontal.setOnCheckedChangeListener(null);
		swHorizontal.setChecked(AppPreference.INSTANCE.isHorizontal());
		swHorizontal.setOnCheckedChangeListener((compoundButton, b) -> {
			AppPreference.INSTANCE.setHorizontal();
			setSwitchColor(swHorizontal);
		});
		swVertical.setOnCheckedChangeListener(null);
		swVertical.setChecked(AppPreference.INSTANCE.isVertical());
		swVertical.setOnCheckedChangeListener((compoundButton, b) -> {
			AppPreference.INSTANCE.setVertical();
			setSwitchColor(swVertical);
		});
		tvCurrencyValue.setText(AppPreference.INSTANCE.getCurrency().getCode());
		onUpdatedTheme();
	}

	@OnClick(R.id.tvCurrencyValue)
	void onSelectCurrency(){
		CurrencyDialog currencyDialog = CurrencyDialog.newInstance(this);
		currencyDialog.show(getFragmentManager(), "currency");
	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
		navitop.setTvTitle(R.string.setting);
		navitop.showImgLeft(false);
		navitop.showImgRight(false);
	}

	@Override
	public void onUpdatedTheme() {
		imgSend.setColorFilter(getColor(R.color.blue));
		setTextColor(tvRate);
		setTextColor(tvSignal);
		setTextColor(tvTelegramPremium);
		setTextColor(tvTelegram);
		setTextColor(tvGeneral);
		setTextColor(tvCurrency);
		setTextColor(tvDarkTheme);
		setTextColor(tvChart);
		setTextColor(tvShowVolume);
		setTextColor(tvShowHorizontal);
		setTextColor(tvShowVertical);
		setTextColor(tvDonate);
		setTextColor(tvAbout);
		setTextColor(tvContact);
		setTextColor(tvCooperation);
		setTextColor(tvAppInfo);
		setTextColor(tvShare);
		setTextColor(tvCredit);
		setSwitchColor(swTheme);
		setSwitchColor(swVolume);
		setSwitchColor(swHorizontal);
		setSwitchColor(swVertical);
		tvCurrencyValue.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
	}

	public void setSwitchColor(Switch _switch) {
		if (_switch.isChecked()) {
			_switch.getThumbDrawable().setColorFilter(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image), PorterDuff.Mode.MULTIPLY);
			_switch.getTrackDrawable().setColorFilter(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image), PorterDuff.Mode.MULTIPLY);
		} else {
			_switch.getThumbDrawable().setColorFilter(getColor(R.color.light_gray), PorterDuff.Mode.MULTIPLY);
			_switch.getTrackDrawable().setColorFilter(getColor(R.color.light_gray), PorterDuff.Mode.MULTIPLY);
		}
	}

	@Override
	public void onCurrencyValue(String currency) {
		tvCurrencyValue.setText(currency);
	}

	@OnClick(R.id.layoutTelegram)
	void openTelegram(){
		openLink("https://t.me/vietincomeofficial");
	}

	@OnClick(R.id.tvDonate)
	void openDonate(){
		openLink("https://vietincome.com/donate/");
	}

	@OnClick(R.id.layoutRate)
	void rateApp(){
		RateDialog rateDialog = new RateDialog();
		rateDialog.show(getFragmentManager(), "rate");
	}

	@OnClick(R.id.layoutContact)
	void contactUs(){
		FeedbackDialog feedbackDialog = new FeedbackDialog();
		feedbackDialog.show(getFragmentManager(), "feedback");
	}

	@OnClick(R.id.layoutCooperation)
	void cooperation(){
		Intent contactIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "contact@vietincome.com", null));
		contactIntent.putExtra(Intent.EXTRA_SUBJECT, "Vietincome advertising");
		startActivity(Intent.createChooser(contactIntent, "Choose app"));
	}

	@OnClick(R.id.tvAppInfo)
	void appInfo(){
		openLink("https://vietincome.com/app");
	}

	@OnClick(R.id.tvShare)
	void shareApp(){
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody = "Download Vietincome app here: https://play.google.com/store/apps/details?id=com.app.vietincome";
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Download Vietincome app now!");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(sharingIntent, "Share Vietincome app"));
	}

	@OnClick(R.id.tvCredit)
	void openCredit(){
		CreditDialog creditDialog = new CreditDialog();
		creditDialog.show(getFragmentManager(), "credit");
	}

	@OnClick(R.id.tvTelegramPremium)
	void openTelegramPremium(){
		openLink("https://vietincome.com/channel-vietincome-signal-premium/");
	}
}
