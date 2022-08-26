package com.example.skucise.repositories;

import com.example.skucise.models.AdCategory;
import com.example.skucise.models.Type;
import com.example.skucise.repositories.interfaces.IAdTypeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AdTypeRepository implements IAdTypeRepository {

    private static final String AD_TYPE_STORED_PROCEDURE_CALL = "{call get_all_ad_types()}";

    @Value("jdbc:mariadb://localhost:3307/skucise")
    private String databaseSourceUrl;

    @Value("root")
    private String databaseUsername;

    @Value("")
    private String databasePassword;

    @Override
    public List<Type> getAll() {
        List<Type> adTypeList = new ArrayList<>();
        Type type;

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
            CallableStatement stmt = conn.prepareCall( AD_TYPE_STORED_PROCEDURE_CALL )){

            ResultSet resultSet = stmt.executeQuery();

            while(resultSet.next()){
                type = new Type();
                type.setId(resultSet.getInt("type_id"));
                type.setName(resultSet.getString("type_name"));

                adTypeList.add(type);
            }

        }
        catch ( SQLException e ){
            e.printStackTrace();
        }

        return adTypeList;
    }

    @Override
    public boolean create(Type type) {
        return false;
    }

    @Override
    public Type get(Integer integer) {
        return null;
    }

    @Override
    public boolean update(Type type, Integer integer) {
        return false;
    }

    @Override
    public boolean delete(Integer integer) {
        return false;
    }
}
