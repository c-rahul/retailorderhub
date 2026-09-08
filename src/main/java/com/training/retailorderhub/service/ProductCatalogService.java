package com.training.retailorderhub.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.training.retailorderhub.model.Product;
import com.training.retailorderhub.repository.ProductRepository;

@Service
public class ProductCatalogService {

    private final ProductRepository productRepository;

    public ProductCatalogService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }
}
