package com.example.skucise.repositories.interfaces;

import com.example.skucise.models.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPropertyRepository extends CRUDRepository<Property, Integer>{
    List<BuyerUser> getPropertyApplicants(Integer propertyId);
    PropertyFeed getFilteredProperties(PropertiesFilter propertiesFilter);
    boolean applyForProperty(Integer propertyId,Integer userId);
    List<Comment> getAllComments(int propertyId);
    boolean postComment(Comment comment, int propertyId, int userId, boolean isApplicant);
    boolean deleteComment(Integer commentId);
    LikeResponse getPropertyLikes(Integer propertyId, Integer buyerId, boolean isApplicant);
    boolean likeProperty(Integer propertyId, Integer buyerId);
    boolean deleteLike(Integer propertyId, Integer buyerId);
}
