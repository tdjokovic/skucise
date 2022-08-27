package com.example.skucise.controllers;

import com.example.skucise.models.AdCategory;
import com.example.skucise.models.City;
import com.example.skucise.security.ResultPair;
import com.example.skucise.security.Role;
import com.example.skucise.services.AdCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.skucise.security.SecurityConfiguration.JWT_CUSTOM_HTTP_HEADER;
import static com.example.skucise.security.SecurityConfiguration.checkAccess;

@RestController
@RequestMapping("/api/adcategories")
public class AdCategoryController {

    private final AdCategoryService adCategoryService;

    @Autowired
    public AdCategoryController(AdCategoryService adCategoryService){this.adCategoryService = adCategoryService;}

    @GetMapping
    public ResponseEntity<List<AdCategory>> getAll(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt){
        ResultPair resultPair = checkAccess(jwt, Role.VISITOR, Role.ADMIN, Role.REG_BUYER, Role.REG_SELLER);
        HttpStatus httpStatus = resultPair.getHttpStatus();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER,jwt);

        //ako ima kategorija vracaju se u bodyju
        if(httpStatus == HttpStatus.OK){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(adCategoryService.getAllAdCategories());
        }

        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
    }
}
