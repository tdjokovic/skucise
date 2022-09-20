package com.example.skucise.controllers;

import com.example.skucise.models.*;
import com.example.skucise.security.ResultPair;
import com.example.skucise.security.Role;
import com.example.skucise.services.PropertyService;
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
import java.util.ArrayList;
import java.util.List;

import static com.example.skucise.security.SecurityConfiguration.*;

@Validated
@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyController.class);

    @Autowired
    public PropertyController(PropertyService propertyService){this.propertyService = propertyService;}

    @GetMapping
    public ResponseEntity<PropertyFeed> getFilteredProperties(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt,
                                                              @RequestParam(required = false) List<Integer> tagList,
                                                              @RequestParam @Min(0) @Max(Integer.MAX_VALUE) int sellerId,
                                                              @RequestParam @Min(0) @Max(Integer.MAX_VALUE) int typeId,
                                                              @RequestParam @Min(0) @Max(Integer.MAX_VALUE) int adCategoryId,
                                                              @RequestParam @Min(0) @Max(Integer.MAX_VALUE) int cityId,
                                                              @RequestParam @Min(1) @Max(Integer.MAX_VALUE) int pageNumber,
                                                              @RequestParam @Min(5) @Max(Integer.MAX_VALUE) int propertiesPerPage,
                                                              @RequestParam boolean newConstruction,
                                                              @RequestParam boolean ascendingOrder){

        //videti da li ovaj filter treba da sadrzi jos nesto?
        PropertiesFilter propertyFilter = setProperty(sellerId,typeId,adCategoryId,cityId,tagList,newConstruction,pageNumber,propertiesPerPage,ascendingOrder);

        ResultPair resultPair =checkAccess(jwt, Role.VISITOR, Role.ADMIN, Role.REG_BUYER, Role.REG_SELLER);
        HttpStatus httpStatus = resultPair.getHttpStatus();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        if(httpStatus != HttpStatus.OK){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(propertyService.getFilteredProperties(propertyFilter));


    }

    public PropertiesFilter setProperty(int sellerId,int typeId,int adCategoryId,int cityId,List<Integer> tagList,boolean newConstructiom,int pageNumber,
                                      int propertiesPerPage,boolean ascendingOrder){
        PropertiesFilter propertyFilter = new PropertiesFilter();

        SellerUser sellerUser = new SellerUser();
        sellerUser.setId(sellerId);

        Type type = new Type();
        type.setId(typeId);

        AdCategory adCategory = new AdCategory();
        adCategory.setId(adCategoryId);

        City city = new City();
        city.setId(cityId);

        List<Tag> tags;
        if(tagList != null){
            tags = new ArrayList<>();

            for (int t : tagList){
                Tag tag = new Tag();
                tag.setId(t);

                tags.add(tag);
            }
        }
        else{
            tags = null;
        }



        propertyFilter.setSellerUser(sellerUser);
        propertyFilter.setAdCategory(adCategory);
        propertyFilter.setCity(city);
        propertyFilter.setType(type);
        propertyFilter.setTags(tags);
        propertyFilter.setNewConstruction(newConstructiom);

        propertyFilter.setPropertiesPerPage(propertiesPerPage);
        propertyFilter.setPageNumber(pageNumber);
        propertyFilter.setAscendingOrder(ascendingOrder);

        return  propertyFilter;
    }


    @PostMapping
    public ResponseEntity<?> postProperty(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt,
                                          @Valid @RequestBody Property property) {

        ResultPair resultPair = checkAccess(jwt, Role.REG_SELLER);
        HttpStatus httpStatus = resultPair.getHttpStatus();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        if(httpStatus != HttpStatus.OK){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        getSellerID(property, resultPair);

        boolean posted = propertyService.postProperty(property);

        if(posted){
            return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders).body(null);
        }
        else{
            return ResponseEntity.status(HttpStatus.CONFLICT).headers(responseHeaders).body(null);
        }

    }

    private void getSellerID(Property property, ResultPair resultPair){
        int sellerId = (int) (double) resultPair.getClaims().get(USER_ID_CLAIM_NAME);

        property.setSellerUser(new SellerUser());
        property.getSellerUser().setId(sellerId);

        validate(property);
    }

    private void validate(Property property){

        //videti sta sve moze da se preda a da nije naznaceno za nekretninu! Da li ovo moze uopste, da se ne setuje lokacija ili cena?

        //ako nije setovano
        if(property.getCity() == null){
            property.setCity(new City());
            property.getCity().setId(0);
        }

        //ako nije naznacena cena
        if(property.getPrice() == 0){
            property.setPrice(0);
        }
    }

    @GetMapping("{propertyId}/applicants")
    public ResponseEntity<List<BuyerUser>> getPropertyApplicants(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt,
                                                                 @PathVariable("propertyId")
                                                                 @Min( 1 )
                                                                 @Max( Integer.MAX_VALUE ) int propertyId )
    {

        ResultPair resultPair = checkAccess(jwt, Role.REG_SELLER, Role.ADMIN);
        HttpStatus httpStatus = resultPair.getHttpStatus();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        if(httpStatus != HttpStatus.OK){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        int userId =(int)(double) resultPair.getClaims().get(USER_ID_CLAIM_NAME);
        String role = (String) resultPair.getClaims().get(ROLE_CLAIM_NAME);

        List<BuyerUser> applicants = null;
        Property property = propertyService.getProperty(propertyId);

        //da li ta nekretnina iz zahteva postoji?
        if(property == null){
            httpStatus = HttpStatus.NOT_FOUND;
            LOGGER.warn("Property with  id {} does not exist! - getPropertyApplicants", propertyId);
        }
        else{
            //postoji
            int sellerId = property.getSellerUser().getId();

            //samo admin ili taj prodavac mogu da pristupe
            //dakle ako je prodavac ali ne taj, nije mu dozvoljeno
            if(Role.REG_SELLER.equalsTo(role) && sellerId != userId){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(responseHeaders).body(null);
            }

            //dozvoljen prikaz
            applicants = propertyService.getPropertyApplicants(propertyId);

            if(applicants == null){
                //greska kod pribavljanja, nije pronadjeno
                httpStatus = HttpStatus.NOT_FOUND;
                LOGGER.info("No one applied for this property {} - getPropertyApplicants",propertyId);
            }
        }
        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(applicants);
    }


    @PostMapping("{propertyId}/applicants")
    public ResponseEntity<?> applyForProperty( @RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt,
                                              @PathVariable("propertyId")
                                              @Min( 1 )
                                              @Max( Integer.MAX_VALUE ) int propertyId )
    {
        ResultPair resultPair = checkAccess(jwt, Role.REG_BUYER);
        HttpStatus httpStatus = resultPair.getHttpStatus();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        if(httpStatus != HttpStatus.OK){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        int userId =(int)(double) resultPair.getClaims().get(USER_ID_CLAIM_NAME);
        String role = (String) resultPair.getClaims().get(ROLE_CLAIM_NAME);

        String result = propertyService.applyForProperty(propertyId, userId);

        if(result.equals("Success")){
            httpStatus = HttpStatus.CREATED;
        }
        else{
            httpStatus = HttpStatus.CONFLICT;
        }

        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);

    }

    @GetMapping("{id}")
    public ResponseEntity<Property> getProperty(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt,
                                                @PathVariable("id")
                                                @Min( 1 )
                                                @Max( Integer.MAX_VALUE ) int id)
    {
        ResultPair resultPair = checkAccess(jwt, Role.VISITOR, Role.REG_BUYER, Role.REG_SELLER, Role.ADMIN);
        HttpStatus httpStatus = resultPair.getHttpStatus();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        if(httpStatus != HttpStatus.OK){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        Property property = propertyService.getProperty(id);

        if(property == null){
            httpStatus = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(property);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteProperty(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt,
                                            @PathVariable("id")
                                            @Min( 1 )
                                            @Max( Integer.MAX_VALUE ) int id)
    {
        ResultPair resultPair = checkAccess(jwt,Role.REG_SELLER, Role.ADMIN);
        HttpStatus httpStatus = resultPair.getHttpStatus();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        if(httpStatus != HttpStatus.OK){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        int userId = (int) (double) resultPair.getClaims().get(USER_ID_CLAIM_NAME);
        String role = (String) resultPair.getClaims().get(ROLE_CLAIM_NAME);

        int sellerId = propertyService.getProperty(id).getSellerUser().getId();

        if(Role.REG_SELLER.equalsTo(role) && userId != sellerId) {
            //ako je prodavac, ali to nije njegova nekretnina
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(responseHeaders).body(null);
        }

        boolean deleted = propertyService.deleteProperty(id);

        if(deleted){
            httpStatus = HttpStatus.NO_CONTENT;
        }
        else{
            httpStatus = HttpStatus.CONFLICT;
        }

        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
    }

    @GetMapping("{propertyId}/comments")
    public ResponseEntity<List<Comment>> getAllComments(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt,
                                                        @PathVariable("propertyId")
                                                        @Min(1)
                                                        @Max(Integer.MAX_VALUE) int propertyId)
    {
        ResultPair resultPair = checkAccess(jwt,Role.VISITOR, Role.REG_BUYER, Role.REG_SELLER, Role.ADMIN);
        HttpStatus httpStatus = resultPair.getHttpStatus();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        if(httpStatus != HttpStatus.OK){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(propertyService.getAllComments(propertyId));
    }

    @PostMapping("{propertyId}/comments")
    public ResponseEntity<?> postComment(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt,
                                         @PathVariable("propertyId")
                                         @Min(1)
                                         @Max(Integer.MAX_VALUE) int propertyId,
                                         @Valid @RequestBody Comment comment)
    {
        ResultPair resultPair = checkAccess(jwt, Role.REG_BUYER, Role.REG_SELLER);
        HttpStatus httpStatus = resultPair.getHttpStatus();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        if(httpStatus != HttpStatus.OK){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        int userId = (int) (double) resultPair.getClaims().get(USER_ID_CLAIM_NAME);
        String role = (String) resultPair.getClaims().get(ROLE_CLAIM_NAME);

        boolean postedSuccess = false;

        if(Role.REG_BUYER.equalsTo(role) && comment.getParentId() == 0){
            //kupac je i zeli da postavi prvi komentar (a ne odgovor)
            postedSuccess = propertyService.postComment(comment, propertyId, userId, true);
        }

        else if (Role.REG_SELLER.equalsTo(role) && comment.getParentId() != 0){
            postedSuccess = propertyService.postComment(comment, propertyId, userId, false);
        }


        if(postedSuccess) httpStatus = HttpStatus.CREATED;
        else httpStatus = HttpStatus.CONFLICT;
        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
    }

    @DeleteMapping("{propertyId}/comments/{id}")
    public ResponseEntity<?> deleteComment(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt,
                                           @PathVariable("id")
                                           @Min(1)
                                           @Max(Integer.MAX_VALUE) int id)
    {
        ResultPair resultPair = checkAccess(jwt, Role.ADMIN);
        HttpStatus httpStatus = resultPair.getHttpStatus();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        if(httpStatus != HttpStatus.OK){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        boolean deletedSuccess = propertyService.deleteComment(id);

        if(deletedSuccess) httpStatus = HttpStatus.NO_CONTENT;
        else httpStatus = HttpStatus.CONFLICT;

        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
    }


    @GetMapping("{propertyId}/likes")
    public ResponseEntity<LikeResponse> getPropertyLikes(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt,
                                                         @PathVariable("propertyId")
                                                         @Min(1)
                                                         @Max(Integer.MAX_VALUE) int propertyId)
    {
        ResultPair resultPair = checkAccess(jwt, Role.ADMIN, Role.VISITOR, Role.REG_BUYER, Role.REG_SELLER);
        HttpStatus httpStatus = resultPair.getHttpStatus();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        if(httpStatus != HttpStatus.OK){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        int buyerId = 0;
        boolean isApplicant= false;

        if(resultPair.getClaims() != null){
            //prijavljen je
            buyerId = (int) (double) resultPair.getClaims().get(USER_ID_CLAIM_NAME);
            String role = (String) resultPair.getClaims().get(ROLE_CLAIM_NAME);

            if(!Role.VISITOR.equalsTo(role)) isApplicant = true;
        }

        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(propertyService.getPropertyLikes(propertyId, buyerId, isApplicant));
    }

    @PostMapping("{propertyId}/likes")
    public ResponseEntity<?> likeProperty(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt,
                                          @PathVariable("propertyId")
                                          @Min(1)
                                          @Max(Integer.MAX_VALUE) int propertyId)
    {
        ResultPair resultPair = checkAccess(jwt,Role.REG_BUYER, Role.REG_SELLER, Role.ADMIN);
        HttpStatus httpStatus = resultPair.getHttpStatus();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        if(httpStatus != HttpStatus.OK){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        int buyerId = (int) (double) resultPair.getClaims().get(USER_ID_CLAIM_NAME);
        boolean likeSuccess = propertyService.likeProperty(propertyId, buyerId);

        if(likeSuccess) httpStatus = HttpStatus.CREATED;
        else httpStatus = HttpStatus.CONFLICT;

        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);

    }

    @DeleteMapping("{propertyId}/likes")
    public ResponseEntity<?> recallLike(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt,
                                        @PathVariable("propertyId")
                                        @Min(1)
                                        @Max(Integer.MAX_VALUE) int propertyId)
    {
        ResultPair resultPair = checkAccess(jwt, Role.REG_BUYER, Role.REG_SELLER, Role.ADMIN);
        HttpStatus httpStatus = resultPair.getHttpStatus();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        if(httpStatus != HttpStatus.OK){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        int buyerId = (int) (double) resultPair.getClaims().get(USER_ID_CLAIM_NAME);
        boolean recallLikeSuccess = propertyService.recallLike(propertyId, buyerId);

        if(recallLikeSuccess) httpStatus = HttpStatus.NO_CONTENT;
        else httpStatus = HttpStatus.CONFLICT;

        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);

    }





}
