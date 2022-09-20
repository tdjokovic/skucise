package com.example.skucise.repositories;

import com.example.skucise.models.*;
import com.example.skucise.repositories.interfaces.IReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReservationRepository implements IReservationRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationRepository.class);
    private static final String GET_RESERVATION_STORED_PROCEDURE = "{call get_reservation(?)}";
    private static final String POST_RESERVATION_STORED_PROCEDURE = "{call post_reservation(?,?,?,?,?)}";
    private static final String GET_RESERVATIONS_STORED_PROCEDURE = "{call get_reservations_by_user(?)}";
    private static final String GET_RESERVATIONS_FOR_USER_STORED_PROCEDURE = "{call get_reservations_for_user(?)}";

    private static final String APPROVE_RESERVATION_STORED_PROCEDURE = "{call approve_reservation(?,?,?)}";

    @Value("jdbc:mariadb://localhost:3306/skucise")
    private String databaseSourceUrl;

    @Value("root")
    private String databaseUsername;

    @Value("")
    private String databasePassword;

    private final UserRepository userRepository;

    @Autowired
    public ReservationRepository(UserRepository userRepository){
        this.userRepository = userRepository;
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
                reservation = setNewReservation(resultSet, false);
                LOGGER.info("Reservaton id {}", reservation.getId());

                reservations.add(reservation);
            }
        }catch (SQLException e){
            LOGGER.error("Error while trying to communicate with the database - get");
            e.printStackTrace();
        }

        return reservations;
    }

    @Override
    public List<Reservation> getReservationsForUser(int user_id) {
        List<Reservation> reservations = new ArrayList<Reservation>();

        Reservation reservation = null;

        LOGGER.info("Trying to find reservations for user with id {}", user_id);

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
            CallableStatement stmt = conn.prepareCall(GET_RESERVATIONS_FOR_USER_STORED_PROCEDURE)){

            stmt.setInt("r_user_id", user_id);
            ResultSet resultSet = stmt.executeQuery();

            while(resultSet.next()){
                reservation = setNewReservation(resultSet, true);
                LOGGER.info("Reservaton id {}", reservation.getId());

                reservations.add(reservation);
            }
        }catch (SQLException e){
            LOGGER.error("Error while trying to communicate with the database - get");
            e.printStackTrace();
        }

        return reservations;
    }
    @Override
    public List<Reservation> getReservationsForProperty(int property_id) {
        List<Reservation> reservations = new ArrayList<Reservation>();



        return reservations;
    }
    @Override
    public Reservation setNewReservation(ResultSet resultSet, boolean getUser) throws SQLException {
        Reservation reservation = new Reservation();

        reservation.setId(resultSet.getInt("id"));
        reservation.setDate(resultSet.getObject("reservation_date", LocalDateTime.class));
        reservation.setBuyer(null);
        reservation.setIsApproved(resultSet.getInt("is_approved"));

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
        property.setPrice(resultSet.getInt("price"));
        property.setArea(resultSet.getString("area"));
        property.setPicture(resultSet.getString("property_picture"));
        property.setCity(city);
        property.setAdCategory(adCategory);
        property.setType(type);
        property.setSellerUser(null);

        reservation.setProperty(property);

        if (getUser == true)
        {
            int user_id = resultSet.getInt("user_id");
            setBuyerToReservation(reservation,user_id);
        }
        else {
            reservation.setBuyer(null);
        }

        return reservation;
    }

    @Override
    public void setBuyerToReservation(Reservation reservation, int user_id) throws SQLException
    {

        BuyerUser buyer = this.userRepository.getAsBuyer(user_id);

        reservation.setBuyer(buyer);

    }

    @Override
    public boolean approveReservation(int user_id, int r_id, boolean approved) {

        boolean approvalSuccess = false;

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
            CallableStatement stmt = conn.prepareCall(APPROVE_RESERVATION_STORED_PROCEDURE)){

            stmt.setInt("r_id",r_id);
            if(approved == true)
            {
                stmt.setInt("r_approved",1);
            }
            else{
                stmt.setInt("r_approved",-1);
            }
            stmt.registerOutParameter("r_approved_successfully", Types.BOOLEAN);

            stmt.executeUpdate();

            approvalSuccess = stmt.getBoolean("p_approved_successfully");


        }catch(SQLException e){
            LOGGER.error("Error while trying to communicate with the database - approve");
            e.printStackTrace();
        }

        return approvalSuccess;
    }

    public void setReservationParameters(CallableStatement stmt, Reservation reservation, int user_id) throws SQLException{

        stmt.setInt("r_user_id", user_id);
        stmt.setInt("r_property_id", reservation.getProperty().getId());
        stmt.setTimestamp("r_date", java.sql.Timestamp.valueOf(reservation.getDate()));
        stmt.setInt("r_is_approved", reservation.getIsApproved());
    }

    @Override
    public List<Reservation> getAll() {
        return null;
    }

    @Override
    public boolean create(Reservation reservation) {
        return false;
    }

    @Override
    public Reservation get(Integer id)
    {
        Reservation reservation = null;

        LOGGER.info("Trying to find reservations with id {}", id);

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
            CallableStatement stmt = conn.prepareCall(GET_RESERVATION_STORED_PROCEDURE)){

            stmt.setInt("r_id", id);
            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.first()){
                reservation = setNewReservation(resultSet, true);
                LOGGER.info("Reservaton id {}", reservation.getId());
            }
        }catch (SQLException e){
            LOGGER.error("Error while trying to communicate with the database - get");
            e.printStackTrace();
        }

        return reservation;
    }

    @Override
    public boolean update(Reservation reservation, Integer integer) {
        return false;
    }

    @Override
    public boolean delete(Integer integer) {
        return false;
    }
}

