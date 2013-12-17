package com.restoar.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.restoar.R;
import com.restoar.data.service.LocationHelper;
import com.restoar.data.service.RestoARCacheService;
import com.restoar.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class LoadingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		new AsyncTask<Void, Void, Void>() {
			
			protected void onPreExecute() {
				LocationHelper.getInstance(getApplicationContext()).start();
				load();
			};

			@Override
			protected Void doInBackground(Void... params) {
				return null;
			}
			
			protected void onPostExecute(Void result) {
				next();
			};
			
		}.execute();
	}
	
	private void next() {
		Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}
	
	public void load() {
		RestoARCacheService instance = RestoARCacheService.getINSTANCE();
		instance.getCommerces();
		instance.getAdvertisements();
		instance.getCategories();
	}

}
