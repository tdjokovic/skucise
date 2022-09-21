package com.example.skucise.services.interfaces;

import com.example.skucise.models.BuyerUser;
import com.example.skucise.models.NewUserData;
import com.example.skucise.models.Property;

import java.util.List;

public interface IBuyerService {
    public String registerBuyer(BuyerUser buyerUser);

    public BuyerUser getBuyer(int id);

    public boolean isApplied(int sellerId, int buyerId);

    public List<BuyerUser> getAllBuyers(boolean approved);

    public boolean approve(int id);

    public boolean delete(int id);

    public List<Property> getAppliedProperties(int id);

    public boolean editData(int id, NewUserData data);
}
