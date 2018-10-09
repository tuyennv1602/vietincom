package com.app.vietincome.adapter;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.CoinDiffCallBack;
import com.app.vietincome.manager.interfaces.ItemClickCancelListener;
import com.app.vietincome.manager.interfaces.ItemClickListener;
import com.app.vietincome.model.Currency;
import com.app.vietincome.model.Data;
import com.app.vietincome.model.USD;
import com.app.vietincome.utils.CommonUtil;
import com.app.vietincome.view.HighLightRelativeLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllCoinAdapter extends RecyclerView.Adapter<AllCoinAdapter.AllCoinViewHolder> {

	private ArrayList<Data> data;
	private ItemClickListener itemClickListener;
	private ItemClickCancelListener itemClickCancelListener;
	private boolean isDarkTheme;
	private boolean isBTC = false;
	private double rate = 1;

	public AllCoinAdapter(ArrayList<Data> data, ItemClickListener listener, ItemClickCancelListener i) {
		this.itemClickListener = listener;
		this.data = data;
		this.itemClickCancelListener = i;
	}

	public void setCoins(ArrayList<Data> data) {
		this.data = data;
		notifyDataSetChanged();
	}

	public void changeCurrency() {
		isBTC = !isBTC;
		notifyDataSetChanged();
	}

	public void updateAllCoin(ArrayList<Data> data) {
		CoinDiffCallBack coinDiffCallBack = new CoinDiffCallBack(this.data, data);
		DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(coinDiffCallBack);
		diffResult.dispatchUpdatesTo(this);
		this.data.clear();
		this.data.addAll(data);
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public void setDarkTheme(boolean darkTheme) {
		isDarkTheme = darkTheme;
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

	public class AllCoinViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.layoutRoot)
		HighLightRelativeLayout layoutRoot;

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

//		@BindView(R.id.imgStar)
//		ImageView imgStar;
//
//		@BindView(R.id.layoutAddFavorite)
//		RelativeLayout layoutAddFavorite;
//
//		@BindView(R.id.tvCancel)
//		HighLightTextView tvCancel;
//
//		@BindView(R.id.tvAddFavourite)
//		TextView tvAddFavourite;

		private AllCoinViewHolder(@NonNull View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			itemView.setOnClickListener(view -> {
				if (itemClickListener != null) {
					itemClickListener.onItemClicked(getAdapterPosition());
				}
			});
			itemView.setOnLongClickListener(view -> {
				if (itemClickCancelListener != null) {
					itemClickCancelListener.onLongClicked(getAdapterPosition());
//					showFavouriteView();
				}
				return false;
			});
//			tvCancel.setOnClickListener(view -> {
//				if(itemClickCancelListener != null){
//					itemClickCancelListener.onCancelClicked(getAdapterPosition());
//				}
//			});
		}

		@SuppressLint("DefaultLocale")
		private void onBind(Data item) {
			layoutRoot.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
			tvRank.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			tvName.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			tvPrice.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			tvPrice.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			tvSymbol.setTextColor(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
			if (item.getQuotes() == null) return;
			USD usd = item.getQuotes().getUSD();
			if (usd.percentChange1h != 0) {
				tv1H.setTextColor(usd.isPlus(usd.percentChange1h) ? getColor(R.color.green) : getColor(R.color.red));
			} else {
				tv1H.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			}
			if (usd.percentChange24h != 0) {
				tv24H.setTextColor(usd.isPlus(usd.percentChange24h) ? getColor(R.color.green) : getColor(R.color.red));
			} else {
				tv24H.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			}
			if (usd.percentChange7d != 0) {
				tv7D.setTextColor(usd.isPlus(usd.percentChange7d) ? getColor(R.color.green) : getColor(R.color.red));
			} else {
				tv7D.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			}
			fillData(item);
//			imgStar.setVisibility(item.isFavourite() ? View.VISIBLE : View.GONE);
//			tvAddFavourite.setText(itemView.getContext().getString(R.string.added_favourite, item.getName()));
// 			layoutAddFavorite.setBackgroundColor(isDarkTheme ? getColor(R.color.light_background) : getColor(R.color.dark_background));
//			tvAddFavourite.setTextColor(isDarkTheme ? getColor(R.color.light_text) : getColor(R.color.dark_text));
//			tvCancel.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
		}

		public void fillData(Data item){
			USD usd = item.getQuotes().getUSD();
			tvRank.setText(String.valueOf(item.getRank()));
			tvName.setText(item.getName());
			tvSymbol.setText(item.getSymbol());
			tv1H.setText(usd.getPercentChange1h());
			tv24H.setText(usd.getPercentChange24h());
			tv7D.setText(usd.getPercentChange7d());
			tvPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, isBTC ? 10 : 12);
			String price;
			if (isBTC) {
				price = itemView.getContext().getString(R.string.bitcoin) + String.format("%.8f", (item.getQuotes().getBTC().getPrice()));
			} else {
				Currency currency = AppPreference.INSTANCE.getCurrency();
				price = CommonUtil.formatCurrency(usd.getPrice(), rate, currency);
			}
			tvPrice.setText(price);
		}

		private int getColor(int color) {
			return ContextCompat.getColor(itemView.getContext(), color);
		}

//		private void showFavouriteView(){
//			YoYo.with(Techniques.SlideInRight)
//					.duration(200)
//					.withListener(new Animator.AnimatorListener() {
//						@Override
//						public void onAnimationStart(Animator animator) {
//							layoutAddFavorite.setVisibility(View.VISIBLE);
//						}
//
//						@Override
//						public void onAnimationEnd(Animator animator) {
//
//						}
//
//						@Override
//						public void onAnimationCancel(Animator animator) {
//
//						}
//
//						@Override
//						public void onAnimationRepeat(Animator animator) {
//
//						}
//					})
//					.playOn(layoutAddFavorite);
//		}
//     	public void hideFavouriteView(Animator.AnimatorListener listener){
//		YoYo.with(Techniques.SlideOutRight)
//				.duration(500)
//				.withListener(listener)
//				.playOn(layoutAddFavorite);
//	}
	}

}
