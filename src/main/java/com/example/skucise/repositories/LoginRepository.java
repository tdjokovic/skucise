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

    @Value("jdbc:mariadb://localhost:3306/skucise")
    private String databaseSourceUrl;

    @Value("root")
    private String databaseUsername;

    @Value("")
    private String databasePassword;



    @Override
    public LoginResponse checkCredentials(LoginCredentials loginCredentials) {
        LoginResponse loginResponse = null;

        try(Connection connection = DriverManager.getConnection(databaseSourceUrl,databaseUsername,databasePassword);
                CallableStatement stmt = connection.prepareCall(LOGIN_STORED_PROCEDURE_CALL)){

            stmt.setString("email",loginCredentials.getEmail());
            stmt.setString("hashed_password",loginCredentials.getHashedPassword());

            stmt.registerOutParameter("user_id", Types.INTEGER);
            stmt.registerOutParameter("valid_creds",Types.BOOLEAN);
            stmt.registerOutParameter("approved",Types.BOOLEAN);
            stmt.registerOutParameter("role",Types.VARCHAR);

            stmt.executeUpdate();

            loginResponse = new LoginResponse(stmt.getInt("user_id")
                        ,stmt.getBoolean("valid_creds")
                        ,stmt.getBoolean("approved")
                        ,stmt.getString("role"));
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
