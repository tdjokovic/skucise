package com.example.skucise.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class BuyerUser extends GeneralUser{

    @NotBlank
    @Pattern(regexp = "^[a-zA-ZšŠđĐčČćĆžŽ]+([ \\-][a-zA-ZšŠđĐčČćĆžŽ]+)*$")
    @Size( min = 1, max = 30)
    private String firstName;

    @NotBlank
    @Pattern(regexp = "^[a-zA-ZšŠđĐčČćĆžŽ]+([ \\-][a-zA-ZšŠđĐčČćĆžŽ]+)*$")
    @Size( min = 1, max = 30)
    private String lastName;

    public BuyerUser(){}

    public BuyerUser(int id, String email, String hashedPassword, String picture, String phoneNumber, String firstName, String lastName) {
        super(id, email, hashedPassword, picture, phoneNumber);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "GeneralUser{" +
                "id="+ super.getId()+
                ",email = " + super.getEmail() + '\'' +
                ",hashedPassword" + super.getHashedPassword() + '\'' +
                ",first name='" + firstName + '\'' +
                ",last name='" + lastName + '\'' +
                '}';
    }
}
