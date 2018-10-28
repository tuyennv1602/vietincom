package com.app.vietincome.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FileUtil {

	public static File createTempFile(Activity activity) throws IOException {
		// Create an image file name
		File file;
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		file = File.createTempFile(
				imageFileName,  /* prefix */
				".jpg",         /* suffix */
				storageDir      /* directory */
		);
		// Save a file: path for use with ACTION_VIEW intents
		return file;
	}

	public static MultipartBody.Part createFileBody(File file, String paramName) {
		String extention = MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath());
		MediaType mediaType = MediaType.parse(Objects.requireNonNull(MimeTypeMap.
				getSingleton().getMimeTypeFromExtension(extention)));
		RequestBody image = RequestBody.create(mediaType, file);
		return MultipartBody.Part.createFormData(paramName, file.getName(), image);
	}

	private static File convertBitmapToFile(Activity activity, Bitmap bitmap, Bitmap.CompressFormat format) {
		File file;
		try {
			file = createTempFile(activity);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(format, 100, bos);
		byte[] bitmapData = bos.toByteArray();
		try {
			File f = new File(file.getAbsolutePath());
			f.delete();
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(bitmapData);
			fos.flush();
			fos.close();
			bitmap.recycle();
			return f;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static File convertToPNGImageFile(Activity activity, Bitmap bitmap){
		return convertBitmapToFile(activity, bitmap, Bitmap.CompressFormat.PNG);
	}

	public static File convertToJPEGImageFile(Activity activity, Bitmap bitmap){
		return convertBitmapToFile(activity, bitmap, Bitmap.CompressFormat.JPEG);
	}

}
