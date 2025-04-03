package ua.deti.tqs.backend.services.interfaces;

import ua.deti.tqs.backend.entities.Restaurant;

import java.util.List;

public interface RestaurantService {

    Restaurant createRestaurant(Restaurant restaurant);

    Restaurant getRestaurantById(Long id);

    Restaurant getRestaurantByName(String name);

    List<Restaurant> getAllRestaurants();

    Restaurant updateRestaurant(Restaurant restaurant);

    boolean deleteRestaurantById(Long id);
}
