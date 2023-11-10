package com.StefanSergiu.Licenta.exception;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({TypeAlreadyExistsException.class})
    public ResponseEntity<Object> handleTypeAlreadyExists(TypeAlreadyExistsException exception){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }

    @ExceptionHandler({TypeNotFoundException.class})
    public ResponseEntity<Object> handleTypeNotFound(TypeNotFoundException exception){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException exception){
        return ResponseEntity
                .status((HttpStatus.NOT_FOUND))
                .body(exception.getMessage());
    }

    @ExceptionHandler({EntityExistsException.class})
    public ResponseEntity<Object> handleEntityExistsException(EntityExistsException exception){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(exception.getMessage());
    }

    @ExceptionHandler({ExpiredJwtException.class})
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException exception){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(exception.getMessage());
    }
}
