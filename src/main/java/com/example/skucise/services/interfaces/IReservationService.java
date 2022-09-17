package com.example.skucise.services.interfaces;

import com.example.skucise.models.Reservation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IReservationService {

    public boolean postReservation(Reservation reservation);

    List<Reservation> getReservationsByUser(int user_id);
}
