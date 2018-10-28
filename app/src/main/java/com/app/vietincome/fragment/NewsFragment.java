package com.app.vietincome.fragment;

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
import com.app.vietincome.model.responses.NewsResponse;
import com.app.vietincome.network.ApiClient;
import com.app.vietincome.utils.Constant;
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

public class NewsFragment extends BaseFragment implements ItemClickListener {

	@BindView(R.id.rcv_news)
	ShimmerRecyclerView rcvNews;

	@BindView(R.id.layoutRoot)
	LinearLayout layoutRoot;

	private NewsAdapter newsAdapter;
	private ArrayList<News> news;
	private int page = 1;
	private boolean needReload = true;
	private boolean canLoadMore;
	private boolean isLoading;
	private LinearLayoutManager layoutNews;

	public static NewsFragment newInstance() {
		NewsFragment fragment = new NewsFragment();
		fragment.needReload = true;
		return fragment;
	}

	public static NewsFragment newInstance(ArrayList<News> news) {
		NewsFragment fragment = new NewsFragment();
		fragment.news = news;
		fragment.needReload = false;
		fragment.canLoadMore = true;
		return fragment;
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventUpdateNews(EventBusListener.UpdateNews event) {
		page = 1;
		getNews(false);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventRefreshData(EventBusListener.RefreshData event) {
		if (event.tab == Constant.TAB_NEWS) {
			page = 1;
			getNews(false);
		}
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
		layoutNews = new LinearLayoutManager(getContext());
		rcvNews.setLayoutManager(layoutNews);
		rcvNews.addItemDecoration(new CustomItemDecoration(2));
		rcvNews.setDemoShimmerDuration(1000000);
		rcvNews.setDemoLayoutReference(isDarkTheme ? R.layout.layout_demo_news_dark : R.layout.layout_demo_news_light);
		rcvNews.setAdapter(newsAdapter);
		getNews(needReload);
		rcvNews.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				int last = layoutNews.findLastVisibleItemPosition();
				if (last >= newsAdapter.getItemCount() - 1 && canLoadMore && !isLoading)
					loadMoreNews();
			}
		});
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
		isLoading = true;
		navigationTopBar.showProgressBar();
		if (showShimmer) {
			rcvNews.showShimmerAdapter();
		}
		ApiClient.getNewsService().getNews(page).enqueue(new Callback<NewsResponse>() {
			@Override
			public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
				navigationTopBar.hideProgressBar();
				rcvNews.hideShimmerAdapter();
				isLoading = false;
				if (response.isSuccessful()) {
					if (response.body().isSuccess()) {
						news.clear();
						news.addAll(response.body().getNews());
						AppPreference.INSTANCE.setNews(new ArrayList<>(news));
						newsAdapter.notifyDataSetChanged();
						canLoadMore = response.body().getCurrentPage() != response.body().getMaxPages();
						page++;
					}
				}
			}

			@Override
			public void onFailure(Call<NewsResponse> call, Throwable t) {
				isLoading = false;
				navigationTopBar.hideProgressBar();
				rcvNews.hideShimmerAdapter();
				Log.d("__", "onFailure: " + t.getMessage());
			}
		});
	}

	private void loadMoreNews() {
		isLoading = true;
		showBottomDialog();
		ApiClient.getNewsService().getNews(page).enqueue(new Callback<NewsResponse>() {
			@Override
			public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
				hideProgressDialog();
				isLoading = false;
				if (response.isSuccessful()) {
					if (response.body().isSuccess()) {
						news.addAll(response.body().getNews());
						newsAdapter.notifyItemRangeChanged((page - 1) * 10, news.size());
						canLoadMore = response.body().getCurrentPage() != response.body().getMaxPages();
						page++;
					}
				}
			}

			@Override
			public void onFailure(Call<NewsResponse> call, Throwable t) {
				isLoading = false;
				hideProgressDialog();
				Log.d("__", "onFailure: " + t.getMessage());
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
