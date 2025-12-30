package com.demo.manager.customException;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String productId, int current, int delta) {
        super("Insufficient stock for product " + productId + ": current= " + current + ", delta= " + delta);
    }
}
