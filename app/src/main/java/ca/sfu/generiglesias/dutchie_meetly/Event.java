package ca.sfu.generiglesias.dutchie_meetly;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by David on 2015-03-07.
 */
public class Event {
    private String eventName;
    private Date eventDate;
    private String cityName;
    private String eventDescription;
    private int eventIconId;

    private double latitude;
    private double longitude;

    public Event(String eventName,
                 Date eventDate,
                 String cityName,
                 String eventDescription,
                 int eventIconId) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.cityName = cityName;
        this.eventDescription = eventDescription;
        this.eventIconId = eventIconId;

        // TODO: get rid of random!!
        this.longitude = Math.random() * 100;
        this.latitude = Math.random() * 100;
    }



    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDate() {
        return new SimpleDateFormat("MM-dd-yyyy").format(eventDate);
    }

    public String getCityName() {
        return cityName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public int getEventIconId() {
        return eventIconId;
    }
}
