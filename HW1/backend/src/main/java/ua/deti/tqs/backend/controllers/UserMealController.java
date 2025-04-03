package ua.deti.tqs.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.deti.tqs.backend.dtos.MealDTO;
import ua.deti.tqs.backend.dtos.UserMealDTO;
import ua.deti.tqs.backend.entities.Meal;
import ua.deti.tqs.backend.entities.UserMeal;
import ua.deti.tqs.backend.services.interfaces.UserMealService;
import ua.deti.tqs.backend.services.interfaces.WeatherService;
import ua.deti.tqs.backend.utils.Constants;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(Constants.API_PATH_PRIVATE_V1 +"user-meal")
@Tag(name = "UserMeals", description = "The UserMeals API")
@AllArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class UserMealController {
    private final UserMealService userMealService;
    private final WeatherService weatherService;

    @PostMapping()
    @Operation(summary = "Create a new userMeal", description = "Adds a new userMeal to the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "UserMeal created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    public ResponseEntity<UserMeal> createUserMeal(@RequestBody UserMeal meal) {
        log.info("Creating a new userMeal");

        UserMeal newUserMeal = userMealService.createUserMeal(meal);
        HttpStatus status = newUserMeal != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;

        if (newUserMeal != null) {
            log.info("UserMeal created successfully with ID: {}", newUserMeal.getId());
        } else {
            log.warn("Failed to create userMeal");
        }
        return new ResponseEntity<>(newUserMeal, status);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get a userMeal by Id", description = "Fetches a userMeal based on its unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "UserMeal found"),
            @ApiResponse(responseCode = "404", description = "UserMeal not found")
    })
    public ResponseEntity<UserMealDTO> getUserMeal(@PathVariable("id") Long id) {
        log.info("Fetching a userMeal by ID: {}", id);

        UserMeal userMeal = userMealService.getUserMealById(id);

        if (userMeal != null) {
            log.info("UserMeal found with ID: {}", userMeal.getId());
            UserMealDTO userMealDTO = UserMealDTO.fromUserMeal(userMeal,weatherService);

            log.info("Sent UserMealDTO successfully for userMeal ID: {}", userMeal.getId());
            return ResponseEntity.ok(userMealDTO);
        }

        log.warn("UserMeal not found with ID: {}", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

    @GetMapping("all/{userId}")
    @Operation(summary = "Get all UserMeals", description = "Fetches a list of all UserMeals per user id.")
    @ApiResponse(responseCode = "200", description = "List of UserMeals retrieved successfully")
    public ResponseEntity<List<UserMealDTO>> getAllMealsByUserId(@PathVariable("userId") Long userId) {
        log.info("Fetching all userMeals for user ID: {}", userId);

        List<UserMeal> userMeals = userMealService.getAllUserMealsByUserId(userId);


        List<UserMealDTO> userMealDTOs = userMeals.stream()
                .map(userMeal -> UserMealDTO.fromUserMeal(userMeal,weatherService))
                .toList();

        log.info("Sent UserMealDTOs successfully for user ID: {}", userId);
        return ResponseEntity.ok(userMealDTOs);
    }

    @GetMapping("all/restaurant/{restaurantId}")
    @Operation(summary = "Get all UserMeals", description = "Fetches a list of all UserMeals per restaurant id.")
    @ApiResponse(responseCode = "200", description = "List of UserMeals retrieved successfully")
    public ResponseEntity<List<UserMealDTO>> getAllMealsByRestaurantId(@PathVariable("restaurantId") Long restaurantId) {
        log.info("Fetching all userMeals for restaurant ID: {}", restaurantId);

        List<UserMeal> userMeals = userMealService.getAllUserMealsByRestaurantId(restaurantId);


        List<UserMealDTO> userMealDTOs = userMeals.stream()
                .map(userMeal -> UserMealDTO.fromUserMeal(userMeal,weatherService))
                .toList();

        log.info("Sent UserMealDTOs successfully for restaurant ID: {}", restaurantId);
        return ResponseEntity.ok(userMealDTOs);
    }

    @PutMapping()
    @Operation(summary = "Update a userMeal", description = "Updates an existing userMeal's details.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "UserMeal updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    public ResponseEntity<UserMeal> updateUserMeal(@RequestBody UserMeal userMeal) {
        log.info("Updating userMeal with ID: {}", userMeal.getId());

        UserMeal updateUserMeal = userMealService.updateUserMeal(userMeal);
        HttpStatus status = updateUserMeal != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        if (updateUserMeal != null) {
            log.info("UserMeal updated successfully with ID: {}", updateUserMeal.getId());
        } else {
            log.warn("Failed to update userMeal with ID: {}", userMeal.getId());
        }
        return new ResponseEntity<>(updateUserMeal, status);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete a userMeal", description = "Deletes a userMeal by its unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "UserMeal deleted successfully"),
            @ApiResponse(responseCode = "404", description = "UserMeal not found")
    })
    public ResponseEntity<Void> deleteUserMeal(@PathVariable("id") Long id) {
        log.info("Deleting userMeal with ID: {}", id);

        boolean result = userMealService.deleteUserMealById(id);

        if (result) {
            log.info("UserMeal deleted successfully with ID: {}", id);
        } else {
            log.warn("Failed to delete userMeal with ID: {}", id);
        }
        return new ResponseEntity<>(result ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }

}
