package com.restoar.activity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.restoar.R;
import com.restoar.activity.views.StableArrayAdapter;
import com.restoar.data.service.LocationHelper;
import com.restoar.data.service.RestoARCacheService;
import com.restoar.model.PlainAdvertisement;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		private static final int FIRST_TAB = 0;
		private static final int SECOND_TAB = 1;
		private static final int THIRD_TAB = 2;

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			Bundle args = new Bundle();
			switch (position) {
			case FIRST_TAB:
				fragment = new BrowseCategories();
				break;
			case SECOND_TAB:
				fragment = new SearchByTag();
				break;
			case THIRD_TAB:
				fragment = new Nearby();
				break;
			}
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	public static class BrowseCategories extends Fragment {
		private Activity context;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.browse_categories,
					container, false);

			ListView list = (ListView) rootView
					.findViewById(R.id.listCategories);
			final Map<String, List<PlainAdvertisement>> adsMap = RestoARCacheService
					.getINSTANCE().getCategoriesMap();

			final StableArrayAdapter adapter = new StableArrayAdapter(context,
					android.R.layout.simple_list_item_1, buildLabels(adsMap));
			list.setAdapter(adapter);
			list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					String category = adapter.getItem(arg2);
					String cat = category.substring(0, category.indexOf("("))
							.trim();
					Intent intent = new Intent(getActivity(),
							ViewAdsInCategoryActivity.class);
					intent.putExtra(ViewAdsInCategoryActivity.CATEGORY_PARAM,
							cat);
					startActivity(intent);
				}
			});
			return rootView;
		}

		private List<String> buildLabels(
				Map<String, List<PlainAdvertisement>> map) {
			List<String> labels = Lists.newArrayList();
			for (String cat : map.keySet()) {
				labels.add(cat + " (" + map.get(cat).size() + ")");
			}
			return labels;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			this.context = activity;
		}
	}

	public static class Nearby extends Fragment {

		private Activity context;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.nearby, container, false);

			Button button = (Button) rootView
					.findViewById(R.id.nearby_augmented_reality);
			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context,
							AugmentedRealityActivity.class);
					startActivity(intent);
				}
			});
			ListView list = (ListView) rootView
					.findViewById(R.id.nearby_restaurants_lits);
			List<PlainAdvertisement> ads = RestoARCacheService.getINSTANCE()
					.getPlainAdvertisements();

			final Location userLocation = LocationHelper.getInstance(
					getActivity()).getLastKnownLocation();

			Collections.sort(ads, new AdComparator(userLocation));
			Log.i("RestoarLocation",
					"Current location is " + userLocation.toString());

			final NumberFormat nf = NumberFormat.getInstance(Locale.US);
			Collection<String> strings = Collections2.transform(ads,
					new Function<PlainAdvertisement, String>() {
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
					});
			final StableArrayAdapter adapter = new StableArrayAdapter(context,
					android.R.layout.simple_list_item_1, new ArrayList<String>(
							strings));
			list.setAdapter(adapter);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			this.context = activity;
		}

	}

	public static class SearchByTag extends Fragment {

		private Activity context;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.search_by_tag, container,
					false);
			Button button = (Button) rootView.findViewById(R.id.buttonBuscar);
			final TextView tv = (TextView) rootView
					.findViewById(R.id.searchField);
			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String textToSearch = tv.getText().toString();
					if (!"".equalsIgnoreCase(textToSearch)) {
						Intent intent = new Intent(context,
								SearchResultsActivity.class);
						intent.putExtra(SearchResultsActivity.SEARCH_PARAM,
								textToSearch);
						startActivity(intent);
					}
				}
			});
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			this.context = activity;
		}
	}

}
