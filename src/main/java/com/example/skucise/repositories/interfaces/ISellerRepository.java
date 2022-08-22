package com.example.skucise.repositories.interfaces;

import com.example.skucise.models.Property;
import com.example.skucise.models.Rating;
import com.example.skucise.models.SellerUser;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISellerRepository extends CRUDRepository<SellerUser,Integer> {
    boolean approve(int id);
    List<Property> getPostedProperties(int id);
    Rating getRating(int sellerId, int buyerId, boolean isApplicant);
    boolean rate(int sellerId, int buyerId, byte feedback);

}
