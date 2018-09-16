package com.app.vietincome.bases;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.app.vietincome.R;
import com.app.vietincome.manager.AppPreference;

import butterknife.ButterKnife;

public abstract class BaseDialogFragment extends DialogFragment {

	public boolean isDarkTheme = AppPreference.INSTANCE.darkTheme;

	protected void setAttribute(Dialog dialog, boolean canOutside, int animation, int left, int top, int right, int bottom) {
		dialog.setCanceledOnTouchOutside(canOutside);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		InsetDrawable inset = new InsetDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT), left, top, right, bottom);
		dialog.getWindow().setBackgroundDrawable(inset);
		dialog.getWindow().setAttributes(lp);
		dialog.getWindow().setDimAmount(0.5f);
		dialog.getWindow().getAttributes().windowAnimations = animation;
	}

	protected void setMarginView(View view, int marginLeft, int marginTop, int marginRight, int marginBottom) {
		if (!(view.getLayoutParams() instanceof RelativeLayout.LayoutParams))
			return;
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
		layoutParams.setMargins(
				marginLeft,
				marginTop,
				marginRight,
				marginBottom
		);
		view.setLayoutParams(layoutParams);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(getLayoutId(), container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.bind(this, view);
	}

	public abstract int getLayoutId();

	public int getColor(int color){
		return ContextCompat.getColor(getContext(), color);
	}

}
