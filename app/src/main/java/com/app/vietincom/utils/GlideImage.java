package com.app.vietincom.utils;

import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.app.vietincom.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

public class GlideImage {

	public static void loadImage(String url, ImageView view, @DrawableRes int placeHolder, @DrawableRes int error) {
		if (url.isEmpty())
			return;
		Glide.with(view.getContext())
				.asBitmap()
				.apply(new RequestOptions().placeholder(placeHolder).error(error))
				.load(url)
				.into(view);
	}

	public static void loadImage(File file, ImageView view, @DrawableRes int placeHolder, @DrawableRes int error) {
		if (file == null)
			return;
		Glide.with(view.getContext())
				.asBitmap()
				.apply(new RequestOptions().placeholder(placeHolder).error(error))
				.load(file)
				.into(view);
	}

	public static void loadImage(@DrawableRes int drawable, ImageView view, @DrawableRes int placeHolder, @DrawableRes int error) {
		Glide.with(view.getContext())
				.asBitmap()
				.apply(new RequestOptions().placeholder(placeHolder).error(error))
				.load(drawable)
				.into(view);
	}

	public static void loadImage(String url, ImageView view) {
		loadImage(url, view, R.color.demo_dark_transparent, R.color.demo_dark_transparent);
	}

	public static void loadImage(File file, ImageView view) {
		loadImage(file, view, R.color.demo_dark_transparent, R.color.demo_dark_transparent);
	}

	public static void loadImage(@DrawableRes int drawable, ImageView view) {
		loadImage(drawable, view, R.color.demo_dark_transparent, R.color.demo_dark_transparent);
	}
}
