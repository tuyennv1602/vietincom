package com.app.vietincome.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.app.vietincome.adapter.AllCoinAdapter;
import com.app.vietincome.adapter.NewsAdapter;

public class CustomItemDecoration extends RecyclerView.ItemDecoration {
	private int space;

	public CustomItemDecoration(float space) {
		this.space = (int) space;
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
		if(parent.getAdapter() instanceof NewsAdapter || parent.getAdapter() instanceof AllCoinAdapter){
			if(parent.getChildAdapterPosition(view) == 0){
				return;
			}
		}
		outRect.top = space;
	}
}
