package ca.sfu.generiglesias.dutchie_meetly;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import ca.sfu.generiglesias.dutchie_meetly.wifilogic.WifiDirectBroadcastReceiver;

/**
 * User can see all the details of the event including
 * its location on the map
 */
public class ViewEventActivity extends ActionBarActivity {
    // Display
    private String eventName;
    private String location;
    private String date;
    private String description;
    private String duration;
    private String event_author;
    private double latitude;
    private double longitude;

    private String startTime;
    private String endTime;
    // Used for calculation
    private Date eventEndTime = new Date();
    private Date eventDate = new Date();
    private Handler handler;
    private boolean running = true;
    // Wifi tools
    private TextView serverMessage;
    private WifiP2pManager wifiManager;
    private WifiP2pManager.Channel channel;
    private WifiDirectBroadcastReceiver receiver;
    private IntentFilter filter;
    private List<WifiP2pDevice> peers = new ArrayList();
    private WifiP2pManager.PeerListListener peerListListener;
    private boolean isWifiP2pEnabled = false;
    private WifiP2pDevice device;
    WifiP2pConfig config;
    private String userName, currentUser;
    private DBAdapter myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        openDB();

        SharedPreferences getUsernamePref = getSharedPreferences("UserName", MODE_PRIVATE);
        currentUser = getUsernamePref.getString("getUsername", "");
        invalidateOptionsMenu();

        extractAndInsertEventDetails();
        calculateTimeLeftUntilEvent();

        setupButtons();

        // wifi setup
        wifiManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = wifiManager.initialize(this, getMainLooper(), null);
        receiver = new WifiDirectBroadcastReceiver(wifiManager, channel, this);
        initializeIntentFilter();
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


