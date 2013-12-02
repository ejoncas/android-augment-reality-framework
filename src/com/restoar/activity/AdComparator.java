package com.restoar.activity;

import java.util.Comparator;

import android.location.Location;

import com.restoar.model.Advertisement;

public class AdComparator implements Comparator<Advertisement> {

	@Override
	public int compare(Advertisement arg0, Advertisement arg1) {
		float[] results = new float[1];
		Location.distanceBetween(arg0.getLatitude(), arg1.getLatitude(), arg0.getLongitude(), arg1.getLongitude(), results);
		return (int) results[0];
	}

}
