package com.example.skucise.repositories.interfaces;

import com.example.skucise.models.Reservation;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public interface IReservationRepository extends CRUDRepository<Reservation,Integer> {

    public boolean postReservation(Reservation reservation, int user_id);

    List<Reservation> getReservationsByUser(int user_id);

    List<Reservation> getReservationsForUser(int user_id, boolean is_new, boolean is_accepted);
    public List<Reservation> getReservationsForProperty(int property_id);

    public Reservation setNewReservation(ResultSet resultSet, boolean getUser) throws SQLException;

    public void setBuyerToReservation(Reservation reservation, int user_id) throws SQLException;

    public boolean approveReservation(int user_id, int r_id, boolean approved);
}
