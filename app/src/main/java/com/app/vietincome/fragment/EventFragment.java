package com.app.vietincome.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.adapter.EventAdapter;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.manager.EventBusListener;
import com.app.vietincome.manager.interfaces.EventClickListener;
import com.app.vietincome.model.Event;
import com.app.vietincome.model.responses.EventResponse;
import com.app.vietincome.network.ApiClient;
import com.app.vietincome.view.CustomItemDecoration;
import com.app.vietincome.view.NavigationTopBar;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventFragment extends BaseFragment implements EventClickListener {

	@BindView(R.id.tvPeriod)
	TextView tvPeriod;

	@BindView(R.id.tvPeriodValue)
	TextView tvPeriodValue;

	@BindView(R.id.tvCoin)
	TextView tvCoin;

	@BindView(R.id.tvCoinValue)
	TextView tvCoinValue;

	@BindView(R.id.rcvEvents)
	ShimmerRecyclerView rcvEvent;

	private ArrayList<Event> events;
	private EventAdapter eventAdapter;

	public static EventFragment newInstance(){
		EventFragment fragment = new EventFragment();
		return fragment;
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventRefreshData(EventBusListener.RefreshData event) {
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_event;
	}

	@Override
	public void onFragmentReady(View view) {
		onUpdatedTheme();
		if(events == null){
			events = new ArrayList<>();
		}
		if(eventAdapter == null){
			eventAdapter = new EventAdapter(events, this);
			eventAdapter.setDarkTheme(isDarkTheme);
		}
		rcvEvent.setLayoutManager(new LinearLayoutManager(getContext()));
		rcvEvent.addItemDecoration(new CustomItemDecoration(20));
		rcvEvent.setDemoShimmerDuration(100000);
		rcvEvent.setNestedScrollingEnabled(false);
		rcvEvent.setAdapter(eventAdapter);
		getEvents(1,true);
	}

	public void getEvents(int page, boolean showShimmer){
		navigationTopBar.showProgressBar();
		if(showShimmer){
			rcvEvent.showShimmerAdapter();
		}
		ApiClient.getEventService().getEvent(
				"YmM3MTJlMGZhMWI1YzY3MDI3MDRjMTUwNmI0NWU4N2FlOTVlYjgxYzVmNTc5Y2ExNzFjOGYxOWFiZTQxYjI0Mw",
				page,
				"",
				"",
				"bitcoin"
		).enqueue(new Callback<EventResponse>() {
			@Override
			public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
				navigationTopBar.hideProgressBar();
				rcvEvent.hideShimmerAdapter();
				if(response.isSuccessful()){
					if(response.body() != null){
						if(page == 1){
							events.clear();
						}
						events.addAll(response.body().getEvents());
						eventAdapter.notifyDataSetChanged();
					}
				}
			}

			@Override
			public void onFailure(Call<EventResponse> call, Throwable t) {
				navigationTopBar.hideProgressBar();
				rcvEvent.hideShimmerAdapter();
				showAlert("Failure", t.getMessage());
			}
		});
	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
		navitop.setTvTitle(R.string.event);
		navitop.showImgLeft(false);
		navitop.showImgRight(false);
	}

	@Override
	public void onUpdatedTheme() {
		setTextColor(tvPeriod);
		setTextColor(tvPeriodValue);
		setTextColor(tvCoin);
		setTextColor(tvCoinValue);
		rcvEvent.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
		rcvEvent.setDemoLayoutReference(isDarkTheme ? R.layout.layout_demo_event_dark : R.layout.layout_demo_event_light);
	}

	@Override
	public void onClickProof(int position) {

	}

	@Override
	public void onClickSource(int position) {

	}
}
