package ua.deti.tqs.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.deti.tqs.backend.entities.Restaurant;
import ua.deti.tqs.backend.services.interfaces.RestaurantService;
import ua.deti.tqs.backend.utils.Constants;

@RestController
@RequestMapping(Constants.API_PATH_V1 + "restaurant")
@Tag(name = "Restaurant", description = "The Restaurant API")
@AllArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    @PostMapping()
    @Operation(summary = "Create a new restaurant", description = "Adds a new restaurant to the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Restaurant created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) {
        Restaurant newRestaurant = restaurantService.createRestaurant(restaurant);
        HttpStatus status = newRestaurant != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(newRestaurant, status);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get a restaurant by ID", description = "Fetches a restaurant based on its unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant found"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    public ResponseEntity<Restaurant> getRestaurant(@PathVariable("id") Long id) {
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        HttpStatus status = restaurant != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(restaurant, status);
    }

    @GetMapping("name/{name}")
    @Operation(summary = "Get a restaurant by name", description = "Fetches a restaurant based on its name.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant found"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    public ResponseEntity<Restaurant> getRestaurantByName(@PathVariable("name") String name) {
        Restaurant restaurant = restaurantService.getRestaurantByName(name);
        HttpStatus status = restaurant != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(restaurant, status);
    }

    @GetMapping("all")
    @Operation(summary = "Get all restaurants", description = "Fetches a list of all restaurants.")
    @ApiResponse(responseCode = "200", description = "List of restaurants retrieved successfully")
    public ResponseEntity<Iterable<Restaurant>> getAllRestaurants() {
        Iterable<Restaurant> restaurants = restaurantService.getAllRestaurants();
        HttpStatus status = restaurants != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(restaurants, status);
    }

    @PutMapping()
    @Operation(summary = "Update a restaurant", description = "Updates an existing restaurant's details.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    public ResponseEntity<Restaurant> updateRestaurant(@RequestBody Restaurant restaurant) {
        Restaurant updatedRestaurant = restaurantService.updateRestaurant(restaurant);
        HttpStatus status = updatedRestaurant != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(updatedRestaurant, status);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete a restaurant", description = "Deletes a restaurant by its unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Restaurant deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    public ResponseEntity<Void> deleteRestaurant(@PathVariable("id") Long id) {
        boolean result = restaurantService.deleteRestaurantById(id);
        return new ResponseEntity<>(result ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }
}
