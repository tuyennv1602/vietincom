package com.app.vietincome.bases;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.app.vietincome.R;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.interfaces.AlertListener;
import com.app.vietincome.manager.interfaces.BackPressListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Objects;
import java.util.Stack;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

	private static final String BUNDLE_KEY_TOP_FRAGMENT = "BaseActivity.TopFragment";

	protected static String TAG = BaseActivity.class.getName();
	private boolean canFinish;
	public BackPressListener backPressListener;
	public boolean isBackPress;
	private Stack<Fragment> backStack;
	private ProgressDialog dialog;
	public boolean isDarkTheme = AppPreference.INSTANCE.isDarkTheme();

	public Stack<Fragment> getBackStack() {
		return backStack;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initStack();
		setContentView(getLayoutId());
		ButterKnife.bind(this);
		setTheme();
		changeStatusBar();
		onViewCreated(getWindow().getDecorView().getRootView());
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (!backStack.isEmpty()) {
			Fragment fragment = backStack.peek();
			outState.putString(BUNDLE_KEY_TOP_FRAGMENT, fragment.getClass().getName());
		}
	}

	public abstract int animationIn();

	public abstract int animationOut();

	public abstract void initStack();

	public abstract int getLayoutId();

	public abstract void setTheme();

	public abstract void onViewCreated(View view);

	public void initData(Stack<Fragment> backStack) {
		this.backStack = backStack;
	}

	protected void onPause() {
		super.onPause();
		hideSoftKeyboard(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void finish() {
		super.finish();
		overridePendingTransition(animationIn(), animationOut());
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

	public void showProgressDialog(Context context) {
		try {
			hideProgressDialog();
			dialog = new ProgressDialog(context);
			dialog.setCancelable(false);
			Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			dialog.getWindow().setDimAmount(0.5f);
			dialog.show();
			dialog.setContentView(R.layout.processing);
		} catch (Exception e) {
			Log.d(TAG, "showProgressDialog: " + e.getMessage());
		}
	}

	public void hideProgressDialog() {
		if (dialog != null && dialog.isShowing())
			dialog.dismiss();
	}

	public boolean isShowProgressDialog() {
		return dialog != null && dialog.isShowing();
	}

	public void showAlert(final String title, final String message) {
		if (!isFinishing()) {
			runOnUiThread(() -> new AlertDialog.Builder(BaseActivity.this, R.style.AlertDialogTheme)
					.setTitle(title)
					.setMessage(message)
					.setNegativeButton(getString(R.string.ok), null).create()
					.show());
		}
	}


	public void showAlert(@StringRes final int titleResId, @StringRes final int messageResId) {
		if (!isFinishing()) {
			runOnUiThread(() -> new AlertDialog.Builder(BaseActivity.this, R.style.AlertDialogTheme)
					.setTitle(titleResId)
					.setMessage(messageResId)
					.setNegativeButton(getString(R.string.ok), null).create()
					.show());
		}
	}

	public void showAlert(@StringRes final int titleResId, @StringRes final int messageResId, final String negativeStr) {
		if (!isFinishing()) {
			runOnUiThread(() -> new AlertDialog.Builder(BaseActivity.this, R.style.AlertDialogTheme)
					.setTitle(titleResId)
					.setMessage(messageResId)
					.setNegativeButton(negativeStr, null).create()
					.show());
		}
	}

	public void showAlert(final String title, final String message, final AlertListener listener) {
		if (!isFinishing()) {
			runOnUiThread(() -> new AlertDialog.Builder(BaseActivity.this, R.style.AlertDialogTheme)
					.setTitle(title)
					.setMessage(message)
					.setNegativeButton(android.R.string.ok, (dialogInterface, i) -> {
						if (listener != null)
							listener.onClickedOk();
					}).create()
					.show());
		}
	}

	public void showAlert(final String title, final boolean cancel, final String message, final AlertListener listener) {
		if (!isFinishing()) {
			runOnUiThread(() -> new AlertDialog.Builder(BaseActivity.this, R.style.AlertDialogTheme)
					.setTitle(title)
					.setMessage(message)
					.setCancelable(cancel)
					.setNegativeButton(android.R.string.ok, (dialogInterface, i) -> {
						if (listener != null)
							listener.onClickedOk();
					}).create()
					.show());
		}
	}

	public void showAlert(final String title, final String message, final String yesTitle, final String noTitle, final AlertListener listener) {
		if (!isFinishing()) {
			runOnUiThread(() -> new AlertDialog.Builder(BaseActivity.this, R.style.AlertDialogTheme)
					.setTitle(title)
					.setMessage(message)
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
	}

	public void showToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	public void showToast(@StringRes final int messageResId) {
		Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
	}

	protected void showKeyboard(View target) {
		if (target == null) {
			return;
		}
		((InputMethodManager) Objects.requireNonNull(getSystemService(Context.INPUT_METHOD_SERVICE))).showSoftInput(target,
				InputMethodManager.SHOW_IMPLICIT);
	}

	protected void hideKeyboard() {
		View view = getCurrentFocus();
		if (view != null) {
			((InputMethodManager) Objects.requireNonNull(getSystemService(Context.INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(view.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	public void setupUI(View view) {
		//Set up touch listener for non-text box views to hide keyboard.
		if (!(view instanceof EditText)) {

			view.setOnTouchListener(new View.OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					hideKeyboard();
					return false;
				}

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

	public Fragment getCurrentFragment() {
		if (!backStack.empty())
			return backStack.peek();
		return null;
	}

	public Fragment getPreviousFragment() {
		if (!backStack.empty())
			if (backStack.size() > 1)
				return backStack.get(backStack.size() - 1);
		return null;
	}

	public void replaceStack(Stack<Fragment> stack) {
		try {
			Fragment currentFragment = null;
			if (backStack != null && !backStack.isEmpty()) {
				currentFragment = backStack.peek();
			}

			backStack = stack;

			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			if (currentFragment != null) {
				ft.remove(currentFragment);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
			}

			Fragment fragment = backStack.peek();
			ft.add(R.id.main_content, fragment, fragment.getClass().getName())
					.addToBackStack(null).commitAllowingStateLoss();
			ft.setTransition(FragmentTransaction.TRANSIT_NONE);
			getSupportFragmentManager().executePendingTransactions();
		} catch (Exception e) {
			Log.d(TAG, "replaceStack: catch");
			e.printStackTrace();
		}
	}

	public void pushFragment(Fragment fragment) {
		pushFragment(fragment, R.anim.slide_down);
	}

	public void pushFragment(Fragment fragment, int animateIn) {
		hideKeyboard();
		if (!backStack.empty())
			if (backStack.peek().getClass().getSimpleName().equals(fragment.getClass().getSimpleName()))
				return;
		backStack.push(fragment);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		if (animateIn != -1) {
			ft.setCustomAnimations(animateIn, 0);
		}
		try {
			ft.add(R.id.main_content, fragment, fragment.getClass().getName())
					.addToBackStack(fragment.getClass().getSimpleName()).commitAllowingStateLoss();
			getSupportFragmentManager().executePendingTransactions();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}

	public void replaceTopFragment(Fragment fragment) {
		replaceTopFragment(fragment, R.anim.slide_down);
	}

	public void replaceTopFragment(Fragment fragment, int animateIn) {
		if (backStack.isEmpty()) {
			pushFragment(fragment, animateIn);
			return;
		}
		Fragment currentFragment = backStack.pop();
		backStack.add(fragment);
		getSupportFragmentManager()
				.beginTransaction().setCustomAnimations(animateIn, 0)
				.remove(currentFragment)
				.add(R.id.main_content, fragment, fragment.getClass().getName())
				.addToBackStack(null).commit();
		getSupportFragmentManager().executePendingTransactions();
	}

	public void popToRootFragment(int animationOut) {
		if (backStack != null && backStack.size() > 1) {
			popFragment(animationOut, backStack.size() - 1);
		}
	}

	public void popFragment() {
		popFragment(R.anim.slide_up, 1);
	}

	public void popFragment(int animateOut) {
		popFragment(animateOut, 1);
	}

	public void popFragment(int animateOut, int depth) {
		if (depth < 1) {
			return;
		}
		Fragment currentFragment = backStack.isEmpty() ? null : backStack.peek();
		hideKeyboard();

		if (backStack.size() < 2) {
			finish();
			return;
		}

		while (depth > 0) {
			if (!backStack.empty())
				backStack.pop();
			depth--;
			if (backStack.size() < 2) {
				break;
			}
		}

		Fragment fragment = backStack.peek();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		if (currentFragment != null) {
			if (animateOut != -1) {
				ft.setCustomAnimations(0, animateOut);
			}
			ft.remove(currentFragment);
		}
		ft.replace(R.id.main_content, fragment, fragment.getClass().getName())
				.addToBackStack(null).commitAllowingStateLoss();

		getSupportFragmentManager().executePendingTransactions();
		backPressListener = null;

	}


	@Override
	public void onBackPressed() {
		if (backPressListener != null) {
			backPressListener.onBackPressed();
			backPressListener = null;
			return;
		}
		if (backStack != null) {
			if (backStack.size() > 1) {
				popFragment(animationOut());
			} else {
				checkBackStack();
			}
		} else
			checkBackStack();

	}

	private void checkBackStack() {
		if (isBackPress) {
			if (canFinish) {
				finish();
			} else {
				canFinish = true;
				Toast.makeText(this, getString(R.string.press_one_more_to_exit), Toast.LENGTH_SHORT).show();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						canFinish = false;
					}
				}, 2500);
			}
		} else {
			finish();
		}
	}

	public void setBackPressListener(boolean enable) {
		this.isBackPress = enable;
	}


	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		List<Fragment> fragments = getSupportFragmentManager().getFragments();
		for (Fragment fg : fragments) {
			if (fg != null) {
				fg.onRequestPermissionsResult(requestCode, permissions, grantResults);
			}
		}
	}

	public boolean checkPermission(String permission) {
		return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
	}

	public void requestPermisson(String[] perssions, int key) {
		ActivityCompat.requestPermissions(this, perssions, key);
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

	public void registerEventBus() {
		if (!EventBus.getDefault().isRegistered(this))
			EventBus.getDefault().register(this);
	}

	public void unRegisterEventBus() {
		if (EventBus.getDefault().isRegistered(this))
			EventBus.getDefault().unregister(this);
	}

	public int getColorRes(int color) {
		return ContextCompat.getColor(this, color);
	}

	public void changeStatusBar() {
		Window window = this.getWindow();
		int flags = this.getWindow().getDecorView().getSystemUiVisibility();
		if(isDarkTheme){
			flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
		}else{
			flags = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
		}
		window.getDecorView().setSystemUiVisibility(flags);
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		window.setStatusBarColor(ContextCompat.getColor(this, isDarkTheme ? R.color.dark_background : R.color.light_background));
	}

}
