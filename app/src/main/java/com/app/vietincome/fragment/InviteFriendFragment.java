package com.app.vietincome.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.view.HighLightTextView;
import com.app.vietincome.view.NavigationTopBar;

import butterknife.BindView;
import butterknife.OnClick;

public class InviteFriendFragment extends BaseFragment {

	@BindView(R.id.layoutRoot)
	LinearLayout layoutRoot;

	@BindView(R.id.tvInstruction)
	TextView tvInstruction;

	@BindView(R.id.tvRefCode)
	TextView tvRefCode;

	@BindView(R.id.tvRefCodeValue)
	TextView tvRefCodeValue;

	@BindView(R.id.tvCopyCode)
	HighLightTextView tvCopyCode;

	@BindView(R.id.layoutCode)
	LinearLayout layoutCode;

	@Override
	public int getLayoutId() {
		return R.layout.fragment_invite_friend;
	}

	@Override
	public void onFragmentReady(View view) {
		onUpdatedTheme();
		tvRefCodeValue.setText(AppPreference.INSTANCE.getProfile().getInviteCode());
	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
		navitop.showImgRight(false);
		navitop.setImgLeft(R.drawable.back);
		navitop.setTvTitle(R.string.invite_friend);
	}

	@Override
	public void onLeftClicked() {
		super.onLeftClicked();
		goBack();
	}

	@Override
	public void onUpdatedTheme() {
		layoutRoot.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
		setTextColor(tvRefCode);
		setTextColor(tvRefCodeValue);
		setTextColor(tvInstruction);
		tvCopyCode.setBackgroundResource(isDarkTheme ? R.drawable.bg_border_dark : R.drawable.bg_border_light);
		tvCopyCode.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		layoutCode.setBackgroundResource(isDarkTheme ? R.drawable.bg_border_dark : R.drawable.bg_border_light);
	}

	@OnClick(R.id.tvCopyCode)
	void onCopyCode(){
		ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clip = ClipData.newPlainText("invite", AppPreference.INSTANCE.getProfile().getInviteCode());
		clipboard.setPrimaryClip(clip);
		showToast("Copied invite code!");
	}
}
