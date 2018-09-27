package com.app.vietincome.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.app.vietincome.R;

public class CustomSeekbar extends android.support.v7.widget.AppCompatSeekBar {

	private int mThumbSize;
	private TextPaint mTextPaint;

	public CustomSeekbar(Context context) {
		super(context, null);
	}

	public CustomSeekbar(Context context, AttributeSet attrs) {
		super(context, attrs, android.R.attr.seekBarStyle);
		initView();
	}

	public CustomSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	private void initView(){
		mThumbSize = getResources().getDimensionPixelSize(R.dimen.thumb_size);
		mTextPaint = new TextPaint();
		mTextPaint.setColor(Color.BLACK);
		mTextPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.thumb_text_size));
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTypeface(Typeface.DEFAULT);
		mTextPaint.setTextAlign(Paint.Align.CENTER);
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		String progressText = String.valueOf(getProgress()) + "%";
		Rect bounds = new Rect();
		mTextPaint.getTextBounds(progressText, 0, progressText.length(), bounds);
		int leftPadding = getPaddingLeft() - getThumbOffset();
		int rightPadding = getPaddingRight() - getThumbOffset();
		int width = getWidth() - leftPadding - rightPadding;
		float progressRatio = (float) getProgress() / getMax();
		float thumbOffset = mThumbSize * (.5f - progressRatio);
		float thumbX = progressRatio * width + leftPadding + thumbOffset;
		float thumbY = getHeight() / 2f + bounds.height() / 2f;
		canvas.drawText(progressText, thumbX, thumbY, mTextPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}

	public void setTextColor(int color){
		mTextPaint.setColor(color);
		invalidate();
	}
}
