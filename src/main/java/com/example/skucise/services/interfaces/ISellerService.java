package com.example.skucise.services.interfaces;

import com.example.skucise.models.Property;
import com.example.skucise.models.Rating;
import com.example.skucise.models.SellerUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ISellerService {
    public String registerSeller( SellerUser seller );
    public List<SellerUser> getAllSeller(boolean notApprovedRequested) ;
    public SellerUser getSeller(int id);
    public boolean approveSeller(int id);
    public boolean deleteSeller(int id);
    public List<Property> getPostedProperties(int id);
    public Rating getRating(int sellerId, int buyerId, boolean isApplicant);
    public boolean rate(int sellerId, int buyerId, byte feedbackValue);
}
