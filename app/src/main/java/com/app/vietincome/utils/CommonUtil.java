package com.app.vietincome.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommonUtil {
	public static void runJustBeforeBeingDrawn(final View view, final Runnable runnable) {
		final ViewTreeObserver.OnPreDrawListener preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				view.getViewTreeObserver().removeOnPreDrawListener(this);
				runnable.run();
				return true;
			}
		};
		view.getViewTreeObserver().addOnPreDrawListener(preDrawListener);
	}

	public static int dpToPx(Context context, int dp) {
		return (int) (dp * context.getResources().getDisplayMetrics().density);
	}

	public static int pxToDp(Context context, int px){
		return px / ( context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
	}

	public static String getStringFromAssets(Context context, String assetsName) {
		try {
			StringBuilder buf = new StringBuilder();
			InputStream json = context.getAssets().open(assetsName);
			BufferedReader in =
					new BufferedReader(new InputStreamReader(json, "UTF-8"));
			String str;

			while ((str = in.readLine()) != null) {
				buf.append(str);
			}

			in.close();
			return buf.toString();
		} catch (Exception e) {
			return null;
		}
	}

	public static float dp2px(float dp){
		Resources r = Resources.getSystem();
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
	}

	public static int dp2pxInt(float dp){
		return (int) dp2px(dp);
	}
}
