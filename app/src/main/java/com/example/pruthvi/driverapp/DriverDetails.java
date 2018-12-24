package com.example.pruthvi.driverapp;

import android.net.Uri;

public class DriverDetails {

    public String driverName;

    public String phoneNo;

    public String location;

    public String photoUrl;


    /**
     *
     */
    public DriverDetails() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }


    /**
     *
     * @param driverName
     * @param phoneNo
     * @param location
     * @param photoUrl
     */
    public DriverDetails(String driverName, String phoneNo, String location, String photoUrl) {
        this.driverName = driverName;
        this.phoneNo = phoneNo;
        this.location = location;
        this.photoUrl = photoUrl;
    }

    /**
     *
     * @return PhotoUrl
     */
    public String getPhotoUrl() {
        return photoUrl;
    }

    /**
     *
     * @return DriverName
     */
    public String getDriverName() {
        return driverName;
    }

    /**
     *
     * @return PhoneNo
     */
    public String getPhoneNo() {
        return phoneNo;
    }

    /**
     *
     * @return Location
     */
    public String getLocation() {
        return location;
    }
}
