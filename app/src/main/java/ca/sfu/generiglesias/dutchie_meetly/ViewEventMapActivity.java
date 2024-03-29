package ca.sfu.generiglesias.dutchie_meetly;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ca.sfu.generiglesias.dutchie_meetly.maplogic.GPSTracker;
import ca.sfu.generiglesias.dutchie_meetly.maplogic.MapActions;

/**
 * Google Maps is used here for user to see
 * where the event is located.
 */
public class ViewEventMapActivity extends FragmentActivity {
    private final static int DEPTH = 17;
    private final static String TAG = "ViewEventMapActivity";

    private GoogleMap map;
    private GPSTracker gpsTracker;
    private Marker marker;
    private double latitude;
    private double longitude;
    private String startTime;
    private String endTime;
    private String name;
    private String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event_map);
        setUpMapIfNeeded();
        setupButtons();
        this.map.clear();
        this.latitude = getIntent().getDoubleExtra("latitude", Double.NaN);
        this.longitude = getIntent().getDoubleExtra("longitude", Double.NaN);
        Log.i(TAG, "WHYY IS THIS NOT WORKING?!?!");
        Log.i(TAG, "" + getIntent().getDoubleExtra("latitude", -12324));
        Log.i(TAG, ""+ getIntent().getDoubleExtra("longitude", -12324));
        this.name = "";
        this.gpsTracker = new GPSTracker(getApplicationContext());
        this.marker = map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));

        MapActions.moveMapToLocation(map, latitude, longitude);
    }

    private void clickFindEvent() {
        MapActions.moveMapToLocation(map, latitude, longitude);
    }
    private void clickFindMe() {
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
    private void setupButtons() {
        Button btnFindEvent = (Button) findViewById(R.id.btnFindEvent);
        btnFindEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickFindEvent();
            }
        });

        Button btnFindMe = (Button) findViewById(R.id.btnFindMe_view);
        btnFindMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickFindMe();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public void onBackPressed() {
        finish();
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
                map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        View view = getLayoutInflater().inflate(R.layout.info_window,null);
                        TextView tvLocality = (TextView) view.findViewById(R.id.info_window_locality);
                        TextView tvTime = (TextView) view.findViewById(R.id.info_window_time);
                        TextView tvSnippet = (TextView) view.findViewById(R.id.info_window_snippet);

                        name = getIntent().getStringExtra("EventName");
                        startTime = getIntent().getStringExtra("StartTime");
                        endTime = getIntent().getStringExtra("EndTime");
                        location = getIntent().getStringExtra("Location");
                        tvLocality.setText(name);
                        tvTime.setText(("Time: " +startTime + " - " + endTime ));
                        tvSnippet.setText(("Location: " + location));

                        return view;
                    }
                });
            }
        }
    }
    private void setUpMap() {
        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
}
