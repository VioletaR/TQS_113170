package ua.deti.tqs.backend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ua.deti.tqs.backend.dtos.Location;
import ua.deti.tqs.backend.dtos.Forecast;
import ua.deti.tqs.backend.entities.UserMeal;
import ua.deti.tqs.backend.entities.utils.WeatherIPMA;
import ua.deti.tqs.backend.services.interfaces.WeatherService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static ua.deti.tqs.backend.utils.Constants.BASE_URL_LOCATIONS;
import static ua.deti.tqs.backend.utils.Constants.BASE_URL_FORECAST;

@Slf4j
@Service
@AllArgsConstructor
public class WeatherServiceImpl implements WeatherService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    @Cacheable(value = "locationsCache")
    public List<Location> getAllLocations() {
        log.info("Fetching locations from IPMA API");
        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    BASE_URL_LOCATIONS,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
                List<Map<String, Object>> data = response.getBody();

                return Optional.ofNullable(data)
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(this::convertToLocation)
                        .filter(Objects::nonNull)
                        .toList();
            }
        } catch (RestClientException e) {
            log.error("Error fetching locations: {}", e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    @Cacheable(value = "forecastCache", key = "#locationId")
    public List<Forecast> getForecastByLocation(int locationId) {
        String url = String.format("%s%d.json", BASE_URL_FORECAST, locationId);
        log.info("Fetching forecast for location ID: {}", locationId);

        try {
            ResponseEntity<List<Forecast>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(response.getBody())
                        .orElse(Collections.emptyList()).stream().filter(
                                forecast -> forecast.periodId() == 24
                        ).toList();
            }
        } catch (RestClientException e) {
            log.error("Error fetching forecast for location {}: {}", locationId, e.getMessage());
        }
        return Collections.emptyList();
    }


    @Override
    @Cacheable(value = "locationIdCache", key = "#locationName")
    public Optional<Integer> getLocationId(String locationName) {
        log.debug("Looking up location ID for: {}", locationName);
        return getAllLocations().stream()
                .filter(location -> location.local().equalsIgnoreCase(locationName.trim()))
                .findFirst()
                .map(Location::globalIdLocal);
    }

    @Override
    public Optional<WeatherIPMA> getWeatherForDate(List<Forecast> forecasts, LocalDateTime date) {
        log.debug("Fetching closest weather for location on {}", date);
        if (forecasts == null || forecasts.isEmpty()) {
            return Optional.empty();
        }

        return forecasts.stream()
                .min(Comparator.comparing(forecast -> {
                    LocalDateTime forecastDateTime = LocalDateTime.parse(forecast.forecastDate());
                    LocalDate forecastDate = forecastDateTime.toLocalDate();
                    return Math.abs(ChronoUnit.DAYS.between(forecastDate, date));
                }))
                .map(this::convertToWeatherIPMA);

    }

    private WeatherIPMA convertToWeatherIPMA(Forecast forecast) {
        return new WeatherIPMA(
                forecast.uvIndex(),
                forecast.minTemp(),
                forecast.maxTemp(),
                forecast.windDirection(),
                forecast.precipitationProbability(),
                forecast.weatherTypeId(),
                forecast.precipitationIntensityId()
        );
    }

    private Location convertToLocation(Map<String, Object> data) {
        try {
            return new Location(
                    (int) data.get("idRegiao"),
                    (String) data.get("idAreaAviso"),
                    (int) data.get("idConcelho"),
                    (int) data.get("globalIdLocal"),
                    (String) data.get("latitude"),
                    (int) data.get("idDistrito"),
                    (String) data.get("local"),
                    (String) data.get("longitude")
            );
        } catch (Exception e) {
            log.warn("Failed to convert location data: {}", e.getMessage());
            return null;
        }
    }

}
