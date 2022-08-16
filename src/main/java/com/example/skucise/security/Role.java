package com.example.skucise.security;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Locale;

public enum Role {
    //posetilac koji nije registrovan
    VISITOR,
    //administrator
    ADMIN,
    //registovan prodavac
    REG_SELLER,
    //registrovan kupac
    REG_BUYER;

    private static final Logger LOGGER = LoggerFactory.getLogger(Role.class);

    /**
     * authorizedRoles predstavlja listu autorizovanih naloga za pristup nekom resursu
     * ova metoda proverava da li se tip korisnickog naloga nalazi u ovoj listi autorizovanih
     * vraca se status 200 ako jeste ili 401 ako nije autorizovan pristup
     * 403 se vraca ako korisnik nije autentifikovan
     */

    public HttpStatus checkAuthorization( Role[] authorizedRoles ){
        for(Role role:authorizedRoles){
            // ovo je metoda enum-a Role
            if(this.equalsTo(role)){
                LOGGER.info("User is authorized!");
                return HttpStatus.OK;
            }
        }

        LOGGER.info("User is not authorized. Authorized roles are: ", Arrays.toString(authorizedRoles));

        if(this.equalsTo(Role.VISITOR)){
            return HttpStatus.UNAUTHORIZED;
        }

        return HttpStatus.FORBIDDEN;
    }


    /**
     * Ova metoda proverava da li je prosledjeni objekat instance Role ili String
     * Ako jeste, proverava da li je String reprezentacija Role objekta u okviru koga je metod pozvan
     * jednaka String reprezentaciji prosledjenog role objekta.
     */
    public boolean equalsTo( Object role ){
        if(role instanceof Role || role instanceof String){
            String thisRole = this.toString().toLowerCase();
            String parameterRole = role.toString().toLowerCase();

            LOGGER.debug("(thisRole, parameterRole) : ({},{})", thisRole, parameterRole);
            boolean comapred = (0 == thisRole.compareTo(parameterRole));
            LOGGER.debug("result : {}", comapred);

            return comapred;
        }
        return false;
    }
}
