package ua.deti.tqs.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.deti.tqs.backend.dtos.MealDTO;
import ua.deti.tqs.backend.entities.Meal;
import ua.deti.tqs.backend.services.interfaces.MealService;
import ua.deti.tqs.backend.services.interfaces.WeatherService;
import ua.deti.tqs.backend.utils.Constants;

import java.util.List;

@RestController
@RequestMapping(Constants.API_PATH_V1 + "meals")
@Tag(name = "Meals", description = "The Meals API")
@AllArgsConstructor
public class MealController {
    private final MealService mealService;
    private final WeatherService weatherService;

    @PostMapping()
    @Operation(summary = "Create a new meal", description = "Adds a new meal to the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Meal created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    public ResponseEntity<Meal> createMeal(@RequestBody Meal meal) {

        Meal newMeal = mealService.createMeal(meal);
        HttpStatus status = newMeal != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(newMeal, status);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get a meal by Id", description = "Fetches a meal based on its unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Meal found"),
            @ApiResponse(responseCode = "404", description = "Meal not found")
    })
    public ResponseEntity<MealDTO> getMeal(@PathVariable("id") Long id) {
        Meal meal = mealService.getMealById(id);

        if (meal != null) {
            MealDTO mealDTO = new MealDTO(weatherService).mapToDTO(meal);
            return ResponseEntity.ok(mealDTO);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("all/{restaurantId}")
    @Operation(summary = "Get all meals", description = "Fetches a list of all meals per restaurant id.")
    @ApiResponse(responseCode = "200", description = "List of meals retrieved successfully")
    public ResponseEntity<List<MealDTO>> getAllMealsByRestaurantId(@PathVariable("restaurantId") Long restaurantId) {
        List<Meal> meals = mealService.getAllMealsByRestaurantId(restaurantId);

        List<MealDTO> mealDTOs = meals.stream()
                .map(meal -> new MealDTO(weatherService).mapToDTO(meal))
                .toList();

        return ResponseEntity.ok(mealDTOs);
    }


    @PutMapping()
    @Operation(summary = "Update a meal", description = "Updates an existing meal's details.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Meal updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    public ResponseEntity<Meal> updateMeal(@RequestBody Meal meal) {
        Meal updatedMeal = mealService.updateMeal(meal);
        HttpStatus status = updatedMeal != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(updatedMeal, status);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete a meal", description = "Deletes a meal by its unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Meal deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Meal not found")
    })
    public ResponseEntity<Void> deleteMeal(@PathVariable("id") Long id) {
        boolean result = mealService.deleteMealById(id);
        return new ResponseEntity<>(result ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }

}
