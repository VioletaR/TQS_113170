package ua.deti.tqs.backend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ua.deti.tqs.backend.dtos.Forecast;
import ua.deti.tqs.backend.dtos.Location;
import ua.deti.tqs.backend.entities.utils.WeatherIPMA;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WeatherServiceTest {

    private WeatherServiceImpl weatherService;

    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        weatherService = new WeatherServiceImpl();
    }

    @Test
    void testGetForecastById_apiError() {
        int locationId = 999999;
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenThrow(new RestClientException("error"));

        List<Forecast> result = weatherService.getForecastByLocation(locationId);

        assertThat(result).isEmpty();
    }


    @Test
    void testGetForecastByLocation_error() {
        when(restTemplate.exchange(anyString(), any(), any(), ArgumentMatchers.<ParameterizedTypeReference<List<Forecast>>>any()))
                .thenThrow(new RestClientException("Failed"));

        List<Forecast> forecasts = weatherService.getForecastByLocation(-123);
        assertTrue(forecasts.isEmpty());
    }

    @Test
    void testGetLocationId_found() {
        Location location = new Location(1, "AV001", 101, 123, "40.0", 10, "Aveiro", "-8.0");

        WeatherServiceImpl serviceSpy = spy(weatherService);
        doReturn(List.of(location)).when(serviceSpy).getAllLocations();

        Optional<Integer> locationId = serviceSpy.getLocationId("Aveiro");
        assertTrue(locationId.isPresent());
        assertEquals(123, locationId.get());
    }

    @Test
    void testGetWeatherForDate_closestMatch() {
        LocalDateTime targetDate = LocalDateTime.of(2025, 4, 6, 12, 0);
        Forecast forecast1 = new Forecast("10", "20", "5", 1, 0, 123, "30%", "2025-04-05T00:00:00", "N", 0, "2025-04-04T00:00:00", 24);
        Forecast forecast2 = new Forecast("11", "21", "6", 2, 1, 123, "40%", "2025-04-07T00:00:00", "S", 1, "2025-04-06T00:00:00", 24);

        Optional<WeatherIPMA> weather = weatherService.getWeatherForDate(List.of(forecast1, forecast2), targetDate);
        assertTrue(weather.isPresent());
        assertEquals("5", weather.get().getIUv());
    }

    @Test
    void testGetWeatherForDate_noForecasts() {
        LocalDateTime targetDate = LocalDateTime.of(2025, 4, 6, 12, 0);
        Optional<WeatherIPMA> weather = weatherService.getWeatherForDate(Collections.emptyList(), targetDate);
        assertFalse(weather.isPresent());
    }

    @Test
    void testGetWeatherForDate_nullForecasts() {
        LocalDateTime targetDate = LocalDateTime.of(2025, 4, 6, 12, 0);
        Optional<WeatherIPMA> weather = weatherService.getWeatherForDate(null, targetDate);
        assertFalse(weather.isPresent());
    }

}
