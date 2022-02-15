package com.tsaravan9.myconciergeandroid.models;

public class Booking {
    private String amenityName;
    private Integer slot;
    private String resident;
    private String apartment;
    private String bookedFor;
    private Long bookedAt;

    public Booking(String amenityName, Integer slot, String resident, String apartment, Long bookedAt, String bookedOn) {
        this.amenityName = amenityName;
        this.slot = slot;
        this.resident = resident;
        this.apartment = apartment;
        this.bookedAt = bookedAt;
        this.bookedFor = bookedOn;
    }

    public Booking(){

    }

    public String getAmenityName() {
        return amenityName;
    }

    public void setAmenityName(String amenityName) {
        this.amenityName = amenityName;
    }

    public Integer getSlot() {
        return slot;
    }

    public void setSlot(Integer slot) {
        this.slot = slot;
    }

    public String getResident() {
        return resident;
    }

    public void setResident(String resident) {
        this.resident = resident;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public Long getBookedAt() {
        return bookedAt;
    }

    public void setBookedAt(Long bookedAt) {
        this.bookedAt = bookedAt;
    }

    public String getBookedFor() {
        return bookedFor;
    }

    public void setBookedFor(String bookedFor) {
        this.bookedFor = bookedFor;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "amenityName='" + amenityName + '\'' +
                ", slot='" + slot + '\'' +
                ", resident='" + resident + '\'' +
                ", apartment='" + apartment + '\'' +
                ", bookedOn='" + bookedFor + '\'' +
                ", bookedAt='" + bookedAt + '\'' +
                '}';
    }
}
