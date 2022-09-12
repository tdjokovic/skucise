package com.example.skucise.services;

import com.example.skucise.models.Type;
import com.example.skucise.repositories.AdTypeRepository;
import com.example.skucise.repositories.CityRepository;
import com.example.skucise.services.interfaces.IAdTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdTypeService implements IAdTypeService {

    private AdTypeRepository adTypeRepository;

    @Autowired
    public AdTypeService(AdTypeRepository adTypeRepository){this.adTypeRepository = adTypeRepository;}

    @Override
    public List<Type> getAllTypes() {
        return this.adTypeRepository.getAll();
    }
}
