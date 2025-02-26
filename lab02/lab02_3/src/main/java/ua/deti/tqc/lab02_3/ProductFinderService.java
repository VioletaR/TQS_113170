package ua.deti.tqc.lab02_3;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ProductFinderService {
    private static final String API_PRODUCTS = "https://fakestoreapi.com/products/";
    private final ISimpleHttpClient httpClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ProductFinderService(ISimpleHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public Optional<Product> findProductDetails(Integer id) {
        try {
            String response = httpClient.doHttpGet(API_PRODUCTS + id);
            if (response == null || response.isEmpty()) {
                return Optional.empty();
            }
            Product product = objectMapper.readValue(response, Product.class);
            return Optional.of(product);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
