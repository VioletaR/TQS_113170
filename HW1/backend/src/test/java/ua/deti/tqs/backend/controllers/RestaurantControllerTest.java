package ua.deti.tqs.backend.controllers;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.deti.tqs.backend.authentication.AuthMiddleware;
import ua.deti.tqs.backend.entities.Restaurant;
import ua.deti.tqs.backend.services.RestaurantServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import ua.deti.tqs.backend.services.interfaces.RestaurantService;
import ua.deti.tqs.backend.services.interfaces.UserService;
import ua.deti.tqs.backend.utils.Constants;
import org.springframework.http.MediaType;


import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(RestaurantController.class)
public class RestaurantControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private RestaurantService restaurantService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void whenGetRestaurantById_withValidId_thenReturnRestaurant() throws Exception {
        Restaurant restaurant = new Restaurant(1L, "restaurant1", "location", 10);
        when(restaurantService.getRestaurantById(restaurant.getId())).thenReturn(restaurant);

        mockMvc.perform(get("/"+Constants.API_PATH_V1+"restaurant/{id}", restaurant.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(restaurant.getId()))
                .andExpect(jsonPath("$.name").value(restaurant.getName()))
                .andExpect(jsonPath("$.location").value(restaurant.getLocation()))
                .andExpect(jsonPath("$.seats").value(restaurant.getSeats()));
    }

    @Test
    void whenGetRestaurantById_withInvalidId_thenReturnNotFound() throws Exception {
        Long invalidId = 999L;
        when(restaurantService.getRestaurantById(invalidId)).thenReturn(null);

        mockMvc.perform(get("/"+Constants.API_PATH_V1+"restaurant/{id}", invalidId))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetRestaurantByName_withValidName_thenReturnRestaurant() throws Exception {
        String validName = "restaurant1";
        Restaurant restaurant = new Restaurant(1L, validName, "Location", 10);
        when(restaurantService.getRestaurantByName(validName)).thenReturn(restaurant);

        mockMvc.perform(get("/"+Constants.API_PATH_V1+"restaurant/name/{name}", validName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(validName));
    }

    @Test
    void whenGetRestaurantByName_withInvalidName_thenReturnNotFound() throws Exception {
        String invalidName = "invalidName";
        when(restaurantService.getRestaurantByName(invalidName)).thenReturn(null);

        mockMvc.perform(get("/"+Constants.API_PATH_V1+"restaurant/name/{name}", invalidName))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetAllRestaurants_thenReturnListOfRestaurants() throws Exception {
        Restaurant restaurant1 = new Restaurant(1L, "restaurant1", "Location1", 10);
        Restaurant restaurant2 = new Restaurant(2L, "restaurant2", "Location2", 20);
        when(restaurantService.getAllRestaurants()).thenReturn(List.of(restaurant1, restaurant2));

        mockMvc.perform(get("/"+Constants.API_PATH_V1+"restaurant/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(restaurant1.getId()))
                .andExpect(jsonPath("$[1].id").value(restaurant2.getId()));
    }

    @Test
    void whenGetAllRestaurants_andNoRestaurantsExist_thenReturnNotFound() throws Exception {
        when(restaurantService.getAllRestaurants()).thenReturn(null);

        mockMvc.perform(get("/"+Constants.API_PATH_V1+"restaurant/all"))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenCreateValidRestaurant_thenReturnCreated() throws Exception {
        Restaurant newRestaurant = new Restaurant(null, "New Restaurant", "New Location", 30);
        Restaurant savedRestaurant = new Restaurant(1L, "New Restaurant", "New Location", 30);
        when(restaurantService.createRestaurant(any(Restaurant.class))).thenReturn(savedRestaurant);

        mockMvc.perform(post("/"+Constants.API_PATH_V1+"restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRestaurant)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(savedRestaurant.getId()));
    }

    @Test
    void whenCreateInvalidRestaurant_thenReturnBadRequest() throws Exception {
        Restaurant invalidRestaurant = new Restaurant(null, null, null, -5);
        when(restaurantService.createRestaurant(any(Restaurant.class))).thenReturn(null);

        mockMvc.perform(post("/"+Constants.API_PATH_V1+"restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRestaurant)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenUpdateValidRestaurant_thenReturnUpdatedRestaurant() throws Exception {
        Restaurant existingRestaurant = new Restaurant(1L, "Existing", "Location", 10);
        Restaurant updatedRestaurant = new Restaurant(1L, "Updated", "New Location", 20);
        when(restaurantService.updateRestaurant(any(Restaurant.class))).thenReturn(updatedRestaurant);

        mockMvc.perform(put("/"+Constants.API_PATH_V1+"restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingRestaurant)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedRestaurant.getName()));
    }

    @Test
    void whenUpdateInvalidRestaurant_thenReturnBadRequest() throws Exception {
        Restaurant invalidRestaurant = new Restaurant(999L, "Non-existing", "Location", 10);
        when(restaurantService.updateRestaurant(any(Restaurant.class))).thenReturn(null);

        mockMvc.perform(put("/"+Constants.API_PATH_V1+"restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRestaurant)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenDeleteExistingRestaurant_thenReturnNoContent() throws Exception {
        Long existingId = 1L;
        when(restaurantService.deleteRestaurantById(existingId)).thenReturn(true);

        mockMvc.perform(delete("/"+Constants.API_PATH_V1+"restaurant/{id}", existingId))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDeleteNonExistingRestaurant_thenReturnNotFound() throws Exception {
        Long nonExistingId = 999L;
        when(restaurantService.deleteRestaurantById(nonExistingId)).thenReturn(false);

        mockMvc.perform(delete("/"+Constants.API_PATH_V1+"restaurant/{id}", nonExistingId))
                .andExpect(status().isNotFound());
    }
}