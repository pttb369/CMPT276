package ca.sfu.generiglesias.dutchie_meetly;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CreateEventActivity extends ActionBarActivity {

    private SimpleDateFormat dateFormatter;
    private DatePickerDialog DatePicker;
    private TimePickerDialog TimePick, TimePickEnd;
    private EditText eventTitle, eventDescription, eventLocation, eventDuration, showDate, showTime,
    showEndTime;
    private List<Event> events = new ArrayList<Event>();
    String evTitle, evDescription;
    Date evDate;
    private int startHour, startMinute, endHour, endMinute;
    private Calendar duration;
    EditText durationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.CANADA);
        evDate = new Date();

        eventTitle = (EditText) findViewById(R.id.eventTitleText);
        eventDescription = (EditText) findViewById(R.id.eventDescriptionId);

        evTitle = eventTitle.getText().toString();
        evDescription = eventTitle.toString();


        showDate = (EditText) findViewById(R.id.showDate);
        showDate.setInputType(InputType.TYPE_NULL);
        showDate.requestFocus();

        showTime = (EditText) findViewById(R.id.showTime);
        showTime.setInputType(InputType.TYPE_NULL);
        showTime.requestFocus();

        showEndTime = (EditText) findViewById(R.id.showEndTime);
        showEndTime.setInputType(InputType.TYPE_NULL);
        showEndTime.requestFocus();

        durationText = (EditText)findViewById(R.id.durationTime);
        durationText.setInputType(InputType.TYPE_NULL);
        durationText.requestFocus();

        setDate();
        setTime();
        setEndTime();

        duration = Calendar.getInstance();

        setupButtons();

    }

    private void setupButtons() {
        Button PickDateBtn = (Button) findViewById(R.id.pickDateButtonId);
        PickDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker.show();
            }
        });

        Button PickTimeBtn = (Button) findViewById(R.id.pickTimeButtonId);
        PickTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePick.show();
            }
        });

        Button PickEndTimeBtn = (Button) findViewById(R.id.pickTimeEndButtonId);
        PickEndTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickEnd.show();
            }
        });

        Button PickLocationBtn = (Button) findViewById(R.id.pickLocationId);
        PickLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchNewActivity = new Intent(getApplicationContext(),
                        MapActivity.class
                );

                launchNewActivity.putExtra(MapActivity.IS_CREATING, true);
                launchNewActivity.putExtra(MapActivity.NAME, "Choose Location");
                launchNewActivity.putExtra(MapActivity.SNIPPET, "");

                startActivity(launchNewActivity);
            }
        });

        Button CreateEventButton = (Button) findViewById(R.id.createEventButton);
        CreateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int tempEndHour = (endHour*60) + endMinute;
                int tempStartHour = (startHour*60) + startMinute;

                if(tempStartHour > tempEndHour)
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(CreateEventActivity.this);
                    builder1.setMessage("Start time cannot happen after end time, please choose" +
                            " a new start time.");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    showTime.setText(null);
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else{
                    duration.set(Calendar.HOUR_OF_DAY, endHour);
                    duration.set(Calendar.MINUTE, endMinute);

                    duration.add(Calendar.HOUR, -startHour);
                    duration.add(Calendar.MINUTE, -startMinute);

                    durationText.setText(duration.get(Calendar.HOUR_OF_DAY) + " Hours and " +
                            duration.get(Calendar.MINUTE) + " Minutes");
                }

//                events.add(new Event(
//                        eventTitle.getText().toString(),
//                        new Date(),
//                        "Surrey",
//                        evDescription,
//                        R.drawable.ic_launcher));

                events.add(new Event(
                        eventTitle.getText().toString(),
                        showDate.getText().toString(),
                        "Surrey",
                        eventDescription.getText().toString(),
                        showTime.getText().toString(),
                        showEndTime.getText().toString(),
                        durationText.getText().toString(),
                        R.drawable.ic_launcher));

                for(Event event: events) {
                    Log.i("Event Name", event.getEventName());
                    Log.i("Event Date", event.getEventDate());
                    Log.i("Event Duration", event.getEventDuration());
                }

                //Log.i()
            }
        });
    }

    private void calculateDuration(int startH, int startM, int endH, int endM)
    {

    }

    //Source: http://androidopentutorials.com/android-datepickerdialog-on-edittext-click-event/
    private void setDate() {

        Calendar newCalendar = Calendar.getInstance();
        DatePicker = new DatePickerDialog(this,
                new android.app.DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                showDate.setText(dateFormatter.format(newDate.getTime()));

            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void setTime(){
        Calendar TimeCalendar = Calendar.getInstance();
        int hour = TimeCalendar.get(Calendar.HOUR_OF_DAY);
        int minutes = TimeCalendar.get(Calendar.MINUTE);

        TimePick = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
                        startHour = hourOfDay;
                        startMinute = minute;
                        String output = String.format("%02d:%02d", hourOfDay, minute);
                        showTime.setText(output);
                    }

                }, hour, minutes, true);

    }

    private void setEndTime(){
        final Calendar TimeCalendar = Calendar.getInstance();
        int hour = TimeCalendar.get(Calendar.HOUR_OF_DAY);
        int minutes = TimeCalendar.get(Calendar.MINUTE);

        TimePickEnd = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
                        endHour = hourOfDay;
                        endMinute = minute;

                        String output = String.format("%02d:%02d", hourOfDay, minute);
                        showEndTime.setText(output);
                    }

                }, hour, minutes, true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_event, menu);
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
}
