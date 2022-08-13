package com.example.skucise.services;

import com.example.skucise.models.LoginCredentials;
import com.example.skucise.models.LoginResponse;
import com.example.skucise.repositories.LoginRepository;
import com.example.skucise.security.SecurityConfiguration;
import com.example.skucise.services.interfaces.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements ILoginService {
    private final LoginRepository loginRepository;

    @Autowired
    public LoginService(LoginRepository loginRepository){
        this.loginRepository = loginRepository;
    }

    @Override
    public LoginResponse checkLoginCredentials(LoginCredentials loginCredentials) {
        LoginResponse loginResponse = loginRepository.checkCredentials(loginCredentials);

        //
        if(loginResponse == null){
            return null;
        }

        //kredencijali za prijavu nisu validni. vraca se prazan response
        if(!loginResponse.getAreCredsValid()){
            return new LoginResponse();
        }

        //korisnik kome nije odobren pristup
        if(!loginResponse.isApproved()){
            loginResponse.setApproved(false);
            return loginResponse;
        }

        loginResponse.setJwt(SecurityConfiguration.createJWT(loginResponse.getUserId(), loginResponse.getRole()));

        return loginResponse;
    }
}
