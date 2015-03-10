package ca.sfu.generiglesias.dutchie_meetly;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ca.sfu.generiglesias.dutchie_meetly.maplogic.GPSTracker;

public class MapActivity extends FragmentActivity {
    public static final String IS_CREATING = "isCreating";
    public static final String NAME = "name";
    public static final String SNIPPET = "snippet";

    private final int STREET_DEPTH = 17;

    private GoogleMap map;
    private GPSTracker gpsTracker;

    private boolean isCreating;
    private String eventName;
    private String eventSnippet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        setUpMapIfNeeded();
        setupMainVars();
        setupButton();

        gpsTracker = new GPSTracker(getApplicationContext());

        if (isCreating) {
            Creating();
        } else {
            Viewing();
        }
    }

    private void setupMainVars() {
        isCreating = getIntent().getBooleanExtra(MapActivity.IS_CREATING, false);
        eventName = getIntent().getStringExtra(MapActivity.NAME);
        eventSnippet = getIntent().getStringExtra(MapActivity.SNIPPET);
    }

    private void Creating() {
        Location loc = gpsTracker.getLocation();
        moveMapToLocation(loc.getLatitude(), loc.getLongitude(), STREET_DEPTH);
    }

    private void Viewing() {
        //TODO: change to the events location...
        double latitude = gpsTracker.getLocation().getLatitude();
        double longitude = gpsTracker.getLocation().getLongitude();

        Marker marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude)
        ));
        marker.setVisible(true);
        marker.setTitle(eventName);
        marker.setSnippet(eventSnippet);
        marker.showInfoWindow();
        moveMapToLocation(latitude, longitude, STREET_DEPTH);
    }

    private void moveMapToLocation(double latitude, double longitude, int depth) {
        LatLng location = new LatLng(latitude, longitude);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location, depth);
        map.animateCamera(update);
    }

    private void setupButton() {
        Button button = (Button) findViewById(R.id.btnWhereAmI);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (! gpsTracker.isLocationUnknown()) {
                    Location loc = gpsTracker.getLocation();
                    moveMapToLocation(loc.getLatitude(), loc.getLongitude(), 16);
                }
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