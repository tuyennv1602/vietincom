package com.app.vietincome.adapter;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.manager.interfaces.ItemClickListener;
import com.app.vietincome.model.Data;
import com.app.vietincome.model.USD;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllCoinAdapter extends RecyclerView.Adapter<AllCoinAdapter.AllCoinViewHolder> {

	private ArrayList<Data> data;
	private ItemClickListener itemClickListener;
	private boolean isDarkTheme;

	public AllCoinAdapter(ItemClickListener listener){
		this.itemClickListener = listener;
	}

	public void setDarkTheme(boolean darkTheme) {
		isDarkTheme = darkTheme;
	}

	public void setAllCoin(ArrayList<Data> data){
		this.data = data;
		notifyDataSetChanged();
	}


	@NonNull
	@Override
	public AllCoinViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		return new AllCoinViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_allcoin, viewGroup, false));
	}

	@Override
	public void onBindViewHolder(@NonNull AllCoinViewHolder allCoinViewHolder, int i) {
		allCoinViewHolder.onBind(data.get(i));
	}

	@Override
	public int getItemCount() {
		return data == null ? 0 : data.size();
	}

	public class AllCoinViewHolder extends RecyclerView.ViewHolder{

		@BindView(R.id.layoutRoot)
		LinearLayout layoutRoot;

		@BindView(R.id.tvRank)
		TextView tvRank;

		@BindView(R.id.tvName)
		TextView tvName;

		@BindView(R.id.tvSymbol)
		TextView tvSymbol;

		@BindView(R.id.tvPrice)
		TextView tvPrice;

		@BindView(R.id.tv1H)
		TextView tv1H;

		@BindView(R.id.tv24H)
		TextView tv24H;

		@BindView(R.id.tv7D)
		TextView tv7D;

		public AllCoinViewHolder(@NonNull View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			itemView.setOnClickListener(view -> {
				if(itemClickListener != null){
					itemClickListener.onItemClicked(getAdapterPosition());
				}
			});
			itemView.setOnLongClickListener(view -> {
				if(itemClickListener != null){
					itemClickListener.onLongClicked(getAdapterPosition());
				}
				return false;
			});
		}

		public void onBind(Data item){
			layoutRoot.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
			tvRank.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			tvName.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			tvPrice.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			USD usd = item.getQuotes().getUSD();
			tv1H.setTextColor(usd.isPlus(usd.percentChange1h) ? getColor(R.color.green) : getColor(R.color.red));
			tv24H.setTextColor(usd.isPlus(usd.percentChange24h) ? getColor(R.color.green) : getColor(R.color.red));
			tv7D.setTextColor(usd.isPlus(usd.percentChange7d) ? getColor(R.color.green) : getColor(R.color.red));
			tvRank.setText(String.valueOf(item.getRank()));
			tvName.setText(item.getName());
			tvSymbol.setText(item.getSymbol());
			tv1H.setText(usd.getPercentChange1h());
			tv24H.setText(usd.getPercentChange24h());
			tv7D.setText(usd.getPercentChange7d());
		}

		private int getColor(int color){
			return ContextCompat.getColor(itemView.getContext(), color);
		}

	}
}
