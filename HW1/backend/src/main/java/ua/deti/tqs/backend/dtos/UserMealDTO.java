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
        if (userMeal == null || userMeal.getMeal() == null) return null;
        log.info("Creating UserMealDTO from userMealId: {}", userMeal.getId());
        UserMealDTO dto = new UserMealDTO();
        dto.userMeal = userMeal;

        Meal meal = userMeal.getMeal();

        WeatherIPMA weather = MealDTO.fromMeal(meal, weatherService).getWeatherIPMA();
        if (weather == null) return dto;

        dto.weatherIPMA = weather;
        return dto;
    }
}