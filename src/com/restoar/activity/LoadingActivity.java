package com.restoar.activity;

import com.restoar.R;
import com.restoar.R.id;
import com.restoar.R.layout;
import com.restoar.data.service.RestoARCacheService;
import com.restoar.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class LoadingActivity extends Activity {

	Handler mHideHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		mHideHandler.post(new Runnable() {
			@Override
			public void run() {
				load();
				next();
			}

		});
	}
	
	private void next() {
		Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}
	
	public void load() {
		RestoARCacheService.getINSTANCE().getAdvertisements();
	}

}
