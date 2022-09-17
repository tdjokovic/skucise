package com.example.skucise.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

public class Reservation {
    @Min(0)
    @Max(Integer.MAX_VALUE)
    private int id;

    private Property property;

    private GeneralUser user;

    @DateTimeFormat
    @FutureOrPresent
    private LocalDateTime date;

    private boolean isApproved;

    public Reservation() { }

    public Reservation(int id, Property property, GeneralUser user, LocalDateTime date, boolean isApproved) {
        this.id = id;
        this.property = property;
        this.user = user;
        this.date = date;
        this.isApproved = isApproved;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public GeneralUser getUser() {
        return user;
    }

    public void setUser(GeneralUser user) {
        this.user = user;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

}
