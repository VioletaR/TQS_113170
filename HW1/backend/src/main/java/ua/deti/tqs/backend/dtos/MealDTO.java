package ua.deti.tqs.backend.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ua.deti.tqs.backend.entities.Meal;
import ua.deti.tqs.backend.entities.utils.WeatherIPMA;
import ua.deti.tqs.backend.services.interfaces.WeatherService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Component
public class MealDTO {
    private Meal meal;
    private WeatherIPMA weatherIPMA;
    private final WeatherService weatherService;

    public MealDTO(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public MealDTO mapToDTO(Meal meal) {
        this.meal = meal;

        // Get district ID
        String districtName = meal.getRestaurant().getDistrict();
        Integer districtId = getDistrictId(districtName);

        if (districtId == null) {
            return this;
        }

        // Get weather forecast for the district
        this.weatherIPMA = getClosestWeather(districtId, meal.getDate());

        return this;
    }

    private Integer getDistrictId(String districtName) {
        Map<String, Object> districtsData = weatherService.getDistricts();
        if (districtsData == null || !districtsData.containsKey("data")) {
            return null;
        }

        List<Map<String, Object>> districts = (List<Map<String, Object>>) districtsData.get("data");
        for (Map<String, Object> district : districts) {
            if (districtName.equals(district.get("local"))) {
                return (Integer) district.get("globalIdLocal");
            }
        }
        return null;
    }

    private WeatherIPMA getClosestWeather(Integer districtId, LocalDate mealDate) {
        Map<String, Object> forecastData = weatherService.getForecastById(districtId);
        if (forecastData == null || !forecastData.containsKey("data")) {
            return null;
        }

        List<Map<String, Object>> forecasts = (List<Map<String, Object>>) forecastData.get("data");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String mealDateString = mealDate.format(formatter);

        for (Map<String, Object> forecast : forecasts) {
            if (forecast.get("dataPrev").toString().startsWith(mealDateString)) {
                return new WeatherIPMA(
                        forecast.get("iUv").toString(),
                        forecast.get("tMin").toString(),
                        forecast.get("tMax").toString(),
                        forecast.get("ddVento").toString(),
                        forecast.get("probabilidadePrecipita").toString(),
                        (Integer) forecast.get("idTipoTempo"),
                        (Integer) forecast.get("idIntensidadePrecipita")
                );
            }
        }
        return null;
    }
}
