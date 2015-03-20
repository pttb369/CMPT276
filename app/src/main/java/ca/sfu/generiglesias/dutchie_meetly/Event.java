package ca.sfu.generiglesias.dutchie_meetly;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents an event. This will be stored on
 * the android system and can be accessed publicly.
 */
public class Event implements Serializable{
    private String eventName;
    private String eventDate;
    private String cityName;
    private String eventDescription;
    private String eventStartTime;
    private String eventEndTime;
    private String eventDuration;
    private int eventIconId;

    private double latitude;
    private double longitude;

    public Event(String eventName,
                 String eventDate,
                 String cityName,
                 String eventDescription,
                 String eventStartTime,
                 String eventEndTime,
                 String eventDuration,
                 int eventIconId,
                 double latitude,
                 double longitude) {

        this.eventName = eventName;
        this.eventDate = eventDate;
        this.cityName = cityName;
        this.eventDescription = eventDescription;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.eventDuration = eventDuration;
        this.eventIconId = eventIconId;
        this.longitude = longitude;
        this.latitude = latitude;
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
        return eventDate;
    }

    public String getCityName() {
        return cityName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public String getEventEndTime() {
        return eventEndTime;
    }

    public String getEventDuration() {
        return eventDuration;
    }

    public int getEventIconId() {
        return eventIconId;
    }
}
