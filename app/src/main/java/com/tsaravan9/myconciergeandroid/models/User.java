package com.tsaravan9.myconciergeandroid.models;

public class User {
    private String firstname = "";
    private String lastname = "";
    private String email = "";
    private String pass = "";
    private int mobileNumber = 0;
    private String address = "";
    private Boolean isAdmin = false;
    private String apartment;

    public User(String firstname, String lastName, String email, String pass, int mobileNumber, Boolean isAdmin) {
        this.firstname = firstname;
        this.lastname = lastName;
        this.email = email;
        this.pass = pass;
        this.mobileNumber = mobileNumber;
        this.isAdmin = isAdmin;
    }

    public User(String firstname, String lastName, String email, String pass, int mobileNumber, String address, Boolean isAdmin) {
        this.firstname = firstname;
        this.lastname = lastName;
        this.email = email;
        this.pass = pass;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.isAdmin = isAdmin;
    }

    public User(String firstname, String lastName, String email, String pass, int mobileNumber, String address, Boolean isAdmin, String apartment) {
        this.firstname = firstname;
        this.lastname = lastName;
        this.email = email;
        this.pass = pass;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.isAdmin = isAdmin;
        this.apartment = apartment;
    }

    public User(){

    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(int mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstname='" + firstname + '\'' +
                ", lastName='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", pass='" + pass + '\'' +
                ", mobileNumber=" + mobileNumber +
                ", address='" + address + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
