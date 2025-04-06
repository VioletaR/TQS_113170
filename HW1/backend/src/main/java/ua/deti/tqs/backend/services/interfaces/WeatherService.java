package ua.deti.tqs.backend.services.interfaces;

import ua.deti.tqs.backend.dtos.Location;
import ua.deti.tqs.backend.dtos.Forecast;
import ua.deti.tqs.backend.entities.utils.WeatherIPMA;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WeatherService {

    Optional<Integer> getLocationId(String districtName);

    Optional<WeatherIPMA> getWeatherForDate(List<Forecast> forecasts, LocalDateTime date);

    List<Forecast> getForecastByLocation(int districtId);

    List<Location> getAllLocations();
}
