package com.app.vietincome.view;

import android.animation.Animator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.interfaces.NavigationTopListener;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import static com.app.vietincome.utils.CommonUtil.dp2pxInt;

public class NavigationTopBar {

	@BindView(R.id.topLayout)
	LinearLayout topLayout;

	@BindView(R.id.imgRight)
	HighLightImageView imgRight;

	@BindView(R.id.imgLeft)
	HighLightImageView imgLeft;

	@BindView(R.id.imgAdditionalRight)
	HighLightImageView imgAdditonalRight;

	@BindView(R.id.tvTitle)
	TextView tvTitle;

	@BindView(R.id.progress_bar)
	MaterialProgressBar progressBar;

	@BindView(R.id.edtSearch)
	EditText edtSearch;

	@BindView(R.id.layoutSearch)
	RelativeLayout layoutSearch;

	@BindView(R.id.imgClose)
	HighLightImageView imgClose;

	@BindView(R.id.tvSubTitle)
	TextView tvSubTitle;

	@BindView(R.id.layoutSecond)
	RelativeLayout layoutSecond;

	private Context context;
	private NavigationTopListener topBarListener;
	private boolean isSearch;
	private int currentLeft;
	private boolean isShowLeft = true;
	private boolean isDarkTheme;

	public NavigationTopBar(View view, Context context, NavigationTopListener topBarListener) {
		if (view == null) return;
		ButterKnife.bind(this, view);
		this.context = context;
		this.topBarListener = topBarListener;
		initTheme(AppPreference.INSTANCE.isDarkTheme());
		edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
				if (i == EditorInfo.IME_ACTION_SEARCH) {
					if (topBarListener != null) {
						topBarListener.onSearchDone();
						Log.d("__topbar", "onEditorAction: ");
					}
					return true;
				}
				return false;
			}
		});
	}

	public void showProgressBar() {
		progressBar.setIndeterminateTintList(ColorStateList.valueOf(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image)));
		progressBar.setVisibility(View.VISIBLE);
	}

	public void hideProgressBar() {
		progressBar.setIndeterminateTintList(ColorStateList.valueOf(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background)));
		progressBar.setVisibility(View.INVISIBLE);
	}

	public void initTheme(boolean isDarkTheme) {
		this.isDarkTheme = isDarkTheme;
		topLayout.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
		layoutSearch.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
		ColorStateList colorStateList = ColorStateList.valueOf(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		ViewCompat.setBackgroundTintList(edtSearch, colorStateList);
		edtSearch.setHintTextColor(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
		edtSearch.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		imgLeft.setColorFilter(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		imgRight.setColorFilter(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		imgClose.setColorFilter(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		imgAdditonalRight.setColorFilter(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		tvTitle.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		tvSubTitle.setTextColor(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
	}

	public int getColor(int color) {
		return ContextCompat.getColor(context, color);
	}

	public void playTitleAnimation() {
		YoYo.with(Techniques.Bounce)
				.duration(3000)
				.repeat(YoYo.INFINITE)
				.playOn(tvTitle);
	}

	@OnClick(R.id.imgRight)
	void onRightClick() {
		if (topBarListener != null) {
			topBarListener.onRightClicked();
		}
	}

	@OnClick(R.id.imgLeft)
	void onLeftClick() {
		if (isSearch) {
			if (topBarListener != null) {
				topBarListener.onCloseSearch();
			}
			closeSearch();
		} else if (topBarListener != null) {
			topBarListener.onLeftClicked();
		}
	}

	@OnClick(R.id.imgAdditionalRight)
	void onAdditionalRight() {
		if (topBarListener != null) {
			topBarListener.onAdditionRightClicked();
		}
	}

	@OnTextChanged(R.id.edtSearch)
	void onSearchChanged(CharSequence text) {
		if (text.toString().isEmpty()) {
			imgClose.setVisibility(View.GONE);
		} else {
			imgClose.setVisibility(View.VISIBLE);
		}
		if (topBarListener != null) {
			topBarListener.onSearchChanged(text.toString());
		}
	}

	@OnClick(R.id.imgClose)
	void onCloseClick() {
		edtSearch.setText("");
	}

	public EditText getEdtSearch() {
		return this.edtSearch;
	}

	public boolean isSearch(){
		return this.isSearch;
	}

	public void openSearch() {
		YoYo.with(Techniques.SlideInRight)
				.duration(200)
				.withListener(new Animator.AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animator) {
						isSearch = true;
					}

					@Override
					public void onAnimationEnd(Animator animator) {
						imgLeft.setImageResource(R.drawable.back);
						imgLeft.setVisibility(View.VISIBLE);
						layoutSearch.setVisibility(View.VISIBLE);
						edtSearch.requestFocus();
					}

					@Override
					public void onAnimationCancel(Animator animator) {

					}

					@Override
					public void onAnimationRepeat(Animator animator) {

					}
				})
				.playOn(layoutSearch);
	}

	public void closeSearch() {
		YoYo.with(Techniques.SlideOutRight)
				.duration(200)
				.withListener(new Animator.AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animator) {
						isSearch = false;
					}

					@Override
					public void onAnimationEnd(Animator animator) {
						imgLeft.setImageResource(currentLeft);
						imgLeft.setVisibility(isShowLeft ? View.VISIBLE : View.INVISIBLE);
						layoutSearch.setVisibility(View.GONE);
						edtSearch.setText("");
					}

					@Override
					public void onAnimationCancel(Animator animator) {

					}

					@Override
					public void onAnimationRepeat(Animator animator) {

					}
				})
				.playOn(layoutSearch);
	}

	public void setTvTitle(int text) {
		tvTitle.setText(text);
	}

	public void setTvTitle(String text) {
		tvTitle.setText(text);
	}

	public void showImgRight(boolean isShow) {
		imgRight.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
	}

	public void showImgLeft(boolean isShow) {
		isShowLeft = isShow;
		imgLeft.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
	}

	public void showAdditionalRight(boolean isShow) {
		imgAdditonalRight.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
	}

	public void setImgRight(int res) {
		imgRight.setImageResource(res);
	}

	public void setImgLeft(int res) {
		currentLeft = res;
		imgLeft.setImageResource(res);
	}

	public void setSubTitle(String subTitle){
		tvSubTitle.setVisibility(View.VISIBLE);
		tvSubTitle.setText(subTitle);
		tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
	}

	public void changePaddingView(View view, float padding) {
		view.setPadding(dp2pxInt(padding), dp2pxInt(padding), dp2pxInt(padding), dp2pxInt(padding));
	}

	public void changeLeftPadding(float padding){
		changePaddingView(imgLeft, padding);
	}

	public void changeRightPadding(float padding){
		changePaddingView(imgRight, padding);
		changePaddingView(imgAdditonalRight, padding);
	}

	public void setBackgroundBorder(){
		topLayout.setBackground(isDarkTheme ? context.getDrawable(R.drawable.border_evulation_gray_dark) : context.getDrawable(R.drawable.border_evulation_gray));
		layoutSecond.setBackground(isDarkTheme ? context.getDrawable(R.drawable.border_evulation_dark) : context.getDrawable(R.drawable.border_evulation_light));

	}
}
