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

    private BuyerUser buyer;

    @DateTimeFormat
    @FutureOrPresent
    private LocalDateTime date;

    @Min(-1)
    @Max(2)
    private int isApproved;

    public Reservation() {}

    public Reservation(int id, Property property,BuyerUser buyer, LocalDateTime date, int isApproved) {
        this.id = id;
        this.property = property;
        this.buyer = buyer;
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

    public BuyerUser getBuyer() {
        return buyer;
    }

    public void setBuyer(BuyerUser buyer) {
        this.buyer = buyer;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(int isApproved) {
        this.isApproved = isApproved;
    }
}
