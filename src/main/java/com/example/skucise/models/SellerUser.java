package com.example.skucise.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class SellerUser extends BuyerUser{

    @NotBlank
    @Pattern(regexp = "^[0-9]{9}$")
    private String tin;

    public SellerUser(){}

    public SellerUser(int id, String email, String hashedPassword, String picture, String phoneNumber, String firstName, String lastName, String tin) {
        super(id, email, hashedPassword, picture, phoneNumber, firstName, lastName);
        this.tin = tin;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    @Override
    public String toString() {
        return "GeneralUser{" +
                "id="+ super.getId()+
                ",email = " + super.getEmail() + '\'' +
                ",hashedPassword" + super.getHashedPassword() + '\'' +
                ",first name='" + super.getFirstName() + '\'' +
                ",last name='" + super.getLastName() + '\'' +
                ",tin='" + tin + '\'' +
                '}';
    }
}
