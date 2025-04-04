//package ua.deti.tqs.backend.services;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import ua.deti.tqs.backend.authentication.utils.CurrentUser;
//import ua.deti.tqs.backend.entities.User;
//import ua.deti.tqs.backend.entities.utils.UserRole;
//import ua.deti.tqs.backend.repositories.UserRepository;
//
//import java.util.Optional;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private CurrentUser currentUser;
//
//    @InjectMocks
//    private UserServiceImpl userService;
//
//    private User user1;
//
//    @BeforeEach
//    void setUp() {
//        user1 = new User();
//        user1.setId(1L);
//        user1.setUsername("user1");
//        user1.setPassword("password1");
//        user1.setRole(UserRole.USER);
//
//    }
//
//    private void mockAuthenticatedUser(Long userId, UserRole role) {
//        when(currentUser.getAuthenticatedUserId()).thenReturn(userId);
//        when(currentUser.getAuthenticatedUserRole()).thenReturn(role);
//    }
//
//
//    @Test
//    void whenValidLoginCredentials_thenReturnUser() {
//        when(userRepository.findUserByUsername("user1")).thenReturn(Optional.of(user1));
//
//        User loggedIn = userService.loginUser("user1", "password1");
//
//        assertThat(loggedIn).isEqualTo(user1);
//        verify(userRepository).findUserByUsername("user1");
//    }
//
//    @Test
//    void whenInvalidLoginCredentials_thenReturnNull() {
//        when(userRepository.findUserByUsername("user1")).thenReturn(Optional.of(user1));
//
//        User result = userService.loginUser("user1", "wrongpassword");
//        assertThat(result).isNull();
//    }
//
//    @Test
//    void whenCreateValidUser_thenReturnUser() {
//        when(userRepository.findUserByUsername("user1")).thenReturn(Optional.empty());
//        when(userRepository.save(any(User.class))).thenReturn(user1);
//
//        User created = userService.createUser(user1);
//
//        assertThat(created).isEqualTo(user1);
//        verify(userRepository).save(user1);
//    }
//
//    @Test
//    void whenCreateUserWithExistingUsername_thenReturnNull() {
//        when(userRepository.findUserByUsername("user1")).thenReturn(Optional.of(user1));
//
//        User newUser = new User();
//        newUser.setUsername("user1");
//        User result = userService.createUser(newUser);
//
//        assertThat(result).isNull();
//        verify(userRepository, never()).save(any());
//    }
//
//    @Test
//    void whenCreateUserWithMissingFields_thenReturnNull() {
//        // Test missing username
//        User invalid = new User();
//        invalid.setPassword("pass");
//        invalid.setRole(UserRole.USER);
//        User result = userService.createUser(invalid);
//        assertThat(result).isNull();
//
//        // Test missing password
//        invalid.setUsername("user2");
//        invalid.setPassword(null);
//        result = userService.createUser(invalid);
//        assertThat(result).isNull();
//
//        // Test missing role
//        invalid.setPassword("pass");
//        invalid.setRole(null);
//        result = userService.createUser(invalid);
//        assertThat(result).isNull();
//    }
//
//    @Test
//    void whenUserGetsOwnProfile_thenAllow() {
//        mockAuthenticatedUser(1L, UserRole.USER);
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
//
//        User found = userService.getUserById(1L);
//        assertThat(found).isEqualTo(user1);
//    }
//
//    @Test
//    void whenUserGetsOtherProfile_thenDeny() {
//        mockAuthenticatedUser(2L, UserRole.USER);
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
//
//        User found = userService.getUserById(1L);
//        assertThat(found).isNull();
//    }
//
//    @Test
//    void whenUserGetsProfileWithUserName_thenAllow() {
//        mockAuthenticatedUser(1L, UserRole.USER);
//        when(userRepository.findUserByUsername("user1")).thenReturn(Optional.of(user1));
//
//        User found = userService.getUserByName("user1");
//        assertThat(found).isEqualTo(user1);
//    }
//
//    @Test
//    void whenUserGetsProfileWithInvalidUserName_thenDeny() {
//        mockAuthenticatedUser(1L, UserRole.USER);
//        when(userRepository.findUserByUsername("user1")).thenReturn(Optional.of(user1));
//
//        User found = userService.getUserByName("invalidUser");
//        assertThat(found).isNull();
//    }
//
//
//    @Test
//    void whenUserUpdatesOwnProfile_thenAllow() {
//        mockAuthenticatedUser(1L, UserRole.USER);
//        User updatedUser = new User();
//        updatedUser.setId(1L);
//        updatedUser.setUsername("updatedName");
//        updatedUser.setPassword("updatedPassword");
//        updatedUser.setRole(UserRole.STAFF);
//
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
//        when(userRepository.save(any())).thenReturn(updatedUser);
//
//        User result = userService.updateUser(updatedUser);
//        assertThat(result.getUsername()).isEqualTo("updatedName");
//        assertThat(result.getPassword()).isEqualTo("updatedPassword");
//        assertThat(result.getRole()).isEqualTo(UserRole.STAFF);
//    }
//
//    @Test
//    void whenUserUpdatesOwnProfileWithInvalidData_thenDeny() {
//        mockAuthenticatedUser(1L, UserRole.USER);
//        User updatedUser = new User();
//        updatedUser.setUsername(null);
//        updatedUser.setPassword(null);
//        updatedUser.setRole(null);
//
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
//
//        User result = userService.updateUser(updatedUser);
//        assertThat(result).isNull();
//    }
//
//    @Test
//    void whenUserUpdatesOtherProfile_thenDeny() {
//        mockAuthenticatedUser(2L, UserRole.USER);
//        User updatedUser = new User();
//        updatedUser.setId(1L);
//
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
//
//        User result = userService.updateUser(updatedUser);
//        assertThat(result).isNull();
//    }
//
//    @Test
//    void whenUnauthenticatedUserUpdates_thenDeny() {
//        mockAuthenticatedUser(null, null);
//        User updatedUser = new User();
//        updatedUser.setId(1L);
//
//        User result = userService.updateUser(updatedUser);
//        assertThat(result).isNull();
//    }
//
//    @Test
//    void whenUserDeletesOwnAccount_thenAllow() {
//        mockAuthenticatedUser(1L, UserRole.USER);
//        when(userRepository.existsById(1L)).thenReturn(true);
//
//        boolean result = userService.deleteUserById(1L);
//        assertThat(result).isTrue();
//        verify(userRepository).deleteById(1L);
//    }
//
//    @Test
//    void whenUserDeletesOtherAccount_thenDeny() {
//        mockAuthenticatedUser(2L, UserRole.USER);
//        when(userRepository.existsById(1L)).thenReturn(true);
//
//        boolean result = userService.deleteUserById(1L);
//        assertThat(result).isFalse();
//        verify(userRepository, never()).deleteById(any());
//    }
//}
