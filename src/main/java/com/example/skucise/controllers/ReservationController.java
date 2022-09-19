package com.example.skucise.controllers;

import com.example.skucise.models.Reservation;
import com.example.skucise.repositories.PropertyRepository;
import com.example.skucise.security.ResultPair;
import com.example.skucise.security.Role;
import com.example.skucise.services.ReservationService;
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
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }
    @PostMapping
    public ResponseEntity<?> postReservation(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt,
                                          @Valid @RequestBody Reservation reservation) {

        ResultPair resultPair = checkAccess(jwt, Role.REG_SELLER, Role.ADMIN, Role.REG_BUYER);
        int user_id =(int)(double) resultPair.getClaims().get(USER_ID_CLAIM_NAME);

        HttpStatus httpStatus = resultPair.getHttpStatus();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        if(httpStatus != HttpStatus.OK){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        boolean posted = reservationService.postReservation(reservation, user_id);

        if(posted){
            return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders).body(null);
        }
        else{
            return ResponseEntity.status(HttpStatus.CONFLICT).headers(responseHeaders).body(null);
        }

    }
    @GetMapping
    public ResponseEntity<List<Reservation>> getReservationsByUser(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt)
    {
        List<Reservation> reservationsByUser = null;

        ResultPair resultPair = checkAccess(jwt, Role.REG_SELLER, Role.ADMIN, Role.REG_BUYER);
        HttpStatus httpStatus = resultPair.getHttpStatus();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        if(httpStatus != HttpStatus.OK){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        int user_id =(int)(double) resultPair.getClaims().get(USER_ID_CLAIM_NAME);


        reservationsByUser = reservationService.getReservationsByUser(user_id);


        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(reservationsByUser);
    }

    @GetMapping("{id}")
    public ResponseEntity<List<Reservation>> getReservationsForUser(@RequestHeader(JWT_CUSTOM_HTTP_HEADER) String jwt,
                                                                    @PathVariable("id")
                                                                    @Min( 1 )
                                                                    @Max( Integer.MAX_VALUE ) int id)
    {
        List<Reservation> reservationsForUser = null;

        ResultPair resultPair = checkAccess(jwt, Role.REG_SELLER, Role.ADMIN, Role.REG_BUYER);

        int userId =(int)(double) resultPair.getClaims().get(USER_ID_CLAIM_NAME);

        HttpStatus httpStatus = resultPair.getHttpStatus();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(JWT_CUSTOM_HTTP_HEADER, jwt);

        if(userId != id){
            httpStatus = HttpStatus.UNAUTHORIZED;
            LOGGER.warn("Trying to get someone elses reservations!");
        }


        if(httpStatus != HttpStatus.OK){
            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(null);
        }

        reservationsForUser = reservationService.getReservationsForUser(id);


        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(reservationsForUser);
    }

}
