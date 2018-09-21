package com.app.vietincome.bases;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.vietincome.R;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.EventBusListener;
import com.app.vietincome.manager.interfaces.AlertListener;
import com.app.vietincome.manager.interfaces.NavigationTopListener;
import com.app.vietincome.view.NavigationTopBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment implements NavigationTopListener {

	public ProgressDialog dialog;
	private static String TAG = BaseFragment.class.getName();
	public boolean isFirstLoad = true;
	public boolean isDarkTheme = true;
	public NavigationTopBar navigationTopBar;

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventUpdatedTheme(EventBusListener.UpdatedTheme event) {
		isDarkTheme = AppPreference.INSTANCE.isDarkTheme();
		navigationTopBar.initTheme(isDarkTheme);
		onUpdatedTheme();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(getLayoutId(), container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.bind(this, view);
		isDarkTheme = AppPreference.INSTANCE.isDarkTheme();
		if (view.findViewById(R.id.topLayout) != null) {
			navigationTopBar = new NavigationTopBar(view, getContext(), this);
			onNavigationTopUpdate(navigationTopBar);
		}
		isFirstLoad = false;
		onFragmentReady(view);
	}

	@Override
	public void onStart() {
		super.onStart();
		registerEventBus();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unRegisterEventBus();
	}

	public abstract int getLayoutId();

	public abstract void onFragmentReady(View view);

	public abstract void onNavigationTopUpdate(NavigationTopBar navitop);

	public abstract void onUpdatedTheme();

	@Override
	public void onLeftClicked() {

	}

	@Override
	public void onRightClicked() {

	}

	@Override
	public void onAdditionRightClicked(){

	}

	@Override
	public void onSearchChanged(String key){

	}

	@Override
	public void onSearchDone(){

	}

	@Override
	public void onCloseSearch(){

	}

	public static void hideSoftKeyboard(Activity activity) {
		if (activity != null) {
			try {
				((InputMethodManager) Objects.requireNonNull(activity.getSystemService(Context.INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
			} catch (Exception e) {
				Log.d(TAG, "hideSoftKeyboard: " + e.getMessage());
			}
		}
	}

	public void showProgressDialog(Activity activity) {
		try {
			hideProgressDialog();
			dialog = new ProgressDialog(activity);
			dialog.setCancelable(false);
			Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			dialog.getWindow().setDimAmount(0.5f);
			dialog.show();
			dialog.setContentView(R.layout.processing);
		} catch (Exception e) {
			Log.d(TAG, "showProgressDialog: " + e.getMessage());
		}
	}

	public void showProgressDialog() {
		showProgressDialog(getActivity());
	}

	public void hideProgressDialog() {
		if (dialog != null && dialog.isShowing())
			dialog.dismiss();
	}

	protected void runOnUiThread(Runnable runnable) {
		if (getActivity() == null || !isAdded()) {
			return;
		}
		getActivity().runOnUiThread(runnable);
	}

	public boolean isShowProgressDialog() {
		return dialog != null && dialog.isShowing();
	}

	protected void showAlert(final String title, final String message, final AlertListener listener) {
		runOnUiThread(() ->
				new AlertDialog.Builder(Objects.requireNonNull(getActivity()), R.style.AlertDialogTheme)
						.setTitle(title)
						.setMessage(message)
						.setCancelable(false)
						.setNegativeButton(getString(R.string.ok), (dialogInterface, i) -> {
							if (listener != null)
								listener.onClickedOk();
						}).create()
						.show());
	}

	protected void showAlert(final String title, final String message, final String yesTitle, final String noTitle, final AlertListener listener) {
		runOnUiThread(() ->
				new AlertDialog.Builder(Objects.requireNonNull(getActivity()), R.style.AlertDialogTheme)
						.setTitle(title)
						.setMessage(message)
						.setCancelable(false)
						.setPositiveButton(yesTitle, (dialogInterface, i) -> {
							if (listener != null) {
								listener.onClickedOk();
							}
						})
						.setNegativeButton(noTitle, (dialogInterface, i) -> {
							if (listener != null) {
								listener.onClickedCancel();
							}
						}).create()
						.show());
	}

	protected void showAlert(final String title, final String message) {
		runOnUiThread(() ->
				new AlertDialog.Builder(Objects.requireNonNull(getActivity()), R.style.AlertDialogTheme)
						.setTitle(title)
						.setMessage(message)
						.setCancelable(false)
						.setNegativeButton(getString(R.string.ok), null).create()
						.show());
	}


	/**
	 * Define content alert in fragment
	 *
	 * @param titleResId
	 * @param messageResId
	 */
	protected void showAlert(@StringRes final int titleResId, @StringRes final int messageResId) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
						.setTitle(titleResId)
						.setMessage(messageResId)
						.setCancelable(false)
						.setNegativeButton(getString(R.string.ok), null).create()
						.show();
			}
		});
	}

	public void showToast(String message) {
		Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
	}

	public void showToast(@StringRes final int messageResId) {
		Toast.makeText(getContext(), messageResId, Toast.LENGTH_SHORT).show();
	}

	protected void showKeyboard(View target) {
		if (target == null) {
			return;
		}
		target.requestFocus();
		((InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(target,
				InputMethodManager.SHOW_IMPLICIT);
		if (target instanceof EditText)
			((EditText) target).setSelection(((EditText) target).getText().length());
	}

	protected void hideKeyboard() {
		View view = Objects.requireNonNull(getActivity()).getCurrentFocus();
		if (view != null) {
			((InputMethodManager) Objects.requireNonNull(getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(view.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
			view.clearFocus();
		}
	}

	public void setupUI(View view) {

		//Set up touch listener for non-text box views to hide keyboard.
		if (!(view instanceof EditText)) {

			view.setOnTouchListener((v, event) -> {
				hideKeyboard();
				return false;
			});
		}

		//If a layout container, iterate over children and seed recursion.
		if (view instanceof ViewGroup) {

			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

				View innerView = ((ViewGroup) view).getChildAt(i);

				setupUI(innerView);
			}
		}
	}

	protected void goBack() {
		Activity activity = getActivity();
		if (activity != null && activity instanceof BaseActivity && isAdded()) {
			((BaseActivity) getActivity()).popFragment();
		}
	}


	public void pushFragment(Fragment fragment) {
		((BaseActivity) getActivity()).pushFragment(fragment);
	}

	public void pushFragment(Fragment fragment, int animateIn, int animateOut) {
		((BaseActivity) getActivity()).pushFragment(fragment, animateIn, animateOut);
	}

	public void replaceTopFragment(Fragment fragment) {
		((BaseActivity) getActivity()).replaceTopFragment(fragment);
	}

	public void replaceTopFragment(Fragment fragment, int animateIn, int animateOut) {
		((BaseActivity) getActivity()).replaceTopFragment(fragment, animateIn, animateOut);
	}

	public void popToRootFragment() {
		((BaseActivity) getActivity()).popToRootFragment();
	}

	public void popFragment() {
		((BaseActivity) getActivity()).popFragment();
	}

	public void popFragment(int depth) {
		((BaseActivity) getActivity()).popFragment( depth);
	}

	public String[] getStringArray(@ArrayRes int array) {
		return getContext().getResources().getStringArray(array);
	}

	public void registerEventBus() {
		if (!EventBus.getDefault().isRegistered(this))
			EventBus.getDefault().register(this);
	}

	public void unRegisterEventBus() {
		if (EventBus.getDefault().isRegistered(this))
			EventBus.getDefault().unregister(this);
	}

	public boolean checkPermission(String permission) {
		return ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), permission) == PackageManager.PERMISSION_GRANTED;
	}

	public void requestPermisson(String[] perssions, int key) {
		requestPermissions(perssions, key);
	}

	public boolean isEnabledPermisson(String[] permissions, int key) {
		for (String permission : permissions) {
			if (!checkPermission(permission)) {
				requestPermisson(permissions, key);
				return false;
			}
		}
		return true;
	}

	public void setSoftInputMode(int type) {
		getActivity().getWindow().setSoftInputMode(type);
	}


	public int getColor(int color){
		return ContextCompat.getColor(getContext(), color);
	}

	public void setTextColor(TextView textView){
		textView.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
	}

}
