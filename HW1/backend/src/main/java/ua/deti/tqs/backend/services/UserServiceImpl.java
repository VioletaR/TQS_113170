package ua.deti.tqs.backend.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.deti.tqs.backend.authentication.utils.CurrentUser;
import ua.deti.tqs.backend.entities.User;
import ua.deti.tqs.backend.entities.utils.UserRole;
import ua.deti.tqs.backend.repositories.UserRepository;
import ua.deti.tqs.backend.services.interfaces.UserService;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private CurrentUser currentUser;

    @Override
    public User loginUser(String userName, String password) {
        User found = userRepository.findUserByUsername(userName).orElse(null);

        if (found == null || !found.getPassword().equals(password))
            return null;

        return found;
    }

    @Override
    public User createUser(User user) {

        User found = userRepository.findUserByUsername(user.getUsername()).orElse(null);

        if (found != null) return null;

        if (user.getRole() == null) {
            user.setRole(UserRole.USER);
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return null;
        }

        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            return null;
        }

        user.setId(null);

        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        if (! currentUser.getAuthenticatedUserId().equals(id)) {
            return null;
        }
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getUserByName(String name) {
        User found = userRepository.findUserByUsername(name).orElse(null);
        if (found == null || !currentUser.getAuthenticatedUserId().equals(found.getId())) {
            return null;
        }
        return found;
    }

    @Override
    public User updateUser(User user) {
        if (!currentUser.getAuthenticatedUserId().equals(user.getId())) {
            return null;
        }

        User found = userRepository.findById(user.getId()).orElse(null);

        if (found == null) return null;

        int changedFields = 0;

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            found.setPassword(user.getPassword());
            changedFields++;
        }

        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            found.setUsername(user.getUsername());
            changedFields++;
        }

        if (user.getRole() != null) {
            found.setRole(user.getRole());
            changedFields++;
        }

        if (changedFields > 0) {
            return userRepository.save(found);
        }

        return null;
    }

    @Override
    public boolean deleteUserById(Long id) {
        if (! currentUser.getAuthenticatedUserId().equals(id)) {
            return false;
        }

        userRepository.deleteById(id);
        return userRepository.existsById(id);
    }
}
