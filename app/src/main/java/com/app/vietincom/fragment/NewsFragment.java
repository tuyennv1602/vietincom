package com.app.vietincom.fragment;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.app.vietincom.R;
import com.app.vietincom.adapter.NewsAdapter;
import com.app.vietincom.bases.BaseFragment;
import com.app.vietincom.manager.AppPreference;
import com.app.vietincom.manager.EventBusListener;
import com.app.vietincom.manager.interfaces.ItemClickListener;
import com.app.vietincom.model.News;
import com.app.vietincom.model.responses.NewsResponse;
import com.app.vietincom.network.ApiClient;
import com.app.vietincom.view.CustomItemDecoration;
import com.app.vietincom.view.NavigationTopBar;
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

	private NewsAdapter newsAdapter;
	private ArrayList<News> news;

	public static NewsFragment newInstance() {
		NewsFragment fragment = new NewsFragment();
		return fragment;
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventRefreshData(EventBusListener.RefreshData event) {
		getNews();
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_news;
	}

	@Override
	public void onFragmentReady(View view) {
		if(news == null){
			news = new ArrayList<>();
		}
		if(newsAdapter == null){
			newsAdapter = new NewsAdapter(this);
			newsAdapter.setNews(news);
			newsAdapter.setDarkTheme(isDarkTheme);
		}
		rcvNews.setLayoutManager(new LinearLayoutManager(getContext()));
		rcvNews.addItemDecoration(new CustomItemDecoration(2));
		rcvNews.setDemoShimmerDuration(60000);
		rcvNews.setDemoLayoutReference(isDarkTheme ? R.layout.layout_demo_news_dark : R.layout.layout_demo_news_light);
		rcvNews.setAdapter(newsAdapter);
		getNews();
	}

	@Override
	public void onNavigationTopUpdate(NavigationTopBar navitop) {
		navitop.setTvTitle(R.string.news);
		navitop.showImgLeft(false);
		navitop.showImgRight(false);
	}

	@Override
	public void onUpdatedTheme() {
		rcvNews.setDemoLayoutReference(isDarkTheme ? R.layout.layout_demo_news_dark : R.layout.layout_demo_news_light);
		newsAdapter.setDarkTheme(isDarkTheme);
		newsAdapter.notifyDataSetChanged();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void getNews() {
		navigationTopBar.showProgressBar();
		rcvNews.showShimmerAdapter();
		ApiClient.getNewsService().getNews().enqueue(new Callback<NewsResponse>() {
			@Override
			public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
				navigationTopBar.hideProgressBar();
				rcvNews.hideShimmerAdapter();
				if (response.isSuccessful()) {
					if (response.body().isSuccess()) {
						news = response.body().getNews();
						newsAdapter.setNews(news);
					}
				}
			}

			@Override
			public void onFailure(Call<NewsResponse> call, Throwable t) {
				navigationTopBar.hideProgressBar();
				rcvNews.hideShimmerAdapter();
				showAlert(getString(R.string.failed), t.getMessage());
			}
		});
	}

	@Override
	public void onItemClicked(int position) {
		AppPreference.INSTANCE.addNewsRead(news.get(position).getId());
		newsAdapter.notifyItemChanged(position);
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.get(position).getUrl()));
		startActivity(browserIntent);
	}
}
