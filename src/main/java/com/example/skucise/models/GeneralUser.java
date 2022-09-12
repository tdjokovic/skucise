package com.example.skucise.models;

import javax.validation.constraints.*;

public class GeneralUser extends User{

    @Pattern(regexp = "^[a-zA-z0-9+/=:;,]+$")
    @Size( min = 5000, max = 65000,
            message = "The length for base64 encoded picture must be between 5000 and 65000 characters" )
    private String picture;


    @NotBlank
    @Pattern(regexp = "^(0|(\\+[1-9][0-9]{0,2}))[1-9][0-9][0-9]{6,7}$")
    private String phoneNumber;


    public GeneralUser(){

    }

    public GeneralUser(int id, String email, String hashedPassword, String picture, String phoneNumber) {
        super(id, email, hashedPassword);
        this.picture = picture;
        this.phoneNumber = phoneNumber;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "GeneralUser{" +
                "id="+ super.getId()+
                ",email = " + super.getEmail() + '\'' +
                ",hashedPassword" + super.getHashedPassword() + '\'' +
                ",picture='" + picture + '\'' +
                ",phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
