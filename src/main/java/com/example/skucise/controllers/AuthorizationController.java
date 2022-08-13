package com.example.skucise.controllers;

import com.example.skucise.security.ResultPair;
import com.example.skucise.security.Role;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.example.skucise.security.SecurityConfiguration.JWT_CUSTOM_HTTP_HEADER;
import static com.example.skucise.security.SecurityConfiguration.checkAccess;

@RestController
@RequestMapping("api/authorization")
public class AuthorizationController {
    @GetMapping
    public ResponseEntity<?> checkAuthorization(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt){
        ResultPair resultPair = checkAccess(jwt, Role.VISITOR, Role.ADMIN, Role.REG_BUYER, Role.REG_SELLER);
        HttpStatus httpStatus = resultPair.getHttpStatus();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
    }
}
