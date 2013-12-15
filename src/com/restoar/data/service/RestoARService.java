package com.restoar.data.service;

import java.util.List;

import com.restoar.model.Advertisement;
import com.restoar.model.Commerce;
import com.restoar.model.PlainAdvertisement;

public interface RestoARService {

	List<Commerce> getCommerces();
	
	List<Advertisement> getAdvertisements();

	List<String> getCategories();

	Advertisement getAdvertisement(String id);

	List<PlainAdvertisement> getPlainAdvertisements();

	PlainAdvertisement getPlainAdvertisement(String id);
}
