package ua.deti.tqs.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.deti.tqs.backend.entities.Restaurant;
import ua.deti.tqs.backend.services.interfaces.RestaurantService;
import ua.deti.tqs.backend.utils.Constants;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(Constants.API_PATH_V1 + "restaurant")
@Tag(name = "Restaurant", description = "The Restaurant API")
@AllArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    @PostMapping()
    @Operation(summary = "Create a new restaurant", description = "Adds a new restaurant to the system.")
    @ApiResponse(responseCode = "201", description = "Restaurant created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request body")
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) {
        log.info("Creating a new restaurant");

        Restaurant newRestaurant = restaurantService.createRestaurant(restaurant);
        HttpStatus status = newRestaurant != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;

        if (newRestaurant != null) {
            log.info("Restaurant created successfully with ID: {}", newRestaurant.getId());
        } else {
            log.warn("Failed to create restaurant");
        }
        return new ResponseEntity<>(newRestaurant, status);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get a restaurant by ID", description = "Fetches a restaurant based on its unique ID.")
    @ApiResponse(responseCode = "200", description = "Restaurant found")
    @ApiResponse(responseCode = "404", description = "Restaurant not found")
    public ResponseEntity<Restaurant> getRestaurant(@PathVariable("id") Long id) {
        log.info("Fetching a restaurant by ID: {}", id);

        Restaurant restaurant = restaurantService.getRestaurantById(id);
        HttpStatus status = restaurant != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        if (restaurant != null) {
            log.info("Restaurant found with ID: {}", restaurant.getId());
        } else {
            log.warn("Restaurant not found with ID: {}", id);
        }
        return new ResponseEntity<>(restaurant, status);
    }

    @GetMapping("name/{name}")
    @Operation(summary = "Get a restaurant by name", description = "Fetches a restaurant based on its name.")
    @ApiResponse(responseCode = "200", description = "Restaurant found")
    @ApiResponse(responseCode = "404", description = "Restaurant not found")
    public ResponseEntity<Restaurant> getRestaurantByName(@PathVariable("name") String name) {
        log.info("Fetching a restaurant by name: {}", name);

        Restaurant restaurant = restaurantService.getRestaurantByName(name);
        HttpStatus status = restaurant != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        if (restaurant != null) {
            log.info("Restaurant found with name: {}", restaurant.getName());
        } else {
            log.warn("Restaurant not found with name: {}", name);
        }
        return new ResponseEntity<>(restaurant, status);
    }

    @GetMapping("all")
    @Operation(summary = "Get all restaurants", description = "Fetches a list of all restaurants.")
    @ApiResponse(responseCode = "200", description = "List of restaurants retrieved successfully")
    @ApiResponse(responseCode = "404", description = "No restaurants found")
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        log.info("Fetching all restaurants");

        List<Restaurant> restaurants = restaurantService.getAllRestaurants();

        if (restaurants.isEmpty()) {
            log.warn("No restaurants found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        log.info("Restaurants retrieved successfully");
        return new ResponseEntity<>(restaurants, HttpStatus.OK );
    }

    @PutMapping()
    @Operation(summary = "Update a restaurant", description = "Updates an existing restaurant's details.")
    @ApiResponse(responseCode = "200", description = "Restaurant updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request body")
    public ResponseEntity<Restaurant> updateRestaurant(@RequestBody Restaurant restaurant) {
        log.info("Updating restaurant with ID: {}", restaurant.getId());

        Restaurant updatedRestaurant = restaurantService.updateRestaurant(restaurant);
        HttpStatus status = updatedRestaurant != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        if (updatedRestaurant != null) {
            log.info("Restaurant updated successfully with ID: {}", updatedRestaurant.getId());
        } else {
            log.warn("Failed to update restaurant with ID: {}", restaurant.getId());
        }
        return new ResponseEntity<>(updatedRestaurant, status);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete a restaurant", description = "Deletes a restaurant by its unique ID.")
    @ApiResponse(responseCode = "204", description = "Restaurant deleted successfully")
    @ApiResponse(responseCode = "404", description = "Restaurant not found")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable("id") Long id) {
        log.info("Deleting restaurant with ID: {}", id);

        boolean result = restaurantService.deleteRestaurantById(id);

        if (result) {
            log.info("Restaurant deleted successfully with ID: {}", id);
        } else {
            log.warn("Failed to delete restaurant with ID: {}", id);
        }
        return new ResponseEntity<>(result ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }
}
