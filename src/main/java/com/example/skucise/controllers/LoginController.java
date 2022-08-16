package com.example.skucise.controllers;

import com.example.skucise.models.LoginCredentials;
import com.example.skucise.models.LoginResponse;
import com.example.skucise.repositories.LoginRepository;
import com.example.skucise.security.Role;
import com.example.skucise.services.LoginService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.skucise.security.SecurityConfiguration.JWT_CUSTOM_HTTP_HEADER;
import static com.example.skucise.security.SecurityConfiguration.checkAccess;
import org.slf4j.Logger;

@Validated
@RestController
@RequestMapping(value = "api/login", method = {RequestMethod.POST})
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService){
        this.loginService = loginService;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    /**
     * ovaj metod prihvata token kao i mejl i password
     * proverava da li su uneti podaci ispravni i da li taj korisnik ima pristup ovom endpointu
     * Imamo X slucajeva
     * 1) korisnik je vec ulogovan, on se vraca uz kod 403 forbiddden. Takodje vraca se i njegov token
     * 2) nije moguce autentifikovati korisnika
     * 3) podaci koje je korisnik uneo nisu ispravni, vraca se uz kod 401 unauthorized
     * 4) podaci koje je korisnik uneo su ispravni, ali jos uvek nije validated od strane admina. vraca se uz kod 403, i jwt se postavlja na null
     * 5) podaci koje je korisnik uneo su ispravni, vraca se kod 200, biva mu dodeljen jwt i smesta se u HTTP header
     */
    @GetMapping
    public ResponseEntity<?> login(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt, @Valid @RequestBody LoginCredentials loginCredentials){
        //LOGGER.info("Usli smo u metodu");
        HttpStatus httpStatus =checkAccess(jwt, Role.VISITOR ).getHttpStatus();
        //LOGGER.info("korisnik zeli da se prijavi");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        //
        LOGGER.info("Prijava korisnika, Status?");

        if (httpStatus != HttpStatus.OK){
            LOGGER.info("Statis is not ok, error!");
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        //proveravamo kredencijale za login
        LoginResponse loginResponse = loginService.checkLoginCredentials(loginCredentials);

        //neuspesno logovanje usled greske sistema
        if(loginResponse == null){
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        else{
            //jwt vracen, gledamo da li je uspesno logovanje!
            jwt = loginResponse.getJwt();

            if(! loginResponse.getAreCredsValid()){
                httpStatus = HttpStatus.UNAUTHORIZED;
            }
            else if(!loginResponse.isApproved()){
                httpStatus = HttpStatus.FORBIDDEN;
            }
        }

        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
    }


}
