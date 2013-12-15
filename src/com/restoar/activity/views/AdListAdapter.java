package com.restoar.activity.views;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.restoar.R;
import com.restoar.model.PlainAdvertisement;

public class AdListAdapter extends BaseAdapter {

	private List<PlainAdvertisement> ads;
	private Context context;

	public AdListAdapter(Context context, List<PlainAdvertisement> ads) {
		super();
		this.ads = ads;
		this.context = context;
	}

	@Override
	public int getCount() {
		return ads.size();
	}

	@Override
	public Object getItem(int arg0) {
		return ads.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return getItem(arg0).hashCode();
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		View itemView = view;
		if (itemView == null) {
			LayoutInflater vi = LayoutInflater.from(context);
			itemView = vi.inflate(R.layout.ad_item, null);
		}
		((TextView)itemView.findViewById(R.id.item_title)).setText(ads.get(pos).getTitle());
		((TextView)itemView.findViewById(R.id.item_subtitle)).setText(ads.get(pos).getDescription());
		return itemView;
	}

}
