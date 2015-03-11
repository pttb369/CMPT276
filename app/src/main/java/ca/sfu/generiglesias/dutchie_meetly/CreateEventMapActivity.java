package ca.sfu.generiglesias.dutchie_meetly;

import android.content.Context;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ca.sfu.generiglesias.dutchie_meetly.maplogic.GPSTracker;
import ca.sfu.generiglesias.dutchie_meetly.maplogic.MapActions;

public class CreateEventMapActivity extends FragmentActivity {
    private GoogleMap map;
    private GPSTracker gpsTracker;
    private Marker marker;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event_map);
        setUpMapIfNeeded();
        setupButtons(this);
        EventHolder.refresh();

        map.clear();
        marker = map.addMarker(new MarkerOptions().position(new LatLng(0,0)));
        marker.setVisible(false);
        gpsTracker = new GPSTracker(getApplicationContext());


        if (gpsTracker.isLocationUnknown()) {
            Toast.makeText(getApplicationContext(), "Current Location Unknown", Toast.LENGTH_SHORT);
        } else {
            MapActions.moveMapToLocation(map, gpsTracker.getLocation().getLatitude(),
                    gpsTracker.getLocation().getLongitude());
        }
    }

    private void setupButtons(final Context context) {
        Button btnFindMe = (Button) findViewById(R.id.btnFindMe_create);

        btnFindMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gpsTracker.isGPSEnabled()) {
                    if (gpsTracker.isLocationUnknown()) {
                        Toast.makeText(getApplicationContext(), "Current Location Unknown", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        Location location = gpsTracker.getLocation();
                        MapActions.moveMapToLocation(map, location.getLatitude(), location.getLongitude());
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please Enable GPS", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnCreateEvent = (Button) findViewById(R.id.btnCreateEvent);
        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Double.isNaN(EventHolder.getLatitude())) {
                    Toast.makeText(getApplicationContext(), "Please select a location", Toast.LENGTH_SHORT)
                    .show();
                } else {
                    finish();
                }
            }
        });

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                EventHolder.setLatitude(latLng.latitude);
                EventHolder.setLongitude(latLng.longitude);
                marker.setPosition(latLng);
                marker.setTitle(latLng.latitude + ":" + latLng.longitude);
                marker.setVisible(true);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            // Try to obtain the map from the SupportMapFragment.
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (map != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
}