package com.restoar.data;

import java.util.ArrayList;
import java.util.List;

import com.restoar.ui.Marker;

public class MockedRestoARDataSource extends DataSource {

	@Override
	public List<Marker> getMarkers() {
		List<Marker> result = new ArrayList<Marker>();
		result.add(createMarkerFor(-34.614076,-58.532551));
		result.add(createMarkerFor(-34.613943,-58.535306));
		result.add(createMarkerFor(-34.614085,-58.534301));
		
		return result;
	}

	private Marker createMarkerFor(double d, double e) {
		Marker m = new Marker("Restorant "+ Double.valueOf(d).hashCode(), d, e, 0, 0);
		return m;
	}

}
