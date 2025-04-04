package ua.deti.tqs.backend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import ua.deti.tqs.backend.entities.Restaurant;
import ua.deti.tqs.backend.repositories.RestaurantRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    private Restaurant restaurant1;

    @BeforeEach
    void setUp() {
        restaurant1 = new Restaurant();
        restaurant1.setId(1L);
        restaurant1.setName("restaurant1");
        restaurant1.setDistrict("district1");
        restaurant1.setSeats(10);
    }

    @Test
    void whenCreateValidRestaurant_thenReturnRestaurant() {
        when(restaurantRepository.save(restaurant1)).thenReturn(restaurant1);
        Restaurant created = restaurantService.createRestaurant(restaurant1);
        assertThat(created).isEqualTo(restaurant1);
        verify(restaurantRepository, times(1)).save(restaurant1);
    }

    @Test
    void whenCreateRestaurantWithInvalidData_thenReturnNull() {
        // Test null name
        Restaurant invalid = new Restaurant();
        invalid.setName(null);
        invalid.setDistrict("district1");
        invalid.setSeats(10);
        Restaurant created = restaurantService.createRestaurant(invalid);
        assertThat(created).isNull();
        verify(restaurantRepository, never()).save(any());

        // Test null district
        invalid.setName("name");
        invalid.setDistrict(null);
        created = restaurantService.createRestaurant(invalid);
        assertThat(created).isNull();
        verify(restaurantRepository, never()).save(any());

        // Test negative seats
        invalid.setDistrict("district1");
        invalid.setSeats(-5);
        created = restaurantService.createRestaurant(invalid);
        assertThat(created).isNull();
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void whenGetRestaurantById_thenReturnRestaurant() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant1));
        Restaurant found = restaurantService.getRestaurantById(1L);
        assertThat(found).isEqualTo(restaurant1);
    }

    @Test
    void whenGetInvalidRestaurantId_thenReturnNull() {
        when(restaurantRepository.findById(-111L)).thenReturn(Optional.empty());
        Restaurant found = restaurantService.getRestaurantById(-111L);
        assertThat(found).isNull();
    }

    @Test
    void whenGetRestaurantByName_thenReturnRestaurant() {
        when(restaurantRepository.findByName("restaurant1")).thenReturn(Optional.of(restaurant1));
        Restaurant found = restaurantService.getRestaurantByName("restaurant1");
        assertThat(found).isEqualTo(restaurant1);
    }

    @Test
    void whenGetRestaurantByInvalidName_thenReturnNull() {
        when(restaurantRepository.findByName("invalidName")).thenReturn(Optional.empty());
        Restaurant found = restaurantService.getRestaurantByName("invalidName");
        assertThat(found).isNull();
    }

    @Test
    void whenGetAllRestaurants_thenReturnList() {
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant1));
        List<Restaurant> found = restaurantService.getAllRestaurants();
        assertThat(found).containsExactly(restaurant1);
    }

    @Test
    void whenUpdateRestaurant_thenReturnUpdatedRestaurant() {
        Restaurant existing = new Restaurant();
        existing.setId(1L);
        existing.setName("oldName");
        existing.setDistrict("oldDistrict");
        existing.setSeats(5);

        Restaurant updatedInfo = new Restaurant();
        updatedInfo.setId(1L);
        updatedInfo.setName("newName");
        updatedInfo.setDistrict("newDistrict");
        updatedInfo.setSeats(20);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(restaurantRepository.save(existing)).thenReturn(updatedInfo);

        Restaurant result = restaurantService.updateRestaurant(updatedInfo);
        assertThat(result.getName()).isEqualTo("newName");
        assertThat(result.getDistrict()).isEqualTo("newDistrict");
        assertThat(result.getSeats()).isEqualTo(20);
        verify(restaurantRepository).save(existing);
    }

    @Test
    void whenUpdateInvalidRestaurant_thenReturnNull() {
        Restaurant invalid = new Restaurant();
        invalid.setId(-1L);
        when(restaurantRepository.findById(-1L)).thenReturn(Optional.empty());
        Restaurant result = restaurantService.updateRestaurant(invalid);
        assertThat(result).isNull();
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void whenDeleteRestaurantById_thenVerifyDeletion() {
        when(restaurantRepository.existsById(1L)).thenReturn(true);
        boolean result = restaurantService.deleteRestaurantById(1L);
        assertThat(result).isTrue();
        verify(restaurantRepository).deleteById(1L);
    }

    @Test
    void whenDeleteInvalidRestaurantId_thenDoNothing() {
        when(restaurantRepository.existsById(-1L)).thenReturn(false);
        boolean result = restaurantService.deleteRestaurantById(-1L);
        assertThat(result).isFalse();
        verify(restaurantRepository, never()).deleteById(any());
    }
}
