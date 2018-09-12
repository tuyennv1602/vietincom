package com.app.vietincome.fragment;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.app.vietincome.R;
import com.app.vietincome.adapter.NewsAdapter;
import com.app.vietincome.bases.BaseFragment;
import com.app.vietincome.manager.AppPreference;
import com.app.vietincome.manager.EventBusListener;
import com.app.vietincome.manager.interfaces.ItemClickListener;
import com.app.vietincome.model.News;
import com.app.vietincome.model.responses.NewsResponse;
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
	public void onEventUpdateNews(EventBusListener.UpdateNews event) {
		getNews(false);
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
		getNews(true);
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

	private void getNews(boolean showShimmer) {
		navigationTopBar.showProgressBar();
		if(showShimmer) {
			rcvNews.showShimmerAdapter();
		}
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

	@Override
	public void onLongClicked(int position) {

	}
}
