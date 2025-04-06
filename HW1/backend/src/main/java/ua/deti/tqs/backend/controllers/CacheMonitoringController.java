package ua.deti.tqs.backend.controllers;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import ua.deti.tqs.backend.utils.Constants;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(Constants.API_PATH_V1  + "cache/health")
class CacheMonitoringController {

    private final CacheManager cacheManager;

    public CacheMonitoringController(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @GetMapping
    public Map<String, Map<String, Object>> getCacheStatistics() {
        Map<String, Map<String, Object>> stats = new HashMap<>();

        for (String cacheName : cacheManager.getCacheNames()) {
            CaffeineCache caffeineCache = (CaffeineCache) cacheManager.getCache(cacheName);
            if (caffeineCache != null) {
                Cache<Object, Object> nativeCache = caffeineCache.getNativeCache();
                CacheStats cacheStats = nativeCache.stats();

                Map<String, Object> cacheMetrics = new HashMap<>();
                cacheMetrics.put("size", nativeCache.estimatedSize());
                cacheMetrics.put("hitCount", cacheStats.hitCount());
                cacheMetrics.put("missCount", cacheStats.missCount());
                cacheMetrics.put("totalRequests", cacheStats.requestCount());
                cacheMetrics.put("hitRate", cacheStats.hitRate());
                cacheMetrics.put("evictionCount", cacheStats.evictionCount());

                stats.put(cacheName, cacheMetrics);
            }
        }

        return stats;
    }
}