package ca.sfu.generiglesias.dutchie_meetly;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import ca.sfu.generiglesias.dutchie_meetly.maplogic.GPSTracker;

/**
 * User can see a list of their created events
 */
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

        setActionBarName();
        populateEventList();
        sortEventList();
        populateEventListView();
        registerClickCallback();
        setCurrentCity();
        setCurrentUsername();
    }

    private void setCurrentUsername() {
        SharedPreferences getUsernamePref = getSharedPreferences("UserName", MODE_PRIVATE);
        String userName = getUsernamePref.getString("getUsername", "");

        System.out.println(userName);

        TextView currentUsername = (TextView) findViewById(R.id.usernameView);
        currentUsername.setText(userName);
    }

    private void setActionBarName() {
        ActionBar actionBar = getSupportActionBar();
        String appName = (String)getResources().getText(R.string.app_name);
        actionBar.setTitle(appName);
    }

    private void createNewEvent(){
        startActivityForResult(new Intent(ListEventsActivity.this, CreateEventActivity.class),
                INFO_KEY);
    }

    private void populateEventList() {

        //http://www.eracer.de/2012/07/09/android-objectinputstream-and-objectoutputstream-snippet/
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

    private void sortEventList() {
        Collections.sort(events, new Comparator<Event>() {

            @Override
            public int compare(Event lhs, Event rhs) {
                int[] lhsDate = splitString(lhs.getEventDate(), "-");
                int[] rhsDate = splitString(rhs.getEventDate(), "-");
                int[] lhsStartTime = splitString(lhs.getEventStartTime(), ":");
                int[] rhsStartTime = splitString(rhs.getEventStartTime(), ":");


                Calendar lhsCal = Calendar.getInstance();
                Calendar rhsCal = Calendar.getInstance();
                lhsCal.set(lhsDate[2], lhsDate[1], lhsDate[0], lhsStartTime[0], lhsStartTime[1]);

                rhsCal.set(rhsDate[2], rhsDate[1], rhsDate[0], rhsStartTime[0], lhsStartTime[1]);

                return lhsCal.compareTo(rhsCal);
            }
        });
    }

    private int[] splitString(String characters, String delimiter) {
        String[] splitedChar = characters.split(delimiter);
        int[] intArray = new int[splitedChar.length];
        for(int i = 0; i < splitedChar.length; i++) {
            intArray[i] = Integer.parseInt(splitedChar[i]);
        }

        return intArray;
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
                launchNewActivity.putExtra("Duration", clickedEvent.getEventDuration());
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

                sortEventList();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        if (id == R.id.new_event) {
            createNewEvent();
            return true;
        } else if (id == R.id.login_event)
        {
            startActivityForResult(new Intent(ListEventsActivity.this, LoginActivity.class),
                    INFO_KEY);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
