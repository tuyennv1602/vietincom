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
import com.app.vietincome.model.CoinInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CoinInfoAdapter extends RecyclerView.Adapter<CoinInfoAdapter.CoinViewHolder> {

	private ArrayList<CoinInfo> coinInfos;
	private boolean isDarkTheme = AppPreference.INSTANCE.isDarkTheme();

	public CoinInfoAdapter(ArrayList<CoinInfo> coinInfos){
		this.coinInfos = coinInfos;
	}

	@NonNull
	@Override
	public CoinViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		return new CoinViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_coin_info, viewGroup, false));
	}

	@Override
	public void onBindViewHolder(@NonNull CoinViewHolder coinViewHolder, int i) {
		coinViewHolder.onBind(coinInfos.get(i));
	}

	@Override
	public int getItemCount() {
		return coinInfos == null ? 0 : coinInfos.size();
	}

	public class CoinViewHolder extends RecyclerView.ViewHolder{

		@BindView(R.id.tvLabel)
		TextView tvLabel;

		@BindView(R.id.tvValue)
		TextView tvValue;

		public CoinViewHolder(@NonNull View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

		public void onBind(CoinInfo coinInfo){
			tvLabel.setTextColor(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
			tvValue.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			tvLabel.setText(coinInfo.getLabel());
			tvValue.setText(coinInfo.getValue());
		}

		private int getColor(int color) {
			return ContextCompat.getColor(itemView.getContext(), color);
		}

	}
}