    @Override
    protected void onResume() {
        super.onResume();
        //registerReceiver(receiver, filter);
    }

    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
        //unregisterReceiver(receiver);
    }


    private void initializeIntentFilter(){
        filter = new IntentFilter();
        filter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    public void displayAllPeers(){
        for(int i =0; i < peers.size();i++){
            Log.i("ViewEvent", peers.get(i).deviceName);
        }
    }



    public void setWifiP2pStatus(boolean isWifiP2pEnabled){
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }


    public void setupDiscoverPeersListener(){
        wifiManager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Peers Discovered", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reasonCode) {
                Toast.makeText(getApplicationContext(), "Cannot discover", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setupPeerListListener() {

        peerListListener = new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peerList) {


                // Out with the old, in with the new.
                peers.clear();
                peers.addAll(peerList.getDeviceList());

                // If an AdapterView is backed by this data, notify it
                // of the change.  For instance, if you have a ListView of available
                // peers, trigger an update.
                if (peers.size() == 0) {
                    Toast.makeText(getApplicationContext(), "No devices found", Toast.LENGTH_LONG);
                    return;
                }
                else{
                    displayAllPeers();
                }

            }
        };
    }

    private void setupButtons() {
        Button btnMap = (Button) findViewById(R.id.button_viewmap);

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickViewMap();
            }
        });
    }

    private void clickViewMap() {
        Intent intent = new Intent(getApplicationContext(), ViewEventMapActivity.class);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("EventName", eventName);
        intent.putExtra("Location", location);
        intent.putExtra("StartTime", startTime);
        intent.putExtra("EndTime", endTime);
        startActivity(intent);
    }


    private void extractAndInsertEventDetails() {

//       Intent intent = getIntent();
//        eventName = intent.getStringExtra("EventName");
//        location = intent.getStringExtra("Location");
//        date = intent.getStringExtra("Date");
//        description = intent.getStringExtra("Description");
//        duration = intent.getStringExtra("Duration");
//        startTime = intent.getStringExtra("startTime");
//        endTime = intent.getStringExtra("endTime");

        long event_id = getIntent().getLongExtra("event_id", 0);

        System.out.println(event_author);
        Cursor cursor = myDb.getRow(event_id);

        if (cursor.moveToFirst()) {
            do {
                eventName = cursor.getString(DBAdapter.COL_EVENTNAME);
                date = cursor.getString(DBAdapter.COL_EVENTDATE);
                location = cursor.getString(DBAdapter.COL_LOCATION);
                description = cursor.getString(DBAdapter.COL_EVENTDESCRIPTION);
                startTime = cursor.getString(DBAdapter.COL_EVENTSTARTTIME);
                endTime = cursor.getString(DBAdapter.COL_EVENTENDTIME);
                duration = cursor.getString(DBAdapter.COL_EVENTDURATION);
                latitude = cursor.getDouble(DBAdapter.COL_LATITUDE);
                longitude = cursor.getDouble(DBAdapter.COL_LONGITUDE);
                event_author = cursor.getString(DBAdapter.COL_EVENTAUTHOR);

            } while(cursor.moveToNext());
        }
        cursor.close();

        TextView view_eventName = (TextView) findViewById(R.id.event_view_id_name);
        view_eventName.setText(eventName);

        TextView view_eventDate = (TextView) findViewById(R.id.event_view_id_date);
        view_eventDate.setText(getResources().getString(R.string.event_view_date) + " " + date);

        TextView view_eventDescription = (TextView) findViewById(R.id.event_view_id_description);
        view_eventDescription.setText(getResources().getString(R.string.event_view_description) + " " + description);

        TextView view_eventLocation = (TextView) findViewById(R.id.event_view_id_location);
        view_eventLocation.setText(getResources().getString(R.string.event_view_location) + " " + location);

        TextView view_eventDuration = (TextView) findViewById(R.id.event_view_id_duration);
        view_eventDuration.setText(getResources().getString(R.string.event_view_duration) + " " + duration);

        TextView view_eventTimePeriod = (TextView) findViewById(R.id.event_view_id_timeperiod);
        view_eventTimePeriod.setText(getResources().getString(R.string.event_view_timeperiod) + startTime + "- " + endTime);

        TextView view_eventAuthor = (TextView) findViewById(R.id.event_view_id_author);
        view_eventAuthor.setText("Created By: " + event_author);
    }

    void calculateTimeLeftUntilEvent() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.CANADA);
        Calendar calendarEndTime = Calendar.getInstance();

        final Date currentDate = new Date();

        try {
            String parts[] = endTime.split(":");
            String endHours = parts[0];
            String endMinutes = parts[1];

            String eventDateToString = date + " " + startTime + ":00";
            eventDate = sdf.parse(eventDateToString);
            calendarEndTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endHours));
            calendarEndTime.set(Calendar.MINUTE, Integer.parseInt(endMinutes));
            eventEndTime = calendarEndTime.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        handler = new Handler();

        final Calendar eventCal = Calendar.getInstance();
        eventCal.setTime(eventDate);

        final TextView timeRemaining = (TextView) findViewById(R.id.event_view_id_time_remaining);
        timeRemaining.setText(getResources().getString(R.string.event_time_until));
        Animation TitleFade = AnimationUtils.loadAnimation(this, R.anim.fade_in2);
        timeRemaining.startAnimation(TitleFade);


        Runnable runnable = new Runnable() {
            Calendar nowCalendar = Calendar.getInstance();
            Date now = new Date();
            long diff;
            long newYears;
            long newMonths;
            long newDays;
            long newHours;
            long newMinutes;
            long newSeconds;

            @Override
            public void run() {
                while (running) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            diff = eventDate.getTime() - currentDate.getTime();
                            nowCalendar.setTimeInMillis(currentDate.getTime());
                            newYears = eventCal.get(Calendar.YEAR) - nowCalendar.get(Calendar.YEAR);
                            newMonths = (newYears * 12 +
                                    (eventCal.get(Calendar.MONTH)
                                            - nowCalendar.get(Calendar.MONTH))) % 12;
                            newDays = TimeUnit.MILLISECONDS.toDays(diff);
                            newHours = TimeUnit.MILLISECONDS.toHours(diff) % 24;
                            newMinutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60;
                            newSeconds = TimeUnit.MILLISECONDS.toSeconds(diff) % 60;

                            if (newSeconds < 0) {
                                if (eventEndTime.before(now)) {
                                    timeRemaining.setText(getResources().getString(R.string.event_status_completed));
                                } else {
                                    timeRemaining.setText(getResources().getString(R.string.event_status_started));
                                }
                                running = false;
                            } else {
                                displayTimeLeftUntilEvent(newYears, newMonths, newDays, newHours,
                                        newMinutes, newSeconds, timeRemaining, currentDate);
                            }
                        }
                    });


                }
            }
        };
        new Thread(runnable).start();
    }

    private void displayTimeLeftUntilEvent(long newYears, long newMonths, long newDays, long newHours,
                                           long newMinutes, long newSeconds, TextView timeRemaining,
                                           Date date1) {
        if (newYears > 0) {
            if (newYears == 1) {
                timeRemaining.setText("Time Until Event: " + newYears + " year");
            } else {
                timeRemaining.setText("Time Until Event: " + newYears + " years");
            }
        } else if (newMonths > 0 && newYears == 0) {
            if (newMonths == 1) {
                timeRemaining.setText("Time Until Event: " + newMonths + " month ");
            } else {
                timeRemaining.setText("Time Until Event: " + newMonths + " months");
            }
        } else {
            timeRemaining.setText("Time Until Event: " + ": " + newDays + "d " + +newHours +
                    "h " + newMinutes + "m " + newSeconds + "s");
        }
        date1.setTime(date1.getTime() + 1000L);
    }


    public void editEvent() {
        //startActivity(new Intent(ViewEventActivity.this, EditEventActivity.class));

        Intent intent = new Intent(this, EditEventActivity.class);
        intent.putExtra("event_name", eventName);
        intent.putExtra("event_date", date);
        intent.putExtra("event_location", location);
        intent.putExtra("event_description", description);
        intent.putExtra("event_start_time", startTime);
        intent.putExtra("event_duration", duration);
        intent.putExtra("event_end_time", endTime);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.edit_event) {
            editEvent();
            return true;
        } else if (id == R.id.share_event)
        {
            setupDiscoverPeersListener();
            setupPeerListListener();

            SharedPreferences getUsernamePref = getSharedPreferences("UserName", MODE_PRIVATE);
            userName = getUsernamePref.getString("getUsername", "");

            if(!userName.isEmpty())
            {
                Toast.makeText(getApplicationContext(), "'Planned' event shared",
                        Toast.LENGTH_SHORT).show();

                //Since we don't have a live server
                /********************************************
                 *                                          *
                 *                                          *
                 *                                          *
                 *                                          *
                 *                                          *
                 *                                          *
                 *           put server logic here          *
                 *                                          *
                 *                                          *
                 *                                          *
                 *                                          *
                 *                                          *
                 *                                          *
                 ********************************************/

            } else{
                Toast.makeText(getApplicationContext(), "'Spontaneous' event shared",
                        Toast.LENGTH_SHORT).show();

                //Put wifi/bluetooth logic here
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {

        MenuItem editMenuItem = menu.findItem(R.id.edit_event);

        if(currentUser.equals(event_author))
        {
            editMenuItem.setVisible(true);

        }else
        {
            editMenuItem.setVisible(false);
        }

        return true;
    }
}
