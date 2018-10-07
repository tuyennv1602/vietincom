package com.app.vietincome.adapter;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.vietincome.R;
import com.app.vietincome.manager.interfaces.ItemClickListener;
import com.app.vietincome.model.News;
import com.app.vietincome.utils.DateUtil;
import com.app.vietincome.utils.GlideImage;
import com.app.vietincome.view.HighLightRelativeLayout;
import com.app.vietincome.view.HighLightTextView;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;
import com.github.siyamed.shapeimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CoinNewsAdapter extends RecyclerView.Adapter<CoinNewsAdapter.CoinNewsViewHolder>{

	private ArrayList<News> news;
	private boolean isDarkTheme;
	private ItemClickListener listener;


	public CoinNewsAdapter(ArrayList<News> news,boolean isDarkTheme, ItemClickListener listener){
		this.news = news;
		this.listener = listener;
		this.isDarkTheme = isDarkTheme;
	}

	@NonNull
	@Override
	public CoinNewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		return new CoinNewsAdapter.CoinNewsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_news_coin, viewGroup, false));

	}

	@Override
	public void onBindViewHolder(@NonNull CoinNewsViewHolder coinNewsViewHolder, int i) {
		coinNewsViewHolder.onBind(news.get(i));
	}

	@Override
	public int getItemCount() {
		if(news == null) return 0;
		if(news.size() < 3) return news.size();
		return 3;
	}

	class CoinNewsViewHolder extends RecyclerView.ViewHolder{

		@BindView(R.id.tvTitleNews)
		HighLightTextView tvTitleNews;

		@BindView(R.id.rootLayout)
		HighLightRelativeLayout rootLayout;

		@BindView(R.id.tvTimeNews)
		HighLightTextView tvTimeNews;

		@BindView(R.id.imgRead)
		ImageView imgRead;

		@BindView(R.id.imgDot)
		ImageView imgDot;

		@BindView(R.id.tvAuthor)
		HighLightTextView tvAuthor;

		public CoinNewsViewHolder(@NonNull View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			rootLayout.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
			tvTimeNews.setTextColor(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
			tvAuthor.setTextColor(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
			imgDot.setColorFilter(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
			tvTitleNews.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			imgRead.setColorFilter(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
			itemView.setOnClickListener(view -> {
				if(listener != null){
					listener.onItemClicked(getAdapterPosition());
				}
			});
		}

		public void onBind(News new_){
			tvTitleNews.setText(new_.getTitle());
			tvTimeNews.setText(TimeAgo.using(DateUtil.toTimestamp(new_.getDate()), new TimeAgoMessages.Builder().withLocale(Locale.forLanguageTag("us")).build()));
			tvAuthor.setText(new_.getAuthor().getDisplayName());
			imgRead.setVisibility(new_.isRead() ? View.INVISIBLE : View.VISIBLE);
		}

		private int getColor(int color){
			return ContextCompat.getColor(itemView.getContext(), color);
		}
	}
}
