package ca.sfu.generiglesias.dutchie_meetly.mapui;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ca.sfu.generiglesias.dutchie_meetly.CreateEventMapActivity;
import ca.sfu.generiglesias.dutchie_meetly.EventHolder;

public class Subactivity_viewer {
    public static void main(Context context, GoogleMap eventMap) {


        String name = EventHolder.getName();
        double latitude = EventHolder.getLatitude();
        double longitude = EventHolder.getLongitude();

        GoogleMap map = eventMap;

        CreateEventMapActivity.moveMapToLocation(map, latitude, longitude, CreateEventMapActivity.STREET_DEPTH);

        Marker eventMarker = map.addMarker(
                new MarkerOptions().position(new LatLng(latitude, longitude)));

        eventMarker.setVisible(true);
        eventMarker.setTitle(name);
    }
}