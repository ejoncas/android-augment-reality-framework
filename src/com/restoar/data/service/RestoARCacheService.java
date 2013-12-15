package com.restoar.data.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.restoar.model.Advertisement;
import com.restoar.model.Commerce;
import com.restoar.model.PlainAdvertisement;

public class RestoARCacheService implements RestoARService {

	private static final String ALL_ADS_KEY = "ALL_ADS";
	private static final String CATEGORIES = "CATEGORIES";
	private static final String COMMERCES = "COMMERCES";
	private static final String AD = "AD_";
	private static final String PLAIN_ADS = "PLAIN_ADS";
	private RestoARService cachedService;

	private static RestoARCacheService INSTANCE = null;
	private LoadingCache<String, Object> cache;

	public synchronized static RestoARCacheService getINSTANCE() {
		if (INSTANCE == null) {
			INSTANCE = new RestoARCacheService();
		}
		return INSTANCE;
	}

	public RestoARCacheService(RestoARService cachedService) {
		this.cachedService = cachedService;
	}

	public RestoARCacheService() {
		this(new RestoARApiService());
		this.cache = CacheBuilder.newBuilder()
				.expireAfterWrite(10, TimeUnit.MINUTES)
				.build(new CacheLoader<String, Object>() {
					@Override
					public Object load(String arg0) throws Exception {
						if (ALL_ADS_KEY.equalsIgnoreCase(arg0)) {
							return cachedService.getAdvertisements();
						}
						if (CATEGORIES.equalsIgnoreCase(arg0)) {
							return cachedService.getCategories();
						}
						if (COMMERCES.equalsIgnoreCase(arg0)) {
							return cachedService.getCommerces();
						}
						if (PLAIN_ADS.equalsIgnoreCase(arg0)) {
							return buildPlainAdvertisements();
						}
						if (arg0.startsWith(AD)) {
							return cachedService.getAdvertisement(arg0
									.split("_")[1]);
						}
						return null;
					}
				});
	}

	@Override
	public List<PlainAdvertisement> getPlainAdvertisements() {
		Map<String, PlainAdvertisement> map = getSafe(PLAIN_ADS);
		return new ArrayList<PlainAdvertisement>(map.values());
	}

	private Map<String, PlainAdvertisement> buildPlainAdvertisements() {
		Map<String, PlainAdvertisement> ads = Maps.newHashMap();
		for (Commerce c : getCommerces()) {
			for (Advertisement a : c.getAdvertisements()) {
				PlainAdvertisement ad = new PlainAdvertisement();
				ad.setAddress(c.getAddress());
				ad.setCategory(c.getCategory());
				ad.setCommerceDescription(c.getDescription());
				ad.setCommerceId(c.getId());
				ad.setCommerceName(c.getName());
				ad.setDescription(a.getDescription());
				ad.setId(a.getId());
				ad.setLatitude(c.getLatitude());
				ad.setLongitude(c.getLongitude());
				ad.setTitle(a.getTitle());
				ads.put(ad.getId(), ad);
			}
		}
		return ads;
	}

	@Override
	public List<Advertisement> getAdvertisements() {
		return getSafe(ALL_ADS_KEY);
	}

	@SuppressWarnings("unchecked")
	public <T> T getSafe(String key) {
		try {
			return (T) cache.get(key);
		} catch (ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<String> getCategories() {
		return getSafe(CATEGORIES);
	}

	@Override
	public PlainAdvertisement getPlainAdvertisement(String id) {
		Map<String, PlainAdvertisement> map = getSafe(PLAIN_ADS);
		return map.get(id);
	}

	@Override
	public Advertisement getAdvertisement(String id) {
		List<Advertisement> ads = getAdvertisements();
		for (Advertisement ad : ads) {
			if (ad.getId().equalsIgnoreCase(id)) {
				return ad;
			}
		}
		return getSafe(AD + id);
	}

	@Override
	public List<Commerce> getCommerces() {
		return getSafe(COMMERCES);
	}

	public Map<String, List<PlainAdvertisement>> getCategoriesMap() {
		Map<String, List<PlainAdvertisement>> result = Maps.newHashMap();
		for (String category : getCategories()) {
			result.put(category, new ArrayList<PlainAdvertisement>());
		}
		for (PlainAdvertisement ad : getPlainAdvertisements()) {
			result.get(ad.getCategory()).add(ad);
		}
		return result;
	}

}
