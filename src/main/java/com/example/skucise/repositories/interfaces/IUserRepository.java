package com.example.skucise.repositories.interfaces;

import com.example.skucise.models.BuyerUser;
import com.example.skucise.models.User;

public interface IUserRepository extends CRUDRepository<User, Integer> {

    public BuyerUser getAsBuyer(Integer id);
}
