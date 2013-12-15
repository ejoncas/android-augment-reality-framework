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

import com.restoar.R;
import com.restoar.ui.IconMarker;
import com.restoar.ui.Marker;

/**
 */
public class RestoARDataSource extends NetworkDataSource {

    private static final String URL = "http://www.restoar.com.ar/api/advertisement/all";

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
                Marker ma = processJSONObject(jo);
                if (ma != null) markers.add(ma);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return markers;
    }

    private Marker processJSONObject(JSONObject jo) {
        if (jo == null) throw new NullPointerException();

        if (!jo.has("location")) throw new NullPointerException();

        Marker ma = null;
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
            	String id = jo.getString("id");
                ma = new IconMarker(title + "\n" + description, lat, lon, 0, Color.RED, icon);
                ma.setObjectId(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ma;
    }
}
