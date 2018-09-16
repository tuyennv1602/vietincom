package com.app.vietincome.view;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class HighLightTextView extends AppCompatTextView {

	public HighLightTextView(Context context) {
		super(context);
	}

	public HighLightTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HighLightTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
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
