package com.example.skucise.repositories.interfaces;

import com.example.skucise.models.BuyerUser;
import com.example.skucise.models.Property;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBuyerRepository extends CRUDRepository<BuyerUser, Integer>{
    List<BuyerUser> getAll(boolean approved);
    boolean approve(int id);
    List<Property> getAppliedProperties(int id);
}
