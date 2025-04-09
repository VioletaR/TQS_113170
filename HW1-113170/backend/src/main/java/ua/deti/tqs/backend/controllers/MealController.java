package ua.deti.tqs.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.deti.tqs.backend.dtos.MealDTO;
import ua.deti.tqs.backend.entities.Meal;
import ua.deti.tqs.backend.services.interfaces.MealService;
import ua.deti.tqs.backend.services.interfaces.WeatherService;
import ua.deti.tqs.backend.utils.Constants;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(Constants.API_PATH_V1 + "meals")
@Tag(name = "Meals", description = "The Meals API")
@AllArgsConstructor
class MealController {
    private final MealService mealService;
    private final WeatherService weatherService;

    @PostMapping()
    @Operation(summary = "Create a new meal", description = "Adds a new meal to the system.")
    @ApiResponse(responseCode = "201", description = "Meal created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request body")
    public ResponseEntity<Meal> createMeal(@RequestBody Meal meal) {
        log.info("Creating a new meal");
        Meal newMeal = mealService.createMeal(meal);
        HttpStatus status = newMeal != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;

        if (newMeal != null) {
            log.info("Meal created successfully with ID: {}", newMeal.getId());
        } else {
            log.warn("Failed to create meal");
        }
        return new ResponseEntity<>(newMeal, status);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get a meal by Id", description = "Fetches a meal based on its unique ID.")
    @ApiResponse(responseCode = "200", description = "Meal found")
    @ApiResponse(responseCode = "404", description = "Meal not found")
    public ResponseEntity<MealDTO> getMeal(@PathVariable("id") Long id) {
        log.info("Fetching a meal by ID: {}", id);

        Meal meal = mealService.getMealById(id);

        if (meal != null) {
            log.info("Meal found with ID: {}", meal.getId());

            MealDTO mealDTO = MealDTO.fromMeal(meal, weatherService);
            log.info("Sent MealDTO successfully for meal ID: {}", meal.getId());
            return ResponseEntity.ok(mealDTO);
        }
        log.warn("Meal not found with ID: {}", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("all/{restaurantId}")
    @Operation(summary = "Get all meals", description = "Fetches a list of all meals per restaurant id.")
    @ApiResponse(responseCode = "200", description = "List of meals retrieved successfully")
    @ApiResponse(responseCode = "404", description = "No meals found for the given restaurant ID")
    public ResponseEntity<List<MealDTO>> getAllMealsByRestaurantId(@PathVariable("restaurantId") Long restaurantId) {
        log.info("Fetching all meals for restaurant ID: {}", restaurantId);
        List<Meal> meals = mealService.getAllMealsByRestaurantId(restaurantId);
        if (meals.isEmpty()) {
            log.warn("No meals found for restaurant ID: {}", restaurantId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<MealDTO> mealDTOs = meals.stream()
                .map(meal ->  MealDTO.fromMeal(meal, weatherService))
                .toList();

        log.info("Sent MealDTOs successfully for restaurant ID: {}", restaurantId);
        return ResponseEntity.ok(mealDTOs);
    }


    @PutMapping()
    @Operation(summary = "Update a meal", description = "Updates an existing meal's details.")
    @ApiResponse(responseCode = "200", description = "Meal updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request body")
    public ResponseEntity<Meal> updateMeal(@RequestBody Meal meal) {
        log.info("Updating meal with ID: {}", meal.getId());

        Meal updatedMeal = mealService.updateMeal(meal);
        HttpStatus status = updatedMeal != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        if (updatedMeal != null) {
            log.info("Meal updated successfully with ID: {}", updatedMeal.getId());
        } else {
            log.warn("Failed to update meal with ID: {}", meal.getId());
        }

        return new ResponseEntity<>(updatedMeal, status);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete a meal", description = "Deletes a meal by its unique ID.")
    @ApiResponse(responseCode = "204", description = "Meal deleted successfully")
    @ApiResponse(responseCode = "404", description = "Meal not found")
    public ResponseEntity<Void> deleteMeal(@PathVariable("id") Long id) {
        log.info("Deleting meal with ID: {}", id);

        boolean result = mealService.deleteMealById(id);

        if (result) {
            log.info("Meal deleted successfully with ID: {}", id);
        } else {
            log.warn("Failed to delete meal with ID: {}", id);
        }
        return new ResponseEntity<>(result ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }

}
