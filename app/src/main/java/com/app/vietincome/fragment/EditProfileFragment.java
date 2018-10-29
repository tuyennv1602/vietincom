package com.app.vietincome.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.dialogs.DialogCamera;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.EventBusListener;
import com.app.vietincome.manager.interfaces.SelectPickupImageListener;
import com.app.vietincome.model.Profile;
import com.app.vietincome.model.responses.AvatarResponse;
import com.app.vietincome.model.responses.BaseResponse;
import com.app.vietincome.network.ApiClient;
import com.app.vietincome.utils.Constant;
import com.app.vietincome.utils.FileUtil;
import com.app.vietincome.utils.GlideImage;
import com.app.vietincome.view.HighLightTextView;
import com.app.vietincome.view.NavigationTopBar;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends BaseFragment implements SelectPickupImageListener {

	@BindView(R.id.imgAvatar)
	CircleImageView imgAvatar;

	@BindView(R.id.tvChangeAvatar)
	HighLightTextView tvChangeAvatar;

	@BindView(R.id.view)
	View view;

	@BindView(R.id.tvName)
	TextView tvName;

	@BindView(R.id.tvUsername)
	TextView tvUserName;

	@BindView(R.id.tvBio)
	TextView tvBio;

	@BindView(R.id.edtBio)
	EditText edtBio;

	@BindView(R.id.tvSave)
	HighLightTextView tvSave;

	@BindView(R.id.layoutRoot)
	LinearLayout layoutRoot;

	private final int MAX_SIZE = 1440;
	private File picture;
	private String bio = "";
	private String username = "";
	Profile profile = AppPreference.INSTANCE.getProfile();

	@Override
	public int getLayoutId() {
		return R.layout.fragment_edit_profile;
	}

	@Override
	public void onFragmentReady(View view) {
		onUpdatedTheme();
		initData();
	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
		navitop.setImgLeft(R.drawable.back);
		navitop.showImgRight(false);
		navitop.setTvTitle("Edit profile");
	}

	@Override
	public void onLeftClicked() {
		super.onLeftClicked();
		goBack();
	}

	@Override
	public void onUpdatedTheme() {
		layoutRoot.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
		view.setBackgroundColor(isDarkTheme ? getColor(R.color.black_background) : getColor(R.color.gray_background));
		tvChangeAvatar.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		setTextColor(tvName);
		setTextColor(tvBio);
		edtBio.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		tvUserName.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
		changeBtnSave();
	}

	private void initData() {
		this.bio = profile.getBio();
		this.username = profile.getName();
		tvUserName.setText(profile.getName());
		if (profile.getBio() != null) {
			edtBio.setText(profile.getBio());
		}
		edtBio.setSelection(edtBio.length());
		GlideImage.loadImage(profile.getAvatar(), R.drawable.avatar_default, imgAvatar);
	}

	public void openCamera() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
			try {
				picture = FileUtil.createTempFile(getActivity());
			} catch (IOException ex) {
				// Error occurred while creating the File
			}
			// Continue only if the File was successfully created
			if (picture != null) {
				Uri photoURI = FileProvider.getUriForFile(getContext(),
						"com.app.vietincome",
						picture);
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
				startActivityForResult(takePictureIntent, Constant.REQUEST_CAPTURE_PHOTO);
			}
		}
	}

	public void openGallery() {
		Intent galleryPhoto = new Intent(Intent.ACTION_PICK);
		galleryPhoto.setType("image/*");
		if (getActivity() != null)
			if (galleryPhoto.resolveActivity(getActivity().getPackageManager()) != null) {
				startActivityForResult(galleryPhoto, Constant.REQUEST_GALLERY_PHOTO);
			}
	}

	public void showDialogTakePhoto() {
		hideKeyboard();
		if (getContext() == null)
			return;
		DialogCamera dialogCamera = DialogCamera.newInstance(this);
		dialogCamera.show(getFragmentManager(), "camera");
	}

	@Override
	public void onSelectCamera() {
		if (isEnabledPermisson(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.PERMISSIONS_CAMERA)) {
			openCamera();
		}
	}

	@Override
	public void onSelectGallery() {
		if (isEnabledPermisson(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.READ_EXTERNAL_PERMISSION)) {
			openGallery();
		}
	}

	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case Constant.PERMISSIONS_CAMERA:
				if (grantResults.length > 0) {
					if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
						openCamera();
					}
				}
				break;
			case Constant.READ_EXTERNAL_PERMISSION:
				if (grantResults.length > 0) {
					if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
						openGallery();
					}
				}
				break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (getContext() == null)
			return;
		if (resultCode == -1) {
			switch (requestCode) {
				case Constant.REQUEST_CAPTURE_PHOTO:
					File file = FileUtil.convertToJPEGImageFile(getActivity(), getRightAngleImage(picture.getAbsolutePath()));
					launchCropOval(Uri.fromFile(file));
					break;
				case Constant.REQUEST_GALLERY_PHOTO:
					launchCropOval(data.getData());
					break;
				case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
					CropImage.ActivityResult result = CropImage.getActivityResult(data);
					uploadImage(new File(result.getUri().getPath()));
					break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@OnClick(R.id.tvChangeAvatar)
	void onChangeAvatar() {
		showDialogTakePhoto();
	}

	@OnTextChanged(R.id.edtBio)
	void changeBio(CharSequence text) {
		this.bio = String.valueOf(text);
		changeBtnSave();
	}

	@OnClick(R.id.tvSave)
	void onSaveChange() {
		if (isFilledData()) {
			hideKeyboard();
			showProgressDialog();
			RequestBody requestBody = new MultipartBody.Builder()
					.setType(MultipartBody.FORM)
					.addFormDataPart("name", username)
					.addFormDataPart("bio", bio)
					.build();
			ApiClient.getApiV2Service().editProfile(requestBody).enqueue(new Callback<BaseResponse>() {
				@Override
				public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
					hideProgressDialog();
					if (response.isSuccessful()) {
						if (response.body().isSuccess()) {
							showToast("Success");
							Profile profile = AppPreference.INSTANCE.getProfile();
							profile.setName(username);
							profile.setBio(bio);
							AppPreference.INSTANCE.updateProfile(profile);
							EventBus.getDefault().post(new EventBusListener.ProfileListener());
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
		return !bio.trim().isEmpty() && !bio.trim().equals(profile.getBio());
	}

	private void changeBtnSave() {
		if (isFilledData()) {
			tvSave.setBackgroundResource(isDarkTheme ? R.drawable.bg_add_coin_dark : R.drawable.bg_add_coin_light);
			tvSave.setTextColor(isDarkTheme ? getColor(R.color.light_text) : getColor(R.color.dark_text));
		} else {
			tvSave.setBackgroundResource(isDarkTheme ? R.drawable.bg_border_dark : R.drawable.bg_border_light);
			tvSave.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		}
	}

	private void uploadImage(File file) {
		if (file == null) return;
		GlideImage.loadImage(file, imgAvatar);
		showProgressDialog();
		MultipartBody.Part partBody =FileUtil.createFileBody(compressFile(file), "file");
		ApiClient.getApiV2Service().uploadAvatar(partBody).enqueue(new Callback<AvatarResponse>() {
			@Override
			public void onResponse(Call<AvatarResponse> call, Response<AvatarResponse> response) {
				hideProgressDialog();
				if(response.isSuccessful()){
					if(response.body().isSuccess()){
						showToast("Success");
						Profile profile = AppPreference.INSTANCE.getProfile();
						profile.setAvatar(response.body().getAvatar().getFileName());
						AppPreference.INSTANCE.updateProfile(profile);
						EventBus.getDefault().post(new EventBusListener.ProfileListener());
					}else{
						showAlert("Failed", response.body().getMessage());
					}
				}
			}

			@Override
			public void onFailure(Call<AvatarResponse> call, Throwable t) {
				hideProgressDialog();
				Log.d("__", "onFailure: " + t.getMessage());
			}
		});
	}

	private void launchCropOval(Uri uri) {
		if (getContext() == null)
			return;
		CropImage.activity(uri)
				.setGuidelines(CropImageView.Guidelines.OFF)
				.setCropShape(CropImageView.CropShape.OVAL)
				.setAspectRatio(5, 5)
				.start(getContext(), this);
	}

	public File compressFile(File file) {
		if (file == null)
			return null;
		if (getContext() == null)
			return null;
		try {
			return new Compressor(getContext())
					.setMaxHeight(500)
					.setMaxWidth(500)
					.setQuality(90)
					.compressToFile(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	private Bitmap getRightAngleImage(String photoPath) {
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(photoPath, bmOptions);
		int srcWidth = bmOptions.outWidth;
		int srcHeight = bmOptions.outHeight;
		if (srcWidth > srcHeight) {
			bmOptions.inDensity = srcWidth;
		} else {
			bmOptions.inDensity = srcHeight;
		}
		bmOptions.inScaled = true;
		bmOptions.inTargetDensity = MAX_SIZE;
		bmOptions.inJustDecodeBounds = false;
		try {
			InputStream input = getActivity().getContentResolver().openInputStream(Uri.fromFile(picture));
			ExifInterface ei;
			if (Build.VERSION.SDK_INT > 23)
				ei = new ExifInterface(input);
			else
				ei = new ExifInterface(photoPath);
			int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			int degree;
			switch (orientation) {
				case ExifInterface.ORIENTATION_NORMAL:
					degree = 0;
					break;
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
//				case ExifInterface.ORIENTATION_UNDEFINED:
//					degree = 0;
//					break;
				default:
					degree = 90;
			}
			Log.d("__cam", "getRightAngleImage: " + degree);
			Bitmap b = BitmapFactory.decodeFile(photoPath, bmOptions);
			if (degree <= 0) {
				return b;
			}
			Matrix matrix = new Matrix();
			if (b.getWidth() > b.getHeight()) {
				matrix.setRotate(degree);
				b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
						matrix, true);
			}
			return b;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
