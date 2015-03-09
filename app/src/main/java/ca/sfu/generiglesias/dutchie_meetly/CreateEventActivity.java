package ca.sfu.generiglesias.dutchie_meetly;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class CreateEventActivity extends ActionBarActivity {

    private SimpleDateFormat dateFormatter;
    private DatePickerDialog DatePicker;
    private TimePickerDialog TimePick;
    private EditText showDate;
    private EditText showTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.CANADA);

        showDate = (EditText) findViewById(R.id.showDate);
        showDate.setInputType(InputType.TYPE_NULL);
        showDate.requestFocus();

        showTime = (EditText) findViewById(R.id.showTime);
        showTime.setInputType(InputType.TYPE_NULL);
        showTime.requestFocus();

        setDate();
        setTime();

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

                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        showTime.setText(hourOfDay + ":" + minute);
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
