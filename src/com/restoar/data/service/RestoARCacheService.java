package com.restoar.data.service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.restoar.model.Advertisement;

public class RestoARCacheService implements RestoARService {

	private static final String ALL_ADS_KEY = "ALL_ADS";
	private static final String CATEGORIES = "CATEGORIES";
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
						return null;
					}
				});
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

}
