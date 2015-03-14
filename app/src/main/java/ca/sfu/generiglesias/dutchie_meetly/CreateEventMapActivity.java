package ca.sfu.generiglesias.dutchie_meetly;

import android.content.Context;
import android.content.Intent;
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

/**
 *  Shows Google Maps to user for choosing
 *  their location of event
 */
public class CreateEventMapActivity extends FragmentActivity {
    private GoogleMap map;
    private GPSTracker gpsTracker;
    private Marker marker;
    boolean isLocationChosen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event_map);
        setUpMapIfNeeded();
        setupButtons(this);

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
                if (isLocationChosen) {
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please select a location", Toast.LENGTH_SHORT).show();
                }
            }
        });

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                marker.setPosition(latLng);
                marker.setTitle(latLng.latitude + ":" + latLng.longitude);
                marker.setVisible(true);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("latitude", latLng.latitude);
                returnIntent.putExtra("longitude", latLng.longitude);
                setResult(RESULT_OK, returnIntent);

                isLocationChosen = true;
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