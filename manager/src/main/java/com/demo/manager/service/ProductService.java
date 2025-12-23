package com.demo.manager.service;

import com.demo.manager.model.Product;
import com.demo.manager.repository.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private static final Logger log = LogManager.getLogger();

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Crud operation
    public void createProduct(Product product) {
        try {
            if (product != null) productRepository.save(product);
            log.info("The product: " + product.toString() + " is correctly save!");
        } catch (NullPointerException e) {
            e.getMessage();
            log.error("The product: " + product + " is null!");
        }
    }

    public Product findById(String id) {
        return productRepository.findById(id).orElseThrow();
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public void updateProduct(Product product, String idProduct) {
        try {
            if (product != null) {
                Product productUpdate = findById(idProduct);

                if (product.getNameProduct() != null &&
                        !product.getNameProduct().equals(productUpdate.getNameProduct())) {
                    productUpdate.setNameProduct(product.getNameProduct());
                }

                if (product.getCodeProduct() != null &&
                        !product.getCodeProduct().equals(productUpdate.getCodeProduct())) {
                    productUpdate.setCodeProduct(product.getCodeProduct());
                }

                if (product.getQuantity() >= 0) {
                    productUpdate.setQuantity(productUpdate.getQuantity() + product.getQuantity());
                }
                productRepository.save(productUpdate);
                log.info("The product has been updated: " + productUpdate);
            }
        } catch (Exception e) {
            log.error("Error updating product: " + e.getMessage(), e);
        }
    }

    public void deleteProduct (String idProduct) {
        productRepository.deleteById(idProduct);
    }
}
