package com.tsaravan9.myconciergeandroid.models;

import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.lang.ref.Reference;
import java.util.UUID;

public class User implements Serializable {
    private String firstname = "";
    private String lastname = "";
    private String email = "";
    private String pass = "";
    private Long mobileNumber = 0L;
    private String address = "";
    private Boolean admin = false;
    private String apartment = "";
    private String proPic = "";

    public User(String firstname, String lastName, String email, String pass, Long mobileNumber, Boolean isAdmin) {
        this.firstname = firstname;
        this.lastname = lastName;
        this.email = email;
        this.pass = pass;
        this.mobileNumber = mobileNumber;
        this.admin = isAdmin;
    }

    public User(String firstname, String lastName, String email, String pass, Long mobileNumber, String address, Boolean isAdmin, String apartment) {
        this.firstname = firstname;
        this.lastname = lastName;
        this.email = email;
        this.pass = pass;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.admin = isAdmin;
        this.apartment = apartment;
    }

    public User(String firstname, String lastname, String email, String pass, Long mobileNumber, String address, Boolean admin, String apartment, String proPic) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.pass = pass;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.admin = admin;
        this.apartment = apartment;
        this.proPic = proPic;
    }

    public String getProPic() {
        return proPic;
    }

    public void setProPic(String proPic) {
        this.proPic = proPic;
    }

    public User() {
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
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
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

    public Long getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(Long mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", pass='" + pass + '\'' +
                ", mobileNumber=" + mobileNumber +
                ", address='" + address + '\'' +
                ", admin=" + admin +
                ", apartment='" + apartment + '\'' +
                ", proPic=" + proPic +
                '}';
    }
}
