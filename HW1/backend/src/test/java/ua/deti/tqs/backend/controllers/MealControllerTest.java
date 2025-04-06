package ua.deti.tqs.backend.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.agent.ByteBuddyAgent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.deti.tqs.backend.dtos.Forecast;
import ua.deti.tqs.backend.dtos.Location;
import ua.deti.tqs.backend.entities.Meal;
import ua.deti.tqs.backend.entities.Restaurant;
import ua.deti.tqs.backend.entities.utils.WeatherIPMA;
import ua.deti.tqs.backend.services.MealServiceImpl;
import ua.deti.tqs.backend.services.WeatherServiceImpl;
import ua.deti.tqs.backend.dtos.MealDTO;
import ua.deti.tqs.backend.services.interfaces.MealService;
import ua.deti.tqs.backend.services.interfaces.UserService;
import ua.deti.tqs.backend.services.interfaces.WeatherService;
import ua.deti.tqs.backend.utils.Constants;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(MealController.class)
@AutoConfigureMockMvc(addFilters = false)
public class MealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private MealService mealService;

    @MockitoBean
    private WeatherService weatherService;

    @Autowired
    private ObjectMapper objectMapper;

    private final LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);

    private Restaurant createTestRestaurant() {
        return new Restaurant(1L, "Test Restaurant", "Lisbon", 100);
    }

    private Meal createTestMeal() {
        return new Meal(1L, createTestRestaurant(), new BigDecimal("12.99"), dateTime , "Pasta");
    }

    private WeatherIPMA createTestWeatherData() {
        return new WeatherIPMA("10", "10", "10", "10", "10", 10, 10);
    }

    private Forecast createTestForecast() {
        return new Forecast(
                "10",
                "10",
                "10",
                10,
                10,
                10,
                "10",
                "10",
                "10",
                10,
                "10",
                10
        );
    }

    private Location createTestLocation() {
        return new Location(
                10,
                "Lisbon",
                10,
                10,
                "10",
                10,
                "10",
                "10"
        );
    }


    @Test
    void whenCreateValidMeal_thenReturnCreated() throws Exception {
        Meal newMeal = createTestMeal();
        when(mealService.createMeal(any(Meal.class))).thenReturn(newMeal);

        mockMvc.perform(post("/"+Constants.API_PATH_V1+"meals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newMeal)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.meal").value("Pasta"))
                .andExpect(jsonPath("$.price").value(12.99))
                .andExpect(jsonPath("$.restaurant.id").value(1))
                .andExpect(jsonPath("$.restaurant.name").value("Test Restaurant"))
                .andExpect(jsonPath("$.restaurant.location").value("Lisbon"))
                .andExpect(jsonPath("$.restaurant.seats").value(100));

    }

    @Test
    void whenCreateInvalidMeal_thenReturnBadRequest() throws Exception {
        Meal invalidMeal = new Meal(null, null, null, null, null);

        mockMvc.perform(post("/"+Constants.API_PATH_V1+"meals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidMeal)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGetMealWithWeather_thenReturnCompleteDTO() throws Exception {
        Meal meal = createTestMeal();
        WeatherIPMA weather = createTestWeatherData();
        Location location = createTestLocation();
        Forecast forecast = createTestForecast();
        Restaurant restaurant = createTestRestaurant();

        when(mealService.getMealById(meal.getId())).thenReturn(meal);
        when(weatherService.getAllLocations()).thenReturn(List.of(location));
        when(weatherService.getLocationId(restaurant.getLocation())).thenReturn(Optional.of(location.globalIdLocal()));

        when(weatherService.getForecastByLocation(location.globalIdLocal())).thenReturn(List.of(forecast));
        when(weatherService.getWeatherForDate(List.of(forecast), dateTime)).thenReturn(Optional.of(weather));

        mockMvc.perform(get("/"+Constants.API_PATH_V1+"meals/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meal.id").value(1))
                .andExpect(jsonPath("$.meal.meal").value("Pasta"))
                .andExpect(jsonPath("$.meal.price").value(12.99))
                .andExpect(jsonPath("$.meal.restaurant.id").value(1))
                .andExpect(jsonPath("$.meal.restaurant.name").value("Test Restaurant"))
                .andExpect(jsonPath("$.meal.restaurant.location").value("Lisbon"))
                .andExpect(jsonPath("$.meal.restaurant.seats").value(100))
                .andExpect(jsonPath("$.weatherIPMA.tempMin").value("10"))
                .andExpect(jsonPath("$.weatherIPMA.tempMax").value("10"))
                .andExpect(jsonPath("$.weatherIPMA.windDirection").value("10"))
                .andExpect(jsonPath("$.weatherIPMA.precipitationProb").value("10"))
                .andExpect(jsonPath("$.weatherIPMA.typeOfWeatherId").value(10))
                .andExpect(jsonPath("$.weatherIPMA.precipitationIntensity").value(10))
                .andExpect(jsonPath("$.weatherIPMA.iuv").value("10"));

    }

    @Test
    void whenGetAllMealsForRestaurant_thenReturnDTOList() throws Exception {
        Meal meal = createTestMeal();
        WeatherIPMA weather = createTestWeatherData();
        Location location = createTestLocation();
        Forecast forecast = createTestForecast();
        Restaurant restaurant = createTestRestaurant();

        when(mealService.getMealById(meal.getId())).thenReturn(meal);
        when(weatherService.getAllLocations()).thenReturn(List.of(location));
        when(weatherService.getLocationId(restaurant.getLocation())).thenReturn(Optional.of(location.globalIdLocal()));

        when(weatherService.getForecastByLocation(location.globalIdLocal())).thenReturn(List.of(forecast));
        when(weatherService.getWeatherForDate(List.of(forecast), dateTime)).thenReturn(Optional.of(weather));

        when(mealService.getAllMealsByRestaurantId(restaurant.getId())).thenReturn(List.of(meal));

        mockMvc.perform(get("/"+Constants.API_PATH_V1+"meals/all/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].meal.id").value(1))
                .andExpect(jsonPath("$[0].meal.meal").value("Pasta"))
                .andExpect(jsonPath("$[0].meal.price").value(12.99))
                .andExpect(jsonPath("$[0].meal.restaurant.id").value(1))
                .andExpect(jsonPath("$[0].meal.restaurant.name").value("Test Restaurant"))
                .andExpect(jsonPath("$[0].meal.restaurant.location").value("Lisbon"))
                .andExpect(jsonPath("$[0].meal.restaurant.seats").value(100))
                .andExpect(jsonPath("$[0].weatherIPMA.tempMin").value("10"))
                .andExpect(jsonPath("$[0].weatherIPMA.tempMax").value("10"))
                .andExpect(jsonPath("$[0].weatherIPMA.windDirection").value("10"))
                .andExpect(jsonPath("$[0].weatherIPMA.precipitationProb").value("10"))
                .andExpect(jsonPath("$[0].weatherIPMA.typeOfWeatherId").value(10))
                .andExpect(jsonPath("$[0].weatherIPMA.precipitationIntensity").value(10))
                .andExpect(jsonPath("$[0].weatherIPMA.iuv").value("10"));

    }
    @Test
    void whenGetNonExistingMeal_thenReturnNotFound() throws Exception {
        when(mealService.getMealById(999L)).thenReturn(null);

        mockMvc.perform(get("/"+Constants.API_PATH_V1+"meals/999"))
                .andExpect(status().isNotFound());
    }


    @Test
    void whenUpdateValidMeal_thenReturnUpdatedMeal() throws Exception {
        Meal existingMeal = createTestMeal();
        Meal updatedMeal = new Meal(1L, createTestRestaurant(), new BigDecimal("14.99"), LocalDateTime.now(), "Premium Pasta");

        when(mealService.updateMeal(any(Meal.class))).thenReturn(updatedMeal);

        mockMvc.perform(put("/"+Constants.API_PATH_V1+"meals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingMeal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(14.99))
                .andExpect(jsonPath("$.meal").value("Premium Pasta"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.restaurant.id").value(1));
    }

    @Test
    void whenUpdateNonExistingMeal_thenReturnNotFound() throws Exception {
        Meal nonExistingMeal = new Meal(999L, createTestRestaurant(), new BigDecimal("14.99"), LocalDateTime.now(), "Non-existing meal");
        when(mealService.updateMeal(any(Meal.class))).thenReturn(null);

        mockMvc.perform(put("/"+Constants.API_PATH_V1+"meals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nonExistingMeal)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenDeleteExistingMeal_thenReturnNoContent() throws Exception {
        when(mealService.deleteMealById(1L)).thenReturn(true);

        mockMvc.perform(delete("/"+Constants.API_PATH_V1+"meals/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDeleteNonExistingMeal_thenReturnNotFound() throws Exception {
        when(mealService.deleteMealById(999L)).thenReturn(false);

        mockMvc.perform(delete("/"+Constants.API_PATH_V1+"meals/999"))
                .andExpect(status().isNotFound());
    }
}