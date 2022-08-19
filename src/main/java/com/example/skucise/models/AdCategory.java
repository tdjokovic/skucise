package com.example.skucise.models;

import javax.validation.constraints.*;


// ova klasa govori da li je oglas izdavanje ili prodaja
public class AdCategory {

    @Min(1)
    @Max(Integer.MAX_VALUE)
    private int id;

    @NotBlank
    @Pattern(regexp = "^[a-zA-ZšŠđĐčČćĆžŽ \\-]+$")
    @Size( min = 2, max = 30, message = "The length of name must be between 2 and 30 characters" )
    private String name;

    public AdCategory() {
    }

    public AdCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AdCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
