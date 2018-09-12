package com.app.vietincome.manager.interfaces;

import com.app.vietincome.model.Data;

import java.util.ArrayList;

public interface GetCoinListener {

	void onSuccessed(ArrayList<Data> data);

	void onError(String error);
}
