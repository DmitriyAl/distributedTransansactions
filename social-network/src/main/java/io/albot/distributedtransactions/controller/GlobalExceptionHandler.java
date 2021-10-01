package io.albot.distributedtransactions.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<String> handleConflict(
            RuntimeException ex, WebRequest request) {
        return ResponseEntity.internalServerError().body("Exception");
    }
}