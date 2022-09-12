package com.example.skucise.repositories;

import com.example.skucise.models.AdCategory;
import com.example.skucise.models.City;
import com.example.skucise.repositories.interfaces.IAdCategoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AdCategoryRepository implements IAdCategoryRepository {

    private static final String AD_CATEGORY_STORED_PROCEDURE_CALL = "{call get_all_ad_categories()}";

    @Value("jdbc:mariadb://localhost:3307/skucise")
    private String databaseSourceUrl;

    @Value("root")
    private String databaseUsername;

    @Value("")
    private String databasePassword;

    @Override
    public List<AdCategory> getAll() {

        List<AdCategory> adCategoryList = new ArrayList<>();
        AdCategory adCategory;

        try(Connection conn = DriverManager.getConnection(databaseSourceUrl, databaseUsername, databasePassword);
            CallableStatement stmt = conn.prepareCall( AD_CATEGORY_STORED_PROCEDURE_CALL )){

            ResultSet resultSet = stmt.executeQuery();

            while(resultSet.next()){
                adCategory = new AdCategory();
                adCategory.setId(resultSet.getInt("ad_category_id"));
                adCategory.setName(resultSet.getString("ad_category_name"));

                adCategoryList.add(adCategory);
            }

        }
        catch ( SQLException e ){
            e.printStackTrace();
        }

        return adCategoryList;
    }

    @Override
    public boolean create(AdCategory adCategory) {
        return false;
    }

    @Override
    public AdCategory get(Integer integer) {
        return null;
    }

    @Override
    public boolean update(AdCategory adCategory, Integer integer) {
        return false;
    }

    @Override
    public boolean delete(Integer integer) {
        return false;
    }
}
