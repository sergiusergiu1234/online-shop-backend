package com.StefanSergiu.Licenta.exception;

import jakarta.persistence.EntityNotFoundException;

public class TypeNotFoundException extends EntityNotFoundException {
    public TypeNotFoundException(String message){
        super(message);
    }
}
