package com.example.pruthvi.driverapp;



public class ACRides {

    private String passengerName;
    private String date;
    private String time;
    private String destination;

    /**
     *
     * @param passengerName
     * @param date
     * @param time
     * @param destination
     */
    public ACRides(String passengerName, String date, String time, String destination) {
        this.passengerName = passengerName;
        this.date = date;
        this.time = time;
        this.destination = destination;
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
     * @return Destination
     */
    public String getDestination() {
        return destination;
    }
}
