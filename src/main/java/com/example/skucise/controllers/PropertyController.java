package com.example.skucise.controllers;

import com.example.skucise.models.PropertyFeed;
import com.example.skucise.services.PropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyController.class);

    @Autowired
    public PropertyController(PropertyService propertyService){this.propertyService = propertyService;}

    public ResponseEntity<PropertyFeed> getFilteredProperties(){
        
    }
}
