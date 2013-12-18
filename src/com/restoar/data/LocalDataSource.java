package com.restoar.data;

import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.google.common.collect.Lists;
import com.restoar.R;
import com.restoar.data.service.RestoARCacheService;
import com.restoar.model.PlainAdvertisement;
import com.restoar.ui.IconMarker;
import com.restoar.ui.Marker;

/**
 * Restoar cached datasource
 * 
 * @author jonatancastrocrespin
 *
 */
public class LocalDataSource extends DataSource {

    private static Bitmap icon = null;

    public LocalDataSource(Resources res) {
        if (res == null) throw new NullPointerException();
        createIcon(res);
    }

    protected void createIcon(Resources res) {
    	  if (res == null) throw new NullPointerException();
          icon = BitmapFactory.decodeResource(res, R.drawable.restaurant);
    }

    public List<Marker> getMarkers() {
    	List<PlainAdvertisement> ads = RestoARCacheService.getINSTANCE().getPlainAdvertisements();
    	List<Marker> markers = Lists.newArrayList();
    	for (PlainAdvertisement ad : ads ) { 
    		Marker ma = new IconMarker(ad.getTitle() + "\n" + ad.getDescription(), ad.getLatitude(), ad.getLongitude(), 0, Color.RED, icon);
            ma.setObjectId(ad.getId());
            markers.add(ma);
    	}
        return markers;
    }
}
