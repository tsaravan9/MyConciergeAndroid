package com.tsaravan9.myconciergeandroid.models;

import java.util.ArrayList;

public class Building {

    private String image;
    private ArrayList<String> residents;
    private String address;
    private ArrayList<String> admins;
    private ArrayList<String> announcements;
    private String id;

    public Building(String image, ArrayList<String> residents, String address, ArrayList<String> admins, ArrayList<String> announcements, String id) {
        this.image = image;
        this.residents = residents;
        this.address = address;
        this.admins = admins;
        this.announcements = announcements;
        this.id = id;
    }

    public Building(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<String> getResidents() {
        return residents;
    }

    public void setResidents(ArrayList<String> residents) {
        this.residents = residents;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<String> getAdmins() {
        return admins;
    }

    public void setAdmins(ArrayList<String> admins) {
        this.admins = admins;
    }

    public ArrayList<String> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(ArrayList<String> announcements) {
        this.announcements = announcements;
    }
}
