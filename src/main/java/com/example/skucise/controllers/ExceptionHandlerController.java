package com.example.skucise.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import static com.example.skucise.security.SecurityConfiguration.JWT_CUSTOM_HTTP_HEADER;

@ControllerAdvice
public class ExceptionHandlerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<?> constraintViolationExceptio(ConstraintViolationException e, HttpServletRequest request){
        LOGGER.warn( "{}", e.getMessage() );


        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, request.getHeader(JWT_CUSTOM_HTTP_HEADER));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .headers(responseHeaders)
                .body(null);
    }
}
