package com.app.vietincome.adapter;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.interfaces.ItemClickListener;
import com.app.vietincome.model.Currency;
import com.app.vietincome.model.Data;
import com.app.vietincome.utils.Constant;
import com.app.vietincome.view.HighLightLinearLayout;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopCoinAdapter extends RecyclerView.Adapter<TopCoinAdapter.TopCoinViewHolder> {

	private ArrayList<Data> fullData;
	private ArrayList<Data> shortData;
	private boolean showMore = false;
	private int type;
	private Double total;
	private boolean isDarkTheme = AppPreference.INSTANCE.isDarkTheme();
	private Currency currency = AppPreference.INSTANCE.getCurrency();
	private double rate;
	private ItemClickListener itemClickListener;

	public TopCoinAdapter(int type, double rate, ArrayList<Data> data, Double total, ItemClickListener itemClickListener) {
		this.type = type;
		this.total = total;
		this.rate = rate;
		this.fullData = data;
		this.shortData = new ArrayList<>();
		this.itemClickListener = itemClickListener;
	}

	public void setTopCoinData(ArrayList<Data> data) {
		this.fullData = data;
		this.shortData = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			this.shortData.add(fullData.get(i));
		}
		notifyDataSetChanged();
	}

	public void toggleShowMore() {
		showMore = !showMore;
		notifyDataSetChanged();
	}

	@NonNull
	@Override
	public TopCoinViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		return new TopCoinViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_global_market, viewGroup, false));
	}

	@Override
	public void onBindViewHolder(@NonNull TopCoinViewHolder topCoinViewHolder, int i) {
		if (showMore) {
			topCoinViewHolder.onBind(fullData.get(i), i + 1);
		} else {
			topCoinViewHolder.onBind(shortData.get(i), i + 1);
		}
	}

	@Override
	public int getItemCount() {
		if (showMore) {
			return fullData == null ? 0 : fullData.size();
		} else {
			return shortData == null ? 0 : shortData.size();
		}
	}

	class TopCoinViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.layoutRoot)
		HighLightLinearLayout layoutRoot;

		@BindView(R.id.tvRank)
		TextView tvRank;

		@BindView(R.id.tvName)
		TextView tvName;

		@BindView(R.id.tvValue)
		TextView tvValue;

		@BindView(R.id.tvPercent)
		TextView tvPercent;

		public TopCoinViewHolder(@NonNull View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			layoutRoot.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
			tvRank.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			tvName.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			tvPercent.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			tvValue.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			itemView.setOnClickListener(view -> {
				if (itemClickListener != null) {
					itemClickListener.onItemClicked(getAdapterPosition());
				}
			});
		}

		public void onBind(Data data, int position) {
			tvRank.setText(String.valueOf(position));
			tvName.setText(data.getName());
			if (type == Constant.MARKET_CAP) {
				tvValue.setText(new StringBuilder().append("$").append(withSuffix(data.getQuotes().getUSD().getMarketCap())).toString());
//				if(currency.getCode().equals("USD")) {
//					tvValue.setText(currency.getSymbol() + withSuffix(data.getQuotes().getUSD().getMarketCap().longValue()));
//				}else{
//					tvValue.setText(currency.getSymbol() + withSuffix((long) (data.getQuotes().getUSD().getMarketCap() * rate)));
//				}
				float percent = (float) ((data.getQuotes().getUSD().getMarketCap() / total) * 100);
				tvPercent.setText(new StringBuilder().append(String.format(Locale.US, "%.2f", percent)).append("%").toString());
			} else {
				tvValue.setText(new StringBuilder().append("$").append(withSuffix(data.getQuotes().getUSD().getVolume24h())).toString());
//				if(currency.getCode().equals("USD")) {
//					tvValue.setText(currency.getSymbol() + withSuffix(data.getQuotes().getUSD().getVolume24h().longValue()));
//				}else{
//					tvValue.setText(currency.getSymbol() + withSuffix((long) (data.getQuotes().getUSD().getVolume24h() * rate)));
//				}
				float percent = (float) ((data.getQuotes().getUSD().getVolume24h() / total) * 100);
				tvPercent.setText(new StringBuilder().append(String.format("%.2f", percent)).append("%").toString());
			}
		}

		public String withSuffix(double price) {
			if (price < 1000) return "" + price;
			int exp = (int) (Math.log(price) / Math.log(1000));
			return String.format(Locale.US, "%.1f%c",
					price / Math.pow(1000, exp),
					"KMBTPE".charAt(exp - 1));
		}

		private int getColor(int color) {
			return ContextCompat.getColor(itemView.getContext(), color);
		}

	}
}
