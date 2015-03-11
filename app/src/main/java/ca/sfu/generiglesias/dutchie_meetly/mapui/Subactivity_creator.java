package ca.sfu.generiglesias.dutchie_meetly.mapui;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ca.sfu.generiglesias.dutchie_meetly.EventHolder;

public class Subactivity_creator {

    public static void main(Context context, final GoogleMap map) {

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                EventHolder.setLatitude(latLng.latitude);
                EventHolder.setLongitude(latLng.longitude);

                Marker marker = map.addMarker(
                        new MarkerOptions().position(latLng).visible(true)
                );
            }
        });
    }
}
