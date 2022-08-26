package com.example.skucise.services;

import com.example.skucise.models.AdCategory;
import com.example.skucise.repositories.AdCategoryRepository;
import com.example.skucise.repositories.CityRepository;
import com.example.skucise.services.interfaces.IAdCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdCategoryService implements IAdCategoryService {

    private AdCategoryRepository adCategoryRepository;

    @Autowired
    public AdCategoryService(AdCategoryRepository adCategoryRepository){this.adCategoryRepository = adCategoryRepository;}

    @Override
    public List<AdCategory> getAllAdCategories() {
        return this.adCategoryRepository.getAll();
    }
}
