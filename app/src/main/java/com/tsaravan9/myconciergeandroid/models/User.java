package com.tsaravan9.myconciergeandroid.models;

public class User {
    private String firstname = "";
    private String lastName = "";
    private String email = "";
    private String pass = "";
    private int mobileNumber = 0;
    private String address = "";
    private Boolean isAdmin = false;

    public User(String firstname, String lastName, String email, String pass, int mobileNumber, Boolean isAdmin) {
        this.firstname = firstname;
        this.lastName = lastName;
        this.email = email;
        this.pass = pass;
        this.mobileNumber = mobileNumber;
        this.isAdmin = isAdmin;
    }

    public User(String firstname, String lastName, String email, String pass, int mobileNumber, String address, Boolean isAdmin) {
        this.firstname = firstname;
        this.lastName = lastName;
        this.email = email;
        this.pass = pass;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.isAdmin = isAdmin;
    }

    public User(){

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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", pass='" + pass + '\'' +
                ", mobileNumber=" + mobileNumber +
                ", address='" + address + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
