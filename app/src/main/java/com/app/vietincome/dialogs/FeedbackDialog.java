package com.app.vietincome.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.bases.BaseDialogFragment;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.network.AppConfig;
import com.app.vietincome.view.HighLightTextView;

import butterknife.BindView;
import butterknife.OnClick;

public class FeedbackDialog extends BaseDialogFragment {

	@BindView(R.id.layoutRoot)
	LinearLayout layoutRoot;

	@BindView(R.id.tvFeedback)
	TextView tvFeedback;

	@BindView(R.id.tvFeedbackContent)
	TextView tvFeedbackContent;

	@BindView(R.id.tvClose)
	HighLightTextView tvClose;

	@BindView(R.id.tvEmail)
	HighLightTextView tvEmail;

	@BindView(R.id.tvTelegram)
	HighLightTextView tvTelegram;

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
		return R.layout.dialog_feedback;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		layoutRoot.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
		tvFeedback.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		tvFeedbackContent.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		tvClose.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		tvEmail.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		tvTelegram.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
	}

	@OnClick(R.id.tvClose)
	void onClose(){
		getDialog().dismiss();
	}

	@OnClick(R.id.tvEmail)
	void onEmail(){
		Intent contactIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "contact@vietincome.com", null));
		contactIntent.putExtra(Intent.EXTRA_SUBJECT, "Vietincome feedback");
		startActivity(Intent.createChooser(contactIntent, "Choose app"));
		getDialog().dismiss();
	}

	@OnClick(R.id.tvTelegram)
	void onTelegram(){
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/vietincome"));
		startActivity(browserIntent);
		getDialog().dismiss();
	}
}
