package ua.deti.tqs.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
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

        UserMeal newUserMeal = userMealService.createUserMeal(meal);
        HttpStatus status = newUserMeal != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(newUserMeal, status);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get a userMeal by Id", description = "Fetches a userMeal based on its unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "UserMeal found"),
            @ApiResponse(responseCode = "404", description = "UserMeal not found")
    })
    public ResponseEntity<UserMealDTO> getUserMeal(@PathVariable("id") Long id) {
        UserMeal userMeal = userMealService.getUserMealById(id);

        if (userMeal != null) {
            UserMealDTO userMealDTO = new UserMealDTO(weatherService).mapToDTO(userMeal);
            return ResponseEntity.ok(userMealDTO);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

    @GetMapping("all/{userId}")
    @Operation(summary = "Get all UserMeals", description = "Fetches a list of all UserMeals per user id.")
    @ApiResponse(responseCode = "200", description = "List of UserMeals retrieved successfully")
    public ResponseEntity<List<UserMealDTO>> getAllMealsByRestaurantId(@PathVariable("userId") Long userId) {
        List<UserMeal> userMeals = userMealService.getAllUserMealsByUserId(userId);


        List<UserMealDTO> userMealDTOs = userMeals.stream()
                .map(userMeal -> new UserMealDTO(weatherService).mapToDTO(userMeal))
                .toList();

        return ResponseEntity.ok(userMealDTOs);
    }

    @PutMapping()
    @Operation(summary = "Update a userMeal", description = "Updates an existing userMeal's details.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "UserMeal updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    public ResponseEntity<UserMeal> updateUserMeal(@RequestBody UserMeal userMeal) {
        UserMeal updateUserMeal = userMealService.updateUserMeal(userMeal);
        HttpStatus status = updateUserMeal != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(updateUserMeal, status);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete a userMeal", description = "Deletes a userMeal by its unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "UserMeal deleted successfully"),
            @ApiResponse(responseCode = "404", description = "UserMeal not found")
    })
    public ResponseEntity<Void> deleteUserMeal(@PathVariable("id") Long id) {
        boolean result = userMealService.deleteUserMealById(id);
        return new ResponseEntity<>(result ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }

}
