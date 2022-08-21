package com.example.skucise.services;

import com.example.skucise.models.Property;
import com.example.skucise.models.Rating;
import com.example.skucise.models.SellerUser;
import com.example.skucise.repositories.SellerRepository;
import com.example.skucise.services.interfaces.ISellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerService implements ISellerService {

    private final SellerRepository sellerRepository;

    @Autowired
    public SellerService(SellerRepository sellerRepository){this.sellerRepository = sellerRepository;}


    @Override
    public String registerSeller(SellerUser seller) {
        boolean success = sellerRepository.create(seller);
        if(success) return "Success";
        else return "Email you submitted is already in use!";
    }

    @Override
    public List<SellerUser> getAllSeller(boolean notApprovedRequested) {
        return sellerRepository.getAll(notApprovedRequested);
    }

    @Override
    public SellerUser getSeller(int id) {
        return sellerRepository.get(id);
    }

    @Override
    public boolean approveSeller(int id) {
        return sellerRepository.approve(id);
    }

    @Override
    public boolean deleteSeller(int id) {
        return sellerRepository.delete(id);
    }

    @Override
    public List<Property> getPostedProperties(int id) {
        return sellerRepository.getPostedProperties(id);
    }

    @Override
    public Rating getRating(int sellerId, int buyerId, boolean isBuyer) {
        return sellerRepository.getRating(sellerId, buyerId, isBuyer);
    }

    @Override
    public boolean rate(int sellerId, int buyerId, byte feedbackValue) {
        return sellerRepository.rate(sellerId, buyerId, feedbackValue);
    }
}
