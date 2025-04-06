package ua.deti.tqs.backend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import ua.deti.tqs.backend.authentication.utils.CurrentUser;
import ua.deti.tqs.backend.entities.User;
import ua.deti.tqs.backend.entities.utils.UserRole;
import ua.deti.tqs.backend.repositories.UserRepository;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CurrentUser currentUser;

    @InjectMocks
    private UserServiceImpl userService;

    private User user1;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setRole(UserRole.USER);

    }

    @Test
    void whenValidLoginCredentials_thenReturnUser() {
        when(userRepository.findUserByUsername("user1")).thenReturn(Optional.of(user1));

        User loggedIn = userService.loginUser("user1", "password1");

        assertThat(loggedIn).isEqualTo(user1);
        verify(userRepository).findUserByUsername("user1");
    }

    @Test
    void whenInvalidLoginCredentials_thenReturnNull() {
        when(userRepository.findUserByUsername("user1")).thenReturn(Optional.of(user1));

        User result = userService.loginUser("user1", "wrongpassword");
        assertThat(result).isNull();
    }

    @Test
    void whenLoginWithNonExistentUser_thenReturnNull() {
        when(userRepository.findUserByUsername("nonexistent")).thenReturn(Optional.empty());

        User result = userService.loginUser("nonexistent", "password");
        assertThat(result).isNull();
    }

    @Test
    void whenCreateValidUser_thenReturnUser() {
        when(userRepository.findUserByUsername("user1")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user1);

        User created = userService.createUser(user1);

        assertThat(created).isEqualTo(user1);
        verify(userRepository).save(user1);
    }

    @Test
    void whenCreateUserWithExistingUsername_thenReturnNull() {
        when(userRepository.findUserByUsername("user1")).thenReturn(Optional.of(user1));

        User newUser = new User();
        newUser.setUsername("user1");
        User result = userService.createUser(newUser);

        assertThat(result).isNull();
        verify(userRepository, never()).save(any());
    }

    @Test
    void whenCreateUserWithMissingFields_thenReturnNull() {
        // Test missing username
        User invalid = new User();
        invalid.setPassword("pass");
        invalid.setRole(UserRole.USER);
        User result = userService.createUser(invalid);
        assertThat(result).isNull();

        // Test missing password
        invalid.setUsername("user2");
        invalid.setPassword(null);
        result = userService.createUser(invalid);
        assertThat(result).isNull();

        // Test missing role
        invalid.setPassword("pass");
        invalid.setRole(null);
        result = userService.createUser(invalid);
        assertThat(result).isNull();
    }

    @Test
    void whenCreateUserWithEmptyFields_thenReturnNull() {
        // Test empty username
        User invalid = new User();
        invalid.setUsername("");
        invalid.setPassword("pass");
        invalid.setRole(UserRole.USER);
        User result = userService.createUser(invalid);
        assertThat(result).isNull();

        // Test empty password
        invalid.setUsername("user2");
        invalid.setPassword("");
        result = userService.createUser(invalid);
        assertThat(result).isNull();

        // Test empty role
        invalid.setPassword("pass");
        invalid.setRole(null);
        result = userService.createUser(invalid);
        assertThat(result).isNull();
    }

    @Test
    void whenUserGetsOwnProfile_thenAllow() {
        when(currentUser.getAuthenticatedUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        User found = userService.getUserById(1L);
        assertThat(found).isEqualTo(user1);
    }

    @Test
    void whenUserGetsOtherProfile_thenDeny() {
        when(currentUser.getAuthenticatedUserId()).thenReturn(2L);

        User found = userService.getUserById(1L);
        assertThat(found).isNull();
    }

    @Test
    void whenUserGetsProfileWithInvalidId_thenDeny() {
        when(currentUser.getAuthenticatedUserId()).thenReturn(null);

        User found = userService.getUserById(1L);
        assertThat(found).isNull();
    }

    @Test
    void whenUserGetsProfileWithUserName_thenAllow() {
        when(currentUser.getAuthenticatedUserId()).thenReturn(1L);
        when(userRepository.findUserByUsername("user1")).thenReturn(Optional.of(user1));

        User found = userService.getUserByName("user1");
        assertThat(found).isEqualTo(user1);
    }

    @Test
    void whenUserGetsProfileWithInvalidUserName_thenDeny() {
        User found = userService.getUserByName("invalidUser");
        assertThat(found).isNull();
    }

    @Test
    void whenUserGetsProfileButNotAuthenticated_thenDeny() {
        when(userRepository.findUserByUsername("user1")).thenReturn(Optional.of(user1));
        when(currentUser.getAuthenticatedUserId()).thenReturn(null);

        User found = userService.getUserByName("user1");
        assertThat(found).isNull();
    }

    @Test
    void whenUserGetsProfileFromOtherUser_thenDeny() {
        when(currentUser.getAuthenticatedUserId()).thenReturn(2L);
        when(userRepository.findUserByUsername("user1")).thenReturn(Optional.of(user1));

        User found = userService.getUserByName("user1");
        assertThat(found).isNull();
    }


    @Test
    void whenUserUpdatesOwnProfile_thenAllow() {
        when(currentUser.getAuthenticatedUserId()).thenReturn(1L);
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("updatedName");
        updatedUser.setPassword("updatedPassword");
        updatedUser.setRole(UserRole.STAFF);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.save(any())).thenReturn(updatedUser);

        User result = userService.updateUser(updatedUser);
        assertThat(result.getUsername()).isEqualTo("updatedName");
        assertThat(result.getPassword()).isEqualTo("updatedPassword");
        assertThat(result.getRole()).isEqualTo(UserRole.STAFF);
    }

    @Test
    void whenUserUpdatesOwnProfileWithInvalidData_thenDeny() {
        when(currentUser.getAuthenticatedUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("");
        updatedUser.setPassword("");
        updatedUser.setRole(UserRole.USER);

        User result = userService.updateUser(updatedUser);
        assertThat(result).isNull();
    }

    @Test
    void whenUserUpdatesOwnProfileWithNullData_thenDeny() {
        when(currentUser.getAuthenticatedUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername(null);
        updatedUser.setPassword(null);
        updatedUser.setRole(null);

        User result = userService.updateUser(updatedUser);
        assertThat(result).isNull();
    }

    @Test
    void whenUserUpdatesProfileThatDoesNotExist_thenDeny() {
        when(currentUser.getAuthenticatedUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("updatedName");
        updatedUser.setPassword("updatedPassword");
        updatedUser.setRole(UserRole.STAFF);

        User result = userService.updateUser(updatedUser);
        assertThat(result).isNull();
    }

    @Test
    void whenUserUpdatesOtherProfile_thenDeny() {
        when(currentUser.getAuthenticatedUserId()).thenReturn(2L);
        User updatedUser = new User();
        updatedUser.setId(1L);

        User result = userService.updateUser(updatedUser);
        assertThat(result).isNull();
    }

    @Test
    void whenUnauthenticatedUserUpdates_thenDeny() {
        when(currentUser.getAuthenticatedUserId()).thenReturn(null);
        User updatedUser = new User();
        updatedUser.setId(1L);

        User result = userService.updateUser(updatedUser);
        assertThat(result).isNull();
    }

    @Test
    void whenUserDeletesOwnAccount_thenAllow() {
        when(currentUser.getAuthenticatedUserId()).thenReturn(1L);
        when(userRepository.existsById(1L)).thenReturn(true);

        boolean result = userService.deleteUserById(1L);
        assertThat(result).isTrue();
        verify(userRepository).deleteById(1L);
    }

    @Test
    void whenUserDeletesOtherAccount_thenDeny() {
        when(currentUser.getAuthenticatedUserId()).thenReturn(2L);

        boolean result = userService.deleteUserById(1L);
        assertThat(result).isFalse();
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void whenUnauthenticatedUserDeletes_thenDeny() {
        when(currentUser.getAuthenticatedUserId()).thenReturn(null);

        boolean result = userService.deleteUserById(1L);
        assertThat(result).isFalse();
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void whenDeleteInvalidUserId_thenDoNothing() {
        when(currentUser.getAuthenticatedUserId()).thenReturn(1L);
        when(userRepository.existsById(1L)).thenReturn(false);

        boolean result = userService.deleteUserById(1L);
        assertThat(result).isFalse();
        verify(userRepository, never()).deleteById(any());
    }
}
