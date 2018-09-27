package com.app.vietincome.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.vietincome.R;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.model.Coin;
import com.app.vietincome.view.HighLightTextView;

import java.util.ArrayList;

import butterknife.BindView;

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.CoinViewHolder>{

	private ArrayList<Coin> coins;
	private boolean isDarkThem = AppPreference.INSTANCE.darkTheme;

	public CoinAdapter(ArrayList<Coin> coins){
		this.coins = coins;
	}

	@NonNull
	@Override
	public CoinViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		return new CoinViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_coin, viewGroup, false));
	}

	@Override
	public void onBindViewHolder(@NonNull CoinViewHolder viewHolder, int i) {
		viewHolder.onBind(coins.get(i));
	}

	@Override
	public int getItemCount() {
		return coins == null ? 0 : coins.size();
	}

	class CoinViewHolder extends RecyclerView.ViewHolder{

		@BindView(R.id.tvCoin)
		HighLightTextView tvCoin;

		public CoinViewHolder(@NonNull View itemView) {
			super(itemView);
			itemView.setOnClickListener(view -> {
			});
		}

		public void onBind(Coin coin){

		}
	}
}
