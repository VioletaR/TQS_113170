package ua.deti.tqs.backend.services.interfaces;

import ua.deti.tqs.backend.dtos.Location;
import ua.deti.tqs.backend.dtos.Forecast;
import ua.deti.tqs.backend.entities.utils.WeatherIPMA;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WeatherService {

    Optional<Integer> getLocationId(String districtName);

    Optional<WeatherIPMA> getWeatherForDate(List<Forecast> forecasts, LocalDate date);

    List<Forecast> getForecastByLocation(int districtId);

    List<Location> getAllLocations();
}
