package com.app.vietincome.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;

import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.model.Currency;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {

	public static int dpToPx(Context context, int dp) {
		return (int) (dp * context.getResources().getDisplayMetrics().density);
	}

	public static int pxToDp(Context context, int px) {
		return px / (context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
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

	public static float dp2px(float dp) {
		Resources r = Resources.getSystem();
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
	}

	public static int dp2pxInt(float dp) {
		return (int) dp2px(dp);
	}

	public static String formatCurrency(double value, double rate, Currency currency) {
		StringBuilder price = new StringBuilder();
		if(currency != null) {
			price.append(currency.getSymbol());
		}
		double priceValue = (currency != null && currency.getCode().equals("USD")) ? value : value * rate;
		if (value < 1.0) {
			price.append(String.format(Locale.US, "%.4f", priceValue));
		} else if (value < 1000) {
			price.append(String.format(Locale.US, "%.2f", priceValue));
		} else {
			DecimalFormat dFormat = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
			dFormat.applyPattern("###,###,###,###,###");
			price.append(dFormat.format(priceValue));
		}
		return price.toString();
	}

	public static String formatCurrency(double price, boolean isUsd) {
		String value;
		if (price == 0) {
			value = String.format(Locale.US, "%.1f", price);
		} else if (price < 1.0) {
			value = String.format(Locale.US, isUsd ? "%.4f" : "%.8f", price);
		} else if (price < 1000) {
			value = String.format(Locale.US, isUsd ? "%.2f" : "%.4f", price);
		} else {
			DecimalFormat dFormat = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
			dFormat.applyPattern("###,###,###,###,##0.00");
			value = dFormat.format(price);
		}
		return value;
	}

	public static boolean validateEmail(String email) {
		Matcher matcher = Patterns.EMAIL_ADDRESS.matcher(email);
		return matcher.matches();
	}

	public static boolean validatePassword(String password) {
		return password.length() > 5 && password.length() < 33;
	}

}
