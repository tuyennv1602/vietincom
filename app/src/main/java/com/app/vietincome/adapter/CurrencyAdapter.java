package com.app.vietincome.adapter;

import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.interfaces.ItemClickListener;
import com.app.vietincome.model.Currency;
import com.app.vietincome.view.HighLightLinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder> {

	private ArrayList<Currency> currencies;
	private ItemClickListener itemClickListener;

	public CurrencyAdapter(ArrayList<Currency> currencies, ItemClickListener itemClickListener) {
		this.currencies = currencies;
		this.itemClickListener = itemClickListener;
	}

	public void changeSelected(int position){
		for (int i = 0; i < currencies.size(); i++){
			currencies.get(i).setSelected(position == i);
		}
		notifyDataSetChanged();
	}

	@NonNull
	@Override
	public CurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		return new CurrencyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_currency, viewGroup, false));
	}

	@Override
	public void onBindViewHolder(@NonNull CurrencyViewHolder currencyViewHolder, int i) {
		currencyViewHolder.onBind(currencies.get(i));
	}

	@Override
	public int getItemCount() {
		return currencies == null ? 0 : currencies.size();
	}

	public class CurrencyViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.rdSelected)
		RadioButton rdSelected;

		@BindView(R.id.tvCurrencyValue)
		TextView tvCurrency;

		@BindView(R.id.layoutRoot)
		LinearLayout layoutRoot;

		public CurrencyViewHolder(@NonNull View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			itemView.setOnClickListener(view -> {
				if (itemClickListener != null) {
					itemClickListener.onItemClicked(getAdapterPosition());
				}
			});
		}

		public void onBind(Currency currency) {
			boolean isDarkTheme = AppPreference.INSTANCE.darkTheme;
			layoutRoot.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
			ColorStateList colorStateList = new ColorStateList(
					new int[][]{
							new int[]{-android.R.attr.state_checked},
							new int[]{android.R.attr.state_checked}
					},
					new int[]{
							getColor(R.color.light_gray)
							, isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image),
					}
			);
			rdSelected.setButtonTintList(colorStateList);
			tvCurrency.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			rdSelected.setChecked(currency.isSelected());
			tvCurrency.setText(new StringBuilder().append(currency.getCode()).append(" (").append(currency.getSymbol()).append(")").toString());
		}

		private int getColor(int color) {
			return ContextCompat.getColor(itemView.getContext(), color);
		}

	}
}
