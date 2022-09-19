package com.example.skucise.repositories;

import com.example.skucise.models.*;
import com.example.skucise.repositories.interfaces.IBuyerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BuyerRepository implements IBuyerRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuyerRepository.class);

    //storne procedure
    private static final String REGISTER_BUYER_PROCEDURE_CALL = "{call register_buyer(?,?,?,?,?,?,?,?)}";
    private static final String CHECK_IF_APPROVED_STORED_PROCEDURE = "{call check_if_approved(?)}";
    private static final String GET_BUYER_STORED_PROCEDURE = "{call get_buyer(?)}";
    private static final String RESERVATION_STORED_PROCEDURE = "{call check_reservation(?,?)}";
    private static final String GET_ALL_BUYERS_STORED_PROCEDURE = "{call get_all_buyers(?)}";
    private static final String APPROVE_STORED_PROCEDURE = "{call approve_user(?,?)}";
    private static final String DELETE_STORED_PROCEDURE = "{call delete_user(?,?)}";
    private static final String PROPERTIES_BUYER_APPLIED_STORED_PROCEDURE = "{call get_properties_buyer_applied_on(?)}";
    private static final String TAG_STORED_PROCEDURE = "{call get_tags_for_a_property(?)}";

    @Value("jdbc:mariadb://localhost:3306/skucise")
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
        boolean successfullyCreatedUser = false;

        LOGGER.info("EMAIL OF USER "+buyerUser.getEmail());

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl,databaseUsername,databasePassword);
        CallableStatement stmt = conn.prepareCall(REGISTER_BUYER_PROCEDURE_CALL)){

            stmt.setString("p_email",buyerUser.getEmail());
            stmt.setString("p_hashed_password",buyerUser.getHashedPassword());
            stmt.setString("p_first_name",buyerUser.getFirstName());
            stmt.setString("p_last_name",buyerUser.getLastName());
            stmt.setString("p_picture",buyerUser.getPicture());
            stmt.setString("p_phone_number",buyerUser.getPhoneNumber());

            stmt.registerOutParameter("p_is_added", Types.BOOLEAN);
            stmt.registerOutParameter("p_already_exists", Types.BOOLEAN);

            stmt.execute();

            successfullyCreatedUser = stmt.getBoolean("p_is_added");
            boolean alreadyExists = stmt.getBoolean("p_already_exists");

            if(!successfullyCreatedUser && !alreadyExists){
                throw new Exception("Creating buyerUser failed!");
            }
            LOGGER.info("Buyer user registered");


        }catch (Exception e){
            LOGGER.error("Error while trying to communicate with the database - create");
            LOGGER.error("{}",e.getMessage());
            e.printStackTrace();
        }

        return successfullyCreatedUser;
    }

    @Override
    public BuyerUser get(Integer id) {
        BuyerUser buyerUser = null;

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl,databaseUsername,databasePassword);
            CallableStatement stmtApproved = conn.prepareCall(CHECK_IF_APPROVED_STORED_PROCEDURE);
            CallableStatement stmtBuyer = conn.prepareCall(GET_BUYER_STORED_PROCEDURE)){

            //immamo dve procedure
            ResultSet resultSet;
            boolean approved;

            stmtApproved.setInt("p_id",id);
            resultSet = stmtApproved.executeQuery();

            if(resultSet.first()){
                //uspesno odradjena procedura
                approved = resultSet.getBoolean("approved");

                if(approved){
                    //samo tada se prikazuje
                    stmtBuyer.setInt("p_id",id);
                    resultSet = stmtBuyer.executeQuery();

                    if (resultSet.first()){
                        buyerUser = createNewBuyer(resultSet);
                    }
                    else buyerUser = null;
                }
            }

        }catch (SQLException e){
            LOGGER.error("Error while trying to communicate with the database - get");
            e.printStackTrace();
        }

        return  buyerUser;
    }

    @Override
    public boolean update(BuyerUser buyerUser, Integer integer) {
        return false;
    }

    @Override
    public boolean delete(Integer id) {

        boolean deletedSuccess  = false;

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
        CallableStatement stmt = conn.prepareCall(DELETE_STORED_PROCEDURE)){

            stmt.setInt("p_id", id);
            stmt.registerOutParameter("p_deleted_successfully", Types.BOOLEAN);

            stmt.executeUpdate();

            deletedSuccess = stmt.getBoolean("p_deleted_successfully");

        }
        catch (SQLException e){
            LOGGER.error("Error while trying to communicate with the database - delete");
            e.printStackTrace();
        }

        return deletedSuccess;

    }

    @Override
    public boolean approve(int id) {

        boolean approvalSuccess = false;

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
            CallableStatement stmt = conn.prepareCall(APPROVE_STORED_PROCEDURE)){

            stmt.setInt("p_id",id);
            stmt.registerOutParameter("p_approved_successfully", Types.BOOLEAN);

            stmt.executeUpdate();

            approvalSuccess = stmt.getBoolean("p_approved_successfully");


        }catch(SQLException e){
            LOGGER.error("Error while trying to communicate with the database - approve");
            e.printStackTrace();
        }

        return approvalSuccess;

    }

    @Override
    public List<Property> getAppliedProperties(int id) {

        List<Property> properties = null;

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
        CallableStatement stmt = conn.prepareCall(PROPERTIES_BUYER_APPLIED_STORED_PROCEDURE);
        CallableStatement stmtTag = conn.prepareCall(TAG_STORED_PROCEDURE)){

            stmt.setInt("p_id", id);
            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.first()){
                properties = new ArrayList<>();

                resultSet.beforeFirst();
                while(resultSet.next()){
                    Property property;
                    property = setProperty(resultSet, stmtTag);

                    properties.add(property);
                }
            }
        }catch (SQLException e){
            LOGGER.error("Error while trying to communicate with the database - getAppliedProperties");
            e.printStackTrace();
        }

        return properties;
    }

    public boolean isApplied(int sellerId, int buyerId){
        boolean isApplied = false;

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
        CallableStatement stmt = conn.prepareCall(RESERVATION_STORED_PROCEDURE)){

            stmt.setInt("p_seller_id", sellerId);
            stmt.setInt("p_buyer_id", buyerId);

            ResultSet resultSet = stmt.executeQuery();

            resultSet.first();
            //ako je broj pronadjenih nekretnina veci od nule
            if(resultSet.getInt("count") != 0){
                isApplied = true;
            }

        }catch(SQLException e){
            LOGGER.error("Error while trying to communicate with the database - isApplied");
            e.printStackTrace();
        }

        return isApplied;
    }

    public Property setProperty(ResultSet resultSet,  CallableStatement stmt) throws SQLException{
        Property property = new Property();

        SellerUser seller = new SellerUser();
        seller.setId(resultSet.getInt("seller_id"));
        seller.setFirstName(resultSet.getString("seller_first_name"));
        seller.setLastName(resultSet.getString("seller_last_name"));
        seller.setTin(resultSet.getString("tin"));
        seller.setPicture(resultSet.getString("picture"));
        seller.setPhoneNumber(resultSet.getString("phone_number"));
        seller.setEmail(resultSet.getString("email"));

        City city = new City();
        city.setId(resultSet.getInt("c_id"));
        city.setName(resultSet.getString("c_name"));

        AdCategory adCategory = new AdCategory();
        adCategory.setId(resultSet.getInt("ad_id"));
        adCategory.setName(resultSet.getString("ad_name"));

        Type type = new Type();
        type.setId(resultSet.getInt("t_id"));
        type.setName(resultSet.getString("t_name"));

        property.setId(resultSet.getInt("id"));
        property.setDescription(resultSet.getString("description"));
        property.setPostingDate(resultSet.getObject("post_date", LocalDateTime.class));
        property.setPrice(resultSet.getString("price"));
        property.setArea(resultSet.getString("area"));
        property.setNewConstruction(resultSet.getBoolean("new_construction"));
        property.setAdCategory(adCategory);
        property.setCity(city);
        property.setType(type);
        property.setSellerUser(seller);


        stmt.setInt("p_property_id",property.getId());
        ResultSet rsTag = stmt.executeQuery();
        List<Tag> tags = new ArrayList<>();
        if(rsTag != null){
            //ima tagova za property
            rsTag.beforeFirst();
            Tag tag;
            while (rsTag.next()){
                tag = new Tag();
                tag.setId(resultSet.getInt("id"));
                tag.setName(resultSet.getString("name"));
                tag.setPropertyTypeId(resultSet.getInt("property_type_id"));

                tags.add(tag);
            }

            property.setTags(tags);
        }


        return property;
    }
}
