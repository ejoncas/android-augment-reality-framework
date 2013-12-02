package com.restoar.data.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.restoar.model.Advertisement;
import com.restoar.widget.utils.NetworkUtils;

public class RestoARApiService implements RestoARService {

	private static final String ENDPOINT = "http://www.restoar.com.ar";
	private static final String ADS_URL = "/api/advertisement/all";

	private String endpoint;

	public RestoARApiService(String backend) {
		this.endpoint = backend;
	}

	public RestoARApiService() {
		this(ENDPOINT);
	}

	@Override
	public List<Advertisement> getAdvertisements() {
		String url = endpoint.concat(ADS_URL);
		InputStream stream = null;
		stream = NetworkUtils.getHttpGETInputStream(url);
		if (stream == null)
			throw new NullPointerException();

		String string = null;
		string = NetworkUtils.getHttpInputString(stream);
		if (string == null)
			throw new NullPointerException();

		JSONObject json = null;
		try {
			json = new JSONObject(string);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (json == null)
			throw new NullPointerException();

		return parseAds(json);
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
        if (jo == null) throw new NullPointerException();
        if (!jo.has("location")) throw new NullPointerException();
        Advertisement ad = null;
        try {
            Double lat = null, lon = null;
            if (!jo.isNull("location")) {
                JSONObject geo = jo.getJSONObject("location");
                lat = Double.parseDouble(geo.getString("latitude"));
                lon = Double.parseDouble(geo.getString("longitude"));
            } 
            if (lat != null) {
            	String title = jo.getString("title");
            	String description = jo.getString("description");
                ad = new Advertisement(title, description, lat, lon);
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ad;
    }

	@Override
	public List<String> getCategories() {
		// TODO llamar al servicio que va a crear chipi
		List<String> categories = new ArrayList<String>();
		categories.add("Alemana");
		categories.add("Arabe");
		categories.add("Armenia");
		categories.add("Autóctona");
		categories.add("Brasilera");
		categories.add("Casera");
		categories.add("Chilena");
		categories.add("China");
		categories.add("Colombiana");
		categories.add("Coreana");
		categories.add("Croata"); 
		categories.add("Cubana");
		categories.add("De");
		categories.add("Deli");
		categories.add("Española");
		categories.add("Francesa");
		categories.add("Griega");
		categories.add("Húngara");
		categories.add("India");
		categories.add("Inglesa");
		categories.add("Internacional");
		categories.add("Irlandesa");
		categories.add("Italiana");
		categories.add("Japonesa");
		categories.add("Judía");
		categories.add("Latinoamericana");
		categories.add("Macrobiótica");
		categories.add("Mediterránea");
		categories.add("Mexicana");
		categories.add("Natural");
		categories.add("Nórdica");
		categories.add("Norteamericana");
		categories.add("Parrilla");
		categories.add("Peruana");
		categories.add("Pescados");
		categories.add("Pizza");
		categories.add("Polaca");
		categories.add("Porteña");
		categories.add("Portuguesa");
		categories.add("Rusa"); 
		categories.add("Sudeste");
		categories.add("Suiza");
		categories.add("Uruguaya");
		categories.add("Variada");
		categories.add("Vasca");
		categories.add("Vegetariana");
		categories.add("Venezolana");
		return categories;
	}

}
