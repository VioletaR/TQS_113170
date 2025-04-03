package ua.deti.tqs.backend.services.interfaces;

import ua.deti.tqs.backend.entities.User;

public interface UserService {
    User loginUser(String userName, String password);

    User createUser(User user);

    User getUserById(Long id);

    User getUserByName(String name);

    User updateUser(User user);

    boolean deleteUserById(Long id);
}
