package com.app.vietincome.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;

import com.app.vietincome.R;
import com.app.vietincome.bases.BaseDialogFragment;
import com.app.vietincome.manager.interfaces.OnSelectedCoin;

public class CoinDialog extends BaseDialogFragment {

	private OnSelectedCoin onSelectedCoin;

	public static CoinDialog newIntance(OnSelectedCoin onSelectedCoin){
		CoinDialog dialog = new CoinDialog();
		Bundle args = new Bundle();
		dialog.setArguments(args);
		dialog.onSelectedCoin = onSelectedCoin;
		return dialog;
	}


	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setAttribute(dialog, true, R.style.ZoomDialogAnimation, 10, 100, 10, 100);
		return dialog;
	}

	@Override
	public int getLayoutId() {
		return R.layout.dialog_coin;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
}
