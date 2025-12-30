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

        when(productRepository.findById(validProduct.getId())).thenReturn(Optional.ofNullable(validProduct));

        productService.updateProduct(updateProd, "123");

        verify(productRepository).findById(eq("123"));
        verify(productRepository).save(productCaptor.capture());

        Product saved = productCaptor.getValue();
        assertEquals("watch", saved.getNameProduct());
        assertEquals(validProduct.getCodeProduct(), saved.getCodeProduct(), "The code not change!");
        assertEquals(22, saved.getQuantity());
        assertNotSame(updateProd, saved, "The service need saved the modify entity, not the input");
    }


    @Test
    public void updateProduct_whenPropductIsValidAndContainOneUpdate_returnProductUpdate(){
        Product updateProd = new Product(
                null,
                "watch",
                null,
                0
        );

        when(productRepository.findById(validProduct.getId())).thenReturn(Optional.ofNullable(validProduct));

        productService.updateProduct(updateProd, "123");

        verify(productRepository).findById(eq("123"));
        verify(productRepository).save(productCaptor.capture());

        Product saved = productCaptor.getValue();
        assertEquals("watch", saved.getNameProduct());
        assertEquals(validProduct.getCodeProduct(), saved.getCodeProduct(), "The code not change!");
        assertEquals(validProduct.getQuantity(), saved.getQuantity(), "the quantity not change");
        assertNotSame(updateProd, saved, "The service need saved the modify entity, not the input");
    }

    @Test
    public void updateProduct_whenPropductIsValidAndContainNegativeQuantityUpdate_returnProductUpdate(){
        Product updateProd = new Product(
                null,
                null,
                null,
                -8
        );

        when(productRepository.findById(validProduct.getId())).thenReturn(Optional.ofNullable(validProduct));

        productService.updateProduct(updateProd, "123");

        verify(productRepository).findById(eq("123"));
        verify(productRepository).save(productCaptor.capture());

        Product saved = productCaptor.getValue();
        assertEquals(validProduct.getNameProduct(), saved.getNameProduct(), "The name of product not change");
        assertEquals(validProduct.getCodeProduct(), saved.getCodeProduct(), "The code not change!");
        assertEquals(2, saved.getQuantity(), "the quantity change");
        assertNotSame(updateProd, saved, "The service need saved the modify entity, not the input");
    }


    @Test
    void updateProduct_withNegativeDelta_andSufficientStock_updatesQuantity() {
        Product updateProd = new Product(null, null, null, -3);
        when(productRepository.findById("123")).thenReturn(Optional.of(validProduct)); // qty=10

        productService.updateProduct(updateProd, "123");

        verify(productRepository).save(productCaptor.capture());
        Product saved = productCaptor.getValue();
        assertEquals(7, saved.getQuantity(), "10 + (-3) = 7");
    }


    @Test
    void updateProduct_withNegativeDelta_andInsufficientStock_throwsAndNoSave() {
        Product updateProd = new Product(null, null, null, -11); // current=10 → newQty=-1
        when(productRepository.findById("123")).thenReturn(Optional.of(validProduct));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> productService.updateProduct(updateProd, "123")
        );
        assertTrue(ex.getMessage().toLowerCase().contains("insufficient"), "Messaggio significativo");

        verify(productRepository, never()).save(any());
    }


    @Test
    void updateProduct_withZeroDelta_andNoOtherChanges_doesNotSave() {
        Product updateProd = new Product(null, "", "", 0); // blank ignorati, delta 0
        when(productRepository.findById("123")).thenReturn(Optional.of(validProduct));

        productService.updateProduct(updateProd, "123");

        verify(productRepository, never()).save(any());
    }


    @Test
    void updateProduct_blankStringsAreIgnored_forNameAndCode() {
        Product updateProd = new Product(null, "   ", "", -2);
        when(productRepository.findById("123")).thenReturn(Optional.of(validProduct));

        productService.updateProduct(updateProd, "123");

        verify(productRepository).save(productCaptor.capture());
        Product saved = productCaptor.getValue();

        assertEquals(validProduct.getNameProduct(), saved.getNameProduct(), "Nome invariato per blank");
        assertEquals(validProduct.getCodeProduct(), saved.getCodeProduct(), "Codice invariato per blank");
        assertEquals(8, saved.getQuantity(), "10 + (-2) = 8");
    }

}
