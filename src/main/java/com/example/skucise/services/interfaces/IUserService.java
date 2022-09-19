package com.example.skucise.services.interfaces;

import com.example.skucise.models.BuyerUser;
import com.example.skucise.models.User;

public interface IUserService {

    public User getUser(int user_id);

    public BuyerUser getAsBuyer(Integer id);

}
