package com.foxhunter.common.util;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * EHCache工具类，使用一个缓存管理器，可以使用多个缓存。
 * 初始化提供一个默认的缓存，自动关联名称：defaultCache。
 *
 * @author Ewing
 */
public class EHCacheUtil {
    public static final String DEFAULT_CACHE_NAME = "defaultCache";
    public static final int DEFAULT_CACHE_SIZE = 1000000;
    private static CacheManager cacheManager;
    private static Cache defaultCache;

    static {
        cacheManager = CacheManager.getInstance();
        // 缓存最长空闲时间为172800秒（两天）
        defaultCache = new Cache(DEFAULT_CACHE_NAME, DEFAULT_CACHE_SIZE, false, false, 0, 172800, false, 200);
        cacheManager.addCache(defaultCache);
    }

    /**
     * 私有构造方法。
     */
    private EHCacheUtil() {
    }

    /**
     * 获取缓存管理器。
     */
    public static CacheManager getCacheManager() {
        return cacheManager;
    }

    /**
     * 使用默认配置添加缓存。
     */
    public static void addCache(Cache cache) {
        String cacheName = cache.getName();
        cacheManager.addCache(cacheName);
        if (DEFAULT_CACHE_NAME.equals(cacheName))
            defaultCache = cache;
    }

    /**
     * 获取默认缓存。
     */
    public static Cache getCache() {
        return defaultCache;
    }

    /**
     * 获取Cache，如不存在将被创建。
     */
    synchronized public static Cache getCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            cache = new Cache(cacheName, DEFAULT_CACHE_SIZE, false, false, 0, 0);
            cacheManager.addCache(cache);
            if (DEFAULT_CACHE_NAME.equals(cacheName))
                defaultCache = cache;
        }
        return cache;
    }

    /**
     * 添加对象进默认缓存。
     */
    public static void put(Object key, Object value) {
        Element element = new Element(key, value);
        defaultCache.put(element);
    }

    /**
     * 添加对象进缓存。
     */
    public static void put(String cacheName, Object key, Object value) {
        Cache cache = getCache(cacheName);
        Element element = new Element(key, value);
        cache.put(element);
    }

    /**
     * 从默认缓存中取出对象。
     */
    @SuppressWarnings("unchecked")
    public static <E> E get(Object key) {
        Element element = defaultCache.get(key);
        return element == null ? null : (E) element.getObjectValue();
    }

    /**
     * 从指定缓存中取出对象。
     */
    @SuppressWarnings("unchecked")
    public static <E> E get(String cacheName, Object key) {
        Cache cache = getCache(cacheName);
        Element element = cache.get(key);
        return element == null ? null : (E) element.getObjectValue();
    }

    /**
     * 删除默认缓存中的元素。
     */
    public static void remove(String key) {
        defaultCache.remove(key);
    }

    /**
     * 删除指定缓存中的元素。
     */
    public static void remove(String cacheName, String key) {
        Cache cache = getCache(cacheName);
        cache.remove(key);
    }

    /**
     * 删除默认缓存中的所有元素。
     */
    public static void removeAll() {
        defaultCache.removeAll();
    }

    /**
     * 删除指定缓存中的所有元素。
     */
    public static void removeAll(String cacheName) {
        Cache cache = getCache(cacheName);
        cache.removeAll();
    }

    /**
     * 删除指定的缓存。
     */
    public static void removeCache(String cacheName) {
        cacheManager.removeCache(cacheName);
        if (DEFAULT_CACHE_NAME.equals(cacheName))
            defaultCache = null;
    }

    /**
     * 删除所有的缓存。
     */
    public static void removeAllCaches() {
        cacheManager.removalAll();
    }

    /**
     * 释放缓存管理器。
     */
    public static void shutdown() {
        cacheManager.shutdown();
    }
}