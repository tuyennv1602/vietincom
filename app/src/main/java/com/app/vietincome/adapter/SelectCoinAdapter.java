package com.app.vietincome.adapter;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.vietincome.R;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.interfaces.ItemClickListener;
import com.app.vietincome.model.Data;
import com.app.vietincome.view.HighLightTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectCoinAdapter extends RecyclerView.Adapter<SelectCoinAdapter.CoinViewHolder>{

	private ArrayList<Data> coins;
	private boolean isDarkTheme = AppPreference.INSTANCE.darkTheme;
	private ItemClickListener itemClickListener;

	public SelectCoinAdapter(ArrayList<Data> coins, ItemClickListener itemClickListener){
		this.coins = coins;
		this.itemClickListener = itemClickListener;
	}

	public void setCoins(ArrayList<Data> coins){
		this.coins = coins;
		notifyDataSetChanged();
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
			ButterKnife.bind(this, itemView);
			tvCoin.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
			tvCoin.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			itemView.setOnClickListener(view -> {
				if(itemClickListener != null){
					itemClickListener.onItemClicked(getAdapterPosition());
				}
			});
		}

		void onBind(Data coin){
			tvCoin.setText(new StringBuilder().append(coin.getName()).append(" (").append(coin.getSymbol()).append(")").toString());
		}

		private int getColor(int color) {
			return ContextCompat.getColor(itemView.getContext(), color);
		}

	}
}
