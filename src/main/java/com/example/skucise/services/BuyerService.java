package com.example.skucise.services;

import com.example.skucise.models.BuyerUser;
import com.example.skucise.models.NewUserData;
import com.example.skucise.models.Property;
import com.example.skucise.repositories.BuyerRepository;
import com.example.skucise.services.interfaces.IBuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

@Service
public class BuyerService implements IBuyerService {

    private final BuyerRepository buyerRepository;

    @Autowired
    public BuyerService(BuyerRepository buyerRepository){this.buyerRepository = buyerRepository;}


    @Override
    public String registerBuyer(BuyerUser buyerUser) {

        boolean success = buyerRepository.create(buyerUser);

        if(success) return "Success";
        else return "Email you submitted is already in use!";

    }

    @Override
    public BuyerUser getBuyer(int id) {
        return buyerRepository.get(id);
    }

    @Override
    public boolean isApplied(int sellerId, int buyerId) {
        return buyerRepository.isApplied(sellerId,buyerId);
    }

    @Override
    public List<BuyerUser> getAllBuyers(boolean approved) {
        return buyerRepository.getAll(approved);
    }

    @Override
    public boolean approve(int id) {
        return buyerRepository.approve(id);
    }

    @Override
    public boolean delete(int id) {
        return buyerRepository.delete(id);
    }

    @Override
    public List<Property> getAppliedProperties(int id) {
        return buyerRepository.getAppliedProperties(id);
    }

    @Override
    public boolean editData(int id, NewUserData data) {
        return buyerRepository.editData(id, data);
    }
}
