package com.demo.manager.service;

import com.demo.manager.model.Product;
import com.demo.manager.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;  // ✅ JUnit 5
import org.junit.jupiter.api.Test;        // ✅ JUnit 5
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*; // ✅ JUnit 5
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Captor
    private ArgumentCaptor<Product> productCaptor;

    private Product validProduct;

    @BeforeEach
    void setUp() {
        validProduct = new Product(
                "123",
                "Smartwatch",
                "isin-fdnuiwe384",
                10
        );
    }


    @Test
    public void createProduct_withValidProduct_savedProduct() {
        when(productRepository.save(validProduct)).thenReturn(validProduct);

        productService.createProduct(validProduct);

        verify(productRepository, times(1)).save(validProduct);
    }

    @Test
    public void createProduct_withNotValidProduct_notSavedProductAndThrowsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> productService.createProduct(null),
                "Excepted IllegalArgumentException for null product"
        );

        verifyNoInteractions(productRepository);
    }

    @Test
    public void createProduct_whenRepositoryThrows_errorIsRethrown() {
        when(productRepository.save(validProduct)).thenThrow(new RuntimeException("DB down"));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> productService.createProduct(validProduct)
        );

        verify(productRepository, times(1)).save(validProduct);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void findById_whenIdIsString() {
        String id = "prod123";
        when(productRepository.findById(any())).thenReturn(Optional.ofNullable(validProduct));

        productService.findById(any());

        verify(productRepository, times(1)).findById(any());
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void updateProduct_whenProductIsValid_returnProductUpdate() {
        Product updateProd = new Product(
                null,
                "watch",
                "isin-fdnuiwe384",
                12
        );
        Product savedProd = new Product(
                "123",
                "watch",
                "isin-fdnuiwe384",
                22
        );
        when(productRepository.findById(validProduct.getId())).thenReturn(Optional.ofNullable(validProduct));
        when(productRepository.save(any(Product.class))).thenReturn(savedProd);

        productService.updateProduct(updateProd, "123");

        verify(productRepository).findById(eq("123"));
        verify(productRepository).save(productCaptor.capture());

        Product saved = productCaptor.getValue();
        assertEquals("watch", saved.getNameProduct());
        assertEquals(validProduct.getCodeProduct(), saved.getCodeProduct(), "The code not change!");
        assertEquals(22, saved.getQuantity());
        assertNotSame(updateProd, saved, "The service need saved the modify entity, not the input");
    }
}
