package com.StefanSergiu.Licenta.exception;

import java.text.MessageFormat;

public class ProductIsAlreadyAssignedToBrandException extends RuntimeException {
    public ProductIsAlreadyAssignedToBrandException(Long productId, Long brandId) {
        super(MessageFormat.format("Product: {0} is already assigned to brand: {1}",productId,brandId));
    }
}
