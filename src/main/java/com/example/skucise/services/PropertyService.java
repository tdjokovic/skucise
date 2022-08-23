package com.example.skucise.services;

import com.example.skucise.models.*;
import com.example.skucise.repositories.PropertyRepository;
import com.example.skucise.services.interfaces.IPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyService implements IPropertyService {

    private PropertyRepository propertyRepository;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository){this.propertyRepository = propertyRepository;}

    @Override
    public PropertyFeed getFilteredProperties(PropertiesFilter propertyFilter) {
        return propertyRepository.getFilteredProperties(propertyFilter);
    }

    @Override
    public boolean postProperty(Property property) {
        return propertyRepository.create(property);
    }

    @Override
    public List<BuyerUser> getPropertyApplicants(int propertyId) {
        return propertyRepository.getPropertyApplicants(propertyId);
    }

    @Override
    public Property getProperty(int id) {
        return propertyRepository.get(id);
    }

    @Override
    public String applyForProperty(int propertyId, int userId) {
        boolean isSuccess = propertyRepository.applyForProperty(propertyId, userId);

        if(isSuccess){
            return "Success";
        }
        else
            return "Unsuccessfull applying for a property";

    }

    @Override
    public boolean deleteProperty(int id) {
        return propertyRepository.delete(id);
    }

    @Override
    public List<Comment> getAllComments(int propertyId) {
        return propertyRepository.getAllComments(propertyId);
    }

    @Override
    public boolean postComment(Comment comment, int propertyId, int buyerId, boolean isApplicant) {
        return propertyRepository.postComment(comment, propertyId, buyerId, isApplicant);
    }

    @Override
    public boolean deleteComment(int id) {
        return propertyRepository.deleteComment(id);
    }

    @Override
    public LikeResponse getPropertyLikes(int propertyId, int buyerId, boolean isApplicant) {
        return propertyRepository.getPropertyLikes(propertyId, buyerId, isApplicant);
    }

    @Override
    public boolean likeProperty(int propertyId, int buyerId) {
        return propertyRepository.likeProperty(propertyId, buyerId);
    }

    @Override
    public boolean recallLike(int propertyId, int buyerId) {
        return propertyRepository.deleteLike(propertyId, buyerId);
    }
}
