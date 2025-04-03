package ua.deti.tqs.backend.repositories;

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ua.deti.tqs.backend.entities.Restaurant;

public class Utils {
    public static Restaurant generateBus(TestEntityManager entityManager) {
        Restaurant bus = new Restaurant();

        entityManager.persistAndFlush(bus);
        return bus;
    }

}
