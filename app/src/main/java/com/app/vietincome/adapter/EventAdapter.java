package com.app.vietincome.adapter;

import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.manager.interfaces.EventClickListener;
import com.app.vietincome.model.Event;
import com.app.vietincome.utils.CommonUtil;
import com.app.vietincome.utils.DateUtil;
import com.app.vietincome.view.CustomSeekbar;
import com.app.vietincome.view.HighLightTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

	private ArrayList<Event> events;
	private boolean isDarkTheme;
	private EventClickListener listener;

	public EventAdapter(ArrayList<Event> events, EventClickListener listener) {
		this.events = events;
		this.listener = listener;
	}

	public void setDarkTheme(boolean darkTheme) {
		isDarkTheme = darkTheme;
	}


	@NonNull
	@Override
	public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		return new EventViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_event, viewGroup, false));
	}

	@Override
	public void onBindViewHolder(@NonNull EventViewHolder eventViewHolder, int i) {
		eventViewHolder.onBind(events.get(i));
	}

	@Override
	public int getItemCount() {
		return events == null ? 0 : events.size();
	}

	class EventViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.layoutRoot)
		LinearLayout layoutRoot;

		@BindView(R.id.tvTime)
		TextView tvTime;

		@BindView(R.id.tvEventCate)
		TextView tvEventCate;

		@BindView(R.id.imgHot)
		ImageView imgHot;

		@BindView(R.id.tvTitle)
		TextView tvTitle;

		@BindView(R.id.tvDescription)
		TextView tvDescription;

		@BindView(R.id.tvProof)
		HighLightTextView tvProof;

		@BindView(R.id.tvSource)
		HighLightTextView tvSource;

		@BindView(R.id.viewLine)
		View viewLine;

		@BindView(R.id.tvValidation)
		TextView tvValidation;

		@BindView(R.id.seekBar)
		CustomSeekbar seekBar;

		@BindView(R.id.layoutInfo)
		LinearLayout layoutInfo;

		public EventViewHolder(@NonNull View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			tvProof.setOnClickListener(view -> {
				if (listener != null) {
					listener.onClickProof(getAdapterPosition());
				}
			});
			tvSource.setOnClickListener(view -> {
				if (listener != null) {
					listener.onClickSource(getAdapterPosition());
				}
			});
		}

		public void onBind(Event event) {
			layoutRoot.setBackgroundResource(isDarkTheme ? R.drawable.border_event_dark : R.drawable.border_event_light);
			layoutInfo.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_info) : getColor(R.color.light_info));
			tvDescription.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			viewLine.setBackgroundColor(isDarkTheme ? getColor(R.color.white) : getColor(R.color.color_line));
			tvValidation.setTextColor(isDarkTheme ? getColor(R.color.dark_text) : getColor(R.color.light_text));
			tvEventCate.setTextColor(isDarkTheme ? getColor(R.color.yellow_text) : getColor(R.color.white));
			tvProof.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
			tvSource.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
			imgHot.setVisibility(event.getIsHot() ? View.VISIBLE : View.GONE);
			tvTime.setText(DateUtil.getStringTime(event.getDateEvent(), DateUtil.FORMAT_EVENT));
			StringBuilder builder = new StringBuilder();
			for(int i = 0; i< event.getCategories().size(); i++){
				builder.append(event.getCategories().get(i).getName());
				if(i != event.getCategories().size() - 1){
					builder.append(",");
				}
			}
			tvEventCate.setText(builder.toString());
			tvTitle.setText(event.getTitle());
			tvDescription.setText(event.getDescription());
			tvValidation.setText(new StringBuilder().append("Validation (").append(event.getPositiveVoteCount()).append("/").append(event.getVoteCount()).append(" votes)").toString());
			seekBar.getProgressDrawable().setColorFilter(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image),PorterDuff.Mode.SRC_ATOP);
			seekBar.setThumb(isDarkTheme ? itemView.getContext().getDrawable(R.drawable.seekbar_thumb_gray) : itemView.getContext().getDrawable(R.drawable.seekbar_thumb_white));
			seekBar.setTextColor(isDarkTheme ? getColor(R.color.dark_image) : getColor(R.color.light_image));
			seekBar.setProgress(event.getPercentage());
			if(event.getPercentage() < 5){
				seekBar.setPadding(CommonUtil.dp2pxInt(10), 0, 0, 0);
			}
		}

		private int getColor(int color) {
			return ContextCompat.getColor(itemView.getContext(), color);
		}

	}
}
