package com.app.vietincome.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.activity.ParentActivity;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.EventBusListener;
import com.app.vietincome.model.Profile;
import com.app.vietincome.model.responses.BaseResponse;
import com.app.vietincome.model.responses.UserResponse;
import com.app.vietincome.network.ApiClient;
import com.app.vietincome.utils.Constant;
import com.app.vietincome.utils.GlideImage;
import com.app.vietincome.view.HighLightTextView;
import com.app.vietincome.view.NavigationTopBar;
import com.github.siyamed.shapeimageview.CircularImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends BaseFragment {

	@BindView(R.id.layoutRoot)
	LinearLayout layoutRoot;

	@BindView(R.id.imgAvatar)
	CircleImageView imgAvatar;

	@BindView(R.id.tvUsername)
	TextView tvUsername;

	@BindView(R.id.tvAccountType)
	TextView tvAccountType;

	@BindView(R.id.imgNext)
	ImageView imgNext;

	@BindView(R.id.tvWallet)
	TextView tvWallet;

	@BindView(R.id.tvWalletValue)
	TextView tvWalletValue;

	@BindView(R.id.tvInviteFriend)
	HighLightTextView tvInviteFriend;

	@BindView(R.id.tvBecomeVip)
	HighLightTextView tvBecomeVip;

	@BindView(R.id.tvSignal)
	HighLightTextView tvSignal;

	@BindView(R.id.tvChangePass)
	HighLightTextView tvChangePassword;

	@BindView(R.id.tvLogout)
	HighLightTextView tvLogout;

	@BindView(R.id.view1)
	View view1;

	@BindView(R.id.view2)
	View view2;

	@BindView(R.id.view3)
	View view3;

	private boolean isNeedUpdate;
	private Profile profile;

	public static ProfileFragment newInstance(boolean isNeedUpdate) {
		ProfileFragment fragment = new ProfileFragment();
		fragment.isNeedUpdate = isNeedUpdate;
		return fragment;
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventProfile(EventBusListener.ProfileListener event){
		initData();
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_profile;
	}

	@Override
	public void onFragmentReady(View view) {
		onUpdatedTheme();
		initData();
		if(isNeedUpdate){
			getProfile();
		}
	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
		navitop.showImgRight(true);
		navitop.setImgRight(R.drawable.settings);
		navitop.showImgLeft(false);
		navitop.setTvTitle("My profile");
	}

	@Override
	public void onRightClicked() {
		super.onRightClicked();
		openScreen(Constant.SETTING);
	}

	@Override
	public void onUpdatedTheme() {
		setTextColor(tvUsername);
		setTextColor(tvAccountType);
		setTextColor(tvWallet);
		setTextColor(tvWalletValue);
		setTextColor(tvInviteFriend);
		setTextColor(tvBecomeVip);
		setTextColor(tvSignal);
		setTextColor(tvChangePassword);
		setTextColor(tvLogout);
		imgNext.setColorFilter(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		view1.setBackgroundColor(isDarkTheme ? getColor(R.color.black_background) : getColor(R.color.gray_background));
		view2.setBackgroundColor(isDarkTheme ? getColor(R.color.black_background) : getColor(R.color.gray_background));
		view3.setBackgroundColor(isDarkTheme ? getColor(R.color.black_background) : getColor(R.color.gray_background));
		setTextViewDrawableColor(tvInviteFriend);
		setTextViewDrawableColor(tvBecomeVip);
		setTextViewDrawableColor(tvSignal);
		setTextViewDrawableColor(tvChangePassword);
	}

	private void initData() {
		profile = AppPreference.INSTANCE.getProfile();
		if (profile == null) return;
		GlideImage.loadImage(profile.getAvatar(), R.drawable.avatar_default, imgAvatar);
		tvUsername.setText(profile.getName());
		tvWalletValue.setText(profile.getVic());
		tvAccountType.setText(profile.getVip());
	}

	@OnClick({R.id.layoutProfile})
	void onEditProfile() {
		openScreen(Constant.EDIT_PROFILE);
	}

	@OnClick(R.id.tvChangePass)
	void onChangePassword() {
		openScreen(Constant.CHANGE_PASSWORD);
	}

	@OnClick(R.id.tvInviteFriend)
	void onInviteFriend() {
		openScreen(Constant.INVITE_FRIEND);
	}

	@OnClick(R.id.tvBecomeVip)
	void onBecomeVip() {
		openScreen(Constant.BECOME_VIP);
	}

	@OnClick(R.id.tvLogout)
	void onLogout() {
		showProgressDialog();
		ApiClient.getApiV2Service().logout().enqueue(new Callback<BaseResponse>() {
			@Override
			public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
				hideProgressDialog();
				if (response.isSuccessful()) {
					AppPreference.INSTANCE.setProfile(null);
					EventBus.getDefault().post(new EventBusListener.ProfileListener());
				}
			}

			@Override
			public void onFailure(Call<BaseResponse> call, Throwable t) {
				hideProgressDialog();
				showAlert("Failed", t.getMessage());
			}
		});
	}

	@OnClick(R.id.tvSignal)
	void onSignal(){
		if(profile.isVip()){
			showToast("Comming soon");
		}else{
			showToast("You aren't VIP");
		}
	}

	private void getProfile(){
		navigationTopBar.showProgressBar();
		ApiClient.getApiV2Service().getProfile().enqueue(new Callback<UserResponse>() {
			@Override
			public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
				navigationTopBar.hideProgressBar();
				if(response.isSuccessful()){
					if(response.body().isSuccess()){
						AppPreference.INSTANCE.updateProfile(response.body().getProfile());
						initData();
					}else{
						showAlert("Failed", response.body().getMessage());
						if(response.body().isExpired()){
							AppPreference.INSTANCE.setProfile(null);
							EventBus.getDefault().post(new EventBusListener.ProfileListener());
						}
					}
				}
			}

			@Override
			public void onFailure(Call<UserResponse> call, Throwable t) {
				navigationTopBar.hideProgressBar();
			}
		});
	}

	private void setTextViewDrawableColor(HighLightTextView textView) {
		Drawable right = ContextCompat.getDrawable(getContext(), R.drawable.next);
		right = DrawableCompat.wrap(right);
		DrawableCompat.setTint(right.mutate(), isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
		textView.setCompoundDrawables(null, null, right, null);
	}

	private void openScreen(int screen) {
		Intent parent = new Intent(getContext(), ParentActivity.class);
		parent.putExtra(Constant.KEY_SCREEN, screen);
		startActivity(parent);
	}
}
