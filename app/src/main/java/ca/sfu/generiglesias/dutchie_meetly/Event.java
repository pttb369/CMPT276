package ca.sfu.generiglesias.dutchie_meetly;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by David on 2015-03-07.
 */
public class Event {
    private String eventName;
    private Date eventDate;
    private String eventLocation;
    private String eventDescription;
    private int eventIconId;

    public Event(String eventName,
                 Date eventDate,
                 String eventLocation,
                 String eventDescription,
                 int eventIconId) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.eventDescription = eventDescription;
        this.eventIconId = eventIconId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDate() {
        return new SimpleDateFormat("MM-dd-yyyy").format(eventDate);
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public int getEventIconId() {
        return eventIconId;
    }
}
