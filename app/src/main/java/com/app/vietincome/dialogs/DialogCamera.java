package com.app.vietincome.dialogs;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.app.vietincome.R;
import com.app.vietincome.bases.BaseDialogFragment;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.interfaces.SelectPickupImageListener;
import com.app.vietincome.view.HighLightTextView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import butterknife.BindView;
import butterknife.OnClick;

public class DialogCamera extends BaseDialogFragment {

	@BindView(R.id.layoutRoot)
	LinearLayout layoutRoot;

	@BindView(R.id.tvCamera)
	HighLightTextView tvCamera;

	@BindView(R.id.tvGallery)
	HighLightTextView tvGallery;

	private boolean isDarkTheme = AppPreference.INSTANCE.isDarkTheme();
	private SelectPickupImageListener listener;

	public static DialogCamera newInstance(SelectPickupImageListener listener){
		DialogCamera dialogCamera = new DialogCamera();
		dialogCamera.listener = listener;
		return dialogCamera;
	}

	@Override
	public void onStart() {
		super.onStart();
		Dialog dialog = getDialog();
		if (dialog != null) {
			dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setAttribute(dialog, true, R.style.ZoomDialogAnimation);
		return dialog;
	}

	@Override
	public int getLayoutId() {
		return R.layout.dialog_camera;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		layoutRoot.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
		tvCamera.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		tvGallery.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
	}

	@OnClick(R.id.tvCamera)
	void onSelectCamera(){
		if(listener != null){
			listener.onSelectCamera();
			getDialog().dismiss();
		}
	}

	@OnClick(R.id.tvGallery)
	void onSelectGallery(){
		if(listener != null){
			listener.onSelectGallery();
			getDialog().dismiss();
		}
	}
}
