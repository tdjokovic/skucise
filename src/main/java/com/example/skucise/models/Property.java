package com.example.skucise.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

public class Property {
    @Min(0)
    @Max(Integer.MAX_VALUE)
    private int id;

    private SellerUser sellerUser;
    private AdCategory adCategory;
    private Type type;
    private City city;
    private List<Tag> tags;

    @DateTimeFormat
    @PastOrPresent
    private LocalDateTime postingDate;

    @NotBlank
    @Size(min = 10, max = 10000, message = "The length of description must be between 10 and 10000 characters")
    private String description;

    @Min(1)
    @Max(Integer.MAX_VALUE)
    private int price;

    @Size(max = 50)
    private String area; //kvadratura

    private boolean newConstruction; //da li je novogradnja

    @Pattern(regexp = "^[a-zA-z0-9+/=:;,]+$")
    @Size( min = 5000, max = 65000,
            message = "The length for base64 encoded picture must be between 5000 and 65000 characters" )
    private String picture;

    public Property(){}

    public Property(int id, SellerUser sellerUser,
                    AdCategory adCategory,
                    List<Tag> tags,
                    Type type,
                    City city,
                    LocalDateTime postingDate,
                    String description,
                    int price,
                    String area,
                    boolean newConstruction,
                    String picture) {
        this.id = id;
        this.sellerUser = sellerUser;
        this.adCategory = adCategory;
        this.type = type;
        this.city = city;
        this.tags = tags;
        this.postingDate = postingDate;
        this.description = description;
        this.price = price;
        this.area = area;
        this.newConstruction = newConstruction;
        this.picture = picture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SellerUser getSellerUser() {
        return sellerUser;
    }

    public void setSellerUser(SellerUser sellerUser) {
        this.sellerUser = sellerUser;
    }

    public AdCategory getAdCategory() {
        return adCategory;
    }

    public void setAdCategory(AdCategory adCategory) {
        this.adCategory = adCategory;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public LocalDateTime getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(LocalDateTime postingDate) {
        this.postingDate = postingDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public boolean isNewConstruction() {
        return newConstruction;
    }

    public void setNewConstruction(boolean newConstruction) {
        this.newConstruction = newConstruction;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
    public String getPicture() {
        return picture;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }
}
