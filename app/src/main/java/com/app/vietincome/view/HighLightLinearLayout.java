package com.app.vietincome.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class HighLightLinearLayout extends LinearLayout {
	public HighLightLinearLayout(Context context) {
		super(context);
	}

	public HighLightLinearLayout(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public HighLightLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public HighLightLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	public void setPressed(boolean pressed) {
		if (isEnabled()) {
			setState(pressed);
		}
		super.setPressed(pressed);
	}

	@Override
	public void setEnabled(boolean enabled) {
		setState(!enabled);
		super.setEnabled(enabled);
	}

	private void setState(boolean pressed) {
		setAlpha(pressed ? 0.8f : 1f);
	}
}
