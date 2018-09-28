package com.app.vietincome.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.app.vietincome.R;
import com.app.vietincome.adapter.EventAdapter;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.dialogs.CoinDialog;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.EventBusListener;
import com.app.vietincome.manager.interfaces.EventClickListener;
import com.app.vietincome.manager.interfaces.OnSelectedCoin;
import com.app.vietincome.model.Event;
import com.app.vietincome.model.responses.EventResponse;
import com.app.vietincome.model.responses.TokenResponse;
import com.app.vietincome.network.ApiClient;
import com.app.vietincome.network.AppConfig;
import com.app.vietincome.utils.DateUtil;
import com.app.vietincome.view.CustomItemDecoration;
import com.app.vietincome.view.NavigationTopBar;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventFragment extends BaseFragment implements EventClickListener, OnSelectedCoin {

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

	@BindView(R.id.tvNotFound)
	TextView tvNotFound;

	private ArrayList<Event> events;
	private EventAdapter eventAdapter;
	private String coins = "";
	private String dateStart = DateUtil.getFirstDayMonth();
	private String dateEnd = "";
	private int page = 1;
	private boolean isCompleted = true;

	public static EventFragment newInstance() {
		EventFragment fragment = new EventFragment();
		return fragment;
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventRefreshData(EventBusListener.RefreshData event) {
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventAddNews(EventBusListener.AddEvent event) {
		events.addAll(event.events);
		eventAdapter.notifyItemRangeChanged(event.position, events.size());
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_event;
	}

	@Override
	public void onFragmentReady(View view) {
		if (events == null) {
			events = new ArrayList<>();
		}
		if (eventAdapter == null) {
			eventAdapter = new EventAdapter(events, this);
			eventAdapter.setDarkTheme(isDarkTheme);
		}
		rcvEvent.setLayoutManager(new LinearLayoutManager(getContext()));
		rcvEvent.addItemDecoration(new CustomItemDecoration(30));
		rcvEvent.setDemoShimmerDuration(100000);
		rcvEvent.setNestedScrollingEnabled(false);
		rcvEvent.setAdapter(eventAdapter);
		checkToken();
		onUpdatedTheme();
		setTextPeriod();

	}

	private void checkToken() {
		if (AppPreference.INSTANCE.getToken() == null) {
			getToken();
			showToast("Get token");
			return;
		} else if (DateUtil.getDiffMinutes(DateUtil.getCurrentDate(), AppPreference.INSTANCE.getToken().getExpireAt()) < 1440) {
			getToken();
			showToast("Get token");
			return;
		}
		Log.d("__event", "checkToken: " + DateUtil.getDiffMinutes(DateUtil.getCurrentDate(), AppPreference.INSTANCE.getToken().getExpireAt()));
		getEvents(true, true);
	}

	public void getToken() {
		navigationTopBar.showProgressBar();
		rcvEvent.showShimmerAdapter();
		ApiClient.getEventService().getToken(AppConfig.CLIENT_ID, AppConfig.CLIENT_SECRET).enqueue(new Callback<TokenResponse>() {
			@Override
			public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
				if (response.isSuccessful()) {
					if (response.body() != null) {
						TokenResponse tokenResponse = response.body();
						tokenResponse.setExpireAt(DateUtil.getMonthAfterCurrent());
						AppPreference.INSTANCE.setToken(tokenResponse);
						getEvents(false, false);
					}
				}
			}

			@Override
			public void onFailure(Call<TokenResponse> call, Throwable t) {
				navigationTopBar.hideProgressBar();
				rcvEvent.hideShimmerAdapter();
				showAlert("Failure", "Get Token: " + t.getMessage());
			}
		});
	}

	public void getEvents(boolean showProgress, boolean showShimmer) {
		if (showProgress) {
			navigationTopBar.showProgressBar();
		}
		if (showShimmer) {
			rcvEvent.showShimmerAdapter();
		}
		ApiClient.getEventService().getEvent(
				AppPreference.INSTANCE.getToken().getAccessToken(),
				dateStart,
				dateEnd,
				coins
		).enqueue(new Callback<EventResponse>() {
			@Override
			public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
				navigationTopBar.hideProgressBar();
				rcvEvent.hideShimmerAdapter();
				if (response.isSuccessful()) {
					if (response.body() != null) {
						events.clear();
						events.addAll(response.body().getEvents());
						eventAdapter.notifyDataSetChanged();
						if (response.body().getMetadata().getPageCount() > 1) {
							getNextPage(response.body().getMetadata().getPageCount());
						}
					}
				}
			}

			@Override
			public void onFailure(Call<EventResponse> call, Throwable t) {
				navigationTopBar.hideProgressBar();
				rcvEvent.hideShimmerAdapter();
				showAlert("Failure", "Get Events: " + t.getMessage());
			}
		});
	}

	@SuppressLint("CheckResult")
	public void getNextPage(int totalPage) {
		Observable.range(2, totalPage - 1)
				.subscribeOn(Schedulers.io())
				.concatMap((Function<Integer, ObservableSource<EventResponse>>) integer -> ApiClient.getEventService().getEventInPage(
						AppPreference.INSTANCE.getToken().getAccessToken(),
						dateStart,
						dateEnd,
						integer,
						coins))
				.doOnError(throwable -> {

				})
				.doOnNext(eventResponse -> {
					page++;
					EventBus.getDefault().post(new EventBusListener.AddEvent(eventResponse.getEvents(), (page - 1) * 150));
					Log.d("__new", "getNextPage: ");
				})
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<EventResponse>() {
					@Override
					public void accept(EventResponse eventResponse) throws Exception {

					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception {

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
		setTextColor(tvNotFound);
		rcvEvent.setBackgroundColor(isDarkTheme ? getColor(R.color.dark_background) : getColor(R.color.light_background));
		rcvEvent.setDemoLayoutReference(isDarkTheme ? R.layout.layout_demo_event_dark : R.layout.layout_demo_event_light);
		eventAdapter.setDarkTheme(isDarkTheme);
		eventAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClickProof(int position) {
		openLink(events.get(position).getProof());
	}

	@Override
	public void onClickSource(int position) {
		openLink(events.get(position).getSource());
	}

	@OnClick(R.id.layoutPeriod)
	void getDate() {
		if (isCompleted) {
			isCompleted = false;
			showDatePickerStart();
		} else {
			showDatePickerEnd();
		}
	}

	@OnClick(R.id.layoutCoin)
	void getCoin() {
		CoinDialog dialog = CoinDialog.newIntance(this);
		dialog.show(getFragmentManager(), "coin");
	}

	private void showDatePickerStart() {
		final Calendar c = Calendar.getInstance();
		DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, year, month, day) -> {
			dateStart = (day / 10 == 0 ? "0" + day : day)
					+ "/" + ((month + 1) / 10 == 0 ? "0" + (month + 1) : month + 1) + "/" + year;
			showDatePickerEnd();
		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		datePickerDialog.show();
	}

	private void showDatePickerEnd() {
		final Calendar c = Calendar.getInstance();
		DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, year, month, day) -> {
			isCompleted = true;
			dateEnd = (day / 10 == 0 ? "0" + day : day)
					+ "/" + ((month + 1) / 10 == 0 ? "0" + (month + 1) : month + 1) + "/" + year;
			setTextPeriod();
			page = 1;
			getEvents(true, false);
		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		datePickerDialog.show();
	}

	private void setTextPeriod() {
		if (dateEnd.isEmpty() || dateEnd.equals(DateUtil.getStrCurrentDate())) {
			tvPeriodValue.setText(dateStart + "-Today");
		} else {
			tvPeriodValue.setText(dateStart + "-" + dateEnd);
		}
	}

	@Override
	public void onSelectedCoin(String coinId, String name) {
		this.coins = coinId;
		tvCoinValue.setText(name);
		getEvents(true, false);
	}

	@Override
	public void onCancel() {
		this.coins = "";
		tvCoinValue.setText("All");
		getEvents(true, false);
	}
}
