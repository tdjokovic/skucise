package com.example.skucise.controllers;

import com.example.skucise.models.BuyerUser;
import com.example.skucise.models.NewUserData;
import com.example.skucise.models.Property;
import com.example.skucise.security.ResultPair;
import com.example.skucise.security.Role;
import com.example.skucise.services.BuyerService;
import com.example.skucise.services.UserService;
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
@RequestMapping("api/buyers")
public class BuyerController {

    private final BuyerService buyerService;
    private final UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(BuyerController.class);

    @Autowired
    public BuyerController(BuyerService buyerService, UserService userService){
        this.buyerService = buyerService;
        this.userService = userService;
    }

    //zahtev za logovanje novog kupca
    @PostMapping
    public ResponseEntity<?> registerBuyer(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt,
                                           @Valid @RequestBody BuyerUser buyerUser ){
        HttpStatus httpStatus = checkAccess(jwt, Role.VISITOR).getHttpStatus(); //samo ako je visitor moze da se registruje kao kupac

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        //ako nije dobar odgovor
        if(httpStatus != HttpStatus.OK){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        //ako jeste, onda moze da se registruje
        String registerResult = buyerService.registerBuyer( buyerUser );

        //sada proveravamo da li je registracija izvedena uspesno
        if(registerResult.equals("Success")){
            httpStatus = HttpStatus.CREATED;
        }
        else{
            httpStatus = HttpStatus.CONFLICT;
        }

        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
    }

    @GetMapping("{id}")
    public ResponseEntity<BuyerUser> getBuyer(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String  jwt,
                                              @PathVariable("id") @Min( 1 ) @Max( Integer.MAX_VALUE ) int id){
        ResultPair resultPair = checkAccess(jwt, Role.ADMIN, Role.REG_SELLER, Role.REG_BUYER);
        HttpStatus httpStatus = resultPair.getHttpStatus();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        //nije odobren pristup
        if( httpStatus != HttpStatus.OK ){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        //odobren mu je pristup
        //trazimo user id i role iz tokena
        int uid = (int) (double) resultPair.getClaims().get(USER_ID_CLAIM_NAME);
        String role =(String) resultPair.getClaims().get(ROLE_CLAIM_NAME);

        //ako je kupac i njegov id nije kao onaj prosledjen ne moze da vidi podatke drugog kupca
        if(Role.REG_BUYER.equalsTo(role) && uid != id){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(responseHeaders).body(null);
        }

        //ako je prodavac, ali prosledjeni kupac nema zahtev za razgledanje stana kod njega
        else if (Role.REG_SELLER.equalsTo(role) && uid != id && !buyerService.isApplied(uid, id)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(responseHeaders).body(null);
        }

        //u suprotnom mogu da se uzmu podaci
        BuyerUser buyerUser = userService.getAsBuyer(id);
        if(buyerUser != null){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(buyerUser);
        }

        //ako kupac ne postoji sa tim idjem
        return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(responseHeaders).body(null);
    }

    @GetMapping
    public ResponseEntity<List<BuyerUser>> getAllBuyers(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt,
                                                        @RequestParam boolean notApprovedRequested){

        ResultPair resultPair = checkAccess(jwt, Role.ADMIN);
        HttpStatus httpStatus = resultPair.getHttpStatus();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        if( httpStatus == HttpStatus.OK ){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(buyerService.getAllBuyers(!notApprovedRequested));
        }

        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
    }

    @PutMapping("/change")
    public ResponseEntity<?> changeToSeller(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt)
    {
        ResultPair resultPair = checkAccess(jwt, Role.REG_BUYER);
        HttpStatus httpStatus = resultPair.getHttpStatus();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER,jwt);

        if (httpStatus != HttpStatus.OK)
        {
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        int uid = (int) (double) resultPair.getClaims().get(USER_ID_CLAIM_NAME);

        boolean isChanged = buyerService.changeToSeller(uid);


        if(isChanged) httpStatus = HttpStatus.NO_CONTENT;
        else httpStatus = HttpStatus.CONFLICT;

        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);

    }
    @PutMapping("{id}")
    public ResponseEntity<?> approve(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt,
                                     @PathVariable("id") @Min( 1 ) @Max( Integer.MAX_VALUE ) int id ){

        ResultPair resultPair = checkAccess(jwt, Role.ADMIN);
        HttpStatus httpStatus = resultPair.getHttpStatus();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        if(httpStatus != HttpStatus.OK){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        boolean approveResponseSuccessfull = buyerService.approve(id);


        if(approveResponseSuccessfull) httpStatus = HttpStatus.NO_CONTENT;
        else httpStatus = HttpStatus.CONFLICT;

        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteBuyer(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt,
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
        boolean successfullDelete = buyerService.delete(id);

        if(successfullDelete) httpStatus = HttpStatus.NO_CONTENT;
        else httpStatus = HttpStatus.CONFLICT;

        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
    }

    //for that user, get list of applied properties
    @GetMapping("{id}/properties")
    public ResponseEntity<List<Property>> getAppliedProperties(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt,
                                                               @PathVariable("id") @Min( 1 ) @Max( Integer.MAX_VALUE ) int id ){

        ResultPair resultPair = checkAccess(jwt, Role.ADMIN, Role.REG_BUYER );
        HttpStatus httpStatus = resultPair.getHttpStatus();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        if( httpStatus != HttpStatus.OK ){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        //ako mu je odobren pristup, sada proveravamo ko je korisnik koji je poslao zahtev
        int uid = (int) (double) resultPair.getClaims().get(USER_ID_CLAIM_NAME);
        String role = (String) resultPair.getClaims().get(ROLE_CLAIM_NAME);

        //ako je to takodje kupac, ali ne taj sa tim idjem, ne moze da vidi spisak nekretnina!
        if(Role.REG_BUYER.equalsTo(role) && uid != id){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(responseHeaders).body(null);
        }

        List<Property> properties = buyerService.getAppliedProperties(id);

        if(properties != null) return ResponseEntity.status(httpStatus).headers(responseHeaders).body(properties);

        //nema ni jedne nekretnine
        return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(responseHeaders).body(null);
    }

    @PostMapping("{id}/editData")
    public ResponseEntity<?> editData(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt,
                                      @PathVariable("id") @Min( 1 ) @Max( Integer.MAX_VALUE ) int id ,
                                      @Valid @RequestBody NewUserData newUserData ){

        ResultPair resultPair = checkAccess(jwt, Role.REG_BUYER);
        HttpStatus httpStatus = resultPair.getHttpStatus();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        if(httpStatus != HttpStatus.OK){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        int userId = (int) (double) resultPair.getClaims().get(USER_ID_CLAIM_NAME);
        LOGGER.info("User id is {}, and id is {}", userId, id);
        if(userId != id){
            //kupac pokusava nekom drugom da izmeni podatke
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(responseHeaders).body(null);
        }

        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(buyerService.editData(id, newUserData));

    }

}
