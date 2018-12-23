package com.example.pruthvi.driverapp;

public class Ride {

    private int photo;
    private String passengerName;
    private String date;
    private String time;
    private String destination;

    /**
     *
     * @param photo
     * @param passengerName
     * @param date
     * @param time
     * @param destination
     */
    public Ride(int photo, String passengerName, String date, String time, String destination) {
        this.photo = photo;
        this.passengerName = passengerName;
        this.date = date;
        this.time = time;
        this.destination = destination;
    }

    /**
     *
     * @return Photo
     */
    public int getPhoto() {
        return photo;
    }


    /**
     *
     * @return Date
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @return Time
     */
    public String getTime() {
        return time;
    }

    /**
     *
     * @return PassengerName
     */
    public String getPassengerName() {
        return passengerName;
    }

    /**
     *
     * @return Destination
     */
    public String getDestination() {
        return destination;
    }
}
