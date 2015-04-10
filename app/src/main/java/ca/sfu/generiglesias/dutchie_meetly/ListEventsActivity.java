package ca.sfu.generiglesias.dutchie_meetly;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.ActionBar;
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
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
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

import ca.sfu.generiglesias.dutchie_meetly.bluetoothlogic.BluetoothReader;
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
    public static TextView currentUsername;
    private Menu menu;
    private String userN, userName, author;
<<<<<<< HEAD
=======

>>>>>>> c1865bbe4b30a98b57a2036bbed69987c69a24a1
    private int userId;
    private int selectedFrequencyVal = 0;

    private DBAdapter myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);

        openDB();

        setActionBarName();
        setupEventList();
        setCurrentCity();
        setCurrentUsername();

    }

    private void setupEventList() {
        BluetoothReader.read(getApplicationContext());
        events.clear();
        getAllEventsFromDatabase();
        sortEventList();
        populateEventListView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }


    private void openDB() {
        myDb = new DBAdapter(getApplicationContext());
        myDb.open();
    }
    private void closeDB() {
        myDb.close();
    }

    private void setCurrentUsername() {
        SharedPreferences getUsernamePref = getSharedPreferences("UserName", MODE_PRIVATE);
        userName = getUsernamePref.getString("getUsername", "");
        userId = getUsernamePref.getInt("getUserToken", 0);

        currentUsername = (TextView) findViewById(R.id.usernameView);
        currentUsername.setText(getResources().getString(R.string.user_title) + userName);

        userN = userName;
    }

    private void setActionBarName() {
        ActionBar actionBar = getSupportActionBar();
        String appName = (String)getResources().getText(R.string.app_name);
        actionBar.setTitle(appName);
    }

    private void createNewEvent(){
        startActivityForResult(
                new Intent(ListEventsActivity.this, CreateEventActivity.class),
                INFO_KEY);
    }

    private void getAllEventsFromDatabase() {
        Cursor cursor = myDb.getAllRows(); //function to retrieve all values from a table- written in MyDb.java file

        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                int eventId = cursor.getInt(DBAdapter.COL_ROWID);
                String eventName = cursor.getString(DBAdapter.COL_EVENTNAME);
                String eventDate = cursor.getString(DBAdapter.COL_EVENTDATE);
                String eventLocation = cursor.getString(DBAdapter.COL_LOCATION);
                String eventDescription = cursor.getString(DBAdapter.COL_EVENTDESCRIPTION);
                String eventStartTme = cursor.getString(DBAdapter.COL_EVENTSTARTTIME);
                String eventEndTime = cursor.getString(DBAdapter.COL_EVENTENDTIME);
                String eventDuration = cursor.getString(DBAdapter.COL_EVENTDURATION);
                int iconId = R.drawable.communityimage;
                double latitude = cursor.getDouble(DBAdapter.COL_LATITUDE);
                double longitude = cursor.getDouble(DBAdapter.COL_LONGITUDE);
                String sharedFlag = cursor.getString(DBAdapter.COL_SHAREDFLAG);
                String eventAuthor = cursor.getString(DBAdapter.COL_EVENTAUTHOR);

                events.add(new Event
                            (eventId,
                            eventName,
                            eventDate,
                            eventLocation,
                            eventDescription,
                            eventStartTme,
                            eventEndTime,
                            eventDuration,
                            iconId,
                            latitude,
                            longitude,
                            sharedFlag,
                            eventAuthor
                                    ));
            } while(cursor.moveToNext());
        }
        cursor.close();
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

        registerClickCallback();
    }

    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.event_list_view);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long id) {

                Event clickedEvent = events.get(position);
                Intent launchNewActivity = new Intent(getApplicationContext(), ViewEventActivity.class);
                long k = clickedEvent.getEventId();
                launchNewActivity.putExtra("event_id", k);
                launchNewActivity.putExtra("eventAuthor", author);
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
        String cityName = getResources().getString(R.string.unknown_location_title);

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
                //author = data.getStringExtra("eventAuthor");
                this.events.add(new Event(
                        data.getLongExtra("eventId", 0),
                        data.getStringExtra("name"),
                        data.getStringExtra("date"),
                        data.getStringExtra("cityName"),
                        data.getStringExtra("description"),
                        data.getStringExtra("startTime"),
                        data.getStringExtra("endTime"),
                        data.getStringExtra("duration"),
                        R.drawable.communityimage,
                        data.getDoubleExtra("latitude", Double.NaN),
                        data.getDoubleExtra("longitude", Double.NaN),
                        data.getStringExtra("sharedFlag"),
                        data.getStringExtra("eventAuthor")
                ));
                sortEventList();
                populateEventListView();
            }

            if (resultCode == 1)
            {
                setCurrentUsername();
                invalidateOptionsMenu();
            }
        }
    }

    public void fetchEventsFromCentralServer() throws MeetlyServer.FailedFetchException {

//        for (Event e : MeetlyServer.fetchEventsAfter(1)) {
//            Log.i("DBTester", "Event " + e.title);
//        }
        List<Event> eventsFromCentralServer;

        MeetlyServer server = new MeetlyServerImpl();

        eventsFromCentralServer = server.fetchEventsAfter(1);

        for(int i =0; i < eventsFromCentralServer.size();i++){
            Log.i("Central Server Event",eventsFromCentralServer.get(i).getEventName());
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
        } else if(id == R.id.settings) {
            Toast.makeText(this,"Settings haven selected", Toast.LENGTH_SHORT);
//            startActivityForResult(
//                    new Intent(ListEventsActivity.this, UpdateFrequency.class),
//                    INFO_KEY);
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.activity_update_frequency);
            dialog.setTitle("Update Frequency");


            createUpdateFrequencyDialog(dialog);

            dialog.show();

        } else if (id == R.id.login_event) {
            startActivityForResult(new Intent(ListEventsActivity.this, LoginActivity.class),
                    INFO_KEY);
            return true;
        } else if (id == R.id.logout_event) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage(getResources().getString(R.string.logout_prompt_title));
            builder1.setCancelable(true);
            builder1.setPositiveButton(getResources().getString(R.string.no_title),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            builder1.setNegativeButton(getResources().getString(R.string.yes_title),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SharedPreferences UserNamePref = getSharedPreferences("UserName", MODE_PRIVATE);
                            SharedPreferences.Editor editor = UserNamePref.edit();
                            editor.putString("getUsername", "");
                            editor.putInt("getUserToken", -999);
                            editor.commit();
                            userName = "";
                            currentUsername.setText("User: " + userName);
                            userN = userName;
                            invalidateOptionsMenu();
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

            return true;
        } else if(id == R.id.fetch_event){
            try {
                fetchEventsFromCentralServer();
            }
            catch(MeetlyServer.FailedFetchException e){
                e.printStackTrace();
            }
        }


        return super.onOptionsItemSelected(item);
    }

    private void createUpdateFrequencyDialog(final Dialog dialog) {
        NumberPicker np = (NumberPicker)dialog.findViewById(R.id.number_picker);
        np.setMinValue(1);// restricted number to minimum value i.e 1
        np.setMaxValue(31);// restricked number to maximum value i.e. 31
        np.setWrapSelectorWheel(true);

        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal)
            {
                selectedFrequencyVal = newVal;
                Log.i("Selected Value:", Integer.toString(selectedFrequencyVal));
            }
        });

        Button buttonApply = (Button) dialog.findViewById(R.id.apply_button);
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // update the frequency time on server with the selected value.
                final MeetlyServer server = new MeetlyServerImpl();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            for (Event e : server.fetchEventsAfter(selectedFrequencyVal)) {
                                Log.i("DBTester", "Event " + e.getEventName());
                                Log.i("Retrieved Start Time: ", e.getEventStartTime());
                                Log.i("Retrieved End Time: ", e.getEventStartTime());
                            }

                            Log.i("Set Value:", "True");
                        } catch (MeetlyServer.FailedFetchException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                dialog.dismiss();
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem login = menu.findItem(R.id.login_event);
        MenuItem logout = menu.findItem(R.id.logout_event);

        if (userN.isEmpty()) {
            logout.setVisible(false);
            login.setVisible(true);
        } else {
            login.setVisible(false);
            logout.setVisible(true);
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupEventList();
    }
}
