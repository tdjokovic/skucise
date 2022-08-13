package com.example.skucise.repositories.interfaces;

import com.example.skucise.models.LoginCredentials;
import com.example.skucise.models.LoginResponse;
import org.springframework.stereotype.Repository;

@Repository
public interface ILoginRepository extends CRUDRepository<LoginCredentials, Integer>{
    LoginResponse checkCredentials(LoginCredentials loginCredentials);
}
