package com.example.skucise.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class NewUserData {

    @NotBlank
    @Pattern(regexp = "^[a-zA-ZšŠđĐčČćĆžŽ]+([ \\-][a-zA-ZšŠđĐčČćĆžŽ]+)*$")
    @Size( min = 1, max = 30)
    private String firstName;

    @NotBlank
    @Pattern(regexp = "^[a-zA-ZšŠđĐčČćĆžŽ]+([ \\-][a-zA-ZšŠđĐčČćĆžŽ]+)*$")
    @Size( min = 1, max = 30)
    private String lastName;

    @NotBlank
    @Pattern(regexp = "^(0|(\\+[1-9][0-9]{0,2}))[1-9][0-9][0-9]{6,7}$")
    private String phoneNumber;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]+([.\\-+][a-zA-Z0-9]+)*@([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}$")
    @Size( max=320 )
    private String email;

    public NewUserData(){

    }

    public NewUserData(String firstName, String lastName, String phoneNumber, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
