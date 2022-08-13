package com.example.skucise.controllers;

import com.example.skucise.models.LoginCredentials;
import com.example.skucise.models.LoginResponse;
import com.example.skucise.security.Role;
import com.example.skucise.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import static com.example.skucise.security.SecurityConfiguration.JWT_CUSTOM_HTTP_HEADER;
import static com.example.skucise.security.SecurityConfiguration.checkAccess;

@Validated
@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService){
        this.loginService = loginService;
    }

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
    public ResponseEntity<?> login(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt, @Valid @RequestBody LoginCredentials loginCredentials){
        HttpStatus httpStatus =checkAccess(jwt, Role.VISITOR ).getHttpStatus();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        if (httpStatus != HttpStatus.OK){
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
