package com.example.skucise.repositories;

import com.example.skucise.models.User;
import com.example.skucise.repositories.interfaces.IUserRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.concurrent.Callable;

@Repository
public class UserRepository implements IUserRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepository.class);
    private static final String GET_USER_PROCEDURE_CALL = "{call get_user(?)}";

    @Value("jdbc:mariadb://localhost:3307/skucise")
    private String databaseSourceUrl;

    @Value("root")
    private String databaseUsername;

    @Value("")
    private String databasePassword;

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public boolean create(User user) {
        return false;
    }

    @Override
    public User get(Integer integer) {
        User user = null;
        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
            CallableStatement stmt = conn.prepareCall(GET_USER_PROCEDURE_CALL)){

                stmt.setInt("p_id", integer);
                ResultSet resultSet = stmt.executeQuery();

                if(resultSet.first()){
                    user = new User();
                    int id = resultSet.getInt("user_id");
                    String email = resultSet.getString("email");
                    String hashedPassword = resultSet.getString("hashed_password");

                    user.setId(id);
                    user.setEmail(email);
                    user.setHashedPassword(hashedPassword);
                }

        }
        catch (SQLException e){
            LOGGER.error("Error trying to communicate with a database!(get)", e);
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public boolean update(User user, Integer integer) {
        return false;
    }

    @Override
    public boolean delete(Integer integer) {
        return false;
    }
}
