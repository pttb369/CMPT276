package ca.sfu.generiglesias.dutchie_meetly;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class CreateEventActivity extends ActionBarActivity {
    public static final int REQUEST_CODE = 666;

    private SimpleDateFormat dateFormatter;
    private DatePickerDialog DatePicker;
    private TimePickerDialog TimePick;
    private EditText eventTitle, eventDescription, eventLocation, eventDuration, showDate, showTime;
    private EditText showLocation;
    String evTitle, evDescription;
    Date evDate;

    //Event Information
    private String name;
    private String date;
    private String cityName;
    private String description;
    private String startTime;
    private String endTime;
    private double latitude;
    private double longitude;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.CANADA);
        evDate = new Date();

        eventTitle = (EditText) findViewById(R.id.eventTitleText);
        eventDescription = (EditText) findViewById(R.id.eventDescriptionId);

        evTitle = eventTitle.toString();
        evDescription = eventTitle.toString();


        showDate = (EditText) findViewById(R.id.showDate);
        showDate.setInputType(InputType.TYPE_NULL);
        showDate.requestFocus();

        showTime = (EditText) findViewById(R.id.showTime);
        showTime.setInputType(InputType.TYPE_NULL);
        showTime.requestFocus();

        showLocation = (EditText) findViewById(R.id.showLocation);
        showLocation.setInputType(InputType.TYPE_NULL);
        showLocation.requestFocus();

        setDate();
        setTime();

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

        Button PickLocationBtn = (Button) findViewById(R.id.pickLocationId);
        PickLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(getApplicationContext(), CreateEventMapActivity.class),
                        REQUEST_CODE);
            }
        });

        Button CreateEventButton = (Button) findViewById(R.id.createEventButton);
        CreateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                endTime = "DUMBBBBBBBBBB";
                name = "This string is not true.";

                boolean validDetails = name != null &&
                        date != null &&
                        startTime != null &&
                        endTime != null &&
                        !Double.isNaN(latitude);

                if (validDetails) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("name", name);
                    returnIntent.putExtra("date", date);
                    returnIntent.putExtra("startTime", startTime);
                    returnIntent.putExtra("endTime", endTime);
                    returnIntent.putExtra("latitude", latitude);
                    returnIntent.putExtra("longitude", longitude);
                    setResult(RESULT_OK, returnIntent);

                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Not all details set", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                latitude = data.getDoubleExtra("latitude", 0);
                longitude = data.getDoubleExtra("longitude", 0);
                showLocation.setText(latitude + ":" + longitude);
            }
        }
    }

    //Source: http://androidopentutorials.com/android-datepickerdialog-on-edittext-click-event/
    private void setDate() {

        Calendar newCalendar = Calendar.getInstance();
        DatePicker = new DatePickerDialog(this,
                new android.app.DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                date = dateFormatter.format(newDate.getTime());

                showDate.setText(date);
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
                        startTime = String.format("%02d:%02d", hourOfDay, minute);
                        showTime.setText(startTime);
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
