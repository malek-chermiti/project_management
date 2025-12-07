package com.example.project_management.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        ErrorResponse error = new ErrorResponse(400, e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
    
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorResponse> handleSecurityException(SecurityException e) {
        ErrorResponse error = new ErrorResponse(401, e.getMessage());
        return ResponseEntity.status(401).body(error);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception e) {
        ErrorResponse error = new ErrorResponse(500, "Une erreur serveur s'est produite");
        return ResponseEntity.status(500).body(error);
    }
}

class ErrorResponse {
    private int status;
    private String message;
    
    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
    
    public int getStatus() {
        return status;
    }
    
    public String getMessage() {
        return message;
    }
}
