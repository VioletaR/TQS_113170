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
import ua.deti.tqs.backend.dtos.District;
import ua.deti.tqs.backend.dtos.Forecast;
import ua.deti.tqs.backend.entities.utils.WeatherIPMA;
import ua.deti.tqs.backend.services.interfaces.WeatherService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static ua.deti.tqs.backend.utils.Constants.BASE_URL_DISTRICTS;
import static ua.deti.tqs.backend.utils.Constants.BASE_URL_FORECAST;

@Slf4j
@Service
@AllArgsConstructor
public class WeatherServiceImpl implements WeatherService {
    private final RestTemplate restTemplate = new RestTemplate();
    private record DistrictsResponse(List<Map<String, Object>> data) {}

    @Override
    @Cacheable(value = "districtsCache")
    public List<District> getAllDistricts() {
        log.info("Fetching districts from IPMA API");
        try {
            ResponseEntity<DistrictsResponse> response = restTemplate.exchange(
                    BASE_URL_DISTRICTS,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
                return Optional.ofNullable(response.getBody())
                        .map(DistrictsResponse::data)
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(this::convertToDistrict)
                        .filter(Objects::nonNull)
                        .toList();
            }
        } catch (RestClientException e) {
            log.error("Error fetching districts: {}", e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    @Cacheable(value = "forecastCache", key = "#districtId")
    public List<Forecast> getForecastByDistrict(int districtId) {
        String url = String.format("%s%d.json", BASE_URL_FORECAST, districtId);
        log.info("Fetching forecast for district ID: {}", districtId);

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
                        .orElse(Collections.emptyList());
            }
        } catch (RestClientException e) {
            log.error("Error fetching forecast for district {}: {}", districtId, e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    @Cacheable(value = "districtIdCache", key = "#districtName")
    public Optional<Integer> getDistrictId(String districtName) {
        log.debug("Looking up district ID for: {}", districtName);
        return getAllDistricts().stream()
                .filter(district -> district.local().equalsIgnoreCase(districtName.trim()))
                .findFirst()
                .map(District::globalIdLocal);
    }

    @Override
    public Optional<WeatherIPMA> getWeatherForDate(List<Forecast> forecasts, LocalDate date) {
        log.debug("Fetching closest weather for district on {}", date);
        try {
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
        } catch (Exception e) {
            log.error("Error getting closest weather for district: {}", e.getMessage());
            return Optional.empty();
        }
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

    private District convertToDistrict(Map<String, Object> data) {
        try {
            return new District(
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
            log.warn("Failed to convert district data: {}", e.getMessage());
            return null;
        }
    }

}