package ca.sfu.generiglesias.dutchie_meetly;

import android.content.Context;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ca.sfu.generiglesias.dutchie_meetly.maplogic.GPSTracker;
import ca.sfu.generiglesias.dutchie_meetly.mapui.Subactivity_creator;
import ca.sfu.generiglesias.dutchie_meetly.mapui.Subactivity_viewer;

public class CreateEventMapActivity extends FragmentActivity {
    public static final String IS_CREATING = "isCreating";

    public static final int STREET_DEPTH = 17;

    //SFU : 49.187559, -122.849545

    private GoogleMap map;
    private GPSTracker gpsTracker;

    private boolean isCreating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event_map);

        isCreating = getIntent().getBooleanExtra(CreateEventMapActivity.IS_CREATING, false);

        setUpMapIfNeeded();
        setupButtons(getApplicationContext(),
                EventHolder.getLatitude(),
                EventHolder.getLongitude()
        );

        gpsTracker = new GPSTracker(getApplicationContext());

        if (isCreating) {
            Subactivity_creator.main(getApplicationContext(), map);
        } else {
            Subactivity_viewer.main(getApplicationContext(), map);
        }
    }

    public static void moveMapToLocation(GoogleMap map, double latitude, double longitude, int depth) {
        LatLng location = new LatLng(latitude, longitude);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location, depth);
        map.animateCamera(update);
    }

    private void setupButtons(final Context context, final double latitude, final double longitude) {
        Button btnFindMe = (Button) findViewById(R.id.btnFindMe);

        btnFindMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (! gpsTracker.isLocationUnknown()) {
                    Location loc = gpsTracker.getLocation();
                    moveMapToLocation(map, loc.getLatitude(), loc.getLongitude(), 16);
                } else {
                    Toast.makeText(context, "Location Unknown", Toast.LENGTH_SHORT);
                }
            }
        });

        Button btnFindEvent = (Button) findViewById(R.id.btnFindEvent);
        if (isCreating) {
            btnFindEvent.setText("Set Location");
        } else {
            btnFindEvent.setText("Find Event");
        }
        btnFindEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveMapToLocation(map, longitude, latitude, STREET_DEPTH);
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