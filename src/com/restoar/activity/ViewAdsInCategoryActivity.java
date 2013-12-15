package com.restoar.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.restoar.R;
import com.restoar.activity.views.AdListAdapter;
import com.restoar.data.service.RestoARCacheService;
import com.restoar.model.PlainAdvertisement;

public class ViewAdsInCategoryActivity extends Activity {

	protected static final String CATEGORY_PARAM = "category";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_results);
		String category = getIntent().getStringExtra(CATEGORY_PARAM);
		
		TextView text = (TextView) findViewById(R.id.searchedText);
		text.setText("Resultados en la categoria " + category);
		
		Map<String, List<PlainAdvertisement>> ads = RestoARCacheService.getINSTANCE().getCategoriesMap();
		Collection<PlainAdvertisement> filtered = ads.get(category);
		ListView list = (ListView) findViewById(R.id.searchResultsList);
		list.setAdapter(new AdListAdapter(getApplicationContext(), new ArrayList<PlainAdvertisement>(filtered)));
	}

}
