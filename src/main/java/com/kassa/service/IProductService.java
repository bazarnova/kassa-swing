package com.kassa.service;

import com.kassa.entity.Product;

import java.util.List;

public interface IProductService {
    Product addNewProduct(Product product);

    List<Product> getAllProducts();

    List<Product> getAllProductsByCheckId(Long checkId);

    Product getProductById(Long id);
    boolean deleteProduct(Long id);
}
