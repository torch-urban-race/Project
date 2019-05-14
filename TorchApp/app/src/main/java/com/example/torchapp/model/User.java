package com.example.torchapp.model;

public class User {

    public final static String TABLE_NAME = "doctor";
    public final static String PERSONAL_NUMBER_COLUMN = "personal_number";
    public final static String PASSWORD_COLUMN = "password";
    public final static String NAME_COLUMN = "name";

    private String personalNumber;
    private String password;
    private String name;

    public User() {
    }

    public User(String personalNumber, String password, String name) {
        this.personalNumber = personalNumber;
        this.password = password;
        this.name = name;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "personalNumber='" + personalNumber + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
