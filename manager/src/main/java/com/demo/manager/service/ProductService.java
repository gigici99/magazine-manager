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
        if (product == null) {
            log.error("Cannot save null product");
            throw new IllegalArgumentException("Product cannot be null");
        }

        try {
            productRepository.save(product);
            log.info("Product saved successfully: {}", product);
        } catch (Exception e) {
            log.error("Error saving product: {}", product, e);
            throw e;
        }
    }

    public Product findById(String id) {
        return productRepository.findById(id).orElseThrow();
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }


    public void updateProduct(Product product, String idProduct) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        Product productUpdate = findById(idProduct); // lancia se non trovato

        boolean changed = false;

        if (product.getNameProduct() != null && !product.getNameProduct().isBlank()
                && !product.getNameProduct().equals(productUpdate.getNameProduct())) {
            productUpdate.setNameProduct(product.getNameProduct());
            changed = true;
        }

        if (product.getCodeProduct() != null && !product.getCodeProduct().isBlank()
                && !product.getCodeProduct().equals(productUpdate.getCodeProduct())) {
            productUpdate.setCodeProduct(product.getCodeProduct());
            changed = true;
        }

        int delta = product.getQuantity();
        if (delta != 0) {
            int current = productUpdate.getQuantity();
            int newQty = current + delta;
            if (newQty < 0) {
                log.error("Insufficient stock: current={}, delta={}, newQty={}", current, delta, newQty);
                throw new IllegalArgumentException("Insufficient stock for product " + idProduct);
            }
            productUpdate.setQuantity(newQty);
            changed = true;
        }

        if (!changed) {
            log.info("No changes for product {}", idProduct);
            return;
        }

        productRepository.save(productUpdate);
        log.info("The product has been updated: {}", productUpdate);
    }


    public boolean existsById(String idProd) {
        Product product = findById(idProd);

        if (product != null) return true;

        return false;
    }

    public void deleteProduct (String idProduct) {
        productRepository.deleteById(idProduct);
    }
}
