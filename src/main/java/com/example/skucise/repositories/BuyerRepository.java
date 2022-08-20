package com.example.skucise.repositories;

import com.example.skucise.models.BuyerUser;
import com.example.skucise.models.Property;
import com.example.skucise.repositories.interfaces.IBuyerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BuyerRepository implements IBuyerRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuyerRepository.class);

    //storne procedure
    private static final String REGISTER_BUYER_PROCEDURE_CALL = "{call register_buyer(?,?,?,?,?,?,?,?)}";
    private static final String CHECK_IF_APPROVED_STORED_PROCEDURE = "{call check_if_approved(?)}";  //////?
    private static final String GET_BUYER_STORED_PROCEDURE = "{call get_buyer(?)}";
    private static final String APPLICATION_STORED_PROCEDURE = "{call check_application(?,?)}"; ///////////////////////////???
    private static final String GET_ALL_BUYERS_STORED_PROCEDURE = "{call get_all_buyers(?)}";
    private static final String APPROVE_STORED_PROCEDURE = "{call approve_user(?,?)}";
    private static final String DELETE_STORED_PROCEDURE = "{call delete_user(?,?)}";
    private static final String PROPERTIES_BUYER_APPLIED_STORED_PROCEDURE = "{call get_properties_buyer_applied_on(?)}";
    private static final String TAG_STORED_PROCEDURE = "{call get_tags_for_a_property(?)}"; //////////////////????


    @Value("jdbc:mariadb://localhost:3307/skucise")
    private String databaseSourceUrl;

    @Value("root")
    private String databaseUsername;

    @Value("")
    private String databasePassword;

    @Override
    public List<BuyerUser> getAll() {
        return null;
    }

    @Override
    public List<BuyerUser> getAll(boolean approved) {

        List<BuyerUser> buyers = null;

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
            CallableStatement stmt = conn.prepareCall(GET_ALL_BUYERS_STORED_PROCEDURE)){

            stmt.setBoolean("p_approved",approved);

            ResultSet resultSet = stmt.executeQuery();

            if(resultSet != null){
                buyers = new ArrayList<>();

                while(resultSet.next()){
                    BuyerUser buyer = createNewBuyer(resultSet);
                    buyers.add(buyer);
                }
            }

        }catch (SQLException e){
            LOGGER.error("Error while trying to communicate with the database - getAll");
            e.printStackTrace();
        }

        return buyers;
    }

    public BuyerUser createNewBuyer(ResultSet resultSet) throws SQLException{
        BuyerUser buyer = new BuyerUser();
        buyer.setId(resultSet.getInt("user_id"));
        buyer.setEmail(resultSet.getString("email"));
        buyer.setFirstName(resultSet.getString("first_name"));
        buyer.setLastName(resultSet.getString("last_name"));
        buyer.setPicture(resultSet.getString("picture"));
        buyer.setPhoneNumber(resultSet.getString("phone_number"));

        return buyer;
    }

    @Override
    public boolean create(BuyerUser buyerUser) {
        return false;
    }

    @Override
    public BuyerUser get(Integer integer) {
        return null;
    }

    @Override
    public boolean update(BuyerUser buyerUser, Integer integer) {
        return false;
    }

    @Override
    public boolean delete(Integer integer) {
        return false;
    }



    @Override
    public boolean approve(int id) {
        return false;
    }

    @Override
    public List<Property> getAppliedProperties(int id) {
        return null;
    }
}
