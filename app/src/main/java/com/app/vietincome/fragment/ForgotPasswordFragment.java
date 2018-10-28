package com.app.vietincome.fragment;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.model.responses.BaseResponse;
import com.app.vietincome.network.ApiClient;
import com.app.vietincome.utils.CommonUtil;
import com.app.vietincome.view.HighLightTextView;
import com.app.vietincome.view.NavigationTopBar;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordFragment extends BaseFragment {

	@BindView(R.id.layoutRoot)
	LinearLayout layoutRoot;

	@BindView(R.id.tvInstruction)
	TextView tvInstruction;

	@BindView(R.id.tvEmail)
	TextView tvEmail;

	@BindView(R.id.edtEmail)
	EditText edtEmail;

	@BindView(R.id.tvSubmit)
	HighLightTextView tvSubmit;

	private String email = "";

	@Override
	public int getLayoutId() {
		return R.layout.fragment_forgot_password;
	}

	@Override
	public void onFragmentReady(View view) {
		onUpdatedTheme();
	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
		navitop.showImgLeft(true);
		navitop.showImgRight(false);
		navitop.setImgLeft(R.drawable.back);
		navitop.setTvTitle("Reset Password");
	}

	@Override
	public void onLeftClicked() {
		super.onLeftClicked();
		goBack();
	}

	@Override
	public void onUpdatedTheme() {
		layoutRoot.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
		setTextColor(tvInstruction);
		setTextColor(tvEmail);
		edtEmail.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		edtEmail.setBackgroundResource(isDarkTheme ? R.drawable.bg_border_dark : R.drawable.bg_border_light);
		changeBtnSubmit();
	}

	@OnTextChanged(R.id.edtEmail)
	void changeEmail(CharSequence text) {
		this.email = String.valueOf(text);
		changeBtnSubmit();
	}

	@OnClick(R.id.tvSubmit)
	void onSubmit() {
		if (isFilledData()) {
			hideKeyboard();
			showProgressDialog();
			RequestBody requestBody = new MultipartBody.Builder()
					.setType(MultipartBody.FORM)
					.addFormDataPart("email", email)
					.build();
			ApiClient.getApiV2Service().forgotPassword(requestBody).enqueue(new Callback<BaseResponse>() {
				@Override
				public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
					hideProgressDialog();
					if (response.isSuccessful()) {
						if (response.body().isSuccess()) {
							showToast("Success!");
							goBack();
						} else {
							showAlert("Failed", response.body().getMessage());
						}
					}
				}

				@Override
				public void onFailure(Call<BaseResponse> call, Throwable t) {
					hideProgressDialog();
					Log.d("__", "onFailure: " + t.getMessage());
				}
			});
		}
	}

	private boolean isFilledData() {
		return !email.trim().isEmpty() && CommonUtil.validateEmail(email);
	}

	private void changeBtnSubmit() {
		if (isFilledData()) {
			tvSubmit.setBackgroundResource(isDarkTheme ? R.drawable.bg_add_coin_dark : R.drawable.bg_add_coin_light);
			tvSubmit.setTextColor(isDarkTheme ? getColor(R.color.light_text) : getColor(R.color.dark_text));
		} else {
			tvSubmit.setBackgroundResource(isDarkTheme ? R.drawable.bg_border_dark : R.drawable.bg_border_light);
			tvSubmit.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		}
	}
}
