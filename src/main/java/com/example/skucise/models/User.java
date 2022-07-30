package com.example.skucise.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.*;

public class User {

    @Min( 0 )
    @Max( Integer.MAX_VALUE )
    private int id;



}
