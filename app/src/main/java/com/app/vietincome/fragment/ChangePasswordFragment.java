package com.app.vietincome.fragment;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.EventBusListener;
import com.app.vietincome.model.responses.BaseResponse;
import com.app.vietincome.network.ApiClient;
import com.app.vietincome.utils.CommonUtil;
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

public class ChangePasswordFragment extends BaseFragment {

	@BindView(R.id.layoutRoot)
	LinearLayout layoutRoot;

	@BindView(R.id.tvOldPassword)
	TextView tvOldPassword;

	@BindView(R.id.edtOldPassword)
	EditText edtOldPassword;

	@BindView(R.id.tvNewPassword)
	TextView tvNewPassword;

	@BindView(R.id.edtNewPassword)
	EditText edtNewPassword;

	@BindView(R.id.tvSubmit)
	HighLightTextView tvSubmit;

	private String oldPass = "";
	private String newPass = "";

	@Override
	public int getLayoutId() {
		return R.layout.fragment_change_password;
	}

	@Override
	public void onFragmentReady(View view) {
		onUpdatedTheme();
	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
		navitop.showImgRight(false);
		navitop.setImgLeft(R.drawable.back);
		navitop.setTvTitle(R.string.change_password);
	}

	@Override
	public void onLeftClicked() {
		super.onLeftClicked();
		goBack();
	}

	@Override
	public void onUpdatedTheme() {
		layoutRoot.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
		setTextColor(tvOldPassword);
		setTextColor(tvNewPassword);
		edtOldPassword.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		edtOldPassword.setBackgroundResource(isDarkTheme ? R.drawable.bg_border_dark : R.drawable.bg_border_light);
		edtNewPassword.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		edtNewPassword.setBackgroundResource(isDarkTheme ? R.drawable.bg_border_dark : R.drawable.bg_border_light);
		changeBtnSubmit();
	}

	@OnTextChanged(R.id.edtOldPassword)
	void changeOldPass(CharSequence text){
		this.oldPass = String.valueOf(text);
		changeBtnSubmit();
	}

	@OnTextChanged(R.id.edtNewPassword)
	void changeNewPass(CharSequence text){
		this.newPass = String.valueOf(text);
		changeBtnSubmit();
	}

	@OnClick(R.id.tvSubmit)
	void onSubmit(){
		if(isFilledData()){
			hideKeyboard();
			showProgressDialog();
			RequestBody requestBody = new MultipartBody.Builder()
					.setType(MultipartBody.FORM)
					.addFormDataPart("oldpassword", oldPass)
					.addFormDataPart("newpassword", newPass)
					.build();
			ApiClient.getApiV2Service().changePassword(requestBody).enqueue(new Callback<BaseResponse>() {
				@Override
				public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
					hideProgressDialog();
					if(response.isSuccessful()){
						if(response.body().isSuccess()){
							showToast("Success");
							goBack();
						}else{
							showAlert("Failed", response.body().getMessage());
						}
					}
				}

				@Override
				public void onFailure(Call<BaseResponse> call, Throwable t) {
					hideProgressDialog();
					showAlert("Failed", t.getMessage());
				}
			});
		}
	}

	private boolean isFilledData() {
		return !oldPass.trim().isEmpty()
				&& CommonUtil.validatePassword(oldPass)
				&& !newPass.trim().isEmpty()
				&& CommonUtil.validatePassword(newPass);
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
