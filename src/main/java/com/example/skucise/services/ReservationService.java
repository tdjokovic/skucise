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

    @Override
    public List<Reservation>getReservationsForUser(int user_id, boolean is_new, boolean is_accepted){
        return reservationRepository.getReservationsForUser(user_id, is_new, is_accepted);
    }

    @Override
    public boolean approveReservation(int user_id, int r_id, boolean approved) {
        return reservationRepository.approveReservation(user_id,r_id,approved);
    }

    @Override
    public Reservation get(Integer id) {
        return reservationRepository.get(id);
    }
}
