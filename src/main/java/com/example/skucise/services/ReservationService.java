package com.example.skucise.services;

import com.example.skucise.models.Reservation;
import com.example.skucise.repositories.ReservationRepository;
import com.example.skucise.services.interfaces.IReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService implements IReservationService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository)
    {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public boolean postReservation(Reservation reservation, int user_id)
    {
        return reservationRepository.postReservation(reservation, user_id);
    }

    @Override
    public List<Reservation> getReservationsByUser(int user_id) {
        return reservationRepository.getReservationsByUser(user_id);
    }
}