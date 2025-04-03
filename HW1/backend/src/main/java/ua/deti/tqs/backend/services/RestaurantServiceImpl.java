package ua.deti.tqs.backend.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.deti.tqs.backend.entities.Restaurant;
import ua.deti.tqs.backend.repositories.RestaurantRepository;
import ua.deti.tqs.backend.services.interfaces.RestaurantService;

import java.util.List;

@Service
@AllArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private RestaurantRepository restaurantRepository;

    @Override
    public Restaurant createRestaurant(Restaurant restaurant) {
        if (restaurant.getName() == null || restaurant.getName().trim().isEmpty()
            || restaurant.getDistrict() == null || restaurant.getDistrict().trim().isEmpty()
            || restaurant.getSeats() == null || restaurant.getSeats() <= 0
        )
            return null;

        Restaurant existingRestaurant = restaurantRepository.findByName(restaurant.getName()).orElse(null);

        if (existingRestaurant != null)  return null;

        restaurant.setName(restaurant.getName().trim());
        restaurant.setDistrict(restaurant.getDistrict().trim());
        restaurant.setId(null);

        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id).orElse(null);
    }

    @Override
    public Restaurant getRestaurantByName(String name) {
       return restaurantRepository.findByName(name).orElse(null);
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public Restaurant updateRestaurant(Restaurant restaurant) {
        Long id = restaurant.getId();
        Restaurant existingRestaurant = restaurantRepository.findById(id).orElse(null);

        if (existingRestaurant == null ) return null;

        int changedFields = 0;
        if (restaurant.getName() != null && !restaurant.getName().trim().isEmpty()) {
            existingRestaurant.setName(restaurant.getName());
            changedFields++;
        }

        if (restaurant.getSeats() != null && restaurant.getSeats() > 0) {
            existingRestaurant.setSeats(restaurant.getSeats());
            changedFields++;
        }

        if (restaurant.getDistrict() != null && !restaurant.getDistrict().trim().isEmpty()) {
            existingRestaurant.setDistrict(restaurant.getDistrict());
            changedFields++;
        }

        if (changedFields == 0) return null;

        return restaurantRepository.save(existingRestaurant);
    }

    @Override
    public boolean deleteRestaurantById(Long id) {
        restaurantRepository.deleteById(id);
        return restaurantRepository.findById(id).isEmpty();
    }
}
