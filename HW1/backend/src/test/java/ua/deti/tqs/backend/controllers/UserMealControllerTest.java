package ua.deti.tqs.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import ua.deti.tqs.backend.entities.*;
import ua.deti.tqs.backend.entities.utils.UserRole;
import ua.deti.tqs.backend.services.interfaces.UserMealService;
import ua.deti.tqs.backend.services.interfaces.UserService;
import ua.deti.tqs.backend.services.interfaces.WeatherService;
import ua.deti.tqs.backend.dtos.Forecast;
import ua.deti.tqs.backend.entities.utils.WeatherIPMA;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.assertj.core.api.AssertionsForClassTypes.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserMealController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class UserMealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserMealService userMealService;

    @MockitoBean
    private WeatherService weatherService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserMeal userMeal;

    @BeforeEach
    void setUp() {
        userMeal = new UserMeal();
        userMeal.setId(1L);
        userMeal.setIsCheck(true);
        userMeal.setCode("ABC123");

        User user = new User();
        user.setId(1L);
        user.setUsername("user1");
        user.setPassword("pass1");
        user.setRole(UserRole.USER);
        userMeal.setUser(user);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("restaurant1");
        restaurant.setLocation("Aveiro");
        restaurant.setSeats(50);

        Meal meal = new Meal();
        meal.setId(1L);
        meal.setMeal("meal1");
        meal.setPrice(BigDecimal.TEN);
        meal.setDate(LocalDateTime.now().plusDays(1));
        meal.setRestaurant(restaurant);

        userMeal.setMeal(meal);

        when(weatherService.getLocationId(any())).thenReturn(Optional.of(10));
        when(weatherService.getForecastByLocation(anyInt())).thenReturn(List.of());
        when(weatherService.getWeatherForDate(any(), any())).thenReturn(Optional.of(createTestWeatherIPMAData()));
    }

    private WeatherIPMA createTestWeatherIPMAData() {
        return new WeatherIPMA("10", "10", "10", "10", "10", 10, 10);
    }

    @Test
    void testCreateUserMeal() throws Exception {
        when(userMealService.createUserMeal(any())).thenReturn(userMeal);

        mockMvc.perform(post("/api/private/v1/user-meal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userMeal)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userMeal.getId()));

    }

    @Test
    void testCreateUserMealBadRequest() throws Exception {
        userMeal.setCode(null);

        mockMvc.perform(post("/api/private/v1/user-meal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userMeal)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetUserMealByIdSuccess() throws Exception {
        when(userMealService.getUserMealById(1L)).thenReturn(userMeal);

        mockMvc.perform(get("/api/private/v1/user-meal/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userMeal.id").value(userMeal.getId()));
    }

    @Test
    void testGetUserMealByIdNotFound() throws Exception {
        when(userMealService.getUserMealById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/private/v1/user-meal/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllMealsByUserId() throws Exception {
        when(userMealService.getAllUserMealsByUserId(1L)).thenReturn(List.of(userMeal));

        mockMvc.perform(get("/api/private/v1/user-meal/all/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userMeal.id").value(userMeal.getId()));
    }

    @Test
    void testGetAllMealsByRestaurantId() throws Exception {
        when(userMealService.getAllUserMealsByRestaurantId(1L)).thenReturn(List.of(userMeal));

        mockMvc.perform(get("/api/private/v1/user-meal/all/restaurant/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userMeal.id").value(userMeal.getId()));
    }

    @Test
    void testGetAllMealsByRestaurantIdNotFound() throws Exception {
        when(userMealService.getAllUserMealsByRestaurantId(1L)).thenReturn(null);

        mockMvc.perform(get("/api/private/v1/user-meal/all/restaurant/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateUserMeal() throws Exception {
        when(userMealService.updateUserMeal(any())).thenReturn(userMeal);

        mockMvc.perform(put("/api/private/v1/user-meal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userMeal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userMeal.getId()));
    }

    @Test
    void testUpdateUserMealBadRequest() throws Exception {
        userMeal.setCode(null);

        mockMvc.perform(put("/api/private/v1/user-meal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userMeal)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteUserMealSuccess() throws Exception {
        when(userMealService.deleteUserMealById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/private/v1/user-meal/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteUserMealNotFound() throws Exception {
        when(userMealService.deleteUserMealById(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/private/v1/user-meal/1"))
                .andExpect(status().isNotFound());
    }
}
