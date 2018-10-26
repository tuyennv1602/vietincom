package com.app.vietincome.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.activity.ParentActivity;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.EventBusListener;
import com.app.vietincome.model.responses.UserResponse;
import com.app.vietincome.network.ApiClient;
import com.app.vietincome.utils.CommonUtil;
import com.app.vietincome.utils.Constant;
import com.app.vietincome.view.HighLightImageView;
import com.app.vietincome.view.HighLightTextView;
import com.app.vietincome.view.NavigationTopBar;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends BaseFragment {

	@BindView(R.id.tvSignUp)
	HighLightTextView tvSignUp;

	@BindView(R.id.layoutRoot)
	LinearLayout layoutRoot;

	@BindView(R.id.tvEmail)
	TextView tvEmail;

	@BindView(R.id.tvPassword)
	TextView tvPassword;

	@BindView(R.id.edtEmail)
	EditText edtEmail;

	@BindView(R.id.edtPassword)
	EditText edtPassword;

	@BindView(R.id.tvLogin)
	HighLightTextView tvLogin;

	@BindView(R.id.tvForgotPassword)
	HighLightTextView tvForgotPassword;

	@BindView(R.id.imgVision)
	HighLightImageView imgVision;

	private String email = "";
	private String password = "";
	private boolean isVision;

	public static LoginFragment newInstance() {
		LoginFragment fragment = new LoginFragment();
		return fragment;
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_login;
	}

	@Override
	public void onFragmentReady(View view) {
		onUpdatedTheme();
		tvSignUp.setHighlightColor(Color.TRANSPARENT);
		tvSignUp.setMovementMethod(LinkMovementMethod.getInstance());
	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
		navitop.showImgRight(true);
		navitop.showImgLeft(false);
		navitop.setImgRight(R.drawable.settings);
		navitop.setTvTitle("Login");
	}

	@Override
	public void onRightClicked() {
		super.onRightClicked();
		openScreen(Constant.SETTING);
	}

	@Override
	public void onUpdatedTheme() {
		layoutRoot.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
		setTextColor(tvPassword);
		setTextColor(tvEmail);
		setTextColor(tvSignUp);
		imgVision.setColorFilter(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		edtPassword.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		edtEmail.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		edtEmail.setBackgroundResource(isDarkTheme ? R.drawable.bg_border_dark : R.drawable.bg_border_light);
		edtPassword.setBackgroundResource(isDarkTheme ? R.drawable.bg_border_dark : R.drawable.bg_border_light);
		tvForgotPassword.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		changeBtnLogin();
		tvSignUp.setText(generateSignup());
	}

	@OnClick(R.id.imgVision)
	void changeVision() {
		imgVision.setImageResource(isVision ? R.drawable.invision : R.drawable.vision);
		edtPassword.setTransformationMethod(isVision ? new PasswordTransformationMethod() : null);
		edtPassword.setSelection(edtPassword.length());
		isVision = !isVision;
	}

	@OnTextChanged(R.id.edtEmail)
	void changeEmail(CharSequence text) {
		this.email = String.valueOf(text);
		changeBtnLogin();
	}

	@OnTextChanged(R.id.edtPassword)
	void changePassword(CharSequence text) {
		this.password = String.valueOf(text);
		changeBtnLogin();
	}

	@OnClick(R.id.tvForgotPassword)
	void forgotPassword() {
		openScreen(Constant.FORGOT_PASSWORD);
	}

	@OnClick(R.id.tvLogin)
	void onLogin() {
		if (isFilledData()) {
			hideKeyboard();
			showProgressDialog();
			RequestBody requestBody = new MultipartBody.Builder()
					.setType(MultipartBody.FORM)
					.addFormDataPart("email", email)
					.addFormDataPart("password", password)
					.build();
			ApiClient.getApiV2Service().login(requestBody).enqueue(new Callback<UserResponse>() {
				@Override
				public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
					hideProgressDialog();
					if (response.isSuccessful()) {
						if (response.body().isSuccess()) {
							AppPreference.INSTANCE.setProfile(response.body().getProfile());
							EventBus.getDefault().post(new EventBusListener.ProfileListener());
						} else {
							showAlert("Failed", response.body().getMessage());
						}
					}
				}

				@Override
				public void onFailure(Call<UserResponse> call, Throwable t) {
					hideProgressDialog();
				}
			});
		}
	}

	private void openScreen(int screen) {
		Intent parent = new Intent(getContext(), ParentActivity.class);
		parent.putExtra(Constant.KEY_SCREEN, screen);
		startActivity(parent);
	}

	private boolean isFilledData() {
		if (email.trim().isEmpty() || password.trim().isEmpty()) {
			return false;
		}
		return CommonUtil.validatePassword(password) && CommonUtil.validateEmail(email);
	}

	private void changeBtnLogin() {
		if (isFilledData()) {
			tvLogin.setBackgroundResource(isDarkTheme ? R.drawable.bg_add_coin_dark : R.drawable.bg_add_coin_light);
			tvLogin.setTextColor(isDarkTheme ? getColor(R.color.light_text) : getColor(R.color.dark_text));
		} else {
			tvLogin.setBackgroundResource(isDarkTheme ? R.drawable.bg_border_dark : R.drawable.bg_border_light);
			tvLogin.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		}
	}

	private SpannableStringBuilder generateSignup() {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		SpannableString string = new SpannableString(getString(R.string.don_t_have_an_account_yet_sign_up));
		String signUp = "Sign up";
		ClickableSpan clickSpan = new ClickableSpan() {
			@Override
			public void onClick(View view) {
				openScreen(Constant.SIGN_UP);
			}

			@Override
			public void updateDrawState(TextPaint ds) {
				super.updateDrawState(ds);
				ds.setUnderlineText(false);
			}
		};

		string.setSpan(clickSpan, string.toString().indexOf(signUp), string.toString().indexOf(signUp) + signUp.length(), 0);
		string.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), isDarkTheme ? R.color.dark_image : R.color.light_image)), string.toString().indexOf(signUp), string.toString().indexOf(signUp) + signUp.length(), 0);
		builder.append(string);
		return builder;
	}
}
