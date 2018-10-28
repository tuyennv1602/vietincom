package com.app.vietincome.fragment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.EventBusListener;
import com.app.vietincome.model.Profile;
import com.app.vietincome.model.responses.BaseResponse;
import com.app.vietincome.network.ApiClient;
import com.app.vietincome.utils.Constant;
import com.app.vietincome.view.HighLightLinearLayout;
import com.app.vietincome.view.HighLightTextView;
import com.app.vietincome.view.NavigationTopBar;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BecomeVipFragment extends BaseFragment {

	@BindView(R.id.layoutRoot)
	LinearLayout layoutRoot;

	@BindView(R.id.layoutActive1)
	HighLightLinearLayout layoutActive1;

	@BindView(R.id.tvVip1)
	TextView tvVip1;

	@BindView(R.id.tvPrice1)
	TextView tvPrice1;

	@BindView(R.id.tvActive1)
	HighLightTextView tvActive1;

	@BindView(R.id.layoutActive2)
	HighLightLinearLayout layoutActive2;

	@BindView(R.id.tvVip2)
	TextView tvVip2;

	@BindView(R.id.tvPrice2)
	TextView tvPrice2;

	@BindView(R.id.tvActive2)
	HighLightTextView tvActive2;

	@BindView(R.id.layoutActive3)
	HighLightLinearLayout layoutActive3;

	@BindView(R.id.tvVip3)
	TextView tvVip3;

	@BindView(R.id.tvPrice3)
	TextView tvPrice3;

	@BindView(R.id.tvActive3)
	HighLightTextView tvActive3;

	@BindView(R.id.tvNote)
	TextView tvNote;

	@BindView(R.id.tvNoteContent)
	TextView tvNoteContent;

	@Override
	public int getLayoutId() {
		return R.layout.fragment_becomevip;
	}

	@Override
	public void onFragmentReady(View view) {
		onUpdatedTheme();
	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
		navitop.showImgRight(false);
		navitop.setImgLeft(R.drawable.back);
		navitop.setTvTitle(R.string.become_vip);
	}

	@Override
	public void onLeftClicked() {
		super.onLeftClicked();
		goBack();
	}

	@Override
	public void onUpdatedTheme() {
		layoutRoot.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
		setTextColor(tvVip1);
		setTextColor(tvPrice1);
		setTextColor(tvVip2);
		setTextColor(tvPrice2);
		setTextColor(tvVip3);
		setTextColor(tvPrice3);
		setTextColor(tvNote);
		setTextColor(tvNoteContent);
		tvActive1.setBackgroundResource(isDarkTheme ? R.drawable.bg_circle_dark : R.drawable.bg_circle_light);
		tvActive1.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		tvActive2.setBackgroundResource(isDarkTheme ? R.drawable.bg_circle_dark : R.drawable.bg_circle_light);
		tvActive2.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		tvActive3.setBackgroundResource(isDarkTheme ? R.drawable.bg_circle_dark : R.drawable.bg_circle_light);
		tvActive3.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		layoutActive1.setBackgroundResource(isDarkTheme ? R.drawable.bg_border_dark : R.drawable.bg_border_light);
		layoutActive2.setBackgroundResource(isDarkTheme ? R.drawable.bg_border_dark : R.drawable.bg_border_light);
		layoutActive3.setBackgroundResource(isDarkTheme ? R.drawable.bg_border_dark : R.drawable.bg_border_light);
	}

	@OnClick(R.id.tvActive1)
	void activeVip1(){
		activeVip(Constant.VIP1);
	}

	@OnClick(R.id.tvActive2)
	void activeVip2(){
		activeVip(Constant.VIP2);
	}

	@OnClick(R.id.tvActive3)
	void activeVip3(){
		activeVip(Constant.VIP3);
	}

	private void activeVip(int vipType){
		showProgressDialog();
		RequestBody requestBody = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("vip_id", String.valueOf(vipType))
				.build();
		ApiClient.getApiV2Service().activeVip(requestBody).enqueue(new Callback<BaseResponse>() {
			@Override
			public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
				hideProgressDialog();
				if(response.isSuccessful()){
					if(response.body().isSuccess()){
						showToast("Actived VIP" + vipType );
						Profile profile = AppPreference.INSTANCE.getProfile();
						profile.setVip(vipType);
						AppPreference.INSTANCE.updateProfile(profile);
						EventBus.getDefault().post(new EventBusListener.ProfileListener());
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
