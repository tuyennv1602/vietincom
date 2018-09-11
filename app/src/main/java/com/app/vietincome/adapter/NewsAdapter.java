package com.app.vietincome.adapter;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.vietincome.R;
import com.app.vietincome.manager.interfaces.ItemClickListener;
import com.app.vietincome.model.News;
import com.app.vietincome.utils.DateUtil;
import com.app.vietincome.utils.GlideImage;
import com.app.vietincome.view.HighLightTextView;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;
import com.github.siyamed.shapeimageview.RoundedImageView;

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
		RoundedImageView imgNews;

		@BindView(R.id.tvTitleNews)
		HighLightTextView tvTitleNews;

		@BindView(R.id.rootLayout)
		RelativeLayout rootLayout;

		@BindView(R.id.tvTimeNews)
		HighLightTextView tvTimeNews;

		@BindView(R.id.imgRead)
		ImageView imgRead;

		@BindView(R.id.imgDot)
		ImageView imgDot;

		@BindView(R.id.tvAuthor)
		HighLightTextView tvAuthor;

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
			tvTimeNews.setTextColor(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
			tvAuthor.setTextColor(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
			imgDot.setColorFilter(isDarkTheme ? getColor(R.color.dark_gray) : getColor(R.color.light_gray));
			tvTitleNews.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			imgRead.setColorFilter(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
			imgNews.setColorFilter(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
			GlideImage.loadImage(new_.getAttachments().get(0).getUrl(), R.drawable.news, imgNews);
			tvTitleNews.setText(new_.getTitle());
			tvTimeNews.setText(TimeAgo.using(DateUtil.toTimestamp(new_.getDate()), new TimeAgoMessages.Builder().withLocale(Locale.forLanguageTag("us")).build()));
			tvAuthor.setText(new_.getAuthor().getName());
			imgRead.setVisibility(new_.isRead() ? View.INVISIBLE : View.VISIBLE);
		}

		private int getColor(int color){
			return ContextCompat.getColor(itemView.getContext(), color);
		}
	}
}
