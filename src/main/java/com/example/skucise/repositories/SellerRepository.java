package com.example.skucise.repositories;

import com.example.skucise.models.*;
import com.example.skucise.repositories.interfaces.ISellerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SellerRepository implements ISellerRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(SellerRepository.class);

    private static final String REGISTER_SELLER_PROCEDURE_CALL = "{call register_seller(?,?,?,?,?,?,?,?,?)}";
    private static final String GET_ALL_SELLERS_PROCEDURE_CALL = "{call get_all_sellers(?)}";
    private static final String GET_SELLER_PROCEDURE_CALL = "{call get_seller(?)}";
    private static final String APPROVE_SELLER_PROCEDURE_CALL = "{call approve_user(?,?)}";
    private static final String DELETE_SELLER_PROCEDURE_CALL = "{call delete_user(?,?)}";
    private static final String GET_FEEDBACK_VALUES_STORED_PROCEDURE = "{call get_feedback_values(?)}";
    private static final String APPLICATION_STORED_PROCEDURE = "{call check_application(?,?)}";
    private static final String RATE_SELLER_STORED_PROCEDURE = "{call rate_seller(?,?,?,?)}";
    private static final String IS_RATED_STORED_PROCEDURE = "{call check_if_rated(?,?)}";

    private static final String SELLER_GET_POSTS_PROCEDURE_CALL = "{call seller_get_posts_without_tags(?)}";
    private static final String GET_TAGS_A_PROPERTY_PROCEDURE_CALL = "{call get_tags_for_a_property(?)}";
    private static final String EDIT_SELLER_STORED_PROCEDURE = "{call edit_seller_data(?,?,?,?,?,?)}";

    @Value("jdbc:mariadb://localhost:3306/skucise")
    private String databaseSourceUrl;

    @Value("root")
    private String databaseUsername;

    @Value("")
    private String databasePassword;

    @Override
    public List<SellerUser> getAll() {
        return getAll(false);
    }

    public List<SellerUser> getAll(boolean notApprovedRequested){
        List<SellerUser> sellers = new ArrayList<>();

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
            CallableStatement stmt = conn.prepareCall(GET_ALL_SELLERS_PROCEDURE_CALL)){

            stmt.setBoolean("p_not_approved_request", notApprovedRequested);
            ResultSet resultSet =stmt.executeQuery();

            SellerUser seller = null;
            while(resultSet.next()){
                seller = new SellerUser();
                seller.setId(resultSet.getInt("user_id"));
                seller.setEmail(resultSet.getString("email"));
                seller.setFirstName(resultSet.getString("first_name"));
                seller.setLastName(resultSet.getString("last_name"));
                seller.setPicture(resultSet.getString("picture"));
                seller.setPhoneNumber(resultSet.getString("phone_number"));
                seller.setTin(resultSet.getString("tin"));

                sellers.add(seller);
            }

        }catch (SQLException e){
            LOGGER.error("Error while trying to communicate with the database - getAll");
            e.printStackTrace();
        }

        return sellers;
    }

    @Override
    public boolean create(SellerUser sellerUser) {
        boolean successfullyCreated = false;

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl,databaseUsername,databasePassword);
        CallableStatement stmt = conn.prepareCall(REGISTER_SELLER_PROCEDURE_CALL)){

            stmt.setString("p_email", sellerUser.getEmail());
            stmt.setString("p_hashed_password", sellerUser.getHashedPassword());
            stmt.setString("p_first_name", sellerUser.getFirstName());
            stmt.setString("p_last_name", sellerUser.getLastName());
            stmt.setString("p_picture", sellerUser.getPicture());
            stmt.setString("p_phone_number", sellerUser.getPhoneNumber());
            stmt.setString("p_tin", sellerUser.getTin());

            stmt.registerOutParameter("p_is_added", Types.BOOLEAN);
            stmt.registerOutParameter("p_already_exists", Types.BOOLEAN);

            stmt.execute();

            successfullyCreated = stmt.getBoolean("p_is_added");
            boolean alreadyExists = stmt.getBoolean("p_already_exists");

            if(!successfullyCreated && !alreadyExists){
                throw new Exception("Failed to create new Seller");
            }

        }catch(Exception e){
            LOGGER.error("Error while trying to communicate with the database - create");
            LOGGER.error("{}",e.getMessage());
            e.printStackTrace();
        }

        return successfullyCreated;
    }

    @Override
    public SellerUser get(Integer id) {
        SellerUser seller = null;

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
        CallableStatement stmt = conn.prepareCall(GET_SELLER_PROCEDURE_CALL)){
            stmt.setInt("p_id", id);
            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.first()){
                seller = new SellerUser();
                seller.setId(resultSet.getInt("user_id"));
                seller.setEmail(resultSet.getString("email"));
                seller.setFirstName(resultSet.getString("first_name"));
                seller.setLastName(resultSet.getString("last_name"));
                seller.setPicture(resultSet.getString("picture"));
                seller.setPhoneNumber(resultSet.getString("phone_number"));
                seller.setTin(resultSet.getString("tin"));
            }
        }catch(SQLException e){
            LOGGER.error("Error while trying to communicate with the database - get");
            e.printStackTrace();
        }

        return seller;
    }

    @Override
    public boolean update(SellerUser sellerUser, Integer integer) {
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        boolean deleteSuccess = false;

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl,databaseUsername,databasePassword);
        CallableStatement stmt = conn.prepareCall(DELETE_SELLER_PROCEDURE_CALL)){

            stmt.setInt("p_id", id);
            stmt.registerOutParameter("p_deleted_successfully", Types.BOOLEAN);

            stmt.executeUpdate();

            deleteSuccess = stmt.getBoolean("p_deleted_successfully");

        }catch(SQLException e){
            LOGGER.error("Error while trying to communicate with the database - delete");
            e.printStackTrace();
        }

        return deleteSuccess;
    }

    @Override
    public boolean approve(int id) {
        boolean approvedSuccessfully = false;

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl,databaseUsername,databasePassword);
            CallableStatement stmt = conn.prepareCall(APPROVE_SELLER_PROCEDURE_CALL)){

            stmt.setInt("p_id", id);
            stmt.registerOutParameter("p_approved_successfully", Types.BOOLEAN);

            stmt.executeUpdate();

            approvedSuccessfully = stmt.getBoolean("p_approved_successfully");

        }catch(SQLException e){
            LOGGER.error("Error while trying to communicate with the database - approve");
            e.printStackTrace();
        }

        return approvedSuccessfully;
    }

    @Override
    public List<Property> getPostedProperties(int id) {
        List<Property> properties = null;

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
        CallableStatement stmt = conn.prepareCall(SELLER_GET_POSTS_PROCEDURE_CALL);
        CallableStatement stmtTag = conn.prepareCall(GET_TAGS_A_PROPERTY_PROCEDURE_CALL)){

            stmt.setInt("p_seller_id", id);
            ResultSet resultSet = stmt.executeQuery();

            Property property;
            Tag tag;
            List<Tag> tags ;

            properties = new ArrayList<>();

            while(resultSet.next()){
                property = new Property();
                property.setId(resultSet.getInt("id"));
                property.setNewConstruction(resultSet.getBoolean("new_construction"));
                property.setPrice(resultSet.getInt("price"));
                property.setPostingDate(resultSet.getObject("post_date", LocalDateTime.class));
                property.setArea(resultSet.getString("area"));
                property.setDescription(resultSet.getString("description"));
                property.setPicture(resultSet.getString("property_picture"));

                SellerUser sellerUser = new SellerUser();
                sellerUser.setId(resultSet.getInt("seller_id"));
                sellerUser.setFirstName(resultSet.getString("first_name"));
                sellerUser.setLastName(resultSet.getString("last_name"));
                sellerUser.setPicture(resultSet.getString("picture"));
                sellerUser.setPhoneNumber(resultSet.getString("phone_number"));
                sellerUser.setTin(resultSet.getString("tin"));
                property.setSellerUser(sellerUser);

                Type type = new Type();
                type.setId(resultSet.getInt("type_id"));
                type.setName(resultSet.getString("type_name"));
                property.setType(type);

                City city = new City();
                city.setId(resultSet.getInt("city_id"));
                city.setName(resultSet.getString("city_name"));
                property.setCity(city);

                AdCategory adCategory = new AdCategory();
                adCategory.setId(resultSet.getInt("ad_id"));
                adCategory.setName(resultSet.getString("ad_category_name"));
                property.setAdCategory(adCategory);

                stmtTag.setInt("p_property_id", property.getId());
                ResultSet rs = stmtTag.executeQuery();

                tags = new ArrayList<>();

                while(rs.next()){
                    tag = new Tag();
                    tag.setId(rs.getInt("id"));
                    tag.setPropertyTypeId(rs.getInt("property_type_id"));
                    tag.setName(rs.getString("name"));

                    tags.add(tag);
                }

                property.setTags(tags);

                properties.add(property);
            }

        }catch (SQLException e){
            LOGGER.error("Error while trying to communicate with the database - getPostedProperties");
            e.printStackTrace();
        }

        return properties;
    }

    @Override
    public Rating getRating(int sellerId, int buyerId, boolean isApplicant) {
        Rating rating = null;

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
        CallableStatement stmt = conn.prepareCall(GET_FEEDBACK_VALUES_STORED_PROCEDURE);
        CallableStatement stmtRated = conn.prepareCall(IS_RATED_STORED_PROCEDURE)){

            stmt.setInt("p_id", sellerId);
            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.first()){
                rating = new Rating();
                rating.setRating(-1);

                double sum = 0;
                int counter = 0;

                resultSet.beforeFirst();
                while(resultSet.next()){
                    counter++;
                    sum = sum + resultSet.getByte("feedback_value");
                }

                rating.setRating(sum / counter);
                rating.setAlreadyRated(false);

                //posmatramo da li je vec ocenio prodavca
                if(isApplicant){
                    stmtRated.setInt("p_seller_id", sellerId);
                    stmtRated.setInt("p_buyer_id", buyerId);

                    resultSet = stmtRated.executeQuery();
                    resultSet.first();

                    int count = resultSet.getInt("count");
                    if(count != 0) rating.setAlreadyRated(true);
                }
            }
        }catch (SQLException e){
            LOGGER.error("Error while trying to communicate with the database - getRating");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean rate(int sellerId, int buyerId, byte feedback) {
        boolean successfullyRated = false;

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
        CallableStatement stmt = conn.prepareCall(RATE_SELLER_STORED_PROCEDURE)){

            stmt.setInt("p_seller_id", sellerId);
            stmt.setInt("p_buyer_id", buyerId);
            stmt.setByte("p_feedback_value", feedback);
            stmt.registerOutParameter("p_is_rated", Types.BOOLEAN);

            stmt.executeUpdate();

            successfullyRated = stmt.getBoolean("p_is_rated");
        }catch (SQLException e){
            LOGGER.error("Error while trying to communicate with the database - rate");
            e.printStackTrace();
        }

        return successfullyRated;
    }

    //da li se prijavio za neki property tog sellera
    public boolean isApplied(int sellerId, int buyerId){
        boolean isApplied = false;

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
        CallableStatement stmt = conn.prepareCall(APPLICATION_STORED_PROCEDURE)){
            stmt.setInt("p_seller_id", sellerId);
            stmt.setInt("p_buyer_id", buyerId);

            ResultSet resultSet = stmt.executeQuery();

            resultSet.first();
            if(resultSet.getInt("count") != 0) isApplied = true;

        }catch (SQLException e){
            LOGGER.error("Error while trying to communicate with the database - isApplied");
            e.printStackTrace();
        }

        return isApplied;
    }

    @Override
    public BuyerUser getAsBuyer(Integer id) {
        BuyerUser buyer = null;

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
            CallableStatement stmt = conn.prepareCall(GET_SELLER_PROCEDURE_CALL)){
            stmt.setInt("p_id", id);
            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.first()){
                buyer = new BuyerUser();
                buyer.setId(resultSet.getInt("user_id"));
                buyer.setEmail(resultSet.getString("email"));
                buyer.setFirstName(resultSet.getString("first_name"));
                buyer.setLastName(resultSet.getString("last_name"));
                buyer.setPicture(resultSet.getString("picture"));
                buyer.setPhoneNumber(resultSet.getString("phone_number"));
            }
        }catch(SQLException e){
            LOGGER.error("Error while trying to communicate with the database - get");
            e.printStackTrace();
        }

        return buyer;
    }

    @Override
    public boolean editData(int id, NewUserData data) {
        boolean isEdited = false;

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
            CallableStatement stmt = conn.prepareCall(EDIT_SELLER_STORED_PROCEDURE)){

            stmt.setInt("p_id",id);
            stmt.setString("p_first_name", data.getFirstName());
            stmt.setString("p_last_name", data.getLastName());
            stmt.setString("p_email", data.getEmail());
            stmt.setString("p_phone_number", data.getPhoneNumber());
            stmt.registerOutParameter("p_edited_successfully", Types.BOOLEAN);

            stmt.executeUpdate();

            isEdited = stmt.getBoolean("p_edited_successfully");


        }catch(SQLException e){
            LOGGER.error("Error while trying to communicate with the database - editData");
            e.printStackTrace();
        }

        return isEdited;
    }
}
