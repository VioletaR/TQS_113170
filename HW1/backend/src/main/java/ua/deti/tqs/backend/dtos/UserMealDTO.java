package ua.deti.tqs.backend.dtos;

import lombok.Getter;
import ua.deti.tqs.backend.entities.Meal;
import ua.deti.tqs.backend.entities.UserMeal;
import ua.deti.tqs.backend.entities.utils.WeatherIPMA;
import ua.deti.tqs.backend.services.interfaces.WeatherService;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Getter
@NoArgsConstructor
public class UserMealDTO {
    private WeatherIPMA weatherIPMA;
    private UserMeal userMeal;

    public static UserMealDTO fromUserMeal(UserMeal userMeal, WeatherService weatherService) {
        UserMealDTO dto = new UserMealDTO();
        dto.userMeal = userMeal;

        if (userMeal == null) return dto;

        Meal meal = userMeal.getMeal();

        if (meal == null) return dto;

        String districtName = meal.getRestaurant().getDistrict();
        Optional<Integer> districtIdOpt = weatherService.getDistrictId(districtName);

        districtIdOpt.ifPresent(districtId -> {
            try {
                List<Forecast> forecasts = weatherService.getForecastByDistrict(districtId);
                LocalDate mealDate = meal.getDate();
                Optional<WeatherIPMA> weatherOpt = weatherService.getWeatherForDate(forecasts, mealDate);
                weatherOpt.ifPresent(weather -> dto.weatherIPMA = weather);
            } catch (NumberFormatException e) {
                System.err.println("Invalid district ID format: " + districtId);
            }
        });

        return dto;
    }
}