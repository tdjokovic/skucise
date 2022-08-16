package com.example.skucise.repositories;

import com.example.skucise.models.LoginCredentials;
import com.example.skucise.models.LoginResponse;
import com.example.skucise.repositories.interfaces.ILoginRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.net.ConnectException;
import java.sql.*;
import java.util.List;

@Repository
public class LoginRepository implements ILoginRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginRepository.class);
    private static final String LOGIN_STORED_PROCEDURE_CALL = "{call check_credentials(?,?,?,?,?,?)}";

    @Value("jdbc:mariadb://localhost:3307/skucise")
    private String databaseSourceUrl;

    @Value("root")
    private String databaseUsername;

    @Value("")
    private String databasePassword;



    @Override
    public LoginResponse checkCredentials(LoginCredentials loginCredentials) {
        LoginResponse loginResponse = null;
        LOGGER.info("Now trying to find the user...");

        try(Connection connection = DriverManager.getConnection(databaseSourceUrl,databaseUsername,databasePassword);
                CallableStatement stmt = connection.prepareCall(LOGIN_STORED_PROCEDURE_CALL)){

            stmt.setString("email_in",loginCredentials.getEmail());
            stmt.setString("hashed_password_in",loginCredentials.getHashedPassword());

            stmt.registerOutParameter("uid", Types.INTEGER);
            stmt.registerOutParameter("valid_cr",Types.BOOLEAN);
            stmt.registerOutParameter("app",Types.BOOLEAN);
            stmt.registerOutParameter("rl",Types.VARCHAR);

            stmt.executeUpdate();

            LOGGER.info("Now finding who the user is...");
            LOGGER.info("The user we found has uid {}",stmt.getInt("uid"));
            loginResponse = new LoginResponse(stmt.getInt("uid")
                        ,stmt.getBoolean("valid_cr")
                        ,stmt.getBoolean("app")
                        ,stmt.getString("rl"));
        }catch (SQLException e){
            LOGGER.error("Error while communicating with database");
            e.printStackTrace();
        }

        return loginResponse;
    }

    @Override
    public List<LoginCredentials> getAll() {
        return null;
    }

    @Override
    public boolean create(LoginCredentials loginCredentials) {
        return false;
    }

    @Override
    public LoginCredentials get(Integer integer) {
        return null;
    }

    @Override
    public boolean update(LoginCredentials loginCredentials, Integer integer) {
        return false;
    }

    @Override
    public boolean delete(Integer integer) {
        return false;
    }
}
