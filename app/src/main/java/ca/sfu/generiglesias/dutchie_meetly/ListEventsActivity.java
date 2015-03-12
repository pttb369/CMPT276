package ca.sfu.generiglesias.dutchie_meetly;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ca.sfu.generiglesias.dutchie_meetly.maplogic.GPSTracker;

public class ListEventsActivity extends ActionBarActivity {
    public static final int INFO_KEY = 342;
    private static final String TAG = "ListEventsActivity";
    private FileOutputStream fileOutputStream;
    private FileInputStream fileInputStream;
    private ObjectOutputStream objectWrite;
    private ObjectInputStream objectRead;
    private List<Event> events = new ArrayList<Event>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);

        createEventButton();
        populateEventList();
        populateEventListView();
        registerClickCallback();
        setCurrentCity();
    }

    private void createEventButton(){
        Button CreateEventBtn = (Button) findViewById(R.id.event_create);
        CreateEventBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivityForResult(new Intent(ListEventsActivity.this, CreateEventActivity.class),
                        INFO_KEY);
            }
        });
    }

    private void populateEventList() {

        try {
            fileInputStream = openFileInput("eventListData");
            objectRead = new ObjectInputStream(fileInputStream);
            events = (ArrayList)objectRead.readObject();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        /*for(int i = 1; i < 2; i++) {
            events.add(new Event(
                    "Event " + i,
                    "123",
                    "City " + i,
                    "Description " + i,
                    "10:00",
                    "13:00",
                    "3 Hours 0 minutes",
                    R.drawable.ic_launcher,
                    49.187559,
                    -122.84954500000003
            ));
        }*/
    }

    private void sortEventList(List<Event> events) {

    }

    private void populateEventListView() {
        ArrayAdapter<Event> adapter = new EventListAdapter(getApplicationContext(), R.layout.event_list_item, events);
        ListView list = (ListView) findViewById(R.id.event_list_view);
        list.setAdapter(adapter);
    }

    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.event_list_view);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long id) {
                Event clickedEvent = events.get(position);
                Intent launchNewActivity = new Intent(getApplicationContext(), ViewEventActivity.class);
                launchNewActivity.putExtra("EventName", clickedEvent.getEventName());
                launchNewActivity.putExtra("Location", clickedEvent.getCityName());
                launchNewActivity.putExtra("Date", clickedEvent.getEventDate());
                launchNewActivity.putExtra("Description", clickedEvent.getEventDescription());
                launchNewActivity.putExtra("latitude", clickedEvent.getLatitude());
                launchNewActivity.putExtra("longitude", clickedEvent.getLongitude());
                launchNewActivity.putExtra("startTime", clickedEvent.getEventStartTime());
                launchNewActivity.putExtra("endTime", clickedEvent.getEventEndTime());
                startActivity(launchNewActivity);
            }
        });
    }

    public void setCurrentCity() {
        TextView currentLocation = (TextView) findViewById(R.id.currentLocation);
        currentLocation.setText(getCurrentCity());
    }

    //http://stackoverflow.com/questions/20325427/get-current-location-city-name-android
    private String getCurrentCity() {
        String cityName = "Unknown Location";

        Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
        GPSTracker gpsTracker = new GPSTracker(getApplicationContext());

        try {
            Location loc = gpsTracker.getLocation();
            List<Address> addresses = gcd.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            cityName = addresses.get(0).getLocality();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

        return cityName;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INFO_KEY) {
            if (resultCode == RESULT_OK) {

                this.events.add(new Event(
                        data.getStringExtra("name"),
                        data.getStringExtra("date"),
                        data.getStringExtra("cityName"),
                        data.getStringExtra("description"),
                        data.getStringExtra("startTime"),
                        data.getStringExtra("endTime"),
                        data.getStringExtra("duration"),
                        R.drawable.ic_launcher,
                        data.getDoubleExtra("latitude", Double.NaN),
                        data.getDoubleExtra("longitude", Double.NaN)
                ));

                try {
                    fileOutputStream = openFileOutput("eventListData", Context.MODE_PRIVATE);
                    objectWrite = new ObjectOutputStream(fileOutputStream );
                    objectWrite.writeObject(events);
                    objectWrite.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }catch (IOException e) {
                    e.printStackTrace();
                }


                populateEventListView();
            }
        }
    }
}
