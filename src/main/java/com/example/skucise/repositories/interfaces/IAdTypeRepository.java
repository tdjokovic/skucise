package com.example.skucise.repositories.interfaces;

import com.example.skucise.models.Type;
import org.springframework.stereotype.Repository;

@Repository
public interface IAdTypeRepository extends CRUDRepository<Type, Integer>{

}
