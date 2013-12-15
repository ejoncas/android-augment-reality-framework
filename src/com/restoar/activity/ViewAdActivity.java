package com.restoar.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.restoar.R;
import com.restoar.data.service.RestoARCacheService;
import com.restoar.model.PlainAdvertisement;

public class ViewAdActivity extends Activity {

	public  static final String ID_PARAM = "idparam";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_ad);
		String id = getIntent().getStringExtra(ID_PARAM);
		PlainAdvertisement ad = RestoARCacheService.getINSTANCE().getPlainAdvertisement(id);
		fillView(ad);
	}

	private void fillView(PlainAdvertisement ad) {
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(ad.getTitle());
		TextView subtitle = (TextView) findViewById(R.id.subtitle);
		subtitle.setText(ad.getDescription());
		TextView address = (TextView) findViewById(R.id.address);
		address.setText(ad.getAddress());
		TextView category = (TextView) findViewById(R.id.category);
		category.setText(ad.getCategory());

		GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();
		map.addMarker(new MarkerOptions().position(
				new LatLng(ad.getLatitude(), ad.getLongitude())).title(
				ad.getTitle()));
	}
}
