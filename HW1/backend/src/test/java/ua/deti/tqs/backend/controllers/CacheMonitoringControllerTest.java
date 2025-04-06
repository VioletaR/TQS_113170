package ua.deti.tqs.backend.controllers;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.deti.tqs.backend.services.interfaces.UserService;
import ua.deti.tqs.backend.utils.Constants;

import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(CacheMonitoringController.class)
@AutoConfigureMockMvc(addFilters = false)
class CacheMonitoringControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CacheManager cacheManager;

    @MockitoBean
    private UserService userService;

    @Test
    void whenMultipleCachesExist_returnsAllStatistics() throws Exception {
        // Setup mock caches
        String weatherCacheName = "weather";
        String mealsCacheName = "meals";

        // Configure cache names
        when(cacheManager.getCacheNames()).thenReturn(List.of(weatherCacheName, mealsCacheName));

        // Setup weather cache
        CaffeineCache weatherCache = mock(CaffeineCache.class);
        Cache<Object, Object> weatherNativeCache = mock(Cache.class);
        CacheStats weatherStats = CacheStats.of(15, 3, 0, 0, 0, 2, 0);
        when(cacheManager.getCache(weatherCacheName)).thenReturn(weatherCache);
        when(weatherCache.getNativeCache()).thenReturn(weatherNativeCache);
        when(weatherNativeCache.stats()).thenReturn(weatherStats);
        when(weatherNativeCache.estimatedSize()).thenReturn(5L);

        // Setup meals cache
        CaffeineCache mealsCache = mock(CaffeineCache.class);
        Cache<Object, Object> mealsNativeCache = mock(Cache.class);
        CacheStats mealsStats = CacheStats.of(42, 8, 0, 0, 0, 5, 0);
        when(cacheManager.getCache(mealsCacheName)).thenReturn(mealsCache);
        when(mealsCache.getNativeCache()).thenReturn(mealsNativeCache);
        when(mealsNativeCache.stats()).thenReturn(mealsStats);
        when(mealsNativeCache.estimatedSize()).thenReturn(10L);

        // Verify response structure
        mockMvc.perform(get("/"+Constants.API_PATH_V1+"cache/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weather.size").value(5))
                .andExpect(jsonPath("$.weather.hitCount").value(15))
                .andExpect(jsonPath("$.weather.hitRate").value(15.0/18))
                .andExpect(jsonPath("$.meals.evictionCount").value(5));
    }

    @Test
    void whenNoCachesExist_returnsEmptyResponse() throws Exception {
        when(cacheManager.getCacheNames()).thenReturn(List.of());

        mockMvc.perform(get("/"+Constants.API_PATH_V1+"cache/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void whenCacheMissing_ignoresInvalidCache() throws Exception {
        String invalidCacheName = "ghostCache";
        when(cacheManager.getCacheNames()).thenReturn(List.of(invalidCacheName));
        when(cacheManager.getCache(invalidCacheName)).thenReturn(null);

        mockMvc.perform(get("/"+Constants.API_PATH_V1+"cache/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}