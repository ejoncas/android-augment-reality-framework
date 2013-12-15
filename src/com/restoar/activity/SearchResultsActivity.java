package com.restoar.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.restoar.R;
import com.restoar.activity.views.AdListAdapter;
import com.restoar.data.service.RestoARCacheService;
import com.restoar.model.PlainAdvertisement;

public class SearchResultsActivity extends Activity {

	public  static final String SEARCH_PARAM = "search";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_results);
		String searchQuery = getIntent().getStringExtra(SEARCH_PARAM);
		
		TextView text = (TextView) findViewById(R.id.searchedText);
		text.setText("Resultados de la busqueda  " + searchQuery);
		
		List<PlainAdvertisement> ads = RestoARCacheService.getINSTANCE().getPlainAdvertisements();
		
		Collection<PlainAdvertisement> filtered = Collections2.filter(ads, new SearchPredicate(searchQuery));
		
		ListView list = (ListView) findViewById(R.id.searchResultsList);
		list.setAdapter(new AdListAdapter(getApplicationContext(), new ArrayList<PlainAdvertisement>(filtered)));
	}

	private static class SearchPredicate implements Predicate<PlainAdvertisement> {
		
		private String search;
		
		public SearchPredicate(String search) {
			this.search = search.toLowerCase();
		}
		
		public boolean apply(PlainAdvertisement pa)  {
			boolean result = false;
			result |= pa.getAddress().toLowerCase().contains(search);
			result |= pa.getCategory().toLowerCase().contains(search);
			result |= pa.getCommerceDescription().toLowerCase().contains(search);
			result |= pa.getCommerceName().toLowerCase().contains(search);
			result |= pa.getDescription().toLowerCase().contains(search);
			result |= pa.getTitle().toLowerCase().contains(search);
			for (String tag : pa.getTags()) {
				result |= tag.toLowerCase().contains(search);
			}
			return result;
		}
		
	}

}
