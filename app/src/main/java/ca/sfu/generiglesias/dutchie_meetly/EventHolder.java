package ca.sfu.generiglesias.dutchie_meetly;

import java.util.Date;

public class EventHolder {
    private static String name;
    private static double latitude;
    private static double longitude;

    private EventHolder() {
        // Prevents this object from being instantiated.
    }

    public static void refresh() {
        name = null;
        latitude = Double.NaN;
        longitude = Double.NaN;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String eventName) {
        name = eventName;
    }

    public static double getLatitude() {
        return latitude;
    }

    public static void setLatitude(double eventLatitude) {
        latitude = eventLatitude;
    }

    public static double getLongitude() {
        return longitude;
    }

    public static void setLongitude(double eventLongitude) {
        longitude = eventLongitude;
    }
}