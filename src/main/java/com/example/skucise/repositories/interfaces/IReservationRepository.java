package com.example.skucise.repositories.interfaces;

import com.example.skucise.models.Reservation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReservationRepository extends CRUDRepository<Reservation,Integer> {

    public boolean postReservation(Reservation reservation);

    List<Reservation> getReservationsByUser(int user_id);
}
