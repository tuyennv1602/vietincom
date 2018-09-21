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
import com.app.vietincome.model.Market;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopMarketAdapter extends RecyclerView.Adapter<TopMarketAdapter.TopMarketViewHolder>{

	private ArrayList<Market> markets;
	private boolean isDarkTheme;

	public TopMarketAdapter(ArrayList<Market> markets, boolean isDarkTheme){
		this.markets = markets;
		this.isDarkTheme = isDarkTheme;
	}

	@NonNull
	@Override
	public TopMarketViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		return new TopMarketViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_top_market, viewGroup, false));
	}

	@Override
	public void onBindViewHolder(@NonNull TopMarketViewHolder topMarketViewHolder, int i) {
		topMarketViewHolder.onBind(markets.get(i));
	}

	@Override
	public int getItemCount() {
		if(markets == null) return 0;
		if(markets.size() < 5) return  markets.size();
		return 5;
	}

	public class TopMarketViewHolder extends RecyclerView.ViewHolder{

		@BindView(R.id.layoutRoot)
		LinearLayout layoutRoot;

		@BindView(R.id.tvRank)
		TextView tvRank;

		@BindView(R.id.tvExchange)
		TextView tvExchange;

		@BindView(R.id.tvPair)
		TextView tvPair;

		@BindView(R.id.tvPriceTop)
		TextView tvPriceTop;

		@BindView(R.id.tvVolume)
		TextView tvVolume;

		public TopMarketViewHolder(@NonNull View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

		public void onBind(Market market){
			layoutRoot.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
			tvRank.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			tvExchange.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			tvPair.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			tvPriceTop.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			tvVolume.setTextColor(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
		}

		private int getColor(int color) {
			return ContextCompat.getColor(itemView.getContext(), color);
		}

	}
}
