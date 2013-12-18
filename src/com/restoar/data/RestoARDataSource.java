package com.restoar.data;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.google.common.collect.Lists;
import com.restoar.R;
import com.restoar.model.Advertisement;
import com.restoar.ui.IconMarker;
import com.restoar.ui.Marker;

/**
 */
public class RestoARDataSource extends NetworkDataSource {

    private static final String URL = "http://www.restoar.com.ar/api/commerce/all";

    private static Bitmap icon = null;

    public RestoARDataSource(Resources res) {
        if (res == null) throw new NullPointerException();
        createIcon(res);
    }

    protected void createIcon(Resources res) {
        if (res == null) throw new NullPointerException();
        icon = BitmapFactory.decodeResource(res, R.drawable.restaurant);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createRequestURL(double lat, double lon, double alt, float radius, String locale) {
        return URL + "?geocode=" + lat + "%2C" + lon + "%2C" + Math.max(radius, 1.0) + "km";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Marker> parse(String url) {
        if (url == null) throw new NullPointerException();

        InputStream stream = null;
        stream = getHttpGETInputStream(url);
        if (stream == null) throw new NullPointerException();

        String string = null;
        string = getHttpInputString(stream);
        if (string == null) throw new NullPointerException();

        JSONObject json = null;
        try {
            json = new JSONObject(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (json == null) throw new NullPointerException();

        return parse(json);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Marker> parse(JSONObject root) {
        if (root == null) throw new NullPointerException();

        JSONObject jo = null;
        JSONArray dataArray = null;
        List<Marker> markers = new ArrayList<Marker>();

        try {
            if (root.has("data")) dataArray = root.getJSONArray("data");
            if (dataArray == null) return markers;
            for (int i = 0; i < dataArray.length(); i++) {
                jo = dataArray.getJSONObject(i);
                List<Marker> mas = processADS(jo);
                if (mas != null) markers.addAll(mas);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return markers;
    }

    private List<Marker> processADS(JSONObject jo) {
    	List<Marker> markers = Lists.newArrayList();
    	if (jo == null)
			throw new NullPointerException();
		if (!jo.has("location"))
			throw new NullPointerException();
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
				if (jo.has("advertisements")) { 
					JSONArray ads = jo.getJSONArray("advertisements");
					for (int i = 0; i < ads.length(); i++) {
						JSONObject ad = ads.getJSONObject(i);
						Advertisement adBean = parseAd(ad);
						Marker ma = new IconMarker(title + "\n" + description, lat, lon, 0, Color.RED, icon);
		                ma.setObjectId(adBean.getId());
		                markers.add(ma);
					}
				}
			}
		} catch (Exception e) {
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
}
