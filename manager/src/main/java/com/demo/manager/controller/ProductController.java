package com.demo.manager.controller;

import com.demo.manager.model.Product;
import com.demo.manager.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
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
            @ApiResponse(responseCode = "200", description = "products has been take"),
            @ApiResponse(responseCode = "400", description = "product hasn't been take")
    })
    @GetMapping("/all")
    public ResponseEntity<List<Product>> createProduct() {
        List<Product> products = productService.findAll();

        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(products);
    }


}
