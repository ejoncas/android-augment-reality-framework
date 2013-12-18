package com.restoar.activity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.restoar.R;
import com.restoar.data.service.RestoARCacheService;
import com.restoar.model.PlainAdvertisement;

public class ViewAdActivity extends Activity {

	public static final String ID_PARAM = "idparam";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_ad);
		String id = getIntent().getStringExtra(ID_PARAM);
		PlainAdvertisement ad = RestoARCacheService.getINSTANCE()
				.getPlainAdvertisement(id);
		getShaKey();
		fillView(ad);
	}

	private void getShaKey() {

		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"your.package.name", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.v("MAPS_PROBLEM",
						"KeyHash:"
								+ Base64.encodeToString(md.digest(),
										Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();

		}

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

		CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(ad
				.getLatitude(), ad.getLongitude()));
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

		map.moveCamera(center);
		map.animateCamera(zoom);
		map.addMarker(new MarkerOptions().position(
				new LatLng(ad.getLatitude(), ad.getLongitude())).title(
				ad.getTitle()));
	}
}
