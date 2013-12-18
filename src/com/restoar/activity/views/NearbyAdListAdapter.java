package com.restoar.activity.views;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.restoar.model.PlainAdvertisement;

public class NearbyAdListAdapter extends BaseAdapter {

	private List<PlainAdvertisement> ads;
	private Context context;

	final NumberFormat nf = NumberFormat.getInstance(Locale.US);
	private Location userLocation;
	
	public NearbyAdListAdapter(Context context, Location userLocation, List<PlainAdvertisement> ads) {
		super();
		this.ads = ads;
		this.userLocation = userLocation;
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
		TextView itemView = (TextView) view;
		if (itemView == null) {
			LayoutInflater vi = LayoutInflater.from(context);
			itemView = (TextView) vi.inflate(android.R.layout.simple_list_item_1, null);
		}
		itemView.setText(apply(ads.get(pos)));
		itemView.setTag(ads.get(pos).getId());
		return itemView;
	}

	public String apply(PlainAdvertisement ad) {
		float[] results = new float[1];
		Location.distanceBetween(ad.getLatitude(),
				ad.getLongitude(),
				userLocation.getLatitude(),
				userLocation.getLongitude(), results);
		int distanceInMeters = ((int) results[0]);
		return ad.getTitle() + " \t"
				+ nf.format(distanceInMeters) + " m";
	}
}
