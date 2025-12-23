package com.demo.manager.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "products")
@Schema(name = "Product", description = "Entity for product in magazine")
public class Product {
    @Id
    private String id;

    @Schema(name = "nameProduct", description = "name for product", example = "watch")
    private String nameProduct;
    @Schema(name = "codeProduct", description = "code for product", example = "isin-fdnuiwe384")
    private String codeProduct;
    @Schema(name = "quantity", description = "quantity of product", example = "10")
    private int quantity;

    public Product() {
    }

    public String getId() {
        return id;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getCodeProduct() {
        return codeProduct;
    }

    public void setCodeProduct(String codeProduct) {
        this.codeProduct = codeProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", nameProduct='" + nameProduct + '\'' +
                ", codeProduct='" + codeProduct + '\'' +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Product product)) return false;

        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
