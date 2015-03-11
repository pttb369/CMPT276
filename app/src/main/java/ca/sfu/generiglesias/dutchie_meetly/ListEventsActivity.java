package ca.sfu.generiglesias.dutchie_meetly;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ca.sfu.generiglesias.dutchie_meetly.maplogic.GPSTracker;


public class ListEventsActivity extends ActionBarActivity {

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
                startActivity(new Intent(ListEventsActivity.this, CreateEventActivity.class));
            }
        });
    }

    //eventTitle.getText().toString()

    private void populateEventList() {
        for(int i = 1; i < 20; i++) {
            events.add(new Event(
                    "Event " + i,
                    "123",
                    "City " + i,
                    "Description " + i,
                    "10:00",
                    "13:00",
                    "3 Hours 0 minutes",
                    R.drawable.ic_launcher));
        }
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

                EventHolder.refresh();
                EventHolder.setName(clickedEvent.getEventName());
                EventHolder.setLatitude(clickedEvent.getLatitude());
                EventHolder.setLongitude(clickedEvent.getLongitude());

                Intent launchNewActivity = new Intent(getApplicationContext(),ViewEventActivity.class);
                launchNewActivity.putExtra("EventName",clickedEvent.getEventName());
                launchNewActivity.putExtra("Location",clickedEvent.getCityName());
                launchNewActivity.putExtra("Date",clickedEvent.getEventDate());
                launchNewActivity.putExtra("Description",clickedEvent.getEventDescription());
                startActivity(launchNewActivity);
            }
        });
    }

    public void setCurrentCity() {
        TextView currentLocation = (TextView) findViewById(R.id.currentLocation);
        currentLocation.setText(getCurrentCity());
    }

    private String getCurrentCity() {
        String cityName = "Unknown Location";

        Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
        GPSTracker gpsTracker = new GPSTracker(getApplicationContext());

        try {

            Location loc = gpsTracker.getLocation();
            List<Address> addresses = gcd.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            cityName = addresses.get(0).getLocality();
//            for(Address address: addresses) {
//                Log.i("Address", address.getLocality());
//                cityName = address.getLocality();
//            }
        } catch (IOException ex) {

        } catch (NullPointerException ex) {

        }

        return cityName;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
