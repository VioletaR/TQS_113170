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
import ua.deti.tqs.backend.entities.User;

import ua.deti.tqs.backend.services.interfaces.UserService;
import ua.deti.tqs.backend.utils.Constants;

@Slf4j
@RestController
@RequestMapping(Constants.API_PATH_PRIVATE_V1 + "users")
@Tag(name = "Users", description = "The Users API")
@AllArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class UserController {
    private final UserService userService;

    @PostMapping()
    @Operation(summary = "Create a new user", description = "Adds a new user to the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    public ResponseEntity<User> createUser(@RequestBody User user) {
        log.info("Creating a new user");

        User newUser = userService.createUser(user);
        HttpStatus status = newUser != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;

        if (newUser != null) {
            log.info("User created successfully with ID: {}", newUser.getId());
        } else {
            log.warn("Failed to create user");
        }
        return new ResponseEntity<>(newUser, status);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get a user by Id", description = "Fetches a user based on its unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        log.info("Fetching a user by ID: {}", id);

        User user = userService.getUserById(id);
        HttpStatus status = user != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        if (user != null) {
            log.info("User found with ID: {}", user.getId());
        } else {
            log.warn("User not found with ID: {}", id);
        }
        return new ResponseEntity<>(user, status);
    }

    @GetMapping("name/{name}")
    @Operation(summary = "Get a user by name", description = "Fetches a user based on its name.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<User> getUserByName(@PathVariable("name") String name) {
        log.info("Fetching a user by name: {}", name);

        User user = userService.getUserByName(name);
        HttpStatus status = user != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        if (user != null) {
            log.info("User found with name: {}", user.getUsername());
        } else {
            log.warn("User not found with name: {}", name);
        }
        return new ResponseEntity<>(user, status);
    }

    @PutMapping()
    @Operation(summary = "Update a user", description = "Updates an existing user's details.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        log.info("Updating user with ID: {}", user.getId());

        User updateUser = userService.updateUser(user);
        HttpStatus status = updateUser != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        if (updateUser != null) {
            log.info("User updated successfully with ID: {}", updateUser.getId());
        } else {
            log.warn("Failed to update user with ID: {}", user.getId());
        }
        return new ResponseEntity<>(updateUser, status);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete a user", description = "Deletes a user by its unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        log.info("Deleting user with ID: {}", id);

        boolean result = userService.deleteUserById(id);

        if (result) {
            log.info("User deleted successfully with ID: {}", id);
        } else {
            log.warn("Failed to delete user with ID: {}", id);
        }
        return new ResponseEntity<>(result ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }

}
