package com.app.vietincome.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.app.vietincome.view.HighLightTextView;

import butterknife.BindView;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class RateDialog extends BaseDialogFragment {

//	startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=PackageName")));

	@BindView(R.id.ratingBar)
	MaterialRatingBar ratingBar;

	@BindView(R.id.tvRateApp)
	TextView tvRateApp;

	@BindView(R.id.tvRateContent)
	TextView tvRateContent;

	@BindView(R.id.layoutRoot)
	LinearLayout layoutRoot;

	@BindView(R.id.tvLater)
	HighLightTextView tvLater;

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
		return R.layout.dialog_rate;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		layoutRoot.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
		tvRateApp.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		tvRateContent.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		((LayerDrawable) ratingBar.getProgressDrawable()).getDrawable(0).setColorFilter(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image), PorterDuff.Mode.SRC_ATOP);
		((LayerDrawable) ratingBar.getProgressDrawable()).getDrawable(1).setColorFilter(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image), PorterDuff.Mode.SRC_ATOP);
		((LayerDrawable) ratingBar.getProgressDrawable()).getDrawable(2).setColorFilter(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image), PorterDuff.Mode.SRC_ATOP);
		tvLater.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		ratingBar.setOnRatingChangeListener((ratingBar, rating) -> {
			if(rating < 4){
				new Handler().postDelayed(() -> {
					FeedbackDialog feedbackDialog = new FeedbackDialog();
					feedbackDialog.show(getFragmentManager(), "feedback");
					getDialog().dismiss();
				}, 1000);
			}else{
				new Handler().postDelayed(() -> {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.app.vietincome")));
					getDialog().dismiss();
				}, 500);
			}
		});
	}

	@OnClick(R.id.tvLater)
	void onLater(){
		getDialog().dismiss();
	}
}
