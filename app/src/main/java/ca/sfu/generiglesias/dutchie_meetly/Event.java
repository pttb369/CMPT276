package ca.sfu.generiglesias.dutchie_meetly;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Represents an event. This will be stored on
 * the android system and can be accessed publicly.
 */
public class Event implements Serializable{
    private long eventId;
    int lastUpdate;
    private String eventName;
    private String eventDate;
    private String cityName;
    private String eventDescription;
    private String eventStartTime;
    private Calendar calEventStart;
    private Calendar calEventEnd;
    private String eventEndTime;
    private String eventDuration;
    private int eventIconId;
    private double latitude;
    private double longitude;
    private String sharedFlag;
    private String eventAuthor;

    public Event(String eventName,
                 String eventDate,
                 String cityName,
                 String eventDescription,
                 String eventStartTime,
                 String eventEndTime,
                 String eventDuration,
                 int eventIconId,
                 double latitude,
                 double longitude,
                 String sharedFlag,
                 String eventAuthor) {

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
        this.sharedFlag = sharedFlag;
        this.eventAuthor = eventAuthor;
    }

    public Event(long eventId,
                 String eventName,
                 String eventDate,
                 String eventLocation,
                 String eventDescription,
                 String eventStartTime,
                 String eventEndTime,
                 String eventDuration,
                 int eventIconId,
                 double latitude,
                 double longitude,
                 String sharedFlag,
                 String eventAuthor) {
        this(eventName,
            eventDate,
            eventLocation,
            eventDescription,
            eventStartTime,
            eventEndTime,
            eventDuration,
            eventIconId,
            latitude,
            longitude,
            sharedFlag,
            eventAuthor);
        this.eventId = eventId;
    }

    public Event(
                 long eventId,
                 int lastUpdate,
                 String eventName,
                 Calendar eventStartTime,
                 Calendar eventEndTime,
                 double latitude,
                 double longitude) {

                this.lastUpdate = lastUpdate;
                this.eventName = eventName;
                this.calEventStart = eventStartTime;
                this.calEventEnd = eventEndTime;
                this.latitude = latitude;
                this.longitude = longitude;
                this.eventId = eventId;
    }

    public long getEventId() {
        return eventId;
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

    public String getSharedFlag() {return sharedFlag; }

    public String getEventAuthor() {return eventAuthor; }

    public int getLastUpdate() {return lastUpdate; }

    public Calendar getCalEventStart() {return calEventStart; }

    public Calendar getCalEventEnd() {return calEventEnd; }
}
