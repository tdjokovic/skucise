package com.example.skucise.repositories.interfaces;

import com.example.skucise.models.AdCategory;
import org.springframework.stereotype.Repository;

@Repository
public interface IAdCategoryRepository extends CRUDRepository<AdCategory, Integer>{
}
