package com.app.vietincome.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class HighLightRelativeLayout extends RelativeLayout {
	public HighLightRelativeLayout(Context context) {
		super(context);
	}

	public HighLightRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HighLightRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public HighLightRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
