package ua.deti.tqs.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.deti.tqs.backend.entities.User;
import ua.deti.tqs.backend.entities.utils.UserRole;
import ua.deti.tqs.backend.services.interfaces.UserService;
import ua.deti.tqs.backend.utils.Constants;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateUserSuccess() throws Exception {
        User user = new User(1L, "user1", "pass1", UserRole.USER);
        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/"+Constants.API_PATH_V1+"users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.role").value(user.getRole().name()))
                .andExpect(jsonPath("$.password").value(user.getPassword()));
    }

    @Test
    void testCreateUserFail() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(null);

        mockMvc.perform(post("/"+Constants.API_PATH_V1+"users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new User())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetUserByIdSuccess() throws Exception {
        User user = new User(1L, "user1", "pass1", UserRole.USER);
        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/"+Constants.API_PATH_PRIVATE_V1+"users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"));
    }

    @Test
    void testGetUserByIdNotFound() throws Exception {
        when(userService.getUserById(999L)).thenReturn(null);

        mockMvc.perform(get("/"+Constants.API_PATH_PRIVATE_V1+"users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetUserByNameSuccess() throws Exception {
        User user = new User(1L, "user1", "pass1", UserRole.USER);
        when(userService.getUserByName("user1")).thenReturn(user);

        mockMvc.perform(get("/"+Constants.API_PATH_PRIVATE_V1+"users/name/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"));
    }

    @Test
    void testGetUserByNameNotFound() throws Exception {
        when(userService.getUserByName("unknown")).thenReturn(null);

        mockMvc.perform(get("/"+Constants.API_PATH_PRIVATE_V1+"users/name/unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        User user = new User(1L, "updated_user", "pass", UserRole.USER);
        when(userService.updateUser(any(User.class))).thenReturn(user);

        mockMvc.perform(put("/"+Constants.API_PATH_PRIVATE_V1+"users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updated_user"));
    }

    @Test
    void testUpdateUserFail() throws Exception {
        when(userService.updateUser(any(User.class))).thenReturn(null);

        mockMvc.perform(put("/"+Constants.API_PATH_PRIVATE_V1+"users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new User())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteUserSuccess() throws Exception {
        when(userService.deleteUserById(1L)).thenReturn(true);

        mockMvc.perform(delete("/"+Constants.API_PATH_PRIVATE_V1+"users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteUserFail() throws Exception {
        when(userService.deleteUserById(999L)).thenReturn(false);


        mockMvc.perform(delete("/"+Constants.API_PATH_PRIVATE_V1+"users/999"))
                .andExpect(status().isNotFound());
    }
}
