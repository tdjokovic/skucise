package com.example.skucise.services.interfaces;


import com.example.skucise.models.LoginCredentials;
import com.example.skucise.models.LoginResponse;

public interface ILoginService {

    public LoginResponse checkLoginCredentials(LoginCredentials loginCredentials );
}
