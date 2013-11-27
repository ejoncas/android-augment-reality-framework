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
	private RestoARService cachedService;
	
	private static RestoARCacheService INSTANCE = null;
	
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
		this.cachedService = new RestoARApiService();
	}

	private LoadingCache<String, Object> cache = CacheBuilder.newBuilder()
			.expireAfterWrite(10, TimeUnit.MINUTES)
			.build(new CacheLoader<String, Object>() {
				@Override
				public Object load(String arg0) throws Exception {
					if (ALL_ADS_KEY.equalsIgnoreCase(arg0)) {
						cachedService.getAdvertisements();
					}
					return null;
				}
			});

	@SuppressWarnings("unchecked")
	@Override
	public List<Advertisement> getAdvertisements() {
		try {
			return (List<Advertisement>) cache.get(ALL_ADS_KEY);
		} catch (ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}

}
