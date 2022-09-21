package com.example.skucise.controllers;


import com.example.skucise.models.NewUserData;
import com.example.skucise.models.Property;
import com.example.skucise.models.Rating;
import com.example.skucise.models.SellerUser;
import com.example.skucise.security.ResultPair;
import com.example.skucise.security.Role;
import com.example.skucise.services.SellerService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import static com.example.skucise.security.SecurityConfiguration.*;

@Validated
@RestController
@RequestMapping("api/sellers")
public class SellerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SellerController.class);
    private final SellerService sellerService;

    @Autowired
    public SellerController(SellerService sellerService){this.sellerService = sellerService;}

    @GetMapping
    public ResponseEntity<List<SellerUser>> getSellerList(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt,
                                                          @RequestParam(value="notApprovedRequested", required = false) boolean notApprovedRequested){

        LOGGER.info("GETTING ALL SELLERS");

        ResultPair resultPair = checkAccess(jwt, Role.VISITOR, Role.ADMIN, Role.REG_BUYER, Role.REG_SELLER);
        HttpStatus httpStatus = resultPair.getHttpStatus();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        if(httpStatus != HttpStatus.OK){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        //odobren je pristup
        String role = Role.VISITOR.toString();
        Claims claims = resultPair.getClaims();
        if(claims != null){
            //ako nije visitor
            role = (String) claims.get(ROLE_CLAIM_NAME);
        }

        //if user is not admin, and isnt approved user
        if( !Role.ADMIN.equalsTo(role) &&  notApprovedRequested){
            LOGGER.warn("User is not admin and is not approved");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(responseHeaders).body(null);
        }

        List<SellerUser> sellers = sellerService.getAllSeller(notApprovedRequested);

        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(sellers);
    }

    @PostMapping
    public ResponseEntity<?> registerSeller(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt,
                                            @Valid @RequestBody SellerUser seller){
        // da li i buyer moze da u nekom trenutku postane seller? U tom slucaju dodaje se jos jedna rola
        HttpStatus httpStatus = checkAccess( jwt, Role.VISITOR ).getHttpStatus();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        if(httpStatus != HttpStatus.OK){
            return  ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        //moze da se registruje
        String registerResult = sellerService.registerSeller(seller);
        if(registerResult.equals("Success")) httpStatus = HttpStatus.CREATED;
        else httpStatus = HttpStatus.CONFLICT;

        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
    }

    @GetMapping("{id}")
    public ResponseEntity<SellerUser> getSeller(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt,
                                                @PathVariable("id") @Min( 1 ) @Max( Integer.MAX_VALUE ) int id ){
        HttpStatus httpStatus = checkAccess(jwt, Role.VISITOR, Role.ADMIN, Role.REG_SELLER, Role.REG_BUYER ).getHttpStatus();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        if(httpStatus != HttpStatus.OK){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        //moze da prikaze
        SellerUser sellerUser = sellerService.getSeller(id);

        if(sellerUser == null ){
            httpStatus = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(sellerUser);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> approveSeller( @RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt,
                                             @PathVariable("id") @Min( 1 ) @Max( Integer.MAX_VALUE ) int id ){
        HttpStatus httpStatus = checkAccess(jwt, Role.ADMIN ).getHttpStatus();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        if( httpStatus!= HttpStatus.OK){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        boolean approvedSuccess = sellerService.approveSeller(id);

        if(approvedSuccess){
            httpStatus = HttpStatus.NO_CONTENT;
        }
        else{
            httpStatus = HttpStatus.CONFLICT;
        }

        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteSeller( @RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt,
                                           @PathVariable("id") @Min( 1 ) @Max( Integer.MAX_VALUE ) int id ){

        ResultPair resultPair = checkAccess(jwt, Role.VISITOR, Role.ADMIN, Role.REG_BUYER, Role.REG_SELLER);
        HttpStatus httpStatus = resultPair.getHttpStatus();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        if(httpStatus != HttpStatus.OK){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }
        int userId = (int) (double) resultPair.getClaims().get(USER_ID_CLAIM_NAME);
        String role = (String) resultPair.getClaims().get(ROLE_CLAIM_NAME);

        if(userId != id && Role.ADMIN.equalsTo(role)){
            //samo svoj profil moze da brise, ili admin
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(responseHeaders).body(null);
        }

        //dozvoljeno
        boolean successfullDelete = sellerService.deleteSeller(id);

        if(successfullDelete) httpStatus = HttpStatus.NO_CONTENT;
        else httpStatus = HttpStatus.CONFLICT;

        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
    }

    @GetMapping("{id}/properties")
    public ResponseEntity<List<Property>> getPostedProperties(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt,
                                                              @PathVariable("id") @Min( 1 ) @Max( Integer.MAX_VALUE ) int id ){

        HttpStatus httpStatus = checkAccess(jwt, Role.VISITOR, Role.ADMIN, Role.REG_BUYER, Role.REG_SELLER).getHttpStatus();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        if(httpStatus != HttpStatus.OK){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        List<Property> properties = sellerService.getPostedProperties(id);

        if(properties == null){
            httpStatus = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(properties);

    }

    @GetMapping("{id}/rating")
    public ResponseEntity<Rating> getRating(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt,
                                            @PathVariable("id") @Min( 1 ) @Max( Integer.MAX_VALUE ) int id ){

        ResultPair resultPair = checkAccess(jwt, Role.VISITOR, Role.ADMIN, Role.REG_BUYER, Role.REG_SELLER);
        HttpStatus httpStatus = resultPair.getHttpStatus();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        if(httpStatus != HttpStatus.OK){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        int userId = 0;
        boolean isBuyer = false;

        //ako postoji taj token
        if(resultPair.getClaims() != null){
            userId = (int) (double) resultPair.getClaims().get(USER_ID_CLAIM_NAME);
            String role = (String) resultPair.getClaims().get(ROLE_CLAIM_NAME);

            if(Role.REG_BUYER.equalsTo(role)){
                isBuyer = true;
            }
        }

        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(sellerService.getRating(id, userId, isBuyer));
    }

    @PostMapping("{id}/rating")
    public ResponseEntity<?> rate(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt,
                                  @PathVariable("id") @Min( 1 ) @Max( Integer.MAX_VALUE ) int id ,
                                  @RequestParam @Min( 0 ) @Max( 5 ) byte feedbackValue ){

        ResultPair resultPair = checkAccess(jwt, Role.REG_BUYER);
        HttpStatus httpStatus = resultPair.getHttpStatus();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        if(httpStatus != HttpStatus.OK){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        int buyerId = (int) (double) resultPair.getClaims().get(USER_ID_CLAIM_NAME);

        boolean successRating = sellerService.rate(id, buyerId, feedbackValue);

        if(successRating) httpStatus = HttpStatus.NO_CONTENT;
        else httpStatus = HttpStatus.CONFLICT;

        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);

    }

    @PostMapping("{id}/editData")
    public ResponseEntity<?> editData(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt,
                                      @PathVariable("id") @Min( 1 ) @Max( Integer.MAX_VALUE ) int id ,
                                      @Valid @RequestBody NewUserData newUserData ){

        ResultPair resultPair = checkAccess(jwt, Role.REG_SELLER);
        HttpStatus httpStatus = resultPair.getHttpStatus();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        if(httpStatus != HttpStatus.OK){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        int userId = (int) (double) resultPair.getClaims().get(USER_ID_CLAIM_NAME);
        if(userId != id){
            //kupac pokusava nekom drugom da izmeni podatke
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(responseHeaders).body(null);
        }

        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(sellerService.editData(id, newUserData));

    }
}
