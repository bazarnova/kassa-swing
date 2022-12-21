package com.kassa.client;

import com.kassa.entity.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Component
public class ProductClient implements IProductClient {
    @Override
    public Product addNewProduct(Product product) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Product> response = restTemplate.postForEntity("http://localhost:8080/product", product, Product.class);
        return response.getBody();
    }

    @Override
    public List<Product> getAllProducts() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Product[]> response = restTemplate.getForEntity("http://localhost:8080/products", Product[].class);
        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }

    @Override
    public List<Product> getAllProductsByCheckId(Long checkId) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Product[]> response = restTemplate.getForEntity(
                "http://localhost:8080/products/id/{checkId}",
                Product[].class,
                new HashMap<String, Long>() {{
                    put("checkId", checkId);
                }});
        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }

    @Override
    public Product getProductById(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Product> response = restTemplate.getForEntity(
                "http://localhost:8080/product/id/{id}",
                Product.class,
                new HashMap<String, Long>() {{
                    put("id", id);
                }});
        return (response.getBody());
    }

    @Override
    public boolean deleteProduct(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(
                "http://localhost:8080/product/id/{id}",
                new HashMap<String, Long>() {{
                    put("id", id);
                }});
        return true;
    }
}
