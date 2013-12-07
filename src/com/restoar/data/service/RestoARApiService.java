package com.restoar.data.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.google.common.collect.Lists;
import com.restoar.model.Advertisement;
import com.restoar.widget.utils.NetworkUtils;

public class RestoARApiService implements RestoARService {

	private static final String ENDPOINT = "http://www.restoar.com.ar";
	private static final String ADS_URL = "/api/advertisement/all";
	private static final String CATEGORIES_URL = "/api/categories";

	private String endpoint;

	public RestoARApiService(String backend) {
		this.endpoint = backend;
	}

	public RestoARApiService() {
		this(ENDPOINT);
	}

	@Override
	public List<Advertisement> getAdvertisements() {
		List<Advertisement> ads = Lists.newArrayList();
		try {
			JSONObject json = new HttpGetTask(endpoint.concat(ADS_URL))
					.execute().get();
			ads = parseAds(json);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return ads;
	}

	static class HttpGetTask extends AsyncTask<Void, Void, JSONObject> {

		private String url;

		public HttpGetTask(String url) {
			this.url = url;
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			JSONObject json = NetworkUtils.parseUrlAsJson(url);
			return json;
		}

	}

	private List<Advertisement> parseAds(JSONObject root) {
		if (root == null)
			throw new NullPointerException();

		JSONObject jo = null;
		JSONArray dataArray = null;
		List<Advertisement> markers = new ArrayList<Advertisement>();

		try {
			if (root.has("data"))
				dataArray = root.getJSONArray("data");
			if (dataArray == null)
				return markers;
			for (int i = 0; i < dataArray.length(); i++) {
				jo = dataArray.getJSONObject(i);
				Advertisement ma = parseAd(jo);
				if (ma != null)
					markers.add(ma);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return markers;
	}

	private Advertisement parseAd(JSONObject jo) {
		if (jo == null)
			throw new NullPointerException();
		if (!jo.has("location"))
			throw new NullPointerException();
		Advertisement ad = null;
		try {
			Double lat = null, lon = null;
			if (!jo.isNull("location")) {
				JSONObject geo = jo.getJSONObject("location");
				lat = Double.parseDouble(geo.getString("latitude"));
				lon = Double.parseDouble(geo.getString("longitude"));
			}
			if (lat != null && lon != null) {
				String title = jo.getString("title");
				String description = jo.getString("description");
				String category = jo.getString("category");
				ad = new Advertisement(title, description, category, lat, lon);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ad;
	}

	@Override
	public List<String> getCategories() {
		List<String> categories = new ArrayList<String>();
		try {
			JSONObject json = new HttpGetTask(endpoint.concat(CATEGORIES_URL))
					.execute().get();
			if (json.has("data")) {
				JSONArray array = json.getJSONArray("data");
				for (int i = 0; i < array.length(); i++) {
					categories.add(array.getString(i));
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return categories;
	}

}
