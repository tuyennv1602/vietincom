package com.app.vietincom.adapter;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.vietincom.R;
import com.app.vietincom.manager.interfaces.ItemClickListener;
import com.app.vietincom.model.News;
import com.app.vietincom.utils.DateUtil;
import com.app.vietincom.utils.GlideImage;
import com.app.vietincom.view.HighLightImageView;
import com.app.vietincom.view.HighLightTextView;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

	private ArrayList<News> news;
	private boolean isDarkTheme;
	private ItemClickListener listener;

	public NewsAdapter(ItemClickListener listener){
		this.listener = listener;
	}

	public void setDarkTheme(boolean darkTheme) {
		isDarkTheme = darkTheme;
	}

	public void setNews(ArrayList<News> news){
		this.news = news;
		notifyDataSetChanged();
	}

	@NonNull
	@Override
	public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		return new NewsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_news, viewGroup, false));
	}

	@Override
	public void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, int i) {
		newsViewHolder.onBind(news.get(i));
	}

	@Override
	public int getItemCount() {
		return news == null ? 0 : news.size();
	}

	class NewsViewHolder extends RecyclerView.ViewHolder{

		@BindView(R.id.imgNews)
		HighLightImageView imgNews;

		@BindView(R.id.tvTitleNews)
		HighLightTextView tvTitleNews;

		@BindView(R.id.rootLayout)
		RelativeLayout rootLayout;

		@BindView(R.id.tvTimeNews)
		HighLightTextView tvTimeNews;

		@BindView(R.id.imgRead)
		ImageView imgRead;

		public NewsViewHolder(@NonNull View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			itemView.setOnClickListener(view -> {
				if(listener != null){
					listener.onItemClicked(getAdapterPosition());
				}
			});
		}

		public void onBind(News new_){
			rootLayout.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
			tvTimeNews.setTextColor(isDarkTheme ? getColor(R.color.white) : getColor(R.color.text_gray));
			imgRead.setColorFilter(getColor(R.color.green));
			GlideImage.loadImage(new_.getAttachments().get(0).getUrl(), imgNews);
			tvTitleNews.setText(new_.getTitle());
			tvTimeNews.setText(TimeAgo.using(DateUtil.toTimestamp(new_.getDate()), new TimeAgoMessages.Builder().withLocale(Locale.forLanguageTag("us")).build()));
			imgRead.setVisibility(new_.isRead() ? View.INVISIBLE : View.VISIBLE);
		}

		private int getColor(int color){
			return ContextCompat.getColor(itemView.getContext(), color);
		}
	}
}
