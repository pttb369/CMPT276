package ca.sfu.generiglesias.dutchie_meetly.maplogic;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class MapActions {
    public static final double SFU_LAT = 49.187559;
    public static final double SFU_LNG = -122.849545;

    public static final int STREET_DEPTH = 17;

    public static void moveMapToLocation(GoogleMap map, double latitude, double longitude) {
        LatLng location = new LatLng(latitude, longitude);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location, STREET_DEPTH);
        map.animateCamera(update);
    }
}
