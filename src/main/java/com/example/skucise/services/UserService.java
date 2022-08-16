package com.example.skucise.services;

import com.example.skucise.models.User;
import com.example.skucise.repositories.UserRepository;
import com.example.skucise.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(int user_id) {
        return null;
    }
}
