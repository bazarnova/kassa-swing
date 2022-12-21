package com.kassa.client;

import com.kassa.entity.Check;
import com.kassa.entity.Product;

import java.util.List;

public interface IProductClient {

    Product addNewProduct(Product product);
    List<Product> getAllProducts();
    List<Product> getAllProductsByCheckId(Long checkId);
    Product getProductById(Long id);
    boolean deleteProduct(Long id);
}
