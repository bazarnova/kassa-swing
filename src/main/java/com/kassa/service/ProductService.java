package com.kassa.service;

import com.kassa.client.IProductClient;
import com.kassa.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProductService {
    private final IProductClient productClient;

    public ProductService(IProductClient productClient) {
        this.productClient = productClient;
    }


    @Override
    public Product addNewProduct(Product product) {
        //проверки
        try {
            return productClient.addNewProduct(product);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Product> getAllProducts() {
        return productClient.getAllProducts();
    }

    @Override
    public List<Product> getAllProductsByCheckId(Long checkId) {
        return productClient.getAllProductsByCheckId(checkId);
    }

    @Override
    public Product getProductById(Long id) {
        return productClient.getProductById(id);
    }
    @Override
    public boolean deleteProduct(Long id){
        return productClient.deleteProduct(id);
    }
}
