package com.example.skucise.repositories;

import com.example.skucise.models.*;
import com.example.skucise.repositories.interfaces.IPropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;


import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PropertyRepository implements IPropertyRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyRepository.class);

    private static final String GET_FILTERED_PROPERTIES_STORED_PROCEDURE = "{call get_filtered_properties(?,?,?,?,?)}";
    private static final String TAG_STORED_PROCEDURE = "{call get_tags_for_a_property(?)}";
    private static final String POST_PROPERTY_STORED_PROCEDURE = "{call post_property(?,?,?,?,?,?,?,?)}";
    private static final String INSERT_TAG_STORED_PROCEDURE = "{call insert_tag(?,?,?)}";
    private static final String GET_PROPERTY_APPLICANTS_PROCEDURE_CALL = "{call get_property_applicants(?)}";
    private static final String PROPERTY_APPLY_PROCEDURE_CALL = "{call apply_for_a_property(?,?,?)}";
    private static final String GET_PROPERTY_STORED_PROCEDURE = "{call get_property(?)}";
    private static final String DELETE_PROPERTY_STORED_PROCEDURE = "{call delete_property(?,?)}";
    private static final String GET_ALL_COMMENTS_STORED_PROCEDURE = "{call get_all_comments(?)}";
    private static final String POST_COMMENT_STORED_PROCEDURE = "{call post_comment(?,?,?,?,?)}";
    private static final String CHECK_IF_SELLERS_PROPERTY_STORED_PROCEDURE = "{call check_if_sellers_property(?,?)}";
    private static final String DELETE_COMMENT_STORED_PROCEDURE = "{call delete_comment(?,?)}";
    private static final String COUNT_LIKES_STORED_PROCEDURE = "{call count_likes(?)}";
    private static final String CHECK_IF_ALREADY_LIKED_STORED_PROCEDURE = "{call check_if_already_liked(?,?)}";
    private static final String LIKE_PROPERTY_STORED_PROCEDURE = "{call like_property(?,?,?)}";
    private static final String RECALL_LIKE_STORED_PROCEDURE = "{call recall_like(?,?,?)}";


    @Value("jdbc:mariadb://localhost:3307/skucise")
    private String databaseSourceUrl;

    @Value("root")
    private String databaseUsername;

    @Value("")
    private String databasePassword;

    @Override
    public List<Property> getAll() {
        return null;
    }

    @Override
    public boolean create(Property property) {
        boolean createSuccess = false;
        int id;

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
        CallableStatement stmt = conn.prepareCall(POST_PROPERTY_STORED_PROCEDURE);
        CallableStatement stmtTag = conn.prepareCall(INSERT_TAG_STORED_PROCEDURE)){

            setPropertyParameters(stmt, property);
            stmt.registerOutParameter("p_is_posted", Types.BOOLEAN);
            ResultSet resultSet = stmt.executeQuery();

            createSuccess = resultSet.getBoolean("p_is_posted");

            if(createSuccess && property.getTags() != null){ //objavljen je post, i imamo spisak tagova koje treba dodati

                boolean inserted;

                resultSet.first();
                id = resultSet.getInt("id"); // id propertyja
                stmtTag.setInt("p_property_id", id);
                stmtTag.registerOutParameter("p_is_added", Types.BOOLEAN);

                for(Tag t : property.getTags()){
                    //dodajemo svaki tag za property
                    stmtTag.setInt("p_tag_id", t.getId());
                    stmtTag.executeQuery();

                    inserted = stmtTag.getBoolean("p_is_added");

                    if(!inserted){
                        //doslo je do greske ubacivanja taga, brise se sve!
                        delete(id); //brise se nekretnina
                        createSuccess = false;
                        break;
                    }
                }
            }

        }catch (SQLException e){
            delete(property.getId());
            createSuccess = false;
            LOGGER.error("Error while trying to communicate with the database - create");
            e.printStackTrace();
        }

        return createSuccess;
    }

    public void setPropertyParameters(CallableStatement stmt, Property property) throws SQLException{

        stmt.setInt("p_seller_id", property.getSellerUser().getId());
        stmt.setInt("p_type_id", property.getType().getId());
        stmt.setInt("p_ad_category_id", property.getAdCategory().getId());
        stmt.setInt("p_city_id", property.getCity().getId());
        stmt.setString("p_description", property.getDescription());
        stmt.setString("p_price", property.getPrice());
        stmt.setBoolean("p_new_construction", property.isNewConstruction());

    }

    @Override
    public Property get(Integer id) {
        Property property = null;

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
        CallableStatement stmt = conn.prepareCall(GET_PROPERTY_STORED_PROCEDURE);
        CallableStatement stmtTag = conn.prepareCall(TAG_STORED_PROCEDURE)){

            stmt.setInt("p_id", id);
            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.first()){
                property = new Property();
                setNewProperty(resultSet, stmtTag);
            }
        }catch (SQLException e){
            LOGGER.error("Error while trying to communicate with the database - get");
            e.printStackTrace();
        }

        return property;
    }

    public Property setNewProperty(ResultSet resultSet, CallableStatement stmtTag) throws SQLException {

        LOGGER.warn("FOUND PROPERTY");
        LOGGER.warn("PROPERTY ID IS {}", resultSet.getInt("id"));

        SellerUser seller = new SellerUser();
        seller.setId(resultSet.getInt("seller_id"));
        seller.setFirstName(resultSet.getString("first_name"));
        seller.setLastName(resultSet.getString("last_name"));
        seller.setPicture(resultSet.getString("picture"));
        seller.setEmail(resultSet.getString("email"));
        seller.setTin(resultSet.getString("tin"));
        seller.setPhoneNumber(resultSet.getString("phone_number"));

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
        property.setId(resultSet.getInt("id"));
        property.setPostingDate(resultSet.getObject("post_date", LocalDateTime.class));
        property.setDescription(resultSet.getString("description"));
        property.setPrice(resultSet.getString("price"));
        property.setArea(resultSet.getString("area"));
        property.setNewConstruction(resultSet.getBoolean("new_construction"));
        property.setCity(city);
        property.setAdCategory(adCategory);
        property.setType(type);
        property.setSellerUser(seller);

        //sada uzimamo tagove ako ih ima
        stmtTag.setInt("p_property_id", property.getId());
        ResultSet rs = stmtTag.executeQuery();

        Tag tag = null;
        List<Tag> tags;
        if(rs != null){
            //znaci da ima tagova
            tags = new ArrayList<>();
            rs.beforeFirst();
            while (rs.next()){
                tag = new Tag();
                tag.setId(rs.getInt("id"));
                tag.setPropertyTypeId(rs.getInt("property_type_id"));
                tag.setName(rs.getString("name"));

                tags.add(tag);
            }

            property.setTags(tags);
        }

        return property;
    }

    @Override
    public boolean update(Property property, Integer integer) {
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        boolean deleteSuccess = false;

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
        CallableStatement stmt = conn.prepareCall(DELETE_PROPERTY_STORED_PROCEDURE)){

            stmt.setInt("p_id", id);
            stmt.registerOutParameter("p_is_deleted", Types.BOOLEAN);

            stmt.execute();

            deleteSuccess = stmt.getBoolean("p_is_deleted");

        }catch (SQLException e){
            LOGGER.error("Error while trying to communicate with the database - delete");
            e.printStackTrace();
        }

        return deleteSuccess;
    }

    @Override
    public List<BuyerUser> getPropertyApplicants(Integer propertyId) {
        List<BuyerUser> propertyApplicants = null;

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
        CallableStatement stmt = conn.prepareCall(GET_PROPERTY_APPLICANTS_PROCEDURE_CALL)){
            stmt.setInt("p_property_id", propertyId);
            ResultSet resultSet = stmt.executeQuery();

            BuyerUser buyerUser;
            propertyApplicants = new ArrayList<>();
            while (resultSet.next()){
                buyerUser = new BuyerUser();
                buyerUser.setId(resultSet.getInt("user_id"));
                buyerUser.setEmail(resultSet.getString("email"));
                buyerUser.setFirstName(resultSet.getString("first_name"));
                buyerUser.setLastName(resultSet.getString("last_name"));
                buyerUser.setPicture(resultSet.getString("picture"));
                buyerUser.setPhoneNumber(resultSet.getString("phone_number"));
                buyerUser.setHashedPassword(resultSet.getString("hashed_password"));

                propertyApplicants.add(buyerUser);
            }
        }catch (SQLException e){
            LOGGER.error("Error while trying to communicate with the database - getPropertyApplicants");
            e.printStackTrace();
        }

        return propertyApplicants;
    }

    @Override
    public PropertyFeed getFilteredProperties(PropertiesFilter propertiesFilter) {
        List<Property> properties = new ArrayList<>();
        List<Tag> tags = propertiesFilter.getTags(); //tagovi za tu nekretninu
        PropertyFeed propertyFeed =new PropertyFeed();

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
            CallableStatement stmt = conn.prepareCall(GET_FILTERED_PROPERTIES_STORED_PROCEDURE);
            CallableStatement stmtTag = conn.prepareCall(TAG_STORED_PROCEDURE)){

            //filter property
            stmt.setInt("p_seller_id", propertiesFilter.getSellerUser().getId());
            stmt.setInt("p_ad_category", propertiesFilter.getAdCategory().getId());
            stmt.setInt("p_type_id", propertiesFilter.getType().getId());
            stmt.setInt("p_city_id", propertiesFilter.getCity().getId());
            stmt.setBoolean("p_new_construction", propertiesFilter.isNewConstruction());

            ResultSet resultSet = stmt.executeQuery(); // ovo je glavni prikaz nekretnina sa obaveznim filterima

            List<Integer> listId = new ArrayList<>();
            int id,count = 0;
            if(resultSet.first() && tags != null){ //pronasli smo filtered property i ima tagova!

                resultSet.beforeFirst();
                while(resultSet.next()){
                    id = resultSet.getInt("id"); //uzimamo id propertyja
                    boolean tagsFlag = checkForTag(stmtTag, propertiesFilter.getTags(), id); //proveravamo da li mozemo da nadjemo tagove

                    if(tagsFlag) listId.add(id);
                }
            }

            //rastuce sortiraj
            int pointerPosition = (propertiesFilter.getPageNumber() - 1) * propertiesFilter.getPropertiesPerPage();
            LOGGER.info("pointer position is {}", pointerPosition);
            int flagPage = propertiesFilter.getPropertiesPerPage();
            int tempId;
            Property prop;
            if(propertiesFilter.isAscendingOrder()){
                LOGGER.info("sort rastuce");
                resultSet.afterLast();
                while(pointerPosition != 0){
                    resultSet.previous();

                    tempId = resultSet.getInt("id");

                    if (tags == null || listId.contains(tempId)) pointerPosition--;
                }

                while(resultSet.previous()){
                    tempId = resultSet.getInt("id");

                    if(tags == null || listId.contains(tempId)){
                        prop = setNewProperty(resultSet,stmtTag);
                        properties.add(prop);

                        flagPage -- ;
                        if(flagPage == 0) break;
                    }
                }
            }
            else{
                //opadajuce sortiraj
                LOGGER.info("sort opadajuce");
                resultSet.beforeFirst();
                while(pointerPosition != 0){
                    resultSet.next();

                    tempId= resultSet.getInt("id");

                    if (tags == null || listId.contains(tempId)) pointerPosition--;
                }

                while(resultSet.next()){
                    tempId = resultSet.getInt("id");

                    if(tags == null || listId.contains(tempId)){

                        prop = setNewProperty(resultSet,stmtTag);
                        properties.add(prop);

                        flagPage -- ;
                        if(flagPage == 0) break;
                    }
                }
            }


            //gotovo sortiranje
            resultSet.beforeFirst();
            while(resultSet.next()) { //trazimo koliko ima ukupno nekretnina koje smo pronasli
                LOGGER.error("usli smo u resultset");
                LOGGER.error("ID je {}", resultSet.getInt("id"));
                tempId = resultSet.getInt("id");
                if(tags == null || listId.contains(tempId))
                    count++;
            }

            LOGGER.info("PROPERTIES GETFILTERED ENDED");
            LOGGER.info("FOUND PROPERTIES:");
            LOGGER.info("Number of properties is {}",count);
            propertyFeed.setProperties(properties);
            propertyFeed.setTotalProperties(count);

        }catch (SQLException e){
            LOGGER.error("Error while trying to communicate with the database - getFilteredProperties");
            e.printStackTrace();
        }

        return propertyFeed;
    }

    public boolean checkForTag(CallableStatement stmt, List<Tag> tags, int id) throws SQLException{
        boolean flag = false;
        int counter = 0;

        //proveravamo da li postoje tagovi za ovu nekretninu
        stmt.setInt("p_property_id",id);
        ResultSet rsTag = stmt.executeQuery();

        if(rsTag != null) //postoje
        {
            rsTag.beforeFirst();
            while (rsTag.next())
            {
                int tmpTag = rsTag.getInt("id");

                for(Tag tag : tags)
                {
                    if(tmpTag == tag.getId()) counter++;
                }
            }
        }

        if(counter == tags.size()) flag = true;
        //flag true se vraca ako smo uspeli da pronadjemo onoliko tagova koliko zapravo ima u bazi
        return flag;
    }

    @Override
    public boolean applyForProperty(Integer propertyId, Integer userId) {
        boolean isSuccess = false;

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
        CallableStatement stmt = conn.prepareCall(PROPERTY_APPLY_PROCEDURE_CALL)){
            stmt.setInt("p_property_id", propertyId);
            stmt.setInt("p_buyer_id", userId);
            stmt.registerOutParameter("p_successfully_applied", Types.BOOLEAN);

            stmt.executeUpdate();

            isSuccess = stmt.getBoolean("p_successfully_applied");

        }catch (SQLException e){
            LOGGER.error("Error while trying to communicate with the database - applyForProperty");
            e.printStackTrace();
        }

        return isSuccess;
    }

    @Override
    public List<Comment> getAllComments(int propertyId) {
        List<Comment> comments = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(databaseSourceUrl,databaseUsername,databasePassword);
             CallableStatement stmt = con.prepareCall(GET_ALL_COMMENTS_STORED_PROCEDURE))
        {
            stmt.setInt("p_property_id", propertyId);
            ResultSet rs = stmt.executeQuery();

            if(rs.first())
            {
                rs.beforeFirst();
                while(rs.next())
                {
                    Comment comment = new Comment();

                    comment.setId(rs.getInt("id"));
                    comment.setParentId(rs.getInt("parent_id"));
                    comment.setText(rs.getString("text"));
                    comment.setPostDate(rs.getObject("post_date",LocalDateTime.class));
                    comment.setAuthorName(rs.getString("first_name") + " " + rs.getString("last_name"));

                    comments.add(comment);
                }
            }
        }

        catch (SQLException e) {
            LOGGER.error("Error while trying to communicate with the database - getAllComments");
            e.printStackTrace();
        }

        return comments;
    }

    @Override
    public boolean postComment(Comment comment, int propertyId, int userId, boolean isApplicant) {
        boolean isSuccessful = false;

        try (Connection con = DriverManager.getConnection( databaseSourceUrl, databaseUsername, databasePassword );
             CallableStatement stmt = con.prepareCall(POST_COMMENT_STORED_PROCEDURE);
             CallableStatement stmtCheck = con.prepareCall(CHECK_IF_SELLERS_PROPERTY_STORED_PROCEDURE))
        {

            if(!isApplicant) // on nije aplicirao za neku nekretninu
            {
                stmtCheck.setInt("p_property_id", propertyId); //gledamo da nije mozda to prodavceva nekretnina
                stmtCheck.setInt("p_user_id", userId);

                ResultSet rs = stmtCheck.executeQuery();

                rs.first();
                if(rs.getInt("count") == 0) return false; // nije njegova nekretnina,a nije ni aplikant i ne moze onda da komentarise!
            }

            stmt.setInt("p_author_id", userId);
            stmt.setInt("p_property_id", propertyId);
            stmt.setInt("p_parent_id", comment.getParentId());
            stmt.setString("p_text", comment.getText());
            stmt.registerOutParameter("p_is_posted", Types.BOOLEAN);

            stmt.executeUpdate();

            isSuccessful = stmt.getBoolean("p_is_posted");
        }

        catch ( SQLException e ) {
            LOGGER.error("Error while trying to communicate with the database - postComment");
            e.printStackTrace();
        }

        return isSuccessful;
    }

    @Override
    public boolean deleteComment(Integer commentId) {
        boolean deleteSuccess = false;

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
            CallableStatement stmt = conn.prepareCall(DELETE_COMMENT_STORED_PROCEDURE)){

            stmt.setInt("p_id", commentId);
            stmt.registerOutParameter("p_is_deleted", Types.BOOLEAN);

            stmt.execute();

            deleteSuccess = stmt.getBoolean("p_is_deleted");

        }catch (SQLException e){
            LOGGER.error("Error while trying to communicate with the database - deleteComment");
            e.printStackTrace();
        }

        return deleteSuccess;
    }

    @Override
    public LikeResponse getPropertyLikes(Integer propertyId, Integer buyerId, boolean isApplicant) {
        LikeResponse likeResponse = new LikeResponse();

        try ( Connection conn = DriverManager.getConnection( databaseSourceUrl, databaseUsername, databasePassword );
              CallableStatement stmt = conn.prepareCall(COUNT_LIKES_STORED_PROCEDURE);
              CallableStatement stmtCheck = conn.prepareCall(CHECK_IF_ALREADY_LIKED_STORED_PROCEDURE))
        {
            likeResponse.setTotalLikes(0);
            likeResponse.setAlreadyLiked(false);

            stmt.setInt("p_property_id", propertyId);
            ResultSet rs = stmt.executeQuery();

            rs.first();
            likeResponse.setTotalLikes(rs.getInt("count"));

            if(isApplicant) //samo tada moze da lajkuje
            {
                stmtCheck.setInt("p_property_id",propertyId);
                stmtCheck.setInt("p_buyer_id", buyerId);
                rs = stmtCheck.executeQuery();

                rs.first();
                if(rs.getInt("count") != 0) likeResponse.setAlreadyLiked(true);
            }
        }

        catch (SQLException e) {
            LOGGER.error("Error while trying to communicate with the database - getPropertyLikes");
            e.printStackTrace();
        }

        return likeResponse;
    }

    @Override
    public boolean likeProperty(Integer propertyId, Integer buyerId) {
        boolean likeSuccess = false;

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
            CallableStatement stmt = conn.prepareCall(LIKE_PROPERTY_STORED_PROCEDURE);
            CallableStatement stmtAlreadyLiked = conn.prepareCall(CHECK_IF_ALREADY_LIKED_STORED_PROCEDURE)){

            stmtAlreadyLiked.setInt("p_property_id", propertyId);
            stmtAlreadyLiked.setInt("p_buyer_id", buyerId);

            ResultSet rs = stmtAlreadyLiked.executeQuery();
            rs.first();
            if(rs.getInt("count") != 0){
                return false;//vec je lajkovao prop
            }

            stmt.setInt("p_property_id", propertyId);
            stmt.setInt("p_buyer_id", buyerId);
            stmt.registerOutParameter("p_is_liked", Types.BOOLEAN);

            stmtAlreadyLiked.executeUpdate();

            likeSuccess = stmt.getBoolean("p_is_liked");

        }catch (SQLException e){
            LOGGER.error("Error while trying to communicate with the database - likeProperty");
            e.printStackTrace();
        }

        return likeSuccess;
    }

    @Override
    public boolean deleteLike(Integer propertyId, Integer buyerId) {
        boolean deleteSuccess = false;

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
            CallableStatement stmt = conn.prepareCall(RECALL_LIKE_STORED_PROCEDURE)){

            stmt.setInt("p_property_id", propertyId);
            stmt.setInt("p_buyer_id", buyerId);
            stmt.registerOutParameter("p_is_deleted", Types.BOOLEAN);

            stmt.execute();

            deleteSuccess = stmt.getBoolean("p_is_deleted");

        }catch (SQLException e){
            LOGGER.error("Error while trying to communicate with the database - deleteLike");
            e.printStackTrace();
        }

        return deleteSuccess;
    }
}
