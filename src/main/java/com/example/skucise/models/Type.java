package com.example.skucise.models;

import javax.validation.constraints.*;

// koji je tip objekta - stan,kuca,plac...
public class Type {
    @Min(1)
    @Max(Integer.MAX_VALUE)
    private int id;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9šŠđĐčČćĆžŽ \\-$#+(),&%/]+$")
    @Size( min = 2, max = 30, message = "The length of name must be between 2 and 30 characters" )
    private String name;

    public Type(){}

    public Type(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
