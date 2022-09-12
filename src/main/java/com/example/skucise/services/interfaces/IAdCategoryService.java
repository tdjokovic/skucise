package com.example.skucise.services.interfaces;

import com.example.skucise.models.AdCategory;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface IAdCategoryService {
    public List<AdCategory> getAllAdCategories();
}
