package com.example.skucise.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

public class PropertyFeed {

    @Min(0)
    @Max(Integer.MAX_VALUE)
    private int totalProperties;

    private List<Property> properties;

    public int getTotalProperties() {
        return totalProperties;
    }

    public void setTotalProperties(int totalProperties) {
        this.totalProperties = totalProperties;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
}
