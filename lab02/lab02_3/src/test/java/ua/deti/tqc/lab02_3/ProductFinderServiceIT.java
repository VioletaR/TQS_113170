package ua.deti.tqc.lab02_3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class ProductFinderServiceIT {
    private ProductFinderService productFinderService;

    @BeforeEach
    public void setUp() {
        ISimpleHttpClient httpClient = new TqsBasicHttpClient();
        productFinderService = new ProductFinderService(httpClient);
    }

    @Test
    public void findProductDetails_fromAValidProduct_shouldReturnProduct() {
        Optional<Product> product = productFinderService.findProductDetails(3);

        assertTrue(product.isPresent());
        assertEquals(3, (int) product.get().getId());
    }

    @Test
    public void findProductDetails_fromAnInvalidProduct_shouldReturnEmpty() {
        Optional<Product> product = productFinderService.findProductDetails(300);

        assertTrue(product.isEmpty());
    }




}
