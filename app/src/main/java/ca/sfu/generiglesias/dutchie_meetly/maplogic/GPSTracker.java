package ca.sfu.generiglesias.dutchie_meetly.maplogic;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class GPSTracker implements LocationListener {
    // 5 min
    private final int MIN_TIME = 5 * 6 * 1000;
    // 10 meters
    private final int MIN_DISTANCE = 10;

    private LocationManager locationManager;

    private Location currentLocation;

    public GPSTracker(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (isGPSEnabled) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    MIN_TIME, MIN_DISTANCE, this);

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {
                currentLocation = location;
            } else {
                Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT);
            }
        } else {
            Toast.makeText(context, "Please enable GPS", Toast.LENGTH_SHORT);
        }
    }

    public boolean isLocationUnknown() {
        return currentLocation == null;
    }

    public boolean isGPSEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public Location getLocation() {
        return currentLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
