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
import com.restoar.model.Commerce;
import com.restoar.model.PlainAdvertisement;
import com.restoar.widget.utils.NetworkUtils;

public class RestoARApiService implements RestoARService {

	private static final String DEFAULT_CATEFORY = "Pizza";
	private static final String ENDPOINT = "http://www.restoar.com.ar";
	private static final String AD_URL = "/api/advertisement/";
	private static final String ADS_URL = "/api/advertisement/all";
	private static final String CATEGORIES_URL = "/api/categories";
	private static final String COMMERCE_URL = "/api/commerce/all";

	private String endpoint;

	public RestoARApiService(String backend) {
		this.endpoint = backend;
	}

	public RestoARApiService() {
		this(ENDPOINT);
	}

	@Override
	public List<Commerce> getCommerces() {
		List<Commerce> commerces = Lists.newArrayList();
		try {
			JSONObject json = new HttpGetTask(endpoint.concat(COMMERCE_URL))
					.execute().get();
			commerces = parseCommerces(json);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return commerces;
	}
	
	private List<Commerce> parseCommerces(JSONObject root) {
		if (root == null)
			throw new NullPointerException();

		JSONObject jo = null;
		JSONArray dataArray = null;
		List<Commerce> commerces = new ArrayList<Commerce>();
		try {
			if (root.has("data"))
				dataArray = root.getJSONArray("data");
			if (dataArray == null)
				return commerces;
			for (int i = 0; i < dataArray.length(); i++) {
				jo = dataArray.getJSONObject(i);
				Commerce ma = parseCommerce(jo);
				if (ma != null)
					commerces.add(ma);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return commerces;
	}

	private Commerce parseCommerce(JSONObject jo) {
		if (jo == null)
			throw new NullPointerException();
		if (!jo.has("location"))
			throw new NullPointerException();
		Commerce commerce = null;
		try {
			Double lat = null, lon = null;
			if (!jo.isNull("location")) {
				JSONObject geo = jo.getJSONObject("location");
				lat = Double.parseDouble(geo.getString("latitude"));
				lon = Double.parseDouble(geo.getString("longitude"));
			}
			if (lat != null && lon != null) {
				String title = jo.getString("name");
				String description = jo.getString("description");
				String category = jo.has("category") ? jo.getString("category")
						: DEFAULT_CATEFORY;
				Long id = jo.getLong("id");
				String address = jo.has("adress") ? jo.getString("address")
						: "FIXME";
				commerce = new Commerce(id, title, description, address, category, lat, lon);
				if (jo.has("advertisements")) { 
					JSONArray ads = jo.getJSONArray("advertisements");
					for (int i = 0; i < ads.length(); i++) {
						JSONObject ad = ads.getJSONObject(i);
						commerce.addAdvertisement(parseAd(ad));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return commerce;
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
		Advertisement ad = null;
		try {
				String title = jo.getString("title");
				String description = jo.getString("description");
				String id = jo.getString("id");
				ad = new Advertisement(id, title, description);
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

	@Override
	public Advertisement getAdvertisement(String id) {
		Advertisement ad = null;
		try {
			JSONObject json = new HttpGetTask(endpoint.concat(AD_URL)
					.concat(id)).execute().get();
			ad = parseAd(json);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return ad;
	}

	@Override
	public List<PlainAdvertisement> getPlainAdvertisements() {
		throw new RuntimeException("not implemented");
	}

	@Override
	public PlainAdvertisement getPlainAdvertisement(String id) {
		throw new RuntimeException("not implemented");
	}

}
