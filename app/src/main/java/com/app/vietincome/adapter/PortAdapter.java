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
import com.app.vietincome.model.Transaction;
import com.app.vietincome.model.Portfolio;
import com.app.vietincome.utils.CommonUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PortAdapter extends RecyclerView.Adapter<PortAdapter.PortViewHolder> {

	private ArrayList<Portfolio> portfolios;
	private ItemClickListener itemClickListener;
	private boolean isDarkTheme;
	private boolean isUSD = true;

	public PortAdapter(ArrayList<Portfolio> portfolios, ItemClickListener itemClickListener) {
		this.portfolios = portfolios;
		this.itemClickListener = itemClickListener;
	}

	public void setDarkTheme(boolean darkTheme) {
		isDarkTheme = darkTheme;
	}

	public void changeCurrency() {
		isUSD = !isUSD;
		notifyDataSetChanged();
	}

	@NonNull
	@Override
	public PortViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		return new PortViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_portfolio, viewGroup, false));
	}

	@Override
	public void onBindViewHolder(@NonNull PortViewHolder portViewHolder, int i) {
		portViewHolder.onBind(portfolios.get(i));
	}

	@Override
	public int getItemCount() {
		return portfolios == null ? 0 : portfolios.size();
	}

	class PortViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.layoutRoot)
		LinearLayout layoutRoot;

		@BindView(R.id.tvName)
		TextView tvName;

		@BindView(R.id.tvSymbol)
		TextView tvSymbol;

		@BindView(R.id.tvPrice)
		TextView tvPrice;

		@BindView(R.id.tvChange24H)
		TextView tvChange24h;

		@BindView(R.id.tvHoldingPrice)
		TextView tvHoldingPrice;

		@BindView(R.id.tvNumHolding)
		TextView tvNumHolding;

		@BindView(R.id.tvCost)
		TextView tvCost;

		public PortViewHolder(@NonNull View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			itemView.setOnClickListener(view -> {
				if (itemClickListener != null) {
					itemClickListener.onItemClicked(getAdapterPosition());
				}
			});
		}

		void onBind(Portfolio portfolio) {
			layoutRoot.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
			tvName.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			tvSymbol.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			tvPrice.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			tvHoldingPrice.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			tvCost.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			tvName.setText(portfolio.getName());
			tvSymbol.setText(portfolio.getSymbol());
			int numHold = portfolio.getNumHold();
			double cost = 0;
			for (Transaction i : portfolio.getTransactions()) {
				if (!isUSD) {
					cost += (i.getQuantity() * i.getPriceBTC());
				} else {
					cost += (i.getQuantity() * i.getPriceUSD());
				}
			}
			double price = isUSD ? portfolio.getQuotes().getUSD().getPrice() : portfolio.getQuotes().getBTC().getPrice();
			double percent = isUSD ? portfolio.getQuotes().getUSD().percentChange24h : portfolio.getQuotes().getBTC().percentChange24h;
			tvCost.setText(new StringBuilder().append(isUSD ? "$" : "฿").append(CommonUtil.formatCurrency(cost, isUSD)).toString());
			tvNumHolding.setText(String.valueOf(numHold));
			tvPrice.setText(new StringBuilder().append(isUSD ? "$" : "฿").append(CommonUtil.formatCurrency(price, isUSD)).toString());
			if (percent != 0) {
				tvChange24h.setTextColor(isPlus(percent) ? getColor(R.color.green) : getColor(R.color.red));
			} else {
				tvChange24h.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			}
			tvChange24h.setText(isUSD ? portfolio.getQuotes().getUSD().getPercentChange24h() : portfolio.getQuotes().getBTC().getPercentChange24h());
			tvHoldingPrice.setText(new StringBuilder().append(isUSD ? "$" : "฿").append(CommonUtil.formatCurrency(price * numHold, isUSD)).toString());
		}

		private int getColor(int color) {
			return ContextCompat.getColor(itemView.getContext(), color);
		}

		public boolean isPlus(double value) {
			return !String.valueOf(value).contains("-");
		}
	}
}
