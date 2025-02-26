package tqs.deti.ua;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductFinderServiceTest {
    @Mock
    private ISimpleHttpClient httpClient;

    @InjectMocks
    private ProductFinderService productFinderService;

    @Test
    public void findProductDetails_fromAValidProduct_shouldReturnProduct() {
        String response =
        """
        {
          "id": 3,
          "title": "Mens Cotton Jacket",
          "price": 55.99,
          "description": "great outerwear jackets for Spring/Autumn/Winter, suitable for many occasions, such as working, hiking, camping, mountain/rock climbing, cycling, traveling or other outdoors. Good gift choice for you or your family member. A warm hearted love to Father, husband or son in this thanksgiving or Christmas Day.",
          "category": "men's clothing",
          "image": "https://fakestoreapi.com/img/71li-ujtlUL._AC_UX679_.jpg"
        }
        """;
        when(httpClient.doHttpGet("https://fakestoreapi.com/products/3")).thenReturn(response);

        Optional<Product> product = productFinderService.findProductDetails(3);
        assertTrue(product.isPresent());
        assertEquals(3, (int) product.get().getId());
    }


    @Test
    public void findProductDetails_fromAnInvalidProduct_shouldReturnEmpty() {
        when(httpClient.doHttpGet("https://fakestoreapi.com/products/300")).thenReturn("");

        Optional<Product> product = productFinderService.findProductDetails(300);
        assertTrue(product.isEmpty());
    }

}
