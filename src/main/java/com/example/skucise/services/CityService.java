package com.example.skucise.services;

import com.example.skucise.models.City;
import com.example.skucise.repositories.CityRepository;
import com.example.skucise.services.interfaces.ICityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService implements ICityService {

    private CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository){this.cityRepository = cityRepository;}

    @Override
    public List<City> getAllCities() {
        return cityRepository.getAll();
    }
}
