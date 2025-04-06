//package ua.deti.tqs.backend.services;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentMatchers;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestTemplate;
//import ua.deti.tqs.backend.dtos.Forecast;
//import ua.deti.tqs.backend.dtos.Location;
//import ua.deti.tqs.backend.entities.utils.WeatherIPMA;
//
//import java.time.LocalDateTime;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class WeatherServiceTest {
//
//    private WeatherServiceImpl weatherService;
//    private RestTemplate restTemplate;
//
//    @BeforeEach
//    void setUp() {
//        restTemplate = mock(RestTemplate.class);
//        weatherService = new WeatherServiceImpl();
//    }
//
//    @Test
//    void testGetAllLocations_success() {
//        Map<String, Object> locationMap = Map.of(
//                "idRegiao", 1,
//                "idAreaAviso", "AV001",
//                "idConcelho", 101,
//                "globalIdLocal", 123,
//                "latitude", "40.0",
//                "idDistrito", 10,
//                "local", "Aveiro",
//                "longitude", "-8.0"
//        );
//
//        ResponseEntity<List<Map<String, Object>>> response =
//                new ResponseEntity<>(List.of(locationMap), HttpStatus.OK);
//
//        when(restTemplate.exchange(
//                eq("http://api.ipma.pt/open-data/distrits-islands.json"),  // BASE_URL_LOCATIONS
//                eq(org.springframework.http.HttpMethod.GET),
//                isNull(),
//                ArgumentMatchers.<ParameterizedTypeReference<List<Map<String, Object>>>>any())
//        ).thenReturn(response);
//
//        List<Location> locations = weatherService.getAllLocations();
//        assertEquals(1, locations.size());
//        assertEquals("Aveiro", locations.get(0).local());
//    }
//
//    @Test
//    void testGetAllLocations_error() {
//        when(restTemplate.exchange(anyString(), any(), any(), ArgumentMatchers.<ParameterizedTypeReference<List<Map<String, Object>>>>any()))
//                .thenThrow(new RestClientException("Failed"));
//
//        List<Location> locations = weatherService.getAllLocations();
//    }
//
//    @Test
//    void testGetForecastByLocation_success() {
//        Forecast forecast = new Forecast("10", "20", "5", 1, 0, 123, "30%", "2025-04-06T00:00:00", "N", 0, "2025-04-05T00:00:00", 24);
//        ResponseEntity<List<Forecast>> response = new ResponseEntity<>(List.of(forecast), HttpStatus.OK);
//
//        when(restTemplate.exchange(anyString(), any(), any(), ArgumentMatchers.<ParameterizedTypeReference<List<Forecast>>>any()))
//                .thenReturn(response);
//
//        List<Forecast> forecasts = weatherService.getForecastByLocation(123);
//        assertEquals(1, forecasts.size());
//        assertEquals("10", forecasts.get(0).minTemp());
//    }
//
//    @Test
//    void testGetForecastByLocation_error() {
//        when(restTemplate.exchange(anyString(), any(), any(), ArgumentMatchers.<ParameterizedTypeReference<List<Forecast>>>any()))
//                .thenThrow(new RestClientException("Failed"));
//
//        List<Forecast> forecasts = weatherService.getForecastByLocation(123);
//        assertTrue(forecasts.isEmpty());
//    }
//
//    @Test
//    void testGetLocationId_found() {
//        Location location = new Location(1, "AV001", 101, 123, "40.0", 10, "Aveiro", "-8.0");
//
//        WeatherServiceImpl serviceSpy = spy(weatherService);
//        doReturn(List.of(location)).when(serviceSpy).getAllLocations();
//
//        Optional<Integer> locationId = serviceSpy.getLocationId("Aveiro");
//        assertTrue(locationId.isPresent());
//        assertEquals(123, locationId.get());
//    }
//
//    @Test
//    void testGetWeatherForDate_closestMatch() {
//        LocalDateTime targetDate = LocalDateTime.of(2025, 4, 6, 12, 0);
//        Forecast forecast1 = new Forecast("10", "20", "5", 1, 0, 123, "30%", "2025-04-05T00:00:00", "N", 0, "2025-04-04T00:00:00", 24);
//        Forecast forecast2 = new Forecast("11", "21", "6", 2, 1, 123, "40%", "2025-04-07T00:00:00", "S", 1, "2025-04-06T00:00:00", 24);
//
//        Optional<WeatherIPMA> weather = weatherService.getWeatherForDate(List.of(forecast1, forecast2), targetDate);
//        assertTrue(weather.isPresent());
//        assertEquals("5", weather.get().getIUv());
//    }
//}
