package ua.deti.tqs.backend.services.interfaces;

import ua.deti.tqs.backend.dtos.District;
import ua.deti.tqs.backend.dtos.Forecast;
import ua.deti.tqs.backend.entities.utils.WeatherIPMA;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WeatherService {

    Optional<Integer> getDistrictId(String districtName);

    Optional<WeatherIPMA> getWeatherForDate(List<Forecast> forecasts, LocalDate date);

    List<Forecast> getForecastByDistrict(int districtId);

    List<District> getAllDistricts();
}
