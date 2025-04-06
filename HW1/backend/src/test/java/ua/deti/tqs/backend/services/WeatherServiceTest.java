//package ua.deti.tqs.backend.services;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.RestClientException;
//import ua.deti.tqs.backend.dtos.Forecast;
//import ua.deti.tqs.backend.dtos.Location;
//import ua.deti.tqs.backend.entities.utils.WeatherIPMA;
//
//import java.time.LocalDateTime;
//import java.util.*;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//import static ua.deti.tqs.backend.utils.Constants.BASE_URL_FORECAST;
//import static ua.deti.tqs.backend.utils.Constants.BASE_URL_LOCATIONS;
//
//
//@ActiveProfiles("test")
//@ExtendWith(MockitoExtension.class)
//public class WeatherServiceTest {
//
//    @Mock
//    private RestTemplate restTemplate;
//
//    @InjectMocks
//    private WeatherServiceImpl weatherService;
//
//    @Test
//    void getAllLocations_SuccessfulResponse_ReturnsLocations() {
//        Map<String, Object> locationData = new HashMap<>();
//        locationData.put("idRegiao", 1);
//        locationData.put("idAreaAviso", "Aviso1");
//        locationData.put("idConcelho", 10);
//        locationData.put("globalIdLocal", 100);
//        locationData.put("latitude", "40.0");
//        locationData.put("idDistrito", 5);
//        locationData.put("local", "Lisbon");
//        locationData.put("longitude", "-8.0");
//
//        ResponseEntity<List<Map<String, Object>>> mockResponse =
//                new ResponseEntity<>(List.of(locationData), HttpStatus.OK);
//
//        when(restTemplate.exchange(anyString(), any(), any(), any()))
//                .thenReturn(mockResponse);
//
//        List<Location> result = weatherService.getAllLocations();
//        assertThat(result).hasSize(1);
//        assertThat(result.get(0).local()).isEqualTo("Lisbon");
//    }
//
//    @Test
//    void getForecastByLocation_ValidId_ReturnsFilteredForecasts() {
//        int locationId = 100;
//        Forecast forecast = new Forecast(
//                "10.0", "20.0", "3", 50, 1, 100, "50",
//                "2024-03-10T00:00:00", "NW", 2, "2024-03-10", 24
//        );
//
//        ResponseEntity<List<Forecast>> mockResponse =
//                new ResponseEntity<>(List.of(forecast), HttpStatus.OK);
//
//        when(restTemplate.exchange(anyString(), any(), any(), any()))
//                .thenReturn(mockResponse);
//
//        List<Forecast> result = weatherService.getForecastByLocation(locationId);
//        assertThat(result).hasSize(1);
//        assertThat(result.get(0).periodId()).isEqualTo(24);
//    }
//
//    @Test
//    void getWeatherForDate_ExactMatch_ReturnsWeather() {
//        LocalDateTime date = LocalDateTime.parse("2024-03-10T00:00:00");
//        Forecast forecast = new Forecast(
//                "10.0", "20.0", "3", 50, 1, 100, "50",
//                "2024-03-10T00:00:00", "NW", 2, "2024-03-10", 24
//        );
//
//        Optional<WeatherIPMA> result =
//                weatherService.getWeatherForDate(List.of(forecast), date);
//
//        assertThat(result).isPresent();
//        assertThat(result.get().getMinTemp()).isEqualTo(10.0);
//        assertThat(result.get().getMaxTemp()).isEqualTo(20.0);
//    }
//
//    @Test
//    void getLocationId_ExistingName_ReturnsId() {
//        Map<String, Object> locationData = new HashMap<>();
//        locationData.put("local", "Lisbon");
//        locationData.put("globalIdLocal", 100);
//
//        ResponseEntity<List<Map<String, Object>>> mockResponse =
//                new ResponseEntity<>(List.of(locationData), HttpStatus.OK);
//
//        when(restTemplate.exchange(anyString(), any(), any(), any()))
//                .thenReturn(mockResponse);
//
//        Optional<Integer> result = weatherService.getLocationId("lisbon");
//        assertThat(result).contains(100);
//    }
//}