package com.example.home.countriesquiz.model;

public class User {
    private String name;
    private String password;
    private String country;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User(String name, String password, String country) {
        this.name = name;
        this.password = password;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getCountry() {
        return country;
    }
}
