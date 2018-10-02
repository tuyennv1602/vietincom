package com.app.vietincome.adapter;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.interfaces.ItemClickListener;
import com.app.vietincome.model.Transaction;
import com.app.vietincome.utils.CommonUtil;
import com.app.vietincome.view.HighLightImageView;
import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>{

	private ArrayList<Transaction> transactions;
	private ItemClickListener onItemClickListener;
	private boolean isDarkTheme = AppPreference.INSTANCE.isDarkTheme();
	private boolean isUSD = true;
	private double totalPrice;

	public TransactionAdapter(ArrayList<Transaction> transactions, double totalPrice,ItemClickListener onItemClickListener){
		this.transactions = transactions;
		this.onItemClickListener = onItemClickListener;
		this.totalPrice = totalPrice;
	}

	public void changeCurrency() {
		isUSD = !isUSD;
		notifyDataSetChanged();
	}

	public void setTotalPrice(double totalPrice){
		this.totalPrice = totalPrice;
	}

	@NonNull
	@Override
	public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		return new TransactionViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_transaction, viewGroup, false));
	}

	@Override
	public void onBindViewHolder(@NonNull TransactionViewHolder transactionViewHolder, int i) {
		transactionViewHolder.onBind(transactions.get(i));
	}

	@Override
	public int getItemCount() {
		return transactions == null ? 0 : transactions.size();
	}

	class TransactionViewHolder extends RecyclerView.ViewHolder{

		@BindView(R.id.layoutRoot)
		SwipeLayout layoutRoot;

		@BindView(R.id.imgDelete)
		ImageView imgDelete;

		@BindView(R.id.layoutDelete)
		RelativeLayout layoutDelete;

		@BindView(R.id.tvHolding)
		TextView tvHolding;

		@BindView(R.id.tvType)
		TextView tvType;

		@BindView(R.id.tvPrice)
		TextView tvPrice;

		@BindView(R.id.tvDate)
		TextView tvDate;

		@BindView(R.id.tvTotal)
		TextView tvTotal;

		@BindView(R.id.tvTotalPercent)
		TextView tvTotalPercent;

		@BindView(R.id.tvProfit)
		TextView tvProfit;

		@BindView(R.id.tvProfitPercent)
		TextView tvProfitPercent;

		public TransactionViewHolder(@NonNull View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			layoutRoot.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
			tvHolding.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			tvPrice.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			tvTotal.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			tvProfit.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			imgDelete.setColorFilter(getColor(R.color.white));
			tvTotalPercent.setVisibility(View.INVISIBLE);
			layoutRoot.setShowMode(SwipeLayout.ShowMode.PullOut);
			layoutRoot.addDrag(SwipeLayout.DragEdge.Right, layoutDelete);
			itemView.setOnClickListener(view -> {
				if(onItemClickListener != null){
					onItemClickListener.onItemClicked(getAdapterPosition());
				}
			});
			layoutDelete.setOnClickListener(view -> {
				Log.d("__port", "TransactionViewHolder: delete");
			});
		}

		void onBind(Transaction transaction){
			tvHolding.setText(String.valueOf(transaction.getQuantity()));
			double price = isUSD ? transaction.getPriceUSD() : transaction.getPriceBTC();
			tvPrice.setText(new StringBuilder().append(isUSD ? "$" : "฿").append(CommonUtil.formatCurrency(price, isUSD)).toString());
			tvDate.setText(transaction.getDateAdd());
			tvType.setText(transaction.isBuy() ? "BUY" : "SELL");
			tvType.setTextColor(transaction.isBuy() ? getColor(R.color.green) : getColor(R.color.red));
			tvType.setBackground(transaction.isBuy() ? itemView.getContext().getDrawable(R.drawable.bg_buy) :  itemView.getContext().getDrawable(R.drawable.bg_sell));
			double total = price * transaction.getQuantity();
			tvTotal.setText(new StringBuilder().append(isUSD ? "$" : "฿").append(CommonUtil.formatCurrency(total, isUSD)).toString());
			double profit = totalPrice - total;
			float percent = (float) (profit / totalPrice);
			tvProfit.setText(new StringBuilder().append(isUSD ? "$" : "฿").append(CommonUtil.formatCurrency(profit, isUSD)).toString());
			if (percent != 0) {
				tvProfitPercent.setTextColor(isPlus(percent) ? getColor(R.color.green) : getColor(R.color.red));
				tvProfitPercent.setText(new StringBuilder().append(isPlus(percent) ? "+" : "").append(String.format(Locale.US, "%.4f", percent)).append("%").toString());
			} else {
				tvProfitPercent.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
				tvProfitPercent.setText("0%");
			}
		}

		private int getColor(int color) {
			return ContextCompat.getColor(itemView.getContext(), color);
		}

		public boolean isPlus(double value) {
			return !String.valueOf(value).contains("-");
		}
	}
}
