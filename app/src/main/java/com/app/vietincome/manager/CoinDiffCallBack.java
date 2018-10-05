package com.app.vietincome.manager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.app.vietincome.model.Data;

import java.util.ArrayList;

public class CoinDiffCallBack extends DiffUtil.Callback {

	private ArrayList<Data> oldData;
	private ArrayList<Data> newData;

	public CoinDiffCallBack(ArrayList<Data> oldData, ArrayList<Data> newData) {
		this.oldData = oldData;
		this.newData = newData;
	}

	@Override
	public int getOldListSize() {
		return oldData == null ? 0 : oldData.size();
	}

	@Override
	public int getNewListSize() {
		return newData == null ? 0 : newData.size();
	}

	@Override
	public boolean areItemsTheSame(int oldPos, int newPos) {
		return true;
	}

	@Override
	public boolean areContentsTheSame(int i, int i1) {
		final Data oldItem = oldData.get(i);
		final Data newItem = newData.get(i1);
		return oldItem.equals(newItem);
	}

	@Nullable
	@Override
	public Object getChangePayload(int oldItemPosition, int newItemPosition) {
		final Data oldItem = oldData.get(oldItemPosition);
		final Data newItem = newData.get(newItemPosition);
		Bundle diff = new Bundle();
		if (newItem.getQuotes().equals(oldItem.getQuotes())) {
			diff.putParcelable("quote", newItem.getQuotes());
		}
		return diff;
	}
}
