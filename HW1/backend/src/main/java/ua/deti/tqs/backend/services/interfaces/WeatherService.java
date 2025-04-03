package ua.deti.tqs.backend.services.interfaces;

import java.util.Map;

public interface WeatherService {

    Map<String, Object> getDistricts();

    Map<String, Object> getForecastById(Integer districtId);
}
