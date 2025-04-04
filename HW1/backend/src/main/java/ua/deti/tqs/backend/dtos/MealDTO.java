package ua.deti.tqs.backend.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.deti.tqs.backend.entities.Meal;
import ua.deti.tqs.backend.entities.utils.WeatherIPMA;
import ua.deti.tqs.backend.services.interfaces.WeatherService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
@NoArgsConstructor
public class MealDTO {
    private Meal meal;
    private WeatherIPMA weatherIPMA;

    public static MealDTO fromMeal(Meal meal, WeatherService weatherService) {
        if (meal == null) return null;
        log.info("Creating MealDTO from mealId: {}", meal.getId());
        MealDTO dto = new MealDTO();
        dto.meal = meal;

        log.info("Obtaining location for mealId: {}", meal.getId());
        String locationName = meal.getRestaurant().getLocation();
        log.info("Location name: {}", locationName);
        Optional<Integer> locationIdOpt = weatherService.getLocationId(locationName);
        log.info("LocationIdOpt: {}", locationIdOpt);
        locationIdOpt.ifPresent(locationId -> {
            try {
                log.info("Fetching forecast for locationId: {}", locationId);
                List<Forecast> forecasts = weatherService.getForecastByLocation(locationId);
                LocalDate mealDate = meal.getDate();

                log.info("Fetching weather for meal date: {}", mealDate);
                Optional<WeatherIPMA> weatherOpt = weatherService.getWeatherForDate(forecasts, mealDate);
                weatherOpt.ifPresent(weather -> dto.weatherIPMA = weather);

            } catch (NumberFormatException e) {
                log.error("Error while Fetching forecast for locationId: {}: {}", locationId,e.getMessage());
            }
        });

        return dto;
    }

}