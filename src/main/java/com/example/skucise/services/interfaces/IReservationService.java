package com.example.skucise.services.interfaces;

import com.example.skucise.models.Reservation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IReservationService {

    public boolean postReservation(Reservation reservation, int user_id);

    List<Reservation> getReservationsByUser(int user_id);

    List<Reservation> getReservationsForUser(int user_id);

    public boolean approveReservation(int user_id, int r_id, boolean approved);

    public Reservation get(Integer id);
}
