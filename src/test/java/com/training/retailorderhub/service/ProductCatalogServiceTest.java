package com.training.retailorderhub.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.training.retailorderhub.model.Product;
import com.training.retailorderhub.repository.ProductRepository;

class ProductCatalogServiceTest {

    @Test
    void returnsAllProductsFromRepository() {
        ProductRepository repository = mock(ProductRepository.class);
        List<Product> products = List.of(new Product());
        when(repository.findAll()).thenReturn(products);
        ProductCatalogService service = new ProductCatalogService(repository);

        assertEquals(products, service.findAll());
        verify(repository).findAll();
    }
}
