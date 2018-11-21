package com.app.vietincome.fragment;

import android.content.res.ColorStateList;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.vietincome.R;
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
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupFragment extends BaseFragment {

	@BindView(R.id.tvSignUp)
	HighLightTextView tvSignUp;

	@BindView(R.id.layoutRoot)
	LinearLayout layoutRoot;

	@BindView(R.id.tvUsername)
	TextView tvUsername;

	@BindView(R.id.edtUsername)
	EditText edtUsername;

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

	@BindView(R.id.tvCode)
	TextView tvCode;

	@BindView(R.id.edtCode)
	EditText edtCode;

	@BindView(R.id.imgVision)
	HighLightImageView imgVision;

	@BindView(R.id.checkBox)
	CheckBox checkBox;

	@BindView(R.id.tvTerm)
	HighLightTextView tvTerm;

	private String email = "";
	private String password = "";
	private String username = "";
	private String code = "";
	private boolean isVision;

	@Override
	public int getLayoutId() {
		return R.layout.fragment_signup;
	}

	@Override
	public void onFragmentReady(View view) {
		onUpdatedTheme();
		tvLogin.setHighlightColor(Color.TRANSPARENT);
		tvLogin.setText(generateLogin());
		tvLogin.setMovementMethod(LinkMovementMethod.getInstance());
		tvTerm.setHighlightColor(Color.TRANSPARENT);
		tvTerm.setText(generateTerm());
		tvTerm.setMovementMethod(LinkMovementMethod.getInstance());
	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
		navitop.showImgRight(false);
		navitop.setImgLeft(R.drawable.back);
		navitop.setTvTitle("Sign Up");
	}

	@Override
	public void onLeftClicked() {
		super.onLeftClicked();
		goBack();
	}

	@Override
	public void onUpdatedTheme() {
		layoutRoot.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
		setTextColor(tvPassword);
		setTextColor(tvEmail);
		setTextColor(tvLogin);
		setTextColor(tvUsername);
		setTextColor(tvCode);
		setTextColor(tvTerm);
		imgVision.setColorFilter(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		edtUsername.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		edtCode.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		edtPassword.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		edtEmail.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		edtEmail.setBackgroundResource(isDarkTheme ? R.drawable.bg_border_dark : R.drawable.bg_border_light);
		edtPassword.setBackgroundResource(isDarkTheme ? R.drawable.bg_border_dark : R.drawable.bg_border_light);
		edtUsername.setBackgroundResource(isDarkTheme ? R.drawable.bg_border_dark : R.drawable.bg_border_light);
		edtCode.setBackgroundResource(isDarkTheme ? R.drawable.bg_border_dark : R.drawable.bg_border_light);
		ColorStateList colorStateList = new ColorStateList(
				new int[][]{
						new int[]{-android.R.attr.state_checked},
						new int[]{android.R.attr.state_checked}
				},
				new int[]{
						isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image)
						, isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image),
				}
		);
		checkBox.setButtonTintList(colorStateList);
		changeBtnSignUp();
	}

	@OnClick(R.id.imgVision)
	void changeVision(){
		imgVision.setImageResource(isVision ? R.drawable.invision : R.drawable.vision);
		edtPassword.setTransformationMethod(isVision ? new PasswordTransformationMethod() : null);
		edtPassword.setSelection(edtPassword.length());
		isVision = !isVision;
	}

	@OnTextChanged(R.id.edtUsername)
	void changeUsername(CharSequence text){
		this.username = String.valueOf(text);
		changeBtnSignUp();
	}

	@OnTextChanged(R.id.edtCode)
	void changeCode(CharSequence text){
		this.code = String.valueOf(text);
	}

	@OnTextChanged(R.id.edtEmail)
	void changeEmail(CharSequence text) {
		this.email = String.valueOf(text);
		changeBtnSignUp();
	}

	@OnTextChanged(R.id.edtPassword)
	void changePassword(CharSequence text) {
		this.password = String.valueOf(text);
		changeBtnSignUp();
	}

	@OnCheckedChanged(R.id.checkBox)
	void changeChecked(CompoundButton button, boolean checked){
		changeBtnSignUp();
	}

	@OnClick(R.id.tvSignUp)
	void onSignup(){
		if(isFilledData()){
			hideKeyboard();
			showProgressDialog();
			RequestBody requestBody = new MultipartBody.Builder()
					.setType(MultipartBody.FORM)
					.addFormDataPart("email", email)
					.addFormDataPart("username", username)
					.addFormDataPart("password", password)
					.addFormDataPart("code", code)
					.build();
			ApiClient.getApiV2Service().signUp(requestBody).enqueue(new Callback<UserResponse>() {
				@Override
				public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
					hideProgressDialog();
					if(response.isSuccessful()){
						if(response.body().isSuccess()){
							AppPreference.INSTANCE.setProfile(response.body().getProfile());
							EventBus.getDefault().post(new EventBusListener.ProfileListener());
							Log.d("__signup", "onResponse: " + new Gson().toJson(response.body()));
							goBack();
						}else{
							showAlert("Failed", response.body().getMessage());
						}
					}
				}

				@Override
				public void onFailure(Call<UserResponse> call, Throwable t) {
					hideProgressDialog();
					Log.d("__", "onFailure: " + t.getMessage());
				}
			});
		}
	}

	private boolean isFilledData(){
		if(username.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty()) return false;
		if(!CommonUtil.validateEmail(email)) return false;
		if(!CommonUtil.validatePassword(password)) return false;
		if(!checkBox.isChecked()) return false;
		return true;
	}

	private void changeBtnSignUp() {
		if (isFilledData()) {
			tvSignUp.setBackgroundResource(isDarkTheme ? R.drawable.bg_add_coin_dark : R.drawable.bg_add_coin_light);
			tvSignUp.setTextColor(isDarkTheme ? getColor(R.color.light_text) : getColor(R.color.dark_text));
		} else {
			tvSignUp.setBackgroundResource(isDarkTheme ? R.drawable.bg_border_dark : R.drawable.bg_border_light);
			tvSignUp.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		}
	}

	private SpannableStringBuilder generateLogin() {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		SpannableString string = new SpannableString(getString(R.string.already_sign_up_login));
		String login = "Login";
		ClickableSpan clickSpan = new ClickableSpan() {
			@Override
			public void onClick(View view) {
				goBack();
			}

			@Override
			public void updateDrawState(TextPaint ds) {
				super.updateDrawState(ds);
				ds.setUnderlineText(false);
			}
		};

		string.setSpan(clickSpan, string.toString().indexOf(login), string.toString().indexOf(login) + login.length(), 0);
		string.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), isDarkTheme ? R.color.dark_image : R.color.light_image)), string.toString().indexOf(login), string.toString().indexOf(login) + login.length(), 0);
		builder.append(string);
		return builder;
	}

	private SpannableStringBuilder generateTerm(){
		SpannableStringBuilder builder = new SpannableStringBuilder();
		SpannableString string = new SpannableString(getString(R.string.i_agree_to_vietincome_s_terms_of_use));
		String term = "Terms of Use";
		ClickableSpan clickSpan = new ClickableSpan() {
			@Override
			public void onClick(View view) {
				openLink("https://vietincome.com/terms-of-use/");
			}

			@Override
			public void updateDrawState(TextPaint ds) {
				super.updateDrawState(ds);
				ds.setUnderlineText(false);
			}
		};

		string.setSpan(clickSpan, string.toString().indexOf(term), string.toString().indexOf(term) + term.length(), 0);
		string.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), isDarkTheme ? R.color.dark_image : R.color.light_image)), string.toString().indexOf(term), string.toString().indexOf(term) + term.length(), 0);
		builder.append(string);
		return builder;
	}
}
