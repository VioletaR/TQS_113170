package ua.deti.tqs.backend.services;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ua.deti.tqs.backend.services.interfaces.WeatherService;
import java.util.Map;
import static ua.deti.tqs.backend.utils.Constants.BASE_URL_DISTRICTS;
import static ua.deti.tqs.backend.utils.Constants.BASE_URL_FORECAST;


@Service
@AllArgsConstructor
public class WeatherServiceImpl implements WeatherService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    @Cacheable(value = "districtsCache")
    public Map<String, Object> getDistricts() {
        return restTemplate.getForObject(BASE_URL_DISTRICTS, Map.class);
    }

    @Override
    @Cacheable(value = "forecastCache", key = "#districtId")
    public Map<String, Object> getForecastById(Integer districtId) {
        String url = BASE_URL_FORECAST + districtId + ".json";
        return restTemplate.getForObject(url, Map.class);
    }
}
