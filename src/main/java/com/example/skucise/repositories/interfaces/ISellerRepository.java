package com.example.skucise.repositories.interfaces;

import com.example.skucise.models.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISellerRepository extends CRUDRepository<SellerUser,Integer> {
    boolean approve(int id);
    List<Property> getPostedProperties(int id);
    Rating getRating(int sellerId, int buyerId, boolean isApplicant);
    boolean rate(int sellerId, int buyerId, byte feedback);
    public BuyerUser getAsBuyer(Integer id);
    public boolean editData(int id, NewUserData data);

}
