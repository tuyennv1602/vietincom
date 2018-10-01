package com.app.vietincome.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.app.vietincome.R;
import com.app.vietincome.adapter.NewsAdapter;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.EventBusListener;
import com.app.vietincome.manager.interfaces.ItemClickListener;
import com.app.vietincome.model.News;
import com.app.vietincome.model.responses.CoinResponse;
import com.app.vietincome.model.responses.NewsResponse;
import com.app.vietincome.network.ApiClient;
import com.app.vietincome.view.CustomItemDecoration;
import com.app.vietincome.view.NavigationTopBar;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;
import com.google.android.gms.common.api.Api;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
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

public class NewsFragment extends BaseFragment implements ItemClickListener {

	@BindView(R.id.rcv_news)
	ShimmerRecyclerView rcvNews;

	@BindView(R.id.layoutRoot)
	LinearLayout layoutRoot;

	private NewsAdapter newsAdapter;
	private ArrayList<News> news;
	private int page = 1;
	private boolean needReload = true;

	public static NewsFragment newInstance() {
		NewsFragment fragment = new NewsFragment();
		fragment.needReload = true;
		return fragment;
	}

	public static NewsFragment newInstance(ArrayList<News> news) {
		NewsFragment fragment = new NewsFragment();
		fragment.news = news;
		fragment.needReload = false;
		return fragment;
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventUpdateNews(EventBusListener.UpdateNews event) {
		page = 1;
		getNews(false);
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_news;
	}

	@Override
	public void onFragmentReady(View view) {
		if (news == null) {
			news = new ArrayList<>();
		}
		if (newsAdapter == null) {
			newsAdapter = new NewsAdapter(news, this);
			newsAdapter.setDarkTheme(isDarkTheme);
		}
		rcvNews.setLayoutManager(new LinearLayoutManager(getContext()));
		rcvNews.addItemDecoration(new CustomItemDecoration(2));
		rcvNews.setDemoShimmerDuration(1000000);
		rcvNews.setNestedScrollingEnabled(false);
		rcvNews.setDemoLayoutReference(isDarkTheme ? R.layout.layout_demo_news_dark : R.layout.layout_demo_news_light);
		rcvNews.setAdapter(newsAdapter);
		if (needReload) {
			getNews(true);
		}
	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
		navitop.setTvTitle(R.string.news);
		navitop.showImgLeft(false);
		navitop.showImgRight(false);
		if (!needReload) {
			navitop.setBackgroundBorder();
			navitop.showImgRight(true);
			navitop.setImgRight(R.drawable.arrow_down);
			navitop.changeRightPadding(18);
		}
	}

	@Override
	public void onRightClicked() {
		super.onRightClicked();
		goBack();
	}

	@Override
	public void onUpdatedTheme() {
		rcvNews.setBackgroundColor(isDarkTheme ? getColor(R.color.black) : getColor(R.color.color_line));
		rcvNews.setDemoLayoutReference(isDarkTheme ? R.layout.layout_demo_news_dark : R.layout.layout_demo_news_light);
		newsAdapter.setDarkTheme(isDarkTheme);
		newsAdapter.notifyDataSetChanged();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void getNews(boolean showShimmer) {
		navigationTopBar.showProgressBar();
		if (showShimmer) {
			rcvNews.showShimmerAdapter();
		}
		ApiClient.getNewsService().getNews().enqueue(new Callback<NewsResponse>() {
			@Override
			public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
				navigationTopBar.hideProgressBar();
				rcvNews.hideShimmerAdapter();
				if (response.isSuccessful()) {
					if (response.body().isSuccess()) {
						news.clear();
						news.addAll(response.body().getNews());
						AppPreference.INSTANCE.setNews(news);
						newsAdapter.notifyDataSetChanged();
						if (response.body().getPages() > 1) {
							getNextPage(response.body().getPages());
						}
					}
				}
			}

			@Override
			public void onFailure(Call<NewsResponse> call, Throwable t) {
				navigationTopBar.hideProgressBar();
				rcvNews.hideShimmerAdapter();
				showAlert("Failed", "Get News: " + t.getMessage());
			}
		});
	}

	@SuppressLint("CheckResult")
	private void getNextPage(int totalPage) {
		Observable.range(2, totalPage - 1)
				.subscribeOn(Schedulers.io())
				.concatMap((Function<Integer, ObservableSource<NewsResponse>>) integer -> ApiClient.getNewsService().getNewsInPage(integer))
				.doOnError(throwable -> {
					Log.d("__news", "getNextPage: error");
				})
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeWith(new Observer<NewsResponse>() {
					@Override
					public void onSubscribe(Disposable d) {

					}

					@Override
					public void onNext(NewsResponse newsResponse) {
						page++;
						news.addAll(newsResponse.getNews());
						newsAdapter.notifyItemRangeChanged((page - 1) * 10, news.size());
						AppPreference.INSTANCE.setNews(news);
					}

					@Override
					public void onError(Throwable e) {
						showAlert("Failed", "Get News: " + e.getMessage());
					}

					@Override
					public void onComplete() {

					}
				});
	}

	@Override
	public void onItemClicked(int position) {
		AppPreference.INSTANCE.addNewsRead(news.get(position).getId());
		newsAdapter.notifyItemChanged(position);
		openLink(news.get(position).getUrl());
	}


}
