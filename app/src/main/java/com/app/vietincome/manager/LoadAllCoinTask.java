package com.app.vietincome.manager;

import android.content.Context;
import android.os.AsyncTask;

import com.app.vietincome.manager.interfaces.GetCoinListener;
import com.app.vietincome.model.Data;
import com.app.vietincome.model.responses.CoinResponse;
import com.app.vietincome.network.ApiClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadAllCoinTask extends AsyncTask<Integer, Void, ArrayList<Data>> {

	private GetCoinListener getCoinListener;

	public LoadAllCoinTask(GetCoinListener getCoinListener){
		this.getCoinListener = getCoinListener;
	}

	@Override
	protected ArrayList<Data> doInBackground(Integer... ints) {
		ApiClient.getAllCoinService().getAllCoin(ints[0]).enqueue(new Callback<CoinResponse>() {
			@Override
			public void onResponse(Call<CoinResponse> call, Response<CoinResponse> response) {
				if (response.isSuccessful()) {
					if (response.body().getMetadata().isSuccess()) {
						if(getCoinListener != null){
							getCoinListener.onSuccessed(response.body().getData());

						}
					}
				}
			}

			@Override
			public void onFailure(Call<CoinResponse> call, Throwable t) {
				if(getCoinListener != null){
					getCoinListener.onError(t.getMessage());
				}
			}
		});
		return null;
	}

}
