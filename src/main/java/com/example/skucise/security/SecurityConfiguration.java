package com.example.skucise.security;

import com.example.skucise.models.User;
import com.example.skucise.repositories.UserRepository;
import com.example.skucise.services.UserService;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Component
public class SecurityConfiguration implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfiguration.class);


    //Naziv HTTP Header-a u kome ce biti smesten JSON Web Token
    public static final String JWT_CUSTOM_HTTP_HEADER = "Json-Web-Token";

    //sajt koji je kreirao token
    public static final String ISSUER = "http://localhost:8080";

    //vreme trajanja jednog tokena
    public static final long TIME_TO_LIVE_MILLS = 7200000L; // 2 sata

    //kljuc koji se koristi za sifrovanje jwt-a
    private static final String SECRET_KEY = "oeRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd9jo1QTy4b7P9Ze5_9hKolVX8";


    public static final String ROLE_CLAIM_NAME = "rol";
    public static final String USER_ID_CLAIM_NAME = "uid";
    private static ApplicationContext applicationContext;


    public static ResultPair checkAccess(String jwt, Role... authorizedRoles){
        ResultPair resultPair = new ResultPair(null, HttpStatus.OK);

        Role role;

        try{
            LOGGER.warn("TRYING TO CHECK ACCESS...");
            //proveravamo da li korisnik ima autentifikaciju
            if (jwt == null || jwt == ""){
                role = Role.VISITOR;
                LOGGER.info("JWT is not fount. User role set to VISITOR");
            }
            else{
                //postoji jwt, sto znaci da je korisnik ulogovan
                Claims claims = decodeJWT(jwt);
                LOGGER.debug("JWT claims: "+claims);

                Object roleClaim = claims.get(ROLE_CLAIM_NAME);
                LOGGER.debug("Role claims: "+roleClaim);

                // sada proveravamo da li token sadrzi ulogu
                if(roleClaim == null){
                    throw new UserNotFoundException("JWT not containing role");
                }

                //ako sadrzi role, gledamo koja je to uloga?
                try{
                    String roleString = (String) roleClaim;
                    role = Role.valueOf(roleString.toUpperCase());
                    LOGGER.info("Role inside JWT is {}", role);
                }catch (IllegalArgumentException | NullPointerException e){
                    LOGGER.error("Error while trying to read role from token!");
                    throw e;
                }

                Object user_idClaim = claims.get(USER_ID_CLAIM_NAME);
                LOGGER.debug("extracted User Id claim {}", user_idClaim);

                //proveravamo da li sadrzi user id
                if(user_idClaim == null){
                    throw new UnsupportedJwtException("Missing User_id claim!");
                }

                //ako prodje znaci da ima, sada ga uzimamo
                int user_id = -1;
                try {
                    user_id = (int)(double)user_idClaim;
                    LOGGER.info("Found User_id inside token is {}",user_id);
                }
                catch (IllegalArgumentException | NullPointerException e){
                    LOGGER.error("Error while trying to get User_id from token!");
                    throw e;
                }

                LOGGER.info("EVERYTHINGS RIGHT");

                //sada kada smo nabavili sve sto nam je potrebno, trazimo tog korisnika!
                UserRepository userRepository = applicationContext.getBean(UserRepository.class);
                UserService userService = applicationContext.getBean(UserService.class, userRepository);
                User user = userService.getUser(user_id);

                //sada proveravamo da li je pronadjen korisnik sa tim id-jem
                if(user == null){
                    //throw new UserNotFoundException("User with id "+ user_id +" is not found!!!"); // da li je ovo potrebno tj da li treba da se implementira getUser gore?
                }

                resultPair.setClaims(claims);
            }

            resultPair.setHttpStatus(role.checkAuthorization(authorizedRoles));

        }
        catch (UserNotFoundException | ExpiredJwtException e){
            LOGGER.warn("Recieved JWT is not valid!");
            LOGGER.warn("{}",e.getMessage());
            resultPair.setHttpStatus(HttpStatus.UNAUTHORIZED);
        }
        catch (MalformedJsonException | MalformedJwtException | JsonSyntaxException | IllegalArgumentException | UnsupportedJwtException e){
            LOGGER.warn("Error while parsing JWT!");
            e.printStackTrace();
            resultPair.setHttpStatus(HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            LOGGER.error("SOMETHING IS WRONG!");
            e.printStackTrace();
            resultPair.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return resultPair;
    }

    /**
     * Ova metoda zove svoju overloadovanu
     * prihvata samo id usera i njegovu ulogu
     **/
    public static String createJWT(int user_id, String role){
        return createJWT(user_id, ISSUER, TIME_TO_LIVE_MILLS, role);
    }
    /**
     * Ova metoda kreira JWT prema korisnikovoj ulozi
     *  prihvata njegov id, ulogu, vreme isteka
     * HS256 se koristi za sifrovanje jwt-a, a parseBase64Binary za header i payload
     * */
    public static String createJWT(int user_id, String issuer, long timeToLiveMilliseconds, String role){
        LOGGER.info("Kreiranje JWT-a pocinje");
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        LOGGER.info("Kreiran signature");
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        LOGGER.info("Kreiran api Key");

        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        LOGGER.info("Kreiran signing key");

        JwtBuilder builder = Jwts.builder().claim(USER_ID_CLAIM_NAME, user_id)
                        .claim(ROLE_CLAIM_NAME, role)
                        .setExpiration(new Date(System.currentTimeMillis() + timeToLiveMilliseconds))
                        .setIssuer(issuer)
                        .signWith(signingKey, signatureAlgorithm);
        LOGGER.info("Kreiran builder");

        String createdJwt = builder.compact();
        LOGGER.info("Izbildovan JWT");

        return createdJwt;
    }

    /**
     * dekodira JWT uz pomoc SECRET_KEY
     * ako uspe, vraca informacije o sadrzaju tokena
     * ako ne uspe, baca odgovarajucu gresku
     * MalformedJwtException - JWT nije kreiran kako treba
     * MalformedJsonException - JSON nije kako treba
     * ExpiredJwtException - JWT istekao
     * JsonSyntaxException - JWT nije kako treba
     */
    public static Claims decodeJWT(String jwt) throws MalformedJwtException,MalformedJsonException, ExpiredJwtException,JsonSyntaxException {
        LOGGER.warn("PARSING JWT...");
        Claims cl = Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter
                        .parseBase64Binary(SECRET_KEY))
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        LOGGER.warn("JWT parsed");
        return cl;
    }

    @Override
    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
