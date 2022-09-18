package com.example.skucise.repositories;

import com.example.skucise.models.*;
import com.example.skucise.repositories.interfaces.IReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReservationRepository implements IReservationRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyRepository.class);
    private static final String POST_RESERVATION_STORED_PROCEDURE = "{call post_reservation(?,?,?,?,?)}";
    private static final String GET_RESERVATIONS_STORED_PROCEDURE = "{call get_reservations_by_user(?)}";

    @Value("jdbc:mariadb://localhost:3306/skucise")
    private String databaseSourceUrl;

    @Value("root")
    private String databaseUsername;

    @Value("")
    private String databasePassword;

    @Override
    public List<Reservation> getAll() {
        return null;
    }

    @Override
    public boolean create(Reservation reservation) {
        return false;
    }

    @Override
    public Reservation get(Integer integer) {
        return null;
    }

    @Override
    public boolean update(Reservation reservation, Integer integer) {
        return false;
    }

    @Override
    public boolean delete(Integer integer) {
        return false;
    }

    @Override
    public boolean postReservation(Reservation reservation, int user_id) {

        boolean createSuccess = false;
        int id;

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
            CallableStatement stmt = conn.prepareCall(POST_RESERVATION_STORED_PROCEDURE)){

            setReservationParameters(stmt, reservation, user_id);
            stmt.registerOutParameter("r_is_posted", Types.BOOLEAN);
            stmt.executeUpdate();

            createSuccess = stmt.getBoolean("r_is_posted");


        }catch (SQLException e){
            delete(reservation.getId());
            createSuccess = false;
            LOGGER.error("Error while trying to communicate with the database - create");
            e.printStackTrace();
        }

        return createSuccess;
    }

    @Override
    public List<Reservation> getReservationsByUser(int user_id) {
        List<Reservation> reservations = new ArrayList<Reservation>();
        Reservation reservation = null;

        LOGGER.info("Trying to find reservations by user with id {}", user_id);

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
            CallableStatement stmt = conn.prepareCall(GET_RESERVATIONS_STORED_PROCEDURE)){

            stmt.setInt("r_user_id", user_id);
            ResultSet resultSet = stmt.executeQuery();

            while(resultSet.next()){
                reservation = setNewReservation(resultSet);
                LOGGER.info("Reservaton id {}", reservation.getId());

                reservations.add(reservation);
            }
        }catch (SQLException e){
            LOGGER.error("Error while trying to communicate with the database - get");
            e.printStackTrace();
        }

        return reservations;
    }

    private Reservation setNewReservation(ResultSet resultSet) throws SQLException {
        Reservation reservation = new Reservation();

        reservation.setId(resultSet.getInt("id"));
        reservation.setUser(null);
        reservation.setDate(resultSet.getObject("reservation_date", LocalDateTime.class));
        reservation.setApproved(resultSet.getBoolean("is_approved"));

        AdCategory adCategory = new AdCategory();
        adCategory.setId(resultSet.getInt("ad_category_id"));
        adCategory.setName(resultSet.getString("ad_category_name"));

        Type type = new Type();
        type.setId(resultSet.getInt("type_id"));
        type.setName(resultSet.getString("type_name"));

        City city = new City();
        city.setId(resultSet.getInt("city_id"));
        city.setName(resultSet.getString("city_name"));

        Property property = new Property();
        property.setId(resultSet.getInt("property_id"));
        property.setPostingDate(resultSet.getObject("post_date", LocalDateTime.class));
        property.setPrice(resultSet.getString("price"));
        property.setArea(resultSet.getString("area"));
        property.setPicture(resultSet.getString("property_picture"));
        property.setCity(city);
        property.setAdCategory(adCategory);
        property.setType(type);
        property.setSellerUser(null);

        reservation.setProperty(property);

        return reservation;
    }

    public void setReservationParameters(CallableStatement stmt, Reservation reservation, int user_id) throws SQLException{

        stmt.setInt("r_user_id", user_id);
        stmt.setInt("r_property_id", reservation.getProperty().getId());
        stmt.setString("r_date",reservation.getDate().toString());
        stmt.setBoolean("r_is_approved", reservation.isApproved());
    }
}
