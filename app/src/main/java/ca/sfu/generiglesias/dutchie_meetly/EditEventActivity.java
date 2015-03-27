package ca.sfu.generiglesias.dutchie_meetly;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class EditEventActivity extends ActionBarActivity {

    private EditText eventTitle,
            eventDescription,
            eventLocation,
            eventDuration,
            eventDate,
            eventStartTime,
            eventEndTime;
    private Calendar duration;
   // private int startHour, startMinute, endHour, endMinute;
    private int month, day, years;
    private TimePickerDialog startTimePickerDialog, endTimePickerDialog, timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private double lat, lng;

    private final String DELIMITER = ":";
    private final int HOUR_INDEX = 0;
    private final int MINUTE_INDEX = 1;
    private final int MINUTES_IN_HOUR = 60;

    public static final int REQUEST_CODE = 666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        setupEditableFields();
        setCurrentEventValues();

        setupListeners();
    }

    public void setupEditableFields() {
        eventTitle = (EditText) findViewById(R.id.create_event_name);
        eventDescription = (EditText) findViewById(R.id.create_event_description);

        eventDate = (EditText) findViewById(R.id.create_event_date);
        eventDate.setInputType(InputType.TYPE_NULL);
        eventDate.requestFocus();

        eventStartTime = (EditText) findViewById(R.id.create_event_start_time);
        eventStartTime.setInputType(InputType.TYPE_NULL);
        eventStartTime.requestFocus();

        eventEndTime = (EditText) findViewById(R.id.create_event_end_time);
        eventEndTime.setInputType(InputType.TYPE_NULL);
        eventEndTime.requestFocus();

        eventLocation = (EditText) findViewById(R.id.create_event_location);
        eventLocation.setInputType(InputType.TYPE_NULL);
        eventLocation.requestFocus();

        eventDuration = (EditText) findViewById((R.id.showDuration));
        duration = Calendar.getInstance();
    }

    private void  setCurrentEventValues() {
        eventTitle.setText(getIntent().getStringExtra("event_name"));
        eventDate.setText(getIntent().getStringExtra("event_date"));
        eventLocation.setText(getIntent().getStringExtra("event_location"));
        eventDescription.setText(getIntent().getStringExtra("event_description"));
        eventStartTime.setText(getIntent().getStringExtra("event_start_time"));
        eventEndTime.setText(getIntent().getStringExtra("event_end_time"));
        //eventDuration.setText(getIntent().getStringExtra("event_duration"));
    }

    private void setupListeners() {
        setupEventDateListener();
        setupStartTimeListener();
        setupEndTimeListener();
        setupLocationListener();
    }


    private void setupEventDateListener() {
        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
                datePickerDialog.show();
            }
        });
    }

    private void setupStartTimeListener() {
        eventStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewTime(eventStartTime);
                timePickerDialog.show();
//                int[] time = splitString(eventStartTime.getText().toString(), DELIMITER);
//                startHour = time[0];
//                startMinute = time[1];
                //checkEventTimeInput();
            }
        });
    }

    private void setupEndTimeListener() {
        eventEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewTime(eventEndTime);
                timePickerDialog.show();
//                int[] time = splitString(eventEndTime.getText().toString(), DELIMITER);
//                endHour = time[0];
//                endMinute = time[1];
                //checkEventTimeInput();
            }
        });
    }

    private int[] splitString(String text, String delimiter) {
        String[] words = text.split(delimiter);
        int[] intArray = new int[words.length];
        for(int i = 0; i < words.length; i++) {
            intArray[i] = Integer.parseInt(words[i]);
        }

        return intArray;
    }

    private void setupLocationListener() {
        eventLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                    new Intent(getApplicationContext(), CreateEventMapActivity.class),
                    REQUEST_CODE);
            }
        });
    }

    private void setDate() {
        Calendar newCalendar = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.CANADA);
        datePickerDialog = new DatePickerDialog(this,
                new android.app.DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        day = dayOfMonth;
                        month = monthOfYear + 1;
                        years = year;
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        eventDate.setText(dateFormat.format(newDate.getTime()));
                    }
                },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void setNewTime(EditText eventTime){
        Calendar TimeCalendar = Calendar.getInstance();
        int hours = TimeCalendar.get(Calendar.HOUR_OF_DAY);
        int minutes = TimeCalendar.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(this,
                            eventSetTimeListener(eventTime),
                            hours,
                            minutes,
                            true);


    }

    private TimePickerDialog.OnTimeSetListener eventSetTimeListener(final EditText eventTime) {
       return new TimePickerDialog.OnTimeSetListener() {

            public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
                String output = String.format("%02d:%02d", hourOfDay, minute);
                eventTime.setText(output);
                checkEventTimeInput();
            }
        };
    }

