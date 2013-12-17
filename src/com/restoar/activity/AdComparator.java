package com.restoar.activity;

import java.util.Comparator;

import android.location.Location;

import com.restoar.model.PlainAdvertisement;

public class AdComparator implements Comparator<PlainAdvertisement> {

	private Location userLocation;

	public AdComparator(Location userLocation) {
		this.userLocation = userLocation;
	}

	@Override
	public int compare(PlainAdvertisement arg0, PlainAdvertisement arg1) {
		float[] results1 = new float[1];
		float[] results2 = new float[1];
		
		Location.distanceBetween(arg0.getLatitude(), arg0.getLongitude(), userLocation.getLatitude(), userLocation.getLongitude(), results1);
		Location.distanceBetween(arg0.getLatitude(), arg0.getLongitude(), userLocation.getLatitude(), userLocation.getLongitude(), results2);
		
		return (int) (results1[0] - results2[0]);
	}

}
