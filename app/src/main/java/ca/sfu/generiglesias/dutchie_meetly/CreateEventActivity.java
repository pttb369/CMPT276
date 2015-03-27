package ca.sfu.generiglesias.dutchie_meetly;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 *  Shows a UI for user to create the event
 */
public class CreateEventActivity extends ActionBarActivity implements MeetlyServer{
    public static final int REQUEST_CODE = 666;

    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog startTimePickerDialog, endTimePickerDialog;
    private EditText eventTitle, eventDescription, eventLocation, eventDuration, eventDate, eventStartTime, eventEndTime;
    private int startHour, startMinute, endHour, endMinute;
    private Calendar duration, StartTime, EndTime;
    private double lat, lng;
    private int month, day, years;

    private DBAdapter myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        openDB();

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.CANADA);
        StartTime = Calendar.getInstance();
        EndTime = Calendar.getInstance();

        getViewItemsById();
        setDate();
        setStartTime();
        setEndTime();

        setupButtons();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }
    private void closeDB() {
        myDb.close();
    }

    private void getViewItemsById() {
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

    private void setupButtons() {
        setupEventDateListener();
        setupStartTimeListener();
        setupEndTimeListener();
        setupLocationListener();
        setupCreateEventButton();
    }

    private void setupEventDateListener() {
        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
    }

    private void setupStartTimeListener() {
        eventStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimePickerDialog.show();
            }
        });
    }

    private void setupEndTimeListener() {
        eventEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTimePickerDialog.show();
            }
        });
    }

    private void setupLocationListener() {
        eventLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EventHolder.refresh();
                startActivityForResult(
                        new Intent(getApplicationContext(), CreateEventMapActivity.class),
                        REQUEST_CODE);
            }
        });
    }

    private void setupCreateEventButton() {
        Button CreateEventButton = (Button) findViewById(R.id.createEventButton);
        CreateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String currentEventName = eventTitle.getText().toString();
                String currentEventDate = eventDate.getText().toString();
                String cityName = eventLocation.getText().toString();
                String currentEventDescription = eventDescription.getText().toString();
                String startTime = eventStartTime.getText().toString();
                String endTime = eventEndTime.getText().toString();
                String durationTime = eventDuration.getText().toString();

                StartTime.set(years, month, day, startHour, startMinute);
                EndTime.set(years, month, day, endHour, endMinute);

                System.out.println(month);

                boolean validDetails = (!currentEventName.isEmpty()
                        && !cityName.isEmpty()
                        && !currentEventDescription.isEmpty()
                        && !currentEventDate.isEmpty()
                        && !startTime.isEmpty()
                        && !endTime.isEmpty())
                        && !durationTime.isEmpty();

                if (validDetails) {

                    final long insertedId = myDb.insertRow(
                            currentEventName,
                            currentEventDate,
                            cityName,
                            currentEventDescription,
                            startTime,
                            endTime,
                            durationTime,
                            lat,
                            lng);

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("eventId", insertedId);
                    returnIntent.putExtra("name", currentEventName);
                    returnIntent.putExtra("date", currentEventDate);
                    returnIntent.putExtra("cityName", cityName);
                    returnIntent.putExtra("description", currentEventDescription);
                    returnIntent.putExtra("startTime", startTime);
                    returnIntent.putExtra("duration", durationTime);
                    returnIntent.putExtra("endTime", endTime);
                    returnIntent.putExtra("latitude", lat);
                    returnIntent.putExtra("longitude", lng);
                    setResult(RESULT_OK, returnIntent);



                    try {
                        publishEvent("Test", 0, currentEventName, StartTime, EndTime, lat, lng);
                    } catch (FailedPublicationException e) {
                        e.printStackTrace();
                    }
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.check_details, Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    private String getCityName(double lat, double lng) {
        String cityName = "Unknown Location";
        Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            List<Address> addresses = gcd.getFromLocation(lat, lng, 1);
            //cityName = addresses.get(0).getLocality();
            cityName = addresses.get(0).getAddressLine(0)
                    + " " + addresses.get(0).getAddressLine(1);

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }

        return cityName;
    }

    //Source: http://androidopentutorials.com/android-datepickerdialog-on-edittext-click-event/
    private void setDate() {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this,
                new android.app.DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        day = dayOfMonth;
                        month = monthOfYear + 1;
                        years = year;
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        eventDate.setText(dateFormatter.format(newDate.getTime()));
                    }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void setStartTime(){
        Calendar TimeCalendar = Calendar.getInstance();
        int hour = TimeCalendar.get(Calendar.HOUR_OF_DAY);
        int minutes = TimeCalendar.get(Calendar.MINUTE);

        startTimePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
                        String output = String.format("%02d:%02d", hourOfDay, minute);
                        eventStartTime.setText(output);

                        String startTime = eventStartTime.getText().toString();
                        String endTime = eventEndTime.getText().toString();
                        startHour = hourOfDay;
                        startMinute = minute;

                        if(!startTime.isEmpty() && !endTime.isEmpty()) {
                            setupDuration();
                        }
                    }

                }, hour, minutes, true);
    }

    private void setEndTime(){
        Calendar TimeCalendar = Calendar.getInstance();
        int hour = TimeCalendar.get(Calendar.HOUR_OF_DAY);
        int minutes = TimeCalendar.get(Calendar.MINUTE);

        endTimePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
                        String output = String.format("%02d:%02d", hourOfDay, minute);
                        eventEndTime.setText(output);
                        String startTime = eventStartTime.getText().toString();
                        String endTime = eventEndTime.getText().toString();
                        endHour = hourOfDay;
                        endMinute = minute;


                        if(!startTime.isEmpty()&& !endTime.isEmpty())
                        {
                            setupDuration();
                        }
                    }
                }, hour, minutes, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 666) {
            if (resultCode == RESULT_OK) {
                lat = data.getDoubleExtra("latitude", 0);
                lng = data.getDoubleExtra("longitude", 0);
                String cityName = getCityName(lat, lng);
                //eventLocation.setText(lat + ":" + lng);
                eventLocation.setText(cityName);
            }
        }
    }

    protected void setupDuration()
    {
        int tempEndHour = (endHour*60) + endMinute;
        int tempStartHour = (startHour*60) + startMinute;

        if(tempStartHour > tempEndHour)
        {
            eventStartTime.setText("");
            eventDuration.setText("");
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_duration),
                    Toast.LENGTH_SHORT).show();
        }else {
            duration.set(Calendar.HOUR_OF_DAY, endHour);
            duration.set(Calendar.MINUTE, endMinute);

            duration.add(Calendar.HOUR, -startHour);
            duration.add(Calendar.MINUTE, -startMinute);

            eventDuration.setText(duration.get(Calendar.HOUR_OF_DAY) + " Hours and " +
                    duration.get(Calendar.MINUTE) + " Minutes");
            Log.i("Event Duration", eventDuration.getText().toString());
        }
    }

    @Override
    public int login(String username, String password) throws FailedLoginException {
        return 0;
    }

    @Override
    public int publishEvent(String username, int userToken, String title, Calendar startTime, Calendar endTime, double latitude, double longitude) throws FailedPublicationException {
        return 0;
    }

    @Override
    public void modifyEvent(int eventID, int userToken, String title, Calendar startTime, Calendar endTime, double latitude, double longitude) throws FailedPublicationException {

    }
}
