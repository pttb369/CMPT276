package ca.sfu.generiglesias.dutchie_meetly;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 *  Shows a UI for user to create the event
 */
public class CreateEventActivity extends ActionBarActivity {
    public static final int REQUEST_CODE = 666;

    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog startTimePickerDialog, endTimePickerDialog;
    private EditText eventTitle, eventDescription, eventLocation, eventDuration, eventDate, eventStartTime, eventEndTime;
    private int startHour, startMinute, endHour, endMinute;
    private Calendar duration;
    private double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.CANADA);

        getViewItemsById();
        setDate();
        setStartTime();
        setEndTime();

        setupButtons();

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

                boolean validDetails = (!currentEventName.isEmpty()
                        && !cityName.isEmpty()
                        && !currentEventDescription.isEmpty()
                        && !currentEventDate.isEmpty()
                        && !startTime.isEmpty()
                        && !endTime.isEmpty())
                        && !durationTime.isEmpty();

                if (validDetails) {
                    Intent returnIntent = new Intent();
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

                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Not all details set", Toast.LENGTH_SHORT)
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
            Toast.makeText(getApplicationContext(), "Start time cannot occur after end time. Choose" +
                            " a new time.",
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
}
