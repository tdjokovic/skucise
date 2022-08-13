package com.example.skucise.models;

public class LoginResponse {
    private int userId;
    private boolean areCredsValid;
    private boolean isApproved;
    private String role;
    public String jwt;

    public LoginResponse(){}

    public LoginResponse(boolean isApproved) {
        this.isApproved = isApproved;
    }

    public LoginResponse(int userId, boolean areCredsValid, boolean isApproved, String role) {
        this.userId = userId;
        this.areCredsValid = areCredsValid;
        this.isApproved = isApproved;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }
    public boolean getAreCredsValid() {
        return areCredsValid;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public String getRole() {
        return role;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
}
