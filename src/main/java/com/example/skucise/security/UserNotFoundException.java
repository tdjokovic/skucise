package com.example.skucise.security;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(String message){
        super(message);
    };
}