//    private void setStartTime(){
//        Calendar TimeCalendar = Calendar.getInstance();
//        int hour = TimeCalendar.get(Calendar.HOUR_OF_DAY);
//        int minutes = TimeCalendar.get(Calendar.MINUTE);
//
//        startTimePickerDialog = new TimePickerDialog(this,
//                new TimePickerDialog.OnTimeSetListener() {
//
//                    public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
//                        String output = String.format("%02d:%02d", hourOfDay, minute);
//                        eventStartTime.setText(output);
//
//                        String startTime = eventStartTime.getText().toString();
//                        String endTime = eventEndTime.getText().toString();
//                        startHour = hourOfDay;
//                        startMinute = minute;
//
//                        if(!startTime.isEmpty() && !endTime.isEmpty()) {
//                            setupDuration();
//                        }
//                    }
//
//                }, hour, minutes, true);
//    }
//
//    private void setEndTime(){
//        Calendar TimeCalendar = Calendar.getInstance();
//        int hour = TimeCalendar.get(Calendar.HOUR_OF_DAY);
//        int minutes = TimeCalendar.get(Calendar.MINUTE);
//
//        endTimePickerDialog = new TimePickerDialog(this,
//                new TimePickerDialog.OnTimeSetListener() {
//                    public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
//                        String output = String.format("%02d:%02d", hourOfDay, minute);
//                        eventEndTime.setText(output);
//                        String startTime = eventStartTime.getText().toString();
//                        String endTime = eventEndTime.getText().toString();
//                        endHour = hourOfDay;
//                        endMinute = minute;
//
//                        if(!startTime.isEmpty()&& !endTime.isEmpty()) {
//                            setupDuration();
//                        }
//                    }
//                }, hour, minutes, true);
//    }

    private void checkEventTimeInput() {
        String startTime = eventStartTime.getText().toString();
        String endTime = eventEndTime.getText().toString();

        if(!startTime.isEmpty() && !endTime.isEmpty()) {
            setupDuration();
        }
    }

    protected void setupDuration() {

        int[] startTime = splitString(eventStartTime.getText().toString(), DELIMITER);
        int startHour = startTime[HOUR_INDEX];
        int startMinute = startTime[MINUTE_INDEX];

        int[] endTime = splitString(eventEndTime.getText().toString(), DELIMITER);
        int endHour = endTime[HOUR_INDEX];
        int endMinute = endTime[MINUTE_INDEX];

        int tempEndHour = (endHour * MINUTES_IN_HOUR) + endMinute;
        int tempStartHour = (startHour * MINUTES_IN_HOUR) + startMinute;

        if (tempStartHour > tempEndHour) {
            eventStartTime.setText("");
            eventDuration.setText("");
            Toast.makeText(getApplicationContext(), "Start time cannot occur after end time. Choose" +
                            " a new time.",
                    Toast.LENGTH_SHORT).show();
        } else {
            duration.set(Calendar.HOUR_OF_DAY, endHour);
            duration.set(Calendar.MINUTE, endMinute);

            duration.add(Calendar.HOUR, -startHour);
            duration.add(Calendar.MINUTE, -startMinute);

            eventDuration.setText(
                    duration.get(Calendar.HOUR_OF_DAY)
                            + " Hours and " +
                    duration.get(Calendar.MINUTE) + " Minutes");

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 666) {
            if (resultCode == RESULT_OK) {
                lat = data.getDoubleExtra("latitude", 0);
                lng = data.getDoubleExtra("longitude", 0);
                eventLocation.setText(getLocation(lat, lng));
            }
        }
    }

    private String getLocation(double lat, double lng) {
        String eventLocation = getResources().getString(R.string.unknown_location);
        Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            List<Address> addresses = gcd.getFromLocation(lat, lng, 1);
            eventLocation = addresses.get(0).getAddressLine(0)
                    + " " + addresses.get(0).getAddressLine(1);

        } catch (IOException | NullPointerException | IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }

        return eventLocation;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_event, menu);
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
