package com.osp.bttp.common.config;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;

import java.util.ArrayList;
import java.util.Collection;

public class MultipleCacheResolver implements CacheResolver {

    private final CacheManager appVersionCacheManager;
    private final CacheManager placesCacheManager;
    private static final String APPVERSION_CACHE = "appVersion";
    private static final String PLACES_CACHE = "places";

    public MultipleCacheResolver(CacheManager appVersion,CacheManager places) {
        this.appVersionCacheManager = appVersion;
        this.placesCacheManager=places;

    }

    @Override
    public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
        Collection<Cache> caches = new ArrayList<Cache>();
        if ("getCata".equals(context.getMethod().getName())) {
            caches.add(placesCacheManager.getCache(PLACES_CACHE));

        } else if ("isLatestVersion".equals(context.getMethod().getName())){
            caches.add(appVersionCacheManager.getCache(APPVERSION_CACHE));
        }
        else if ("emptyPlacesCache".equals(context.getMethod().getName())) {
            caches.add(placesCacheManager.getCache(PLACES_CACHE));
        }
        else if ("emptyAppVersionCache".equals(context.getMethod().getName())) {
            caches.add(appVersionCacheManager.getCache(APPVERSION_CACHE));
        }
        return caches;
    }

}