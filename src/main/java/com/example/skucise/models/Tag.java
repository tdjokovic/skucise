package com.example.skucise.models;

import javax.validation.constraints.*;

public class Tag {
    @Min(1)
    @Max(Integer.MAX_VALUE)
    private int id;

    @Min(1)
    @Max(Integer.MAX_VALUE)
    private int propertyTypeId;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9šŠđĐčČćĆžŽ \\-$#+(),&%/]+$")
    @Size( min = 2, max = 30, message = "The length of name must be between 2 and 30 characters" )
    private String name;

    public Tag(){}

    public Tag(int id, int propertyTypeId, String name) {
        this.id = id;
        this.propertyTypeId = propertyTypeId;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPropertyTypeId() {
        return propertyTypeId;
    }

    public void setPropertyTypeId(int propertyTypeId) {
        this.propertyTypeId = propertyTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", propertyTypeId=" + propertyTypeId +
                ", name='" + name + '\'' +
                '}';
    }
}
