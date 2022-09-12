package com.example.skucise.services.interfaces;

import com.example.skucise.models.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IPropertyService {

    public PropertyFeed getFilteredProperties(PropertiesFilter propertyFilter);

    public boolean postProperty(Property property);

    public List<BuyerUser> getPropertyApplicants(int propertyId);

    public Property getProperty(int id);

    public String applyForProperty(int propertyId, int userId);

    public boolean deleteProperty(int id);

    public List<Comment> getAllComments(int propertyId);

    public boolean postComment(Comment comment, int propertyId, int buyerId, boolean isApplicant);

    public boolean deleteComment(int id);

    public LikeResponse getPropertyLikes(int propertyId, int buyerId, boolean isApplicant);

    public boolean likeProperty(int propertyId, int buyerId);

    public boolean recallLike(int jobId, int buyerId);
}
