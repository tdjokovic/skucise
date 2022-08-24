package com.example.skucise.repositories;

import com.example.skucise.models.City;
import com.example.skucise.repositories.interfaces.ICityRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CityRepository implements ICityRepository {

    private static final String CITY_STORED_PROCEDURE_CALL = "{call get_all_cities()}";

    @Value("jdbc:mariadb://localhost:3307/skucise")
    private String databaseSourceUrl;

    @Value("root")
    private String databaseUsername;

    @Value("")
    private String databasePassword;

    @Override
    public List<City> getAll() {

        List<City> cityList = new ArrayList<>();
        City oneCity;

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
            CallableStatement stmt = conn.prepareCall( CITY_STORED_PROCEDURE_CALL )){

            ResultSet resultSet = stmt.executeQuery();

            while(resultSet.next()){
                oneCity = new City();
                oneCity.setId(resultSet.getInt("city_id"));
                oneCity.setName(resultSet.getString("city_name"));

                cityList.add(oneCity);
            }

        }
        catch ( SQLException e ){
            e.printStackTrace();
        }

        return cityList;
    }

    @Override
    public boolean create(City city) {
        return false;
    }

    @Override
    public City get(Integer integer) {
        return null;
    }

    @Override
    public boolean update(City city, Integer integer) {
        return false;
    }

    @Override
    public boolean delete(Integer integer) {
        return false;
    }
}
