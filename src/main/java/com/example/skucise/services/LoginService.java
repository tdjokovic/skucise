package com.example.skucise.services;

import com.example.skucise.models.LoginCredentials;
import com.example.skucise.models.LoginResponse;
import com.example.skucise.repositories.LoginRepository;
import com.example.skucise.security.SecurityConfiguration;
import com.example.skucise.services.interfaces.ILoginService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

@Service
public class LoginService implements ILoginService {
    private final LoginRepository loginRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    public LoginService(LoginRepository loginRepository){
        this.loginRepository = loginRepository;
    }

    @Override
    public LoginResponse checkLoginCredentials(LoginCredentials loginCredentials) {
        LoginResponse loginResponse = loginRepository.checkCredentials(loginCredentials);

        LOGGER.info("Vracen response iz repository");

        //
        if(loginResponse == null){
            LOGGER.info("Vracen je null");
            return null;
        }


        //kredencijali za prijavu nisu validni. vraca se prazan response
        if(!loginResponse.getAreCredsValid()){
            LOGGER.info("Kredencijali korisnika nisu validni");
            return new LoginResponse();
        }

        //korisnik kome nije odobren pristup
        if(!loginResponse.isApproved()){
            LOGGER.info("Korisnicki pristup jos uvek nije odobren");
            loginResponse.setApproved(false);
            return loginResponse;
        }

        LOGGER.info("Sve je okej, korisnik moze da se uloguje bez problema");
        loginResponse.setJwt(SecurityConfiguration.createJWT(loginResponse.getUserId(), loginResponse.getRole()));
        LOGGER.info("Uspesno kreiran JWT za korisnika i on moze da se loguje, vracamo se controlleru");

        return loginResponse;
    }
}
