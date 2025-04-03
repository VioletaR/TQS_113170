package ua.deti.tqs.backend.dtos;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ua.deti.tqs.backend.entities.Meal;
import ua.deti.tqs.backend.entities.UserMeal;
import ua.deti.tqs.backend.entities.utils.WeatherIPMA;
import ua.deti.tqs.backend.services.interfaces.WeatherService;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
@NoArgsConstructor
public class UserMealDTO {
    private WeatherIPMA weatherIPMA;
    private UserMeal userMeal;

    public static UserMealDTO fromUserMeal(UserMeal userMeal, WeatherService weatherService) {
        log.info("Creating UserMealDTO from userMealId: {}", userMeal.getId());
        UserMealDTO dto = new UserMealDTO();
        dto.userMeal = userMeal;

        if (userMeal == null) return dto;

        Meal meal = userMeal.getMeal();

        if (meal == null) return dto;

        log.info("Fetching weather for meal date: {}", meal.getDate());
        String districtName = meal.getRestaurant().getDistrict();
        Optional<Integer> districtIdOpt = weatherService.getDistrictId(districtName);

        districtIdOpt.ifPresent(districtId -> {
            try {
                log.info("Fetching forecast for districtId: {}", districtId);
                List<Forecast> forecasts = weatherService.getForecastByDistrict(districtId);
                LocalDate mealDate = meal.getDate();

                log.info("Fetching weather for meal date: {}", mealDate);
                Optional<WeatherIPMA> weatherOpt = weatherService.getWeatherForDate(forecasts, mealDate);
                weatherOpt.ifPresent(weather -> dto.weatherIPMA = weather);

            } catch (NumberFormatException e) {
                log.error("Error while Fetching forecast for districtId: {}: {}", districtId,e.getMessage());
            }
        });

        return dto;
    }
}