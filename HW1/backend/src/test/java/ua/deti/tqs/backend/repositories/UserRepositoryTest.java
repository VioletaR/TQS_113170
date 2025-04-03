package ua.deti.tqs.backend.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import ua.deti.tqs.backend.entities.User;
import ua.deti.tqs.backend.entities.utils.UserRole;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user1;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setRole(UserRole.USER);

        entityManager.persist(user1);
    }

    @Test
    void findUserByUsername_whenUserExists_thenReturnUser() {
        User found = userRepository.findUserByUsername(user1.getUsername()).orElse(null);
        assertThat(found).isEqualTo(user1);
    }

    @Test
    void findUserByUsername_whenUserDoesNotExist_thenReturnNull() {
        User found = userRepository.findUserByUsername("nonexistent").orElse(null);
        assertThat(found).isNull();
    }

    @Test
    void findUserByUsername_whenUsernameIsNull_thenReturnNull() {
        User found = userRepository.findUserByUsername(null).orElse(null);
        assertThat(found).isNull();
    }

}
