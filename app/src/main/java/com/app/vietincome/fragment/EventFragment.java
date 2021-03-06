package com.app.vietincome.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.app.vietincome.model.Coin;
import com.app.vietincome.model.Event;
import com.app.vietincome.model.responses.EventResponse;
import com.app.vietincome.model.responses.TokenResponse;
import com.app.vietincome.network.ApiClient;
import com.app.vietincome.network.AppConfig;
import com.app.vietincome.utils.Constant;
import com.app.vietincome.utils.DateUtil;
import com.app.vietincome.view.CustomItemDecoration;
import com.app.vietincome.view.NavigationTopBar;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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
	private ArrayList<Coin> coins;
	private String coin = "";
	private String dateStart = DateUtil.getStrCurrentDate();
	private String dateEnd = DateUtil.getFirstDay3Years();
	private int page = 1;
	private boolean isCompleted = true;
	private boolean canLoadMore;
	private boolean isLoading;
	private LinearLayoutManager layoutEvent;

	public static EventFragment newInstance() {
		EventFragment fragment = new EventFragment();
		return fragment;
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventRefreshData(EventBusListener.RefreshData event) {
		if (event.tab == Constant.TAB_EVENT) {
			page = 1;
			getEvents(true, false);
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventUpdateEvents(EventBusListener.UpdateEvent event) {
		page = 1;
		getEvents(true, false);
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
		}
		onUpdatedTheme();
		layoutEvent = new LinearLayoutManager(getContext());
		rcvEvent.setLayoutManager(layoutEvent);
		rcvEvent.addItemDecoration(new CustomItemDecoration(30));
		rcvEvent.setDemoShimmerDuration(1000000);
		rcvEvent.setAdapter(eventAdapter);
		checkToken();
		rcvEvent.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				int last = layoutEvent.findLastVisibleItemPosition();
				if (last >= eventAdapter.getItemCount() - 1 && canLoadMore && !isLoading)
					loadMoreEvent();
			}
		});
		try {
			setTextPeriod();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private void checkToken() {
		if (AppPreference.INSTANCE.getToken() == null) {
			getToken();
			return;
		} else if (DateUtil.getDiffMinutes(DateUtil.getCurrentDate(), AppPreference.INSTANCE.getToken().getExpireAt()) < 1440) {
			getToken();
			return;
		}
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
				Log.d("__", "onFailure: " + t.getMessage());
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
		isLoading = true;
		ApiClient.getEventService().getEventInPage(
				AppPreference.INSTANCE.getToken().getAccessToken(),
				dateStart,
				dateEnd,
				page,
				coin
		).enqueue(new Callback<EventResponse>() {
			@Override
			public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
				navigationTopBar.hideProgressBar();
				rcvEvent.hideShimmerAdapter();
				isLoading = false;
				if (response.isSuccessful()) {
					if (response.body() != null) {
						events.clear();
						events.addAll(response.body().getEvents());
						eventAdapter.notifyDataSetChanged();
						canLoadMore = response.body().getMetadata().getPage() != response.body().getMetadata().getPageCount();
						page++;
						tvNotFound.setVisibility(response.body().getEvents().size() == 0 ? View.VISIBLE : View.GONE);
					}
				}
			}

			@Override
			public void onFailure(Call<EventResponse> call, Throwable t) {
				navigationTopBar.hideProgressBar();
				rcvEvent.hideShimmerAdapter();
				isLoading = false;
				Log.d("__", "onFailure: " + t.getMessage());
			}
		});
	}

	private void loadMoreEvent() {
		isLoading = true;
		showBottomDialog();
		ApiClient.getEventService().getEventInPage(
				AppPreference.INSTANCE.getToken().getAccessToken(),
				dateStart,
				dateEnd,
				page,
				coin
		).enqueue(new Callback<EventResponse>() {
			@Override
			public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
				isLoading = false;
				hideProgressDialog();
				if (response.isSuccessful()) {
					if (response.body() != null) {
						events.addAll(response.body().getEvents());
						eventAdapter.notifyItemRangeChanged((page - 1) * 150, events.size());
						canLoadMore = response.body().getMetadata().getPage() != response.body().getMetadata().getPageCount();
						page++;
					}
				}
			}

			@Override
			public void onFailure(Call<EventResponse> call, Throwable t) {
				hideProgressDialog();
				isLoading = false;
				Log.d("__", "onFailure: " + t.getMessage());
			}
		});
	}

	private void getListCoins() {
		showProgressDialog();
		ApiClient.getEventService().getCoins(AppPreference.INSTANCE.getToken().getAccessToken()).enqueue(new Callback<List<Coin>>() {
			@Override
			public void onResponse(Call<List<Coin>> call, Response<List<Coin>> response) {
				hideProgressDialog();
				if (response.isSuccessful()) {
					if (response.body() != null) {
						coins = (ArrayList<Coin>) response.body();
						CoinDialog dialog = CoinDialog.newIntance(coins, EventFragment.this);
						dialog.show(getFragmentManager(), "coin");
					}
				}
			}

			@Override
			public void onFailure(Call<List<Coin>> call, Throwable t) {
				Log.d("__", "onFailure: " + t.getMessage());
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

	public void setTextColor(TextView textView) {
		textView.setTextColor(isDarkTheme ? getColor(R.color.yellow_text) : getColor(R.color.light_image));
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
		if (coins == null) {
			getListCoins();
		} else {
			CoinDialog dialog = CoinDialog.newIntance(coins, this);
			dialog.show(getFragmentManager(), "coin");
		}
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
			try {
				setTextPeriod();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			page = 1;
			getEvents(true, false);
		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		datePickerDialog.show();
	}

	private void setTextPeriod() throws ParseException {
		if (dateStart.equals(DateUtil.getStrCurrentDate())) {
			tvPeriodValue.setText("Today" + "-" + DateUtil.parseDate(dateEnd, "dd/MM/yy"));
		} else {
			tvPeriodValue.setText(DateUtil.parseDate(dateStart, "dd/MM/yy") + "-" + DateUtil.parseDate(dateEnd, "dd/MM/yy"));
		}
	}

	@Override
	public void onSelectedCoin(String coinId, String name) {
		this.coin = coinId;
		page = 1;
		tvCoinValue.setText(name);
		getEvents(true, false);
	}

	@Override
	public void onCancel() {
		this.coin = "";
		page = 1;
		tvCoinValue.setText("All");
		getEvents(true, false);
	}
}
