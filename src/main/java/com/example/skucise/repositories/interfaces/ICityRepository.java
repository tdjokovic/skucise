package com.example.skucise.repositories.interfaces;

import com.example.skucise.models.City;
import org.springframework.stereotype.Repository;

@Repository
public interface ICityRepository extends CRUDRepository<City, Integer>{
}
