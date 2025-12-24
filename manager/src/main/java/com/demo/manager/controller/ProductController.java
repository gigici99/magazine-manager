package com.demo.manager.controller;

import com.demo.manager.model.Product;
import com.demo.manager.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private static final Logger log = LogManager.getLogger();

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @Operation(description = "Create new product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "product has been created"),
            @ApiResponse(responseCode = "400", description = "product hasn't created")
    })
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        System.out.println("received: " + product.toString());
        if (product == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }


    @Operation(description = "Return all products")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "list of products has been take"),
            @ApiResponse(responseCode = "400", description = "list products hasn't been take")
    })
    @GetMapping
    public ResponseEntity<List<Product>> getProductAll() {
        List<Product> products = productService.findAll();

        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(products);
    }

    @Operation(description = "Return product by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "product has been take"),
            @ApiResponse(responseCode = "400", description = "product hasn't been take")
    })
    @GetMapping("/{idProd}")
    public ResponseEntity<Product> getProductById(@PathVariable String idProd){
        Product prod = productService.findById(idProd);
        if (prod == null){
            return ResponseEntity.status(400).build();
        }
        return ResponseEntity.ok(prod);
    }

    @Operation(description = "Update product by id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "product has been update"),
            @ApiResponse(responseCode = "404", description = "product not found"),
            @ApiResponse(responseCode = "409", description = "product has a conflic with db"),
            @ApiResponse(responseCode = "500", description = "all error")
    })
    @PutMapping("/{idProd}")
    public ResponseEntity<Product> updateProductById(@PathVariable String idProd, @RequestBody Product product){
        log.info("Update product id={}", idProd);

        try {
            if (!productService.existsById(idProd)){
                return ResponseEntity.notFound().build();
            }
            productService.updateProduct(product, idProd);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            log.error("Integrity violation deleting product {}: {}", idProd, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 se vincoli DB
        } catch (Exception e) {
            log.error("Error deleting product {}: {}", idProd, e.getMessage(), e);
            return ResponseEntity.internalServerError().build(); // 500 altri casi
        }
    }

    @Operation(description = "Delete product by id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "product has been deleted"),
            @ApiResponse(responseCode = "404", description = "product not found"),
            @ApiResponse(responseCode = "409", description = "product has a conflic with db"),
            @ApiResponse(responseCode = "500", description = "all error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable String id) {
        log.info("DELETE product id={}", id);
        try {
            if (!productService.existsById(id)) {
                return ResponseEntity.notFound().build(); // 404 se non esiste
            }
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build(); // 204 OK
        } catch (DataIntegrityViolationException e) {
            log.error("Integrity violation deleting product {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 se vincoli DB
        } catch (Exception e) {
            log.error("Error deleting product {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build(); // 500 altri casi
        }
    }



}
